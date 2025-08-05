package com.gourmetcaree.shop.pc.application.action.arbeitMail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic.MailPattern;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;
import com.gourmetcaree.shop.pc.application.form.arbeitMail.DetailForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.sys.enums.MailListKbn;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * アルバイトメール詳細アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class DetailAction extends PcShopAction {

	/** アクションフォーム */
	@ActionForm
	@Resource
	private DetailForm detailForm;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	public List<ApplicationMailListDto> mailList;

	public TArbeitApplication arbeitApplication;

	/** メール一覧区分 */
	private static final String MAIL_LIST_KBN = "com.gourmetcaree.shop.pc.application.action.arbeitMail.DetailAction.MAIL_LIST_KBN";


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{arbeitApplicationId}", input = TransitionConstants.Application.JSP_SPC05R01)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, String.valueOf(detailForm.arbeitApplicationId));
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "indexAgain/{arbeitApplicationId}", input = TransitionConstants.Application.JSP_SPC05R01)
	public String indexAgain() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, String.valueOf(detailForm.arbeitApplicationId));
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexFromDetailList/{id}", input = TransitionConstants.Application.JSP_SPC05R01)
	public String indexFromDetailList() {
		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.DETAIL_LIST);
		return show();
	}


	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		session.removeAttribute("originaMailId");
		session.removeAttribute("arbeitApplicationId");

		try {
			arbeitApplication = applicationLogic.findByIdFromArbeitApplication(detailForm.arbeitApplicationId);

			// 全件取得するために0-200で設定
			PagerProperty property = new PagerProperty();
			property.targetPage = 0;
			property.maxRow = 200;

			mailList = new ArrayList<>();

			for(MailSelectDto mailDto : applicationLogic.getApplicationIdEachMail(property, detailForm.arbeitApplicationId, MAIL_KBN.ARBEIT_APPLICATION).retList) {
				ApplicationMailListDto applicationDto = new ApplicationMailListDto();
				applicationDto.senderKbn = mailDto.senderKbn;
				applicationDto.subject = mailDto.subject;
				applicationDto.body = mailDto.body;
				applicationDto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(mailDto.sendDatetime);
				applicationDto.receiveReadingDatetime = mailDto.receiveReadingDatetime;
				mailList.add(applicationDto);

				if(mailDto.sendKbn == MTypeConstants.SendKbn.RECEIVE &&
						(mailDto.senderKbn == MTypeConstants.SenderKbn.MEMBER || mailDto.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER)) {
					session.setAttribute("originaMailId", mailDto.id);
					detailForm.subject = "re:" + mailDto.subject;
				}

				// メールステータスの更新
				if(mailDto.sendKbn == MTypeConstants.SendKbn.RECEIVE
						&& (mailDto.senderKbn == MTypeConstants.SenderKbn.MEMBER || mailDto.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER)
						&& mailDto.mailStatus == MTypeConstants.MailStatus.UNOPENED
						&& !userDto.isMasqueradeFlg()) {
					applicationLogic.changeArbeitMailToOpened(mailDto.id);
				}
			}

			detailForm.setProcessFlgOk();
			detailForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			//changeMailToOpenedからもスローされるので注意
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		session.setAttribute("arbeitApplicationId", detailForm.arbeitApplicationId);

		return TransitionConstants.Application.JSP_SPC05R01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05R01)
	public String submit() {

		Integer originaMailId = (Integer)session.getAttribute("originaMailId");
		Integer arbeitApplicationId = (Integer)session.getAttribute("arbeitApplicationId");

		if(originaMailId == null || arbeitApplicationId == null || !arbeitApplicationId.equals(detailForm.arbeitApplicationId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + detailForm);
		}

		if(detailForm.mailBody.isEmpty() || detailForm.subject.isEmpty()) {
			detailForm.errorMessage = "件名と本文を入力してください。";
			return "/arbeitMail/detail/indexAgain/" + arbeitApplicationId;
		}

		sendMail(originaMailId);

		session.removeAttribute("originaMailId");
		session.removeAttribute("arbeitApplicationId");

		return "/arbeitMail/detail/index/" + arbeitApplicationId +"?redirect=true";
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail(Integer originaMailId) {

		String applicantName;
		try {
			applicantName = sendMailLogic.getArbeitApplicantNameByMailId(originaMailId);
		} catch (WNoResultException e) {
			//対象が存在しない場合は楽観的排他制御のエラーを返す
			throw new SOptimisticLockException();
		}

		ApplicationMailProperty property = new ApplicationMailProperty();
		property.originalMailId = originaMailId;
		property.subject = PatternSentenceUtil.replacePattern(detailForm.subject, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);
		property.body = PatternSentenceUtil.replacePattern(detailForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);

		try {
			sendMailLogic.doReplyArbeitMail(property, MailPattern.NORMAL_REPLY);
		} catch (WNoResultException e) {
			throw new SOptimisticLockException();
		}
	}



	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, urlPattern = "delete/{id}/{deleteVersion}", input = TransitionConstants.Application.JSP_SPC05R01)
	public String delete() {


		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.deleteVersion);

		try {
			//データの存在を確認し、バージョン情報のみフォームの情報を使用して論理削除
			TMail entity = applicationLogic.findByIdFromMail(NumberUtils.toInt(detailForm.id));
			entity.version = NumberUtils.toLong(detailForm.deleteVersion);
			mailService.logicalDelete(entity);

		} catch (WNoResultException e) {
			//削除対象が存在しない場合は楽観的排他制御のエラーを返す
			throw new SOptimisticLockException();
		}

		return TransitionConstants.Application.JSP_SPC05D04;
	}

	/**
	 * 戻る用パス取得
	 */
	public String getBackUrl() {
		Object obj = session.getAttribute(MAIL_LIST_KBN);
		if (obj == null || !(obj instanceof MailListKbn)) {
			return "/arbeitMail/list/showList/";
		}

		MailListKbn kbn = (MailListKbn) obj;
		if (kbn == MailListKbn.DETAIL_LIST) {
			return "/arbeit/detailMailList/showList";
		}

		return "/arbeitMail/list/showList/";
	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Application.JSP_SPC05R01)
	public String comp() {
		return TransitionConstants.Application.JSP_SPC02D03;
	}

    @Override
    public MenuInfo getMenuInfo() {
        return MenuInfo.mailInstance();
    }
}
