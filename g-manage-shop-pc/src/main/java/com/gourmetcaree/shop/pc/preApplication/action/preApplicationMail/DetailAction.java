package com.gourmetcaree.shop.pc.preApplication.action.preApplicationMail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.entity.TPreApplicationCareerHistory;
import com.gourmetcaree.db.common.entity.TPreApplicationCareerHistoryAttribute;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.PreApplicationAttributeService;
import com.gourmetcaree.db.common.service.PreApplicationCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.PreApplicationCareerHistoryService;
import com.gourmetcaree.db.common.service.PreApplicationSchoolHistoryService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic.MailPattern;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.dto.application.CareerHistoryDto;
import com.gourmetcaree.shop.pc.preApplication.dto.preApplicationMail.PreApplicationMailListDto;
import com.gourmetcaree.shop.pc.preApplication.form.preApplicationMail.DetailForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.sys.enums.MailListKbn;


/**
 * プレ応募メール詳細を表示するアクションクラスです。
 */
@ManageLoginRequired
public class DetailAction extends MailListAction {

	/** プレ応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	protected PreApplicationService preApplicationService;

	@Resource
	protected PreApplicationAttributeService preApplicationAttributeService;

	@Resource
	protected PreApplicationCareerHistoryService preApplicationCareerHistoryService;

	@Resource
	protected PreApplicationCareerHistoryAttributeService preApplicationCareerHistoryAttributeService;

	@Resource
	protected PreApplicationSchoolHistoryService preApplicationSchoolHistoryService;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/** 詳細一覧に戻るためのフラグ(本番でメール区分のセッションの動きがおかしいために作成) */
	private boolean returnDetailListFlg = false;

	public List<PreApplicationMailListDto> mailList;

	public TPreApplication preApplication;

