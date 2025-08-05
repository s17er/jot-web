package com.gourmetcaree.admin.service.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.common.util.PropertiesUtil;
import com.gourmetcaree.db.common.entity.TWebIpPhone;

/**
 * インテックのAPIに新規登録するためのクラスです。
 *
 * @author hara
 *
 */
public class IntecCreateConnection extends AbstractBaseIntecConnection {

	/** 認証トークン */
	private String token;

	/** IP電話データ */
	private ClientData ipPhone;

	/**
	 * コンストラクタ
	 * @param ipPhoneList
	 */
	public IntecCreateConnection(ClientData ipPhone, String token) {
		this.ipPhone = ipPhone;
		this.token = token;
	}

	/**
	 * 接続先URLを取得します
	 */
	@Override
	protected String getUrl() {

		String baseUrl = getBaseUrl();

		if (StringUtils.isBlank(baseUrl)) {
			return null;
		}

		return String.format("%s/%s/%s/clients/create.xml", baseUrl, COMPANY_CD, SERVICE_CD);
	}

	/**
	 * パラメータを取得します
	 */
	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("client_cd", ipPhone.clientCd);
		params.put("client_number", ipPhone.customerTel);
		params.put("name", ipPhone.customerName);
		params.put("token", token);

		return params;
	}


	/**
	 * コネクションに渡すためのクラス
	 *
	 * @author hara
	 *
	 */
	public static class ClientData extends TWebIpPhone {

		private static final long serialVersionUID = 568238015253487310L;

		/** 顧客名 */
		public String customerName;

		/**
		 * クライアントコード用文字列を作成します。
		 * @return
		 *
		 * @author hara
		 */
		private String getClientCd() {

			if (StringUtils.isNotBlank(clientCd)) {
				return clientCd;
			}

			if (webId == null
					|| customerId == null
					|| edaNo == null) {
				return null;
			}

			StringBuilder sb = new StringBuilder();

			sb.append("w");
			sb.append(webId);
			sb.append("c");
			sb.append(customerId);
			sb.append("e");
			sb.append(edaNo);

			String suffix = PropertiesUtil.getGourmetCareeProperty("intec.api.clientCd.suffix");
			if (StringUtils.isNotBlank(suffix)) {
				sb.append(suffix);
			}

			return sb.toString();
		}

		/**
		 * クライアントコードを作成してセットします
		 * @return
		 *
		 * @author hara
		 */
		public void createClientCd() {

			String clientCdStr = getClientCd();
			if (clientCdStr != null) {
				this.clientCd = clientCdStr;
			}
		}

	}



}
