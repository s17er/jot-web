package com.gourmetcaree.admin.pc.maintenance.action.company;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.company.DeleteForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.service.CompanyService;
/**
*
* 会社削除を行うクラス
* @author Makoto Otani
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

	/** 会社マスタのサービス */
	@Resource
	protected CompanyService companyService;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, reset="resetFormWithoutDelValue", input=TransitionConstants.Maintenance.JSP_APJ05R01)
	@MethodAccess(accessCode="COMPANY_DELETE_INDEX")
	public String index() {

		// 削除
		return submit();
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="COMPANY_DELETE_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05D03;
	}

	/**
	 * 削除
	 * @return 完了へリダイレクト
	 */
	private String submit() {

		// 確認画面のダイアログから遷移していない場合はエラー
		if (!deleteForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		// 削除処理
		delete();

		return TransitionConstants.Maintenance.REDIRECT_COMPANY_DELETE_COMP;
	}

	/**
	 * 会社マスタを論理削除
	 */
	private void delete() {

		// 会社マスタエンティティ
		MCompany entity = new MCompany();

		// 会社マスタエンティティにフォームをコピー
		Beans.copy(deleteForm, entity).dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "registrationDatetime").execute();

		try {
			// 会社マスタから論理削除
			companyService.logicalDelete(entity);

			// 楽観的排他制御でエラーとなった場合は、画面非表示にしてエラーを投げる
		} catch (SOptimisticLockException e) {
			deleteForm.resetForm();
			deleteForm.setExistDataFlgNg();
			throw new SOptimisticLockException("会社マスタの論理削除時に楽観的排他制御エラーが発生しました。");
		}

		log.debug("会社マスタを削除しました。" + deleteForm);
	}

}