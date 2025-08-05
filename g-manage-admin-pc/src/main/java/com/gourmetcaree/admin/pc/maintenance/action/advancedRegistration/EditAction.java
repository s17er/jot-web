package com.gourmetcaree.admin.pc.maintenance.action.advancedRegistration;

import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;

/**
 * 事前登録マスタ変更アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends AbstractAdvancedRegistrationBaseAction {

	/** アクションフォーム */
	@Resource
	@ActionForm
	private EditForm editForm;

	/**
	 * 初期画面
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "{id}", input = TransitionConstants.Maintenance.JSP_APJ07C01)
	@MethodAccess(accessCode = "ADVANCE_EDIT_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		return show();
	}

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	private String show() {
		createDetail(editForm);
		return TransitionConstants.Maintenance.JSP_APJ07C01;
	}


	/**
	 * 確認画面
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "@, validate", input = TransitionConstants.Maintenance.JSP_APJ07C01)
	@MethodAccess(accessCode = "ADVANCE_EDIT_CONF")
	public String conf() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		saveToken();
		return TransitionConstants.Maintenance.JSP_APJ07C02;
	}

	/**
	 * 変更実行
	 * @return 完了画面リダイレクト
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Maintenance.JSP_APJ07C01)
	@MethodAccess(accessCode = "ADVANCE_EDIT_SUBMIT")
	public String submit() {
		checkTokenThrowable();
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		update();
		return TransitionConstants.Maintenance.REDIRECT_ADVANCED_REGISTRATION_EDIT_COMP;
	}

	/**
	 * 完了画面
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Maintenance.JSP_APJ07C01)
	@MethodAccess(accessCode = "ADVANCE_EDIT_COMP")
	public String comp() {
		return TransitionConstants.Maintenance.JSP_APJ07C03;
	}

	/**
	 * UPDATEを行います。
	 */
	private void update() {
		MAdvancedRegistration entity = Beans.createAndCopy(MAdvancedRegistration.class, editForm).execute();

		try {
			entity.termStartDatetime = editForm.createTermStartDatetime();
			entity.termEndDatetime = editForm.createTermEndDatetime();
		} catch (ParseException e) {
			log.info("事前登録マスタのUPDATE時に、日付変換に失敗しました。" + ToStringBuilder.reflectionToString(editForm, ToStringStyle.MULTI_LINE_STYLE), e);
			throw new FraudulentProcessException("日付変換に失敗しました。", e);
		}

		advancedRegistrationService.update(entity);
		log.info("事前登録マスタをUPDATEしました。" + ToStringBuilder.reflectionToString(editForm, ToStringStyle.MULTI_LINE_STYLE));
	}

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ07C01)
	@MethodAccess(accessCode = "ADVANCE_EDIT_CORRECT")
	public String corret() {
		return TransitionConstants.Maintenance.JSP_APJ07C01;
	}

}
