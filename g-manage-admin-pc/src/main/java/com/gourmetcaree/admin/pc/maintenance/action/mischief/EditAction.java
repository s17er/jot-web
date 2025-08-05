package com.gourmetcaree.admin.pc.maintenance.action.mischief;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.mischief.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MischiefApplicationConditionLogic;
import com.gourmetcaree.admin.service.property.MischiefApplicationConditionProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;

/**
 * いたずら応募条件編集クラス
 * @author Aquarius
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends PcAdminAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** いたずら応募条件ロジック  */
	@Resource
	protected MischiefApplicationConditionLogic mischiefApplicationConditionLogic;

	/**
	 * 初期表示
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ10E01)
	@MethodAccess(accessCode = "MISCHIEF_EDIT_INDEX")
	public String index() {

		return show();
	}
	/**
	 * 戻る
	 * @return 詳細画面の初期表示
	 */
	@Execute(validator = false, reset = "resetFormWithoutId")
	@MethodAccess(accessCode="MISCHIEF_EDIT_BACK")
	public String back() {

		// 確認画面の表示メソッドへリダイレクト
		return TransitionConstants.Maintenance.ACTION_MISCHIEF_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 確認
	 *
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Maintenance.JSP_APJ10E01)
	@MethodAccess(accessCode = "MISCHIEF_EDIT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10E02;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MISCHIEF_EDIT_INDEX")
	public String correct() {

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10E01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ10E01)
	@MethodAccess(accessCode="MISCHIEF_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		edit();

		return TransitionConstants.Maintenance.REDIRECT_MISCHIEF_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MISCHIEF_EDIT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10E03;
	}


	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {

		createDisplayValue();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10E01;
	}

	/**
	 * 画面に表示する値を作成する
	 */
	private void createDisplayValue() {

		try {
			MMischiefApplicationCondition entity = mischiefApplicationConditionLogic.findById(Integer.valueOf(editForm.id));
			Beans.copy(entity, editForm).execute();
		} catch (SNoResultException e) {
			editForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * 更新処理
	 */
	private void edit() {

		MMischiefApplicationCondition entity = convertToMischiefApplicationCondition();

		MischiefApplicationConditionProperty property = new MischiefApplicationConditionProperty();

		property.mMischiefApplicationCondition = entity;

		mischiefApplicationConditionLogic.update(property);
	}

	/**
	 * 画面から入力された値を登録用の値に変換する
	 * @return
	 */
	private MMischiefApplicationCondition convertToMischiefApplicationCondition() {
		MMischiefApplicationCondition mMischiefApplicationCondition = new MMischiefApplicationCondition();

		mMischiefApplicationCondition.id = Integer.parseInt(editForm.id);
		mMischiefApplicationCondition.version = editForm.version;

		if(StringUtils.isNotBlank(editForm.name)) {
			mMischiefApplicationCondition.name = editForm.name;
		}

		if(StringUtils.isNotBlank(editForm.nameKana)) {
			mMischiefApplicationCondition.nameKana = editForm.nameKana;
		}

		if(StringUtils.isNotBlank(editForm.prefecturesCd)) {
			mMischiefApplicationCondition.prefecturesCd = Integer.parseInt(editForm.prefecturesCd);
		}

		if(StringUtils.isNotBlank(editForm.municipality)) {
			mMischiefApplicationCondition.municipality = editForm.municipality;
		}

		if(StringUtils.isNotBlank(editForm.address)) {
			mMischiefApplicationCondition.address = editForm.address;
		}

		if(StringUtils.isNotBlank(editForm.phoneNo1) && StringUtils.isNotBlank(editForm.phoneNo2) && StringUtils.isNotBlank(editForm.phoneNo3)) {
			mMischiefApplicationCondition.phoneNo1 = editForm.phoneNo1;
			mMischiefApplicationCondition.phoneNo2 = editForm.phoneNo2;
			mMischiefApplicationCondition.phoneNo3 = editForm.phoneNo3;
		}

		if(StringUtils.isNotBlank(editForm.mailAddress)) {
			mMischiefApplicationCondition.mailAddress = editForm.mailAddress;
		}

		if(StringUtils.isNotBlank(editForm.memberFlg)) {
			mMischiefApplicationCondition.memberFlg = Integer.parseInt(editForm.memberFlg);
		}

		if(StringUtils.isNotBlank(editForm.terminalKbn)) {
			mMischiefApplicationCondition.terminalKbn = Integer.parseInt(editForm.terminalKbn);
		}

		if(StringUtils.isNotBlank(editForm.applicationSelfPr)) {
			mMischiefApplicationCondition.applicationSelfPr = editForm.applicationSelfPr;
		}

		return mMischiefApplicationCondition;
	}

}