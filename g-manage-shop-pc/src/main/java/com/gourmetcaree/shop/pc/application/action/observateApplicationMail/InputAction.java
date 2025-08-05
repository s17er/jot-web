package com.gourmetcaree.shop.pc.application.action.observateApplicationMail;

import static org.apache.commons.lang.math.NumberUtils.*;

import javax.annotation.Resource;

import com.gourmetcaree.shop.pc.valueobject.MenuInfo;
import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants.PatternReplace;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic.MailPattern;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;
import com.gourmetcaree.shop.pc.application.form.observateApplicationMail.InputForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * メール入力をするアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class InputAction extends PcShopAction {

	/** メール入力フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04C01)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId, inputForm.observateApplicationId, inputForm.fromPageKbn);
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		try {
			TMail entity = mailService.getEntityByUser(toInt(inputForm.originalMailId), userDto.customerId);
			Beans.copy(entity, inputForm).execute();
			inputForm.mailBody = "\n\n" + WztStringUtil.insertQuotationMark(GourmetCareeConstants.MAIL_REPLY_QUOTATION_MARK, entity.body);

			inputForm.setProcessFlgOk();
			inputForm.setExistDataFlgOk();

			session.setAttribute("fromName", inputForm.fromName);

		} catch (WNoResultException e) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return TransitionConstants.Application.JSP_SPC04C01;
	}

	/**
	 * 確認画面表示
	 * @return
	 */
	@Execute(validator = true, input = TransitionConstants.Application.JSP_SPC04C01)
	public String conf() {

		//必要なパラメータをチェック
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId, inputForm.observateApplicationId, inputForm.fromPageKbn);

		inputForm.setProcessFlgOk();

		super.saveToken();

		return TransitionConstants.Application.JSP_SPC04C02;
	}

	/**
	 * 遷移元の画面へ戻る処理
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	public String back() {

		//必要なパラメータをチェック
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId, inputForm.observateApplicationId, inputForm.fromPageKbn);
		session.removeAttribute("fromName");

		if (ApplicationMailForm.FROM_MAIL_DETAIL.equals(inputForm.fromPageKbn)) {
			return "/observateApplicationMail/detail/index/" + inputForm.originalMailId + GourmetCareeConstants.REDIRECT_STR;
		} else {
			return "/observateApplicationMail/detail/index/" + inputForm.observateApplicationId + GourmetCareeConstants.REDIRECT_STR;
		}
	}

	/**
	 * 戻るボタン押下時の処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04C01)
	public String correct() {

		//必要なパラメータをチェック
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId, inputForm.observateApplicationId, inputForm.fromPageKbn);

		inputForm.setProcessFlgNg();

		return TransitionConstants.Application.JSP_SPC04C01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04C01)
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg || !super.isTokenValid()) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		sendMail();


		return TransitionConstants.Application.REDIRECT_OBSERVATEAPPLICATIONMAIL_INPUT_COMP;
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail() {

		ApplicationMailProperty property = new ApplicationMailProperty();
		Beans.copy(inputForm, property).execute();
		property.body = PatternSentenceUtil.replacePattern(inputForm.mailBody, PatternReplace.MEMBER_NAME, inputForm.fromName);

		try {
			//詳細画面から遷移の場合は削除フラグの有無にかかわらずメールを取得できるようにする。
			SendMailLogic.MailPattern mailPattern = (ApplicationMailForm.FROM_APPLICANT_DETAIL.equals(inputForm.fromPageKbn))
					? MailPattern.FIRST_MAIL_REPLY : MailPattern.NORMAL_REPLY;

			sendMailLogic.doReplyObservateApplicationMail(property, mailPattern);

		} catch (WNoResultException e) {
			//対象が存在しない場合は楽観的排他制御のエラーを返す
			throw new SOptimisticLockException();
		}
	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {
		session.removeAttribute("fromName");
		return TransitionConstants.Application.JSP_SPC04C03;
	}

	/**
	 * !!target_member!! をリプレースしたものを返します。
	 */
	public String getReplacedBody() {
		if (StringUtils.isBlank(inputForm.mailBody)) {
			return "";
		}

		return inputForm.mailBody.replaceAll(ShopServiceConstants.PatternReplace.MEMBER_NAME, inputForm.fromName);
	}

    @Override
    public MenuInfo getMenuInfo() {
        return MenuInfo.mailInstance();
    }
}
