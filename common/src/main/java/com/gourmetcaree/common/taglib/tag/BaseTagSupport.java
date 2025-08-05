package com.gourmetcaree.common.taglib.tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * ベースとなるタグサポート
 * @author nakamori
 *
 */
public abstract class BaseTagSupport extends TagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1483237452958334156L;

	protected final Logger logger = Logger.getLogger(this.getClass());

	/** ページスコープ */
	public static final String SCOPE_PAGE = "page";

	/** リクエストスコープ */
	public static final String SCOPE_REQUEST = "request";

	/** セッションスコープ */
	public static final String SCOPE_SESSION = "session";


	/**
	 * ページコンテキストに値を設定します。
	 * @param name 名称
	 * @param value 値
	 * @author nakamori
	 */
	protected void setAttribute(String name, Object value) {
		setAttribute(name, value, SCOPE_PAGE);
	}

	/**
	 * ページコンテキストに値を設定します。
	 * @param name 名称
	 * @param value 値
	 * @param scope スコープ
	 * @author nakamori
	 */
	protected void setAttribute(String name, Object value, String scope) {
		pageContext.setAttribute(name, value, convertToScopeValue(scope));
	}

	/**
	 * コンポーネントの取得
	 * @param clazz コンポーネントのクラス
	 * @return コンポーネント
	 * @author nakamori
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getComponent(Class<T> clazz) {
		return (T) SingletonS2ContainerFactory.getContainer().getComponent(clazz);
	}

	/**
	 * スコープの値に変換
	 * @param scope スコープ名
	 * @return スコープの値
	 * @author nakamori
	 */
	private int convertToScopeValue(String scope) {
		if (SCOPE_PAGE.equals(scope)) {
			return PageContext.PAGE_SCOPE;
		}

		if (SCOPE_REQUEST.equals(scope)) {
			return PageContext.REQUEST_SCOPE;
		}

		if (SCOPE_SESSION.equals(scope)) {
			return PageContext.SESSION_SCOPE;
		}

		return PageContext.PAGE_SCOPE;
	}
}