	/** メール一覧区分 */
	private static final String MAIL_LIST_KBN = "com.gourmetcaree.shop.pc.application.action.applicationMail.DetailAction.MAIL_LIST_KBN";

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{applicationId}", input = TransitionConstants.Application.JSP_SPP02R01)
	public String index() {

		session.removeAttribute(DetailForm.APPLICATION_DETAIL_MAIL_LIST_ID);
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);
		returnDetailListFlg = false;
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexAgain/{applicationId}", input = TransitionConstants.Application.JSP_SPP02R01)
	public String indexAgain() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, String.valueOf(detailForm.applicationId));
		returnDetailListFlg = false;
		return show();
	}



	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}/{applicationId}", input = TransitionConstants.Application.JSP_SPC02R01)
	public String indexApplicationIdDetailMailList() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		session.setAttribute(DetailForm.APPLICATION_DETAIL_MAIL_LIST_ID, detailForm.applicationId);
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);
		returnDetailListFlg = false;
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexFromDetailList/{id}/{applicationId}", input = TransitionConstants.Application.JSP_SPP02R01)
	public String indexFromDetailList() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		session.setAttribute(DetailForm.APPLICATION_DETAIL_MAIL_LIST_ID, detailForm.applicationId);
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.DETAIL_LIST);
		returnDetailListFlg = true;
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		session.removeAttribute("originaMailId");
		session.removeAttribute("applicationId");

		try {
			preApplication = preApplicationLogic.findByIdFromPreApplication(detailForm.applicationId);


			detailForm.qualificationKbnList = GourmetCareeUtil.toIntToStringArray(
					preApplicationAttributeService.getPreApplicationAttrValue(
							preApplication.id, MTypeConstants.QualificationKbn.TYPE_CD));

			detailForm.applicationSchoolHistory = preApplicationSchoolHistoryService.getPreApplicationSchoolHistoryByApplicationId(preApplication.id);

			List<TPreApplicationCareerHistory> entityList = preApplicationCareerHistoryService.getPreApplicationCareerHistoryByApplicationId(preApplication.id);
			List<CareerHistoryDto> dtoList = new ArrayList<CareerHistoryDto>();
			if(entityList != null) {
				for (TPreApplicationCareerHistory entity : entityList) {
					entity.tpreApplicationCareerHistoryAttributeList = preApplicationCareerHistoryAttributeService.getPreApplicationCareerHistoryByApplicationCareerHistoryId(entity.id);
					CareerHistoryDto dto = new CareerHistoryDto();
					Beans.copy(entity, dto).execute();

					List<String> jobList = new ArrayList<String>();
					List<String> industryList = new ArrayList<String>();

					if(entity.tpreApplicationCareerHistoryAttributeList != null) {
						for (TPreApplicationCareerHistoryAttribute attrEntity : entity.tpreApplicationCareerHistoryAttributeList) {

							// 職種
							if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
								jobList.add(String.valueOf(attrEntity.attributeValue));
								continue;
							}

							// 業態
							if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
								industryList.add(String.valueOf(attrEntity.attributeValue));
								continue;
							}
						}
					}

					// 職種の値設定
					if (CollectionUtils.isNotEmpty(jobList)) {
						dto.jobKbnName = valueToNameConvertLogic.convertToJobName(jobList.toArray(new String[0])).replace(",", "\n");
					} else {
						dto.jobKbnName = "";
					}

					// 業態の値設定
					if (CollectionUtils.isNotEmpty(industryList)) {
						dto.industryKbnName = valueToNameConvertLogic.convertToIndustryName(industryList.toArray(new String[0])).replace(",", "\n");
					} else {
						dto.industryKbnName = "";
					}

					dtoList.add(dto);
				}
			}

			detailForm.applicationCareerHistoryList = dtoList;

			// 全件取得するために0-200で設定
			PagerProperty property = new PagerProperty();
			property.targetPage = 0;
			property.maxRow = 200;

			mailList = new ArrayList<>();

			for(MailSelectDto mailDto : preApplicationLogic.getApplicationIdEachMail(property, detailForm.applicationId, MAIL_KBN.PRE_APPLICATION_MAIL).retList) {
				PreApplicationMailListDto applicationDto = new PreApplicationMailListDto();
				applicationDto.senderKbn = mailDto.senderKbn;
				applicationDto.subject = mailDto.subject;
				applicationDto.body = mailDto.body;
				applicationDto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(mailDto.sendDatetime);
				applicationDto.receiveReadingDatetime = mailDto.receiveReadingDatetime;
				mailList.add(applicationDto);

				if(mailDto.sendKbn == MTypeConstants.SendKbn.RECEIVE && mailDto.senderKbn == MTypeConstants.SenderKbn.MEMBER) {
					session.setAttribute("originaMailId", mailDto.id);
					detailForm.subject = "re:" + mailDto.subject;
				}

				// メールステータスの更新
				if(mailDto.sendKbn == MTypeConstants.SendKbn.RECEIVE && mailDto.senderKbn == MTypeConstants.SenderKbn.MEMBER
						&& mailDto.mailStatus == MTypeConstants.MailStatus.UNOPENED
						&& !userDto.isMasqueradeFlg()) {
					preApplicationLogic.changeMailToOpened(mailDto.id);
				}
			}

			detailForm.setProcessFlgOk();
			detailForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			//changeMailToOpenedからもスローされるので注意
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		try {
			memberService.findById(preApplication.memberId);
		} catch(SNoResultException e) {
			detailForm.unsubscribeFlg = true;
		}

		session.setAttribute("applicationId", detailForm.applicationId);
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPP02R01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP02R01)
	public String submit() {

		Integer originaMailId = (Integer)session.getAttribute("originaMailId");
		Integer applicationId = (Integer)session.getAttribute("applicationId");

		if(originaMailId == null || applicationId == null || !applicationId.equals(detailForm.applicationId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + detailForm);
		}

		if(detailForm.mailBody.isEmpty() || detailForm.subject.isEmpty()) {
			detailForm.errorMessage = "件名と本文を入力してください。";
			return "/preApplicationMail/detail/indexAgain/" + applicationId;
		}

		sendMail(originaMailId);

		session.removeAttribute("originaMailId");
		session.removeAttribute("applicationId");

		return "/preApplicationMail/detail/index/" + applicationId +"?redirect=true";
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail(Integer originaMailId) {

		String applicantName;
		try {
			applicantName = sendMailLogic.getPreApplicantNameByMailId(originaMailId);
		} catch (WNoResultException e) {
			//対象が存在しない場合は楽観的排他制御のエラーを返す
			throw new SOptimisticLockException();
		}

		ApplicationMailProperty property = new ApplicationMailProperty();
		property.originalMailId = originaMailId;
		property.subject = PatternSentenceUtil.replacePattern(detailForm.subject, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);
		property.body = PatternSentenceUtil.replacePattern(detailForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, applicantName);

		try {
			sendMailLogic.doReplyPreApplicationMail(property, MailPattern.NORMAL_REPLY);
		} catch (WNoResultException e) {
			throw new SOptimisticLockException();
		}
	}

	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, urlPattern = "delete/{id}/{deleteVersion}", input = TransitionConstants.Application.JSP_SPP02R01)
	public String delete() {


		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.deleteVersion);

		try {
			//データの存在を確認し、バージョン情報のみフォームの情報を使用して論理削除
			TMail entity = preApplicationLogic.findByIdFromMail(NumberUtils.toInt(detailForm.id));
			entity.version = NumberUtils.toLong(detailForm.deleteVersion);
			mailService.logicalDelete(entity);

		} catch (WNoResultException e) {
			//削除対象が存在しない場合は楽観的排他制御のエラーを返す
			throw new SOptimisticLockException();
		}

		return TransitionConstants.Application.JSP_SPC02D03;
	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Application.JSP_SPP02R01)
	public String comp() {
		return TransitionConstants.Application.JSP_SPC02D03;
	}

	public String getBackUrl() {
		Object obj = session.getAttribute(MAIL_LIST_KBN);
		if (obj == null || !(obj instanceof MailListKbn)) {
			return "/preApplicationMail/list/showList";
		}

		MailListKbn kbn = (MailListKbn) obj;

		if (kbn == MailListKbn.DETAIL_LIST && returnDetailListFlg) {
			return "/preApplication/detailMailList/showList";
		}
		return "/preApplicationMail/list/showList";
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.PRE_APPLICATION_MAIL;
	}

}
