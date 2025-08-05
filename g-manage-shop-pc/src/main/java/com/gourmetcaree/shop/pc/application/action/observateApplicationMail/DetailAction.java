package com.gourmetcaree.shop.pc.application.action.observateApplicationMail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic.MailPattern;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;
import com.gourmetcaree.shop.pc.application.form.observateApplicationMail.DetailForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.sys.enums.MailListKbn;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * 応募メール詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class DetailAction extends PcShopAction {

	/** 応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	@Resource
	private MemberService memberService;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** 店舗見学サービス */
	@Resource
	protected ObservateApplicationService observateApplicationService;

	public List<ApplicationMailListDto> mailList;

	public TObservateApplication observateApplication;

	/** メール一覧区分 */
	private static final String MAIL_LIST_KBN = "com.gourmetcaree.shop.pc.application.action.observateApplicationMail.DetailAction.MAIL_LIST_KBN";

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{observateApplicationId}", input = TransitionConstants.Application.JSP_SPC04R01)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, String.valueOf(detailForm.observateApplicationId));

		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);

		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "indexAgain/{observateApplicationId}", input = TransitionConstants.Application.JSP_SPC04R01)
	public String indexAgain() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, String.valueOf(detailForm.observateApplicationId));

		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}/{dispObservateApplicationId}", input = TransitionConstants.Application.JSP_SPC04R01)
	public String detailIndex() {

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
		session.removeAttribute("observateApplicationId");

		try {
			observateApplication = applicationLogic.findByIdFromObservateApplication(detailForm.observateApplicationId);
			observateApplication.age = GourmetCareeUtil.convertToAge(observateApplication.birthday);
			if(StringUtils.isBlank(observateApplication.name)) {
				observateApplication.name = "匿名";
			}

			// 全件取得するために0-200で設定
			PagerProperty property = new PagerProperty();
			property.targetPage = 0;
			property.maxRow = 200;

			mailList = new ArrayList<>();

			for(MailSelectDto mailDto : applicationLogic.getApplicationIdEachMail(property, detailForm.observateApplicationId, MAIL_KBN.OBSERVATE_APPLICATION).retList) {
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
					applicationLogic.changeObservationMailToOpened(mailDto.id);
				}
			}

			detailForm.setProcessFlgOk();
			detailForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			//changeMailToOpenedからもスローされるので注意
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		if(observateApplication.memberId != null) {
			try {
				memberService.findById(observateApplication.memberId);
			} catch(SNoResultException e) {
				detailForm.unsubscribeFlg = true;
			}
		}

		session.setAttribute("observateApplicationId", detailForm.observateApplicationId);
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC04R01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04R01)
	public String submit() {

		Integer originaMailId = (Integer)session.getAttribute("originaMailId");
		Integer observateApplicationId = (Integer)session.getAttribute("observateApplicationId");

		if(originaMailId == null || observateApplicationId == null || !observateApplicationId.equals(detailForm.observateApplicationId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + detailForm);
		}

		if(detailForm.mailBody.isEmpty() || detailForm.subject.isEmpty()) {
			detailForm.errorMessage = "件名と本文を入力してください。";
			return "/applicationMail/detail/indexAgain/" + observateApplicationId;
		}

		sendMail(originaMailId);

		session.removeAttribute("originaMailId");
		session.removeAttribute("observateApplicationId");

		return "/observateApplicationMail/detail/index/" + observateApplicationId +"?redirect=true";
	}

	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, urlPattern = "delete/{id}/{deleteVersion}", input = TransitionConstants.Application.JSP_SPC04R01)
	public String delete() {

//		if (!detailForm.processFlg) {
//			throw new FraudulentProcessException("不正な操作が行われました。" + detailForm);
//		}

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

		return TransitionConstants.Application.JSP_SPC04D03;
	}


	public String getBackUrl() {
		Object obj = session.getAttribute(MAIL_LIST_KBN);
		if (obj == null || !(obj instanceof MailListKbn)) {
			return "/observateApplicationMail/list/showList";
		}

		MailListKbn kbn = (MailListKbn) obj;
		if (kbn == MailListKbn.DETAIL_LIST) {
			String id;
			try {
				TMail entity = mailService.findById(NumberUtils.toInt(detailForm.id));
				id = String.valueOf(entity.observateApplicationId);
			} catch (SNoResultException e) {
				return "/observateApplicationMail/list/showList";
			}
			return String.format("/observateApplication/detailMailList/index/%s", id);
		}

		return "/observateApplicationMail/list/showList";
	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Application.JSP_SPC04R01)
	public String comp() {
		return TransitionConstants.Application.JSP_SPC02D03;
	}

	/**
	 * 応募メール詳細のダイアログ
	 * @return
	 */
	@Execute(validator = false, urlPattern = "subApplicationDetail/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04R01_SUB)
	public String subApplicationDetail() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);
		createDetailData();

		return TransitionConstants.Application.JSP_SPC04R01_SUB;
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail(Integer originaMailId) {

		String applicantName;
		try {
			applicantName = sendMailLogic.getObservateApplicantNameByMailId(originaMailId);
		} catch (WNoResultException e) {
			//対象が存在しない場合は匿名として扱う
			applicantName = "匿名";
		}

		ApplicationMailProperty property = new ApplicationMailProperty();
		property.originalMailId = originaMailId;
		property.subject = PatternSentenceUtil.replacePattern(detailForm.subject, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);
		property.body = PatternSentenceUtil.replacePattern(detailForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);

		try {
			sendMailLogic.doReplyObservateApplicationMail(property, MailPattern.NORMAL_REPLY);
		} catch (WNoResultException e) {
			throw new SOptimisticLockException();
		}
	}

	/**
	 * サブウィンドウ用詳細データ取得
	 */
	private void createDetailData(){

		try {
			TObservateApplication observateApplication = new TObservateApplication();
			observateApplication = observateApplicationService.getApplicationDataById(Integer.parseInt(detailForm.id));
			Beans.copy(observateApplication, detailForm).execute();
			// 電話番号
			detailForm.phoneNo = observateApplication.phoneNo1.concat("-").concat(observateApplication.phoneNo2).concat("-").concat(observateApplication.phoneNo3);
		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

    @Override
    public MenuInfo getMenuInfo() {
        return MenuInfo.mailInstance();
    }
}
