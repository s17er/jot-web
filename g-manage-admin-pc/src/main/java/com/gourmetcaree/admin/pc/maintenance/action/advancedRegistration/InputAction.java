package com.gourmetcaree.admin.pc.maintenance.action.advancedRegistration;

import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration.InputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;

/**
 *
 * 事前登録登録アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class InputAction extends AbstractAdvancedRegistrationBaseAction {

	@Resource
	@ActionForm
	private InputForm inputForm;

	/**
	 * 初期画面
	 * @return 登録画面JSP
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ06C01)
	@MethodAccess(accessCode = "ADVANCE_INPUT_INDEX")
	public String index() {

		return show();
	}


	/**
	 * 初期表示
	 * @return 登録画面JSP
	 */
	private String show() {
		return TransitionConstants.Maintenance.JSP_APJ06C01;
	}


	/**
	 * 確認画面
	 * @return 確認画面JSP
	 */
	@Execute(validator = true, validate = "@, validate", stopOnValidationError = false, input = TransitionConstants.Maintenance.JSP_APJ06C01)
	@MethodAccess(accessCode = "ADVANCE_INPUT_CONF")
	public String conf() {
		super.saveToken();

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		return TransitionConstants.Maintenance.JSP_APJ06C02;
	}


	/**
	 * 登録を行います。
	 * @return 完了画面へのリダイレクト
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Maintenance.JSP_APJ06C01)
	@MethodAccess(accessCode = "ADVANCE_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		checkTokenThrowable();
		insert();
		return TransitionConstants.Maintenance.REDIRECT_ADVANCED_REGISTRATION_INPUT_COMP;
	}

	/**
	 * 完了画面
	 * @return 完了画面JSP
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Maintenance.JSP_APJ06C01)
	@MethodAccess(accessCode = "ADVANCE_INPUT_COMP")
	public String comp() {
		return TransitionConstants.Maintenance.JSP_APJ06C03;
	}

	/**
	 * インサートを行います。
	 */
	private void insert() {
		MAdvancedRegistration entity = Beans.createAndCopy(MAdvancedRegistration.class, inputForm).execute();

		try {
			entity.termStartDatetime = inputForm.createTermStartDatetime();
			entity.termEndDatetime = inputForm.createTermEndDatetime();
		} catch (ParseException e) {
			log.info("事前登録マスタのINSERT時に、日付変換に失敗しました。" + ToStringBuilder.reflectionToString(inputForm, ToStringStyle.MULTI_LINE_STYLE), e);
			throw new FraudulentProcessException(e);
		}
		advancedRegistrationService.insert(entity);
		log.info("事前登録マスタをINSERTしました。" + ToStringBuilder.reflectionToString(inputForm, ToStringStyle.MULTI_LINE_STYLE));
	}


	/**
	 * 初期表示
	 * @return 登録画面JSP
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ06C01)
	@MethodAccess(accessCode = "ADVANCE_INPUT_CORRECT")
	public String corret() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		return TransitionConstants.Maintenance.JSP_APJ06C01;
	}
}
