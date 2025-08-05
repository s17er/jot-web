package com.gourmetcaree.shop.pc.sys.action.error;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * エラーを処理するアクションクラスです。
 * @author Hiroyuki Sugimoto
 * @version 1.0
 */
public class IndexAction extends PcShopAction {

	/**
	 * セッションタイムアウトの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_SESSION_TIMEOUT)
	public String sessionTimeout() {
		throw new ActionMessagesException("errors.sesstionTimeout");
	}

	/**
	 * アクセスエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_AUTHORIZATION_ERROR)
	public String authorizationError() {
		throw new ActionMessagesException("errors.authorizationError");
	}

	/**
	 * 担当エラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_AUTHORIZATION_ERROR)
	public String noHandlingPermissionError() {
		throw new ActionMessagesException("errors.noHandlingPermissionError");
	}

	/**
	 * データ不整合エラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_INCONSISTENT_DATA_ERROR)
	public String inconsistentDataError() {
		throw new ActionMessagesException("errors.inconsistentDataError");
	}

	/**
	 * 不正なプロセスエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_FRAUDULEN_PROCESS_ERROR)
	public String fraudulentProcessError() {
		throw new ActionMessagesException("errors.fraudulentProcessError");
	}

	/**
	 * ページが見つからないエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_PAGE_NOT_FOUND_ERROR)
	public String pageNotFoundError() {
		throw new ActionMessagesException("errors.pageNotFoundError");
	}

	/**
	 * 有効期限が切れたエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_OUT_OF_DATE_ERROR)
	public String outOfDateError() {
		throw new ActionMessagesException("errors.outOfDateError");
	}

	/**
	 * メールが存在しないエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_MAIL_NOT_FOUND_ERROR)
	public String mailNotFoundError() {
		throw new ActionMessagesException("errors.app.mailNotFoundError");
	}

	/**
	 * データ登録エラーの処理を行います。
	 * @return
	 */
	@Execute(validator = true, input = TransitionConstants.ErrorTransition.JSP_REGISTERD_DATA_ERROR)
	public String registeredDataError() {
		throw new ActionMessagesException("errors.registeredDataError");
	}
}
