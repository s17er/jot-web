package com.gourmetcaree.admin.pc.maintenance.action.mischief;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.mischief.DeleteForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;
import com.gourmetcaree.db.common.service.MischiefApplicationConditionService;

/**
 * いたずら応募条件削除するクラス
 * @author Aquarius
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

	/** いたずら応募条件ロジック  */
	@Resource
	protected MischiefApplicationConditionService mischiefApplicationConditionService;

	/**
	 * 初期表示
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetFormWithoutId", input = TransitionConstants.Maintenance.JSP_APJ10R01)
	@MethodAccess(accessCode = "MISCHIEF_DELETE_INDEX")
	public String index() {

		return submit();
	}

	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ10R01)
	@MethodAccess(accessCode="MISCHIEF_EDIT_SUBMIT")
	public String submit() {

		// 確認画面のダイアログから遷移していない場合はエラー
		if (!deleteForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		delete();

		return TransitionConstants.Maintenance.REDIRECT_MISCHIEF_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MISCHIEF_DELETE_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10D03;
	}


	/**
	 * 削除処理
	 */
	private void delete() {

		MMischiefApplicationCondition entity = new MMischiefApplicationCondition();

		Beans.copy(deleteForm, entity).execute();

		try {
			mischiefApplicationConditionService.logicalDelete(entity);
		} catch (SOptimisticLockException e) {
			deleteForm.resetForm();
			deleteForm.setExistDataFlgNg();
			throw new SOptimisticLockException("いたずら応募条件の論理削除時に楽観的排他制御エラーが発生しました。");
		}

		log.debug("会社マスタを削除しました。" + deleteForm);
	}

}