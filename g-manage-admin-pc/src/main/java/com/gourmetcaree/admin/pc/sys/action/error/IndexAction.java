package com.gourmetcaree.admin.pc.sys.action.error;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;

/**
 * エラーを処理するアクションクラスです。
 * @author Hiroyuki Sugimoto
 * @version 1.0
 */
public class IndexAction extends PcAdminAction {

	/**
	 * セッションタイムアウトの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_SESSION_TIMEOUT)
	public String sessionTimeout() {
		throw new ActionMessagesException("errors.sesstionTimeout");
	}

	/**
	 * Ajaxのセッションタイムアウトの処理を行います。
	 * ※処理としては未処理とする。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_SESSION_TIMEOUT)
	public String ajaxTimeout() {
		return null;
	}

	/**
	 * アクセスエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_AUTHORIZATION_ERROR)
	public String authorizationError() {
		throw new ActionMessagesException("errors.authorizationError");
	}

	/**
	 * 担当エラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_AUTHORIZATION_ERROR)
	public String noHandlingPermissionError() {
		throw new ActionMessagesException("errors.noHandlingPermissionError");
	}

	/**
	 * データ不整合エラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_INCONSISTENT_DATA_ERROR)
	public String inconsistentDataError() {
		throw new ActionMessagesException("errors.inconsistentDataError");
	}

	/**
	 * 不正なプロセスエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_FRAUDULEN_PROCESS_ERROR)
	public String fraudulentProcessError() {
		throw new ActionMessagesException("errors.fraudulentProcessError");
	}

	/**
	 * ページが見つからないエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_PAGE_NOT_FOUND_ERROR)
	public String pageNotFoundError() {
		throw new ActionMessagesException("errors.pageNotFoundError");
	}
	/**
	 * メールが存在しないエラーの処理を行います。
	 */
	@Execute(validator = true, input = TransitionConstants.JSP_MAIL_NOT_FOUND_ERROR)
	public String mailNotFoundError() {
		throw new ActionMessagesException("errors.app.mailNotFoundError");
	}
}
