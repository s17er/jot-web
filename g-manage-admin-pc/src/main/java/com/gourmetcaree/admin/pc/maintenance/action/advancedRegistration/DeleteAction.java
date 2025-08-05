package com.gourmetcaree.admin.pc.maintenance.action.advancedRegistration;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration.DeleteForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;

/**
 * 事前登録削除アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DeleteAction extends AbstractAdvancedRegistrationBaseAction {


	/** アクションフォーム */
	@Resource
	@ActionForm
	private DeleteForm deleteForm;


	/**
	 * 初期画面
	 * @return
	 */
	@Execute(validator = false, urlPattern = "{id}", reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ06D01)
	@MethodAccess(accessCode = "ADVANCE_DELETE_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	private String show() {
		createDetail(deleteForm);
		saveToken();
		deleteForm.setProcessFlgOk();
		return TransitionConstants.Maintenance.JSP_APJ06D01;
	}


	/**
	 * 削除実行
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ06D01)
	@MethodAccess(accessCode = "ADVANCE_DELETE_SUBMIT")
	public String submit() {
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);
		if (!isTokenValid()) {
			throw new FraudulentProcessException("アクセストークンが不正です。");
		}
		if (!deleteForm.processFlg) {
			throw new FraudulentProcessException("不正なプロセスです。");
		}

		delete();

		return TransitionConstants.Maintenance.REDIRECT_ADVANCED_REGISTRATION_DELETE_COMP;
	}

	/**
	 * 削除完了画面
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ06D01)
	@MethodAccess(accessCode = "ADVANCE_DELETE_COMP")
	public String comp() {
		return TransitionConstants.Maintenance.JSP_APJ06D03;
	}

	/**
	 * 事前登録の削除
	 */
	private void delete() {
		try {
			MAdvancedRegistration entity = advancedRegistrationService.findById(NumberUtils.toInt(deleteForm.id));
			advancedRegistrationService.logicalDelete(entity);
		} catch (SNoResultException e) {
			throw new FraudulentProcessException(e);
		}
	}

	@Execute(validator = false)
	@MethodAccess(accessCode = "ADVANCE_DELETE_CORRECT")
	public String correct() {
		deleteForm.resetForm();
		return TransitionConstants.Maintenance.REDIRECT_ADVANCED_REGISTRATION_LIST;
	}
}
