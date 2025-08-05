package com.gourmetcaree.common.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.util.TokenProcessor;

import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.exception.TransactionTokenException;

/**
 * ベースとなるアクションクラスです。<br>
 * 基本はこのクラスを継承してアクションクラスを作成して下さい。
 * @author Takahiro Ando
 * @version 1.0
 */
public abstract class BaseAction {

	/** リクエストオブジェクト*/
	@Resource
	protected HttpServletRequest request;

	/** レスポンスオブジェクト */
	@Resource
	protected HttpServletResponse response;

	/** セッションオブジェクト */
	@Resource
	protected HttpSession session;

	/** アプリケーションオブジェクト */
	@Resource
	protected ServletContext application;

	protected final Logger actionLog = Logger.getLogger(this.getClass());

	/**
	 * セッションを破棄します。
	 */
	protected void sessionInvalidate() {
		this.session.invalidate();
	}

	/**
	 * 指定のヘッダ名でリクエストヘッダの値を取得します。
	 * @param headerName ヘッダ名
	 * @return 値
	 */
	protected String getRequestHeader(String headerName) {
		return request.getHeader(headerName);
	}

	/**
	 * リモートアドレスを取得します。
	 * @return リモートアドレス文字列
	 */
	protected String getRemoteAddress() {
		return request.getRemoteAddr();
	}

	/**
	 * 共通プロパティファイルから指定のキーの値を取得します。
	 * @param key キー
	 * @return 値
	 */
	protected String getCommonProperty(String key) {
		return (String) ((Map<?, ?>) application.getAttribute("common")).get(key);
	}

	/**
	 * 共通定型文プロパティファイルから指定のキーの値を取得します。
	 * @param key キー
	 * @return 値
	 */
	protected String getSentenceProperty(String key) {
		return (String) ((Map<?, ?>) application.getAttribute("sentence")).get(key);
	}

	/**
	 * 指定のプロパティのMapを取得します。
	 * @param key キー
	 * @return プロパティ
	 */
	protected Map<?, ?> getProperty(String key) {
		return (Map<?, ?>) application.getAttribute(key);
	}


	/**
	 * トランザクショントークンを保存します。
	 */
	protected void saveToken() {
		TokenProcessor.getInstance().saveToken(request);
	}

	/**
	 * トランザクショントークンをチェックし、
	 * 無効であれば {@link TransactionTokenException} をスローします。
	 */
	protected void checkTokenThrowable() {
		if (!isTokenValid()) {
			throw new FraudulentProcessException("二重投稿が行われました。");
		}
	}

	/**
	 * トランザクショントークンが有効か判定します。
	 * @return
	 */
	protected boolean isTokenValid() {
		return TokenProcessor.getInstance().isTokenValid(request, true);
	}

}
