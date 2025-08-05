package com.gourmetcaree.shop.pc.application.action.arbeitMail;

import static org.apache.commons.lang.math.NumberUtils.*;

import javax.annotation.Resource;

import com.gourmetcaree.shop.pc.valueobject.MenuInfo;
import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic.MailPattern;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;
import com.gourmetcaree.shop.pc.application.form.arbeitMail.InputForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * アルバイトメール返信入力アクション
 * @author Takehiro Nakamori
 *
 */
public class InputAction extends PcShopAction {

	/** アクションフォーム */
	@ActionForm
	@Resource
	private InputForm inputForm;

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
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05C01)
	public String index() {

		//必要なパラメータをチェック

		checkArg();

		inputForm.lumpSendFlg = false;
		return show();
	}

	/**
	 * 初期表示（一括送信）
	 * @return
	 */
	@Execute(validator = false, reset = "resetForLumpSend", input = TransitionConstants.Application.JSP_SPC05C01)
	public String lumpSend() {

		//必要なパラメータをチェック
		if (inputForm.originalMailId.length != inputForm.arbeitApplicationId.length) {
			throw new FraudulentProcessException();
		}

		for (int i = 0; i < inputForm.originalMailId.length; i++) {

			checkNumber(inputForm.originalMailId[i]);
			checkNumber(inputForm.arbeitApplicationId[i]);

			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.arbeitApplicationId[i], inputForm.fromPageKbn);
		}


		return show();
	}

	/**
	 * 引数のチェック
	 */
	private void checkArg() {

		if (inputForm.originalMailId == null) {
			throw new FraudulentProcessException("パラメータがNULLです。");
		}

		if (inputForm.originalMailId.length != inputForm.arbeitApplicationId.length) {
			throw new FraudulentProcessException("パラメータの数が一致しません。");
		}

		for (int i = 0; i < inputForm.originalMailId.length; i++) {

			checkNumber(inputForm.originalMailId[i]);
			checkNumber(inputForm.arbeitApplicationId[i]);

			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.arbeitApplicationId[i], inputForm.fromPageKbn);
		}
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		try {

			//応募一覧から来た場合は本文を空にする。それ以外は本文を引用形式に変更
			if (ApplicationMailForm.FROM_APPLICANT_DETAIL.equals(inputForm.fromPageKbn)) {
				TMail entity = mailService.getEntityIgnoreDeleteByUser(toInt(inputForm.originalMailId[0]), userDto.customerId);
				Beans.copy(entity, inputForm).excludes("originalMailId", "arbeitApplicationId", "fromName").execute();
				inputForm.arbeitApplicationId[0] = String.valueOf(entity.arbeitApplicationId);
				inputForm.fromName = new String[1];
				inputForm.fromName[0] = entity.fromName;

				inputForm.mailBody = "";

			// 一括送信の場合は、件名、本文が無い状態にする
			} else if (ApplicationMailForm.FROM_APPLICATION_LIST.equals(inputForm.fromPageKbn)) {

//				inputForm.fromName = new String[inputForm.originalMailId.length];
//
//				int i = 0;
//				for (String originalMailId : inputForm.originalMailId) {
//
//					TMail entity = mailService.getEntityIgnoreDeleteByUser(toInt(originalMailId), userDto.customerId);
//					Beans.copy(entity, inputForm).excludes("originalMailId", "arbeitApplicationId", "fromName").execute();
//					inputForm.arbeitApplicationId[i] = String.valueOf(entity.arbeitApplicationId);
//					inputForm.fromName[i] = String.valueOf(entity.fromName);
//					i++;
//				}
//
//				inputForm.mailBody = "";
//				inputForm.subject = "";


				inputForm.fromName = new String[inputForm.originalMailId.length];

				int i = 0;
				for (String originalMailId : inputForm.originalMailId) {

					TMail entity = mailService.getEntityIgnoreDeleteByUser(toInt(originalMailId), userDto.customerId);

					Beans.copy(entity, inputForm).excludes("arbeitApplicationId", "fromName").execute();

					try {
						int applicationId = Integer.parseInt(inputForm.arbeitApplicationId[i]);
						if (!GourmetCareeUtil.eqInt(applicationId, entity.arbeitApplicationId)) {
							throw new FraudulentProcessException();
						}
					} catch (NumberFormatException e) {
						throw new FraudulentProcessException(e);
					}


					inputForm.fromName[i] = entity.fromName;
					i++;
				}

				inputForm.mailBody = "";
				inputForm.subject = "";

			} else {
				TMail entity = mailService.getEntityByUser(toInt(inputForm.originalMailId[0]), userDto.customerId);
				Beans.copy(entity, inputForm).excludes("originalMailId", "arbeitApplicationId", "fromName").execute();
				inputForm.arbeitApplicationId[0] = String.valueOf(entity.arbeitApplicationId);
				inputForm.fromName = new String[1];
				inputForm.fromName[0] = entity.fromName;

				inputForm.mailBody = "\n\n" + WztStringUtil.insertQuotationMark(GourmetCareeConstants.MAIL_REPLY_QUOTATION_MARK, entity.body);
			}

			inputForm.setProcessFlgOk();
			inputForm.setExistDataFlgOk();

			StringBuffer name = new StringBuffer(inputForm.fromName[0]);
			for (int i = 1; i < inputForm.fromName.length; i++) {
				name.append(",");
				name.append(inputForm.fromName[i]);
			}

			String nameStr = name.toString();
			session.setAttribute("fromName", nameStr);

		} catch (WNoResultException e) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		inputForm.createRandomKey();

		return TransitionConstants.Application.JSP_SPC05C01;
	}

	/**
	 * 確認画面表示
	 * @return
	 */
	@Execute(validator = true, input = TransitionConstants.Application.JSP_SPC05C01)
	public String conf() {

		//必要なパラメータをチェック
		checkArg();

		if (!GourmetCareeUtil.eqString(inputForm.randomKey, inputForm.hiddenRandomKey)) {
			throw new ActionMessagesException("errors.pluralBrowser",
					MessageResourcesUtil.getMessage("msg.replyForm"),
					MessageResourcesUtil.getMessage("msg.reply"));
		}

		inputForm.setProcessFlgOk();

		saveToken();

		return TransitionConstants.Application.JSP_SPC05C02;
	}

	/**
	 * 遷移元の画面へ戻る処理
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	public String back() {

		//必要なパラメータをチェック
		checkArg();
		session.removeAttribute("fromName");

		if (inputForm.lumpSendFlg) {
			return "/arbeit/list/showList".concat(GourmetCareeConstants.REDIRECT_STR);
		}
		return String.format("/arbeitMail/detail/index/%s%s", inputForm.originalMailId[0], GourmetCareeConstants.REDIRECT_STR);
	}

	/**
	 * 戻るボタン押下時の処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05C01)
	public String correct() {

		//必要なパラメータをチェック
		checkArg();

		inputForm.setProcessFlgNg();

		return TransitionConstants.Application.JSP_SPC05C01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05C01)
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg
				|| !isTokenValid()) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		sendMail();


		return TransitionConstants.Application.JSP_SPC05C04;
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail() {


		for (String originalMailId : inputForm.originalMailId) {

			int mailId = NumberUtils.toInt(originalMailId);

			String applicantName;
			try {
				applicantName = sendMailLogic.getArbeitApplicantNameByMailId(mailId);
			} catch (WNoResultException e) {
				//対象が存在しない場合は楽観的排他制御のエラーを返す
				throw new SOptimisticLockException();
			}

			ApplicationMailProperty property = Beans.createAndCopy(ApplicationMailProperty.class, inputForm)
					.excludes("originalMailId").execute();
			property.body = PatternSentenceUtil.replacePattern(inputForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);
			property.originalMailId = mailId;

			try {
				//詳細画面から遷移の場合は削除フラグの有無にかかわらずメールを取得できるようにする。
				SendMailLogic.MailPattern mailPattern = (ApplicationMailForm.FROM_APPLICANT_DETAIL.equals(inputForm.fromPageKbn))
						? MailPattern.FIRST_MAIL_REPLY : MailPattern.NORMAL_REPLY;

				sendMailLogic.doReplyArbeitMail(property, mailPattern);
			} catch (WNoResultException e) {
				//対象が存在しない場合は楽観的排他制御のエラーを返す
				throw new SOptimisticLockException();
			}

		}

	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {
		session.removeAttribute("fromName");
		return TransitionConstants.Application.JSP_SPC05C03;
	}


	/**
	 * !!target_member!! をリプレースしたものを返します。
	 */
	public String getReplacedBody() {
		if (StringUtils.isBlank(inputForm.mailBody)) {
			return "";
		}

		return inputForm.mailBody.replaceAll(ShopServiceConstants.PatternReplace.MEMBER_NAME, inputForm.fromName[0]);
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.mailInstance();
	}
}
