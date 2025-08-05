package com.gourmetcaree.shop.pc.preApplication.action.preApplicationMail;

import static org.apache.commons.lang.math.NumberUtils.*;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic.MailPattern;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;
import com.gourmetcaree.shop.pc.application.form.applicationMail.InputForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.commons.WztStringUtil;


/**
 * メール入力をするアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class InputAction extends MailListAction {

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

	@Resource
	private PreApplicationService preApplicationService;

	/** メール返信時に頭に付ける文字 */
	private final String RE = "re:";

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPP02C01)
	public String index() {

		checkArgs();

		//必要なパラメータをチェック
		for (int i = 0; i < inputForm.originalMailId.length; i++) {
			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.applicationId[i], inputForm.fromPageKbn);
		}
		return show();
	}

	private void checkArgs() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.fromPageKbn);

		if (ArrayUtils.isEmpty(inputForm.originalMailId)) {
			throw new FraudulentProcessException("メールIDが指定されていません。");
		}

		if (ArrayUtils.isEmpty(inputForm.applicationId)) {
			throw new FraudulentProcessException("メールIDが指定されていません。");
		}

		if (inputForm.originalMailId.length != inputForm.applicationId.length) {
			throw new FraudulentProcessException("メールIDと応募IDの数が合いません。");
		}
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForLumpSend", input = TransitionConstants.Application.JSP_SPP02C01)
	public String lumpSend() {

		//必要なパラメータをチェック
		if (inputForm.originalMailId.length != inputForm.applicationId.length) {
			throw new FraudulentProcessException();
		}

		for (int i = 0; i < inputForm.originalMailId.length; i++) {

			checkNumber(inputForm.originalMailId[i]);
			checkNumber(inputForm.applicationId[i]);

			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.applicationId[i], inputForm.fromPageKbn);
		}

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		try {
			//応募一覧から来た場合は本文を空にする。それ以外は本文を引用形式に変更
			if (ApplicationMailForm.FROM_APPLICANT_DETAIL.equals(inputForm.fromPageKbn)) {
				inputForm.fromName = new String[1];
				TMail entity = mailService.getEntityIgnoreDeleteByUser(toInt(inputForm.originalMailId[0]), userDto.customerId);
				Beans.copy(entity, inputForm).excludes("applicationId", "fromName").execute();
				inputForm.applicationId[0] = String.valueOf(entity.applicationId);
				inputForm.fromName[0] = String.valueOf(entity.fromName);
				inputForm.mailBody = "";

			// 一括送信の時は、件名、本文が無い状態にする
			} else if (ApplicationMailForm.FROM_APPLICATION_LIST.equals(inputForm.fromPageKbn)) {

				inputForm.fromName = new String[inputForm.originalMailId.length];

				int i = 0;
				for (String originalMailId : inputForm.originalMailId) {

					TMail entity = mailService.getEntityIgnoreDeleteByUser(toInt(originalMailId), userDto.customerId);

					Beans.copy(entity, inputForm).excludes("applicationId", "fromName").execute();

					try {
						int applicationId = Integer.parseInt(inputForm.applicationId[i]);
						if (!GourmetCareeUtil.eqInt(applicationId, entity.applicationId)) {
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
				inputForm.fromName = new String[1];
				TMail entity = mailService.getEntityByUser(toInt(inputForm.originalMailId[0]), userDto.customerId);
				Beans.copy(entity, inputForm).excludes("applicationId", "fromName").execute();
				inputForm.applicationId[0] = String.valueOf(entity.applicationId);

				inputForm.fromName[0] = String.valueOf(entity.fromId);
				if(entity.parentMailId == null) {
					inputForm.mailBody = "";
					inputForm.subject = "ご応募ありがとうございます";
				} else {
					inputForm.mailBody = "\n\n" + WztStringUtil.insertQuotationMark(GourmetCareeConstants.MAIL_REPLY_QUOTATION_MARK, entity.body);
	                if(inputForm.subject.indexOf(RE) == -1) {
	                	inputForm.subject = RE + entity.subject;
					} else {
						inputForm.subject = entity.subject;
					}
				}
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

		return TransitionConstants.Application.JSP_SPP02C01;
	}

	/**
	 * 確認画面表示
	 * @return
	 */
	@Execute(validator = true, input = TransitionConstants.Application.JSP_SPP02C01)
	public String conf() {

		checkArgs();

		//必要なパラメータをチェック
		for (int i = 0; i < inputForm.originalMailId.length; i++) {
			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.applicationId[i], inputForm.fromPageKbn);
		}

		inputForm.setProcessFlgOk();

		super.saveToken();

		return TransitionConstants.Application.JSP_SPP02C02;
	}

	/**
	 * 遷移元の画面へ戻る処理
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	public String back() {

		checkArgs();

		//必要なパラメータをチェック
		for (int i = 0; i < inputForm.originalMailId.length; i++) {
			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.applicationId[i], inputForm.fromPageKbn);
		}
		session.removeAttribute("fromName");

		if (ApplicationMailForm.FROM_MAIL_DETAIL.equals(inputForm.fromPageKbn)) {
			return   String.format("/preApplicationMail/detail/indexAgain/%s%s", inputForm.originalMailId[0], GourmetCareeConstants.REDIRECT_STR);
		} else {
			return "/preApplication/list/showList".concat(GourmetCareeConstants.REDIRECT_STR);
		}
	}

	/**
	 * 戻るボタン押下時の処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP02C01)
	public String correct() {

		checkArgs();

		//必要なパラメータをチェック
		for (int i = 0; i < inputForm.originalMailId.length; i++) {
			checkArgsNull(NO_BLANK_FLG_NG, inputForm.originalMailId[i], inputForm.applicationId[i], inputForm.fromPageKbn);
		}

		inputForm.setProcessFlgNg();

		return TransitionConstants.Application.JSP_SPP02C01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP02C01)
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg || !super.isTokenValid()) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		sendMail();

		return TransitionConstants.Application.REDIRECT_APPLICATIONMAIL_INPUT_COMP;
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail() {


		for (String originalMailId :inputForm.originalMailId) {

			int mailId = NumberUtils.toInt(originalMailId);

			ApplicationMailProperty property = Beans.createAndCopy(ApplicationMailProperty.class, inputForm)
					.excludes("originalMailId").execute();
			property.body = PatternSentenceUtil.replacePattern(inputForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, inputForm.fromName[0]);
			property.originalMailId = mailId;

			try {
				//詳細画面から遷移の場合は削除フラグの有無にかかわらずメールを取得できるようにする。
				SendMailLogic.MailPattern mailPattern = (ApplicationMailForm.FROM_APPLICANT_DETAIL.equals(inputForm.fromPageKbn))
						? MailPattern.FIRST_MAIL_REPLY : MailPattern.NORMAL_REPLY;

				sendMailLogic.doReplyPreApplicationMail(property, mailPattern);
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
		return TransitionConstants.Application.JSP_SPC02C03;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.APPLICATION_MAIL;
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

}
