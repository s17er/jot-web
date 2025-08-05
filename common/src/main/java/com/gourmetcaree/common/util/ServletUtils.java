package com.gourmetcaree.common.util;

import javax.servlet.http.HttpSession;

import org.apache.struts.util.TokenProcessor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.struts.util.RequestUtil;

import com.gourmetcaree.db.common.exception.TransactionTokenException;

/**
 * JAVAサーブレットに関するユーティリティ
 * @author nakamori
 *
 */
public class ServletUtils {

	// インスタンス生成不可
	private ServletUtils() {}


	/**
	 * セッションの取得
	 */
	public static HttpSession getSession() {
		return SingletonS2Container.getComponent(HttpSession.class);
	}

	/**
	 * セッションに値を保存
	 * @param key セッションキー
	 * @param value 保存する値
	 */
	public static void setSessionAttribute(String key, Object value) {
		getSession().setAttribute(key, value);
	}


	/**
	 * セッションから値を取得
	 * @param key セッションキー
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(String key) {
		return (T) getSession().getAttribute(key);
	}


	/**
	 * トランザクショントークンを保存します。
	 */
	public static void saveToken() {
		TokenProcessor.getInstance().saveToken(RequestUtil.getRequest());
	}

	/**
	 * トランザクショントークンをチェックし、
	 * 無効であれば {@link TransactionTokenException} をスローします。
	 */
	public static void checkToken() {
		if (!isTokenValid()) {
			throw new TransactionTokenException("二重投稿が行われました。");
		}
	}

	/**
	 * トランザクショントークンが有効か判定します。
	 * @return
	 */
	public static boolean isTokenValid() {
		return TokenProcessor.getInstance()
				.isTokenValid(RequestUtil.getRequest(), true);
	}
}
