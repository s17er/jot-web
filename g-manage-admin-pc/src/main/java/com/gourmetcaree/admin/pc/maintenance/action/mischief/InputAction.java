package com.gourmetcaree.admin.pc.maintenance.action.mischief;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.mischief.InputForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MischiefApplicationConditionLogic;
import com.gourmetcaree.admin.service.property.MischiefApplicationConditionProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;

/**
 * いたずら応募条件登録を行うクラス
 * @author Aquarius
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** いたずら応募条件ロジック  */
	@Resource
	protected MischiefApplicationConditionLogic mischiefApplicationConditionLogic;

	/**
	 * 初期表示
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ10C01)
	@MethodAccess(accessCode = "MISCHIEF_INPUT_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 確認
	 *
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Maintenance.JSP_APJ10C01)
	@MethodAccess(accessCode = "MISCHIEF_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10C02;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MISCHIEF_INPUT_INDEX")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10C01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode="MISCHIEF_INPUT_SUBMIT")
	public String submit() {

		MischiefApplicationConditionProperty property = new MischiefApplicationConditionProperty();

		property.mMischiefApplicationCondition = convertToMischiefApplicationCondition();

		mischiefApplicationConditionLogic.insertMischiefApplicationConditionData(property);

		return TransitionConstants.Maintenance.REDIRECT_MISCHIEF_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MISCHIEF_INPUT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10C03;
	}


	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10C01;
	}

	/**
	 * 画面から入力された値を登録用の値に変換する
	 * @return
	 */
	private MMischiefApplicationCondition convertToMischiefApplicationCondition() {
		MMischiefApplicationCondition mMischiefApplicationCondition = new MMischiefApplicationCondition();

		if(StringUtils.isNotBlank(inputForm.name)) {
			mMischiefApplicationCondition.name = inputForm.name;
		}

		if(StringUtils.isNotBlank(inputForm.nameKana)) {
			mMischiefApplicationCondition.nameKana = inputForm.nameKana;
		}

		if(StringUtils.isNotBlank(inputForm.prefecturesCd)) {
			mMischiefApplicationCondition.prefecturesCd = Integer.parseInt(inputForm.prefecturesCd);
		}

		if(StringUtils.isNotBlank(inputForm.municipality)) {
			mMischiefApplicationCondition.municipality = inputForm.municipality;
		}

		if(StringUtils.isNotBlank(inputForm.address)) {
			mMischiefApplicationCondition.address = inputForm.address;
		}

		if(StringUtils.isNotBlank(inputForm.phoneNo1) && StringUtils.isNotBlank(inputForm.phoneNo2) && StringUtils.isNotBlank(inputForm.phoneNo3)) {
			mMischiefApplicationCondition.phoneNo1 = inputForm.phoneNo1;
			mMischiefApplicationCondition.phoneNo2 = inputForm.phoneNo2;
			mMischiefApplicationCondition.phoneNo3 = inputForm.phoneNo3;
		}

		if(StringUtils.isNotBlank(inputForm.mailAddress)) {
			mMischiefApplicationCondition.mailAddress = inputForm.mailAddress;
		}

		if(StringUtils.isNotBlank(inputForm.memberFlg)) {
			mMischiefApplicationCondition.memberFlg = Integer.parseInt(inputForm.memberFlg);
		}

		if(StringUtils.isNotBlank(inputForm.terminalKbn)) {
			mMischiefApplicationCondition.terminalKbn = Integer.parseInt(inputForm.terminalKbn);
		}

		if(StringUtils.isNotBlank(inputForm.applicationSelfPr)) {
			mMischiefApplicationCondition.applicationSelfPr = inputForm.applicationSelfPr;
		}

		return mMischiefApplicationCondition;
	}

}