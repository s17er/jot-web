package com.gourmetcaree.admin.pc.member.action.tempMember;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessages;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.member.form.tempMember.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.tempMember.TempMemberLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.ActionMessageUtil;


/**
 * 仮会員変更アクション
 * @author nakamori
 *
 */
@ManageLoginRequired(authLevel = {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class EditAction extends PcAdminAction {

	@Resource
	protected TempMemberLogic tempMemberLogic;


	@ActionForm
	@Resource(name = "tempMember_editForm")
	EditForm form;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "{id}")
	@MethodAccess(accessCode="TEMPMEMBER_EDIT")
	public String index() {
		try {
			tempMemberLogic.createData(form);
			form.setExistDataFlgOk();
		} catch (SNoResultException e) {
			form.setExistDataFlgNg();
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.Member.JSP_APH03R01;
		}
		return TransitionConstants.Member.JSP_APH03E01;
	}



	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate, validateAction", input = TransitionConstants.Member.JSP_APH03E01, reset = "resetMultibox")
	@MethodAccess(accessCode="TEMPMEMBER_EDIT")
	public String conf() {
		saveToken();
		form.setProcessFlgOk();
		tempMemberLogic.createConfData(form);
		return TransitionConstants.Member.JSP_APH03E02;
	}


	/**
	 * 入力画面へ戻る
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="TEMPMEMBER_EDIT")
	public String correct() {
		form.setProcessFlgNg();
		return TransitionConstants.Member.JSP_APH03E01;
	}

	/**
	 * 詳細へ戻る
	 */
	@MethodAccess(accessCode="TEMPMEMBER_EDIT")
	@Execute(validator = false, removeActionForm = true)
	public String back() {
		return String.format("/tempMember/detail/%s?redirect=true", form.getId());
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Member.JSP_APH03E01)
	@MethodAccess(accessCode="TEMPMEMBER_EDIT")
	public String submit() {

		checkTokenThrowable();

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!form.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + form);
		}

//		// セッションのIdと、画面で保持するIdが違う場合エラー
//		if (!form.id.equals(form.hiddenId)) {
//			throw new FraudulentProcessException("不正な操作が行われました。" + form);
//		}

//		// 携帯メールアドレスのドメインチェック
//		if (String.valueOf(MTypeConstants.TerminalKbn.PC_VALUE).equals(form.terminalKbn) && StringUtils.isNotEmpty(editForm.mobileMail)) {
//			if (!typeService.checkMobileType(editForm.mobileMail)) {
//				throw new ActionMessagesException("errors.mobilemailaddress", "携帯メールアドレス");
//			}
//		}

		// 登録処理
		tempMemberLogic.update(form);

		return "/tempMember/edit/comp?redirect=true";
	}


	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="TEMPMEMBER_EDIT")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Member.JSP_APH03E03;
	}


	/**
	 * アクション側のバリデートを行います。
	 */
	public ActionMessages validateAction() {
		return tempMemberLogic.validate(form);
	}

}
