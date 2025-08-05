package com.gourmetcaree.admin.pc.maintenance.action.terminal;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.terminal.DeleteForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.TerminalLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
*
* 駅グループ削除を行うクラス
* @author yamane
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DeleteAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	@Resource
	protected TerminalLogic terminalLogic;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", input=TransitionConstants.Maintenance.JSP_APJ08L01)
	@MethodAccess(accessCode="TERMINAL_DELETE_INDEX")
	public String index() {

		// 削除
		return submit();
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="TERMINAL_DELETE_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08D03;
	}

	/**
	 * 削除
	 * @return 完了へリダイレクト
	 */
	private String submit() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		// 削除処理
		try {
			terminalLogic.allDelete(Integer.parseInt(deleteForm.id));
		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.possibleDelData");
		}

		return TransitionConstants.Maintenance.REDIRECT_TERMINAL_DELETE_COMP;
	}
}