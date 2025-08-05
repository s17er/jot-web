package com.gourmetcaree.common.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * コネクションの基底クラス
 * XXX whizzCommonに置きたい。
 * @author nakamori
 *
 */
public abstract class AbstractBaseConnection {

	/** ログ */
	protected final Logger log = Logger.getLogger(this.getClass());

	/** httpステータス */
	private int httpStatus = -1;

	/** レスポンスヘッダ */
	private Map<String, List<String>> responseHeaders;


	/**
	 * URLへのアクセスを実行します。
	 */
	public InputStream execute() throws IOException {

		URLConnection connection = null;
		try {


			BasicAuthInfo basicInfo = getBasicAuthInfo();
			if (basicInfo != null) {
				Authenticator.setDefault(basicInfo.getAuthenticator());
			}

			String u = getUrl();
			Map<String, Object> params = getParams();
			if (getMethod() == RequestMethod.GET
					&& (params != null && !params.isEmpty())) {
				String s = createRequestParamsStr(params);
				u = String.format("%s?%s", u, s);
			}
			URL url = new URL(u);

			log.debug(String.format("URLへアクセスを行います。 URL:[%s]", url.toString()));

			connection = url.openConnection();
			createRequestHeaders(connection);
			createRequestParams(connection, params);

			return connection.getInputStream();

		} finally {
			if (connection != null) {

				createHttpStatus(connection);
				createResponseHeaders(connection);

//				if (connection instanceof HttpURLConnection) {
//					((HttpURLConnection) connection).disconnect();
//				} else if (connection instanceof HttpsURLConnection) {
//					((HttpsURLConnection) connection).disconnect();
//				}
			}

		}

	}




	/**
	 * リクエストヘッダを設定
	 */
	protected void createRequestHeaders(URLConnection connection) {
		Map<String, String> headers = getRequestHeaders();
		if (headers == null || headers.isEmpty()) {
			return;
		}

		log.debug(String.format("ヘッダを設定します。[%s]", ToStringBuilder.reflectionToString(headers)));

		for (Entry<String, String> entry : headers.entrySet()) {
			connection.setRequestProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * リクエストパラメータの作成
	 * @throws IOException
	 */
	protected void createRequestParams(URLConnection connection, Map<String, Object> params) throws IOException {
		if (params == null || params.isEmpty() || getMethod() == RequestMethod.GET) {
			return;
		}

		connection.setDoInput(true);
		connection.setDoOutput(true);

		if (connection instanceof HttpURLConnection) {
			((HttpURLConnection) connection).setRequestMethod(getMethod().value);
		} else if (connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) connection).setRequestMethod(getMethod().value);
		}


		String paramsStr = createRequestParamsStr(params);
		log.debug(String.format("パラメータを設定します。 パラメータ：[%s]", paramsStr));

		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(connection.getOutputStream(), getEncoding());
			writer.write(paramsStr);
			writer.flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * リクエストパラメータの文字列を作成
	 */
	private String createRequestParamsStr(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Entry<String, Object> entry : params.entrySet()) {
			if (index++ > 0) {
				sb.append("&");
			}

			sb.append(encode(entry.getKey()))
				.append("=")
				.append(encode(entry.getValue()));
		}
		return sb.toString();
	}

	/**
	 * 引数を 文字列に変換後、URLエンコードを行います。
	 */
	protected String encode(Object value) {
		String str = String.valueOf(value);

		try {
			return URLEncoder.encode(str, getEncoding());
		} catch (UnsupportedEncodingException e) {
			log.warn(String.format("文字列[%s]のURLエンコードに失敗しました。文字コード:[%s]", str, getEncoding()), e);
			return str;
		}
	}

	/**
	 * エンコードの取得
	 * エンコードを変更する場合はこのメソッドをオーバライド
	 */
	protected String getEncoding() {
		return "UTF-8";
	}



	/**
	 * URL取得
	 */
	protected abstract String getUrl();

	/**
	 * POST/GETのパラメータ取得
	 *
	 * パラメータを設定する場合はこのメソッドをオーバライド
	 */
	protected Map<String, Object> getParams() {
		return null;
	}

	/**
	 * ヘッダの取得
	 *
	 * ヘッダを設定する場合はこのメソッドをオーバライド
	 */
	protected Map<String, String> getRequestHeaders() {
		return null;
	}

	/**
	 * POST/GET等の取得
	 *
	 * リクエストメソッドを変更する場合はこのメソッドをオーバライド。
	 *
	 * デフォルトでは POST を指定。
	 */
	protected RequestMethod getMethod() {
		return RequestMethod.POST;
	}



	/**
	 * リクエストメソッド列挙型
	 * @author nakamori
	 *
	 */
	public static enum RequestMethod {
		POST("POST"),
		GET("GET")
		;


		private String value;

		private RequestMethod(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * HTTPステータスの作成
	 */
	protected void createHttpStatus(URLConnection connection) {

		try {
			if (connection instanceof HttpURLConnection) {
				this.httpStatus = ((HttpURLConnection) connection).getResponseCode();
				return;
			}

			if (connection instanceof HttpsURLConnection) {
				this.httpStatus = ((HttpsURLConnection) connection).getResponseCode();
				return;
			}
		} catch (IOException e) {
			log.warn("レスポンスコードの取得に失敗しました。", e);
		}

		this.httpStatus = -1;
	}


	/**
	 * レスポンスヘッダの作成
	 */
	private void createResponseHeaders(URLConnection connection) {
		this.responseHeaders = connection.getHeaderFields();
	}

	/**
	 * httpステータスの取得。
	 * @return 正常に取得できていない場合は -1
	 */
	public int getHttpStatus() {
		return httpStatus;
	}

	/**
	 * レスポンスヘッダの取得
	 */
	public Map<String, List<String>> getResponseHeaders() {
		if (responseHeaders == null) {
			responseHeaders = new HashMap<String, List<String>>(0);
		}
		return responseHeaders;
	}


	/**
	 * ベーシック認証情報を取得
	 */
	public BasicAuthInfo getBasicAuthInfo() {
		return null;
	}


	/**
	 * ベーシック認証用情報
	 * @author nakamori
	 *
	 */
	public static class BasicAuthInfo implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = -963170400277341194L;

		public BasicAuthInfo(String id, String password) {
			this.id = id;
			this.password = password;
		}

		/** ID */
		private String id;

		/** パスワード */
		private String password;

		private Authenticator getAuthenticator() {
			return new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
				    return new PasswordAuthentication(id, password.toCharArray());
				   }
			};
		}

	}


}
