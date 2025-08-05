package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.dto.ScoutMailDetailDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutMailTargetDto;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutmailSearchProperty;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.logic.util.PatternSentenceUtil;
import com.gourmetcaree.shop.pc.application.dto.application.CareerHistoryDto;
import com.gourmetcaree.shop.pc.application.dto.application.MailForApplicationIdDto;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.DetailForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.sys.enums.MailListKbn;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * スカウトメール詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class DetailAction extends ScoutMailBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 求職者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** 履歴書 */
	@Resource
	protected CareerHistoryService careerHistoryService;

	@Resource
	protected CareerHistoryAttributeService careerHistoryAttributeService;

	/** スカウト会員情報リスト */
	public ScoutMailTargetDto scoutMailTargetDto;

	/** メールを保持するリスト */
	public List<MailForApplicationIdDto> dataList;

	/** メール詳細画面ID */
	public static final String DISP_MAIL_DETAIL = "spE02R01";

	/** メール一覧区分 */
	private static final String MAIL_LIST_KBN = "com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail.DetailAction.MAIL_LIST_KBN";



	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", urlPattern = "index/{scoutMailLogId}", input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String index() {
		detailForm.fromMenu = DetailForm.FromMenuKbn.SCOUT_MAIL;
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);
		return show();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "indexAgain/{scoutMailLogId}", input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String indexAgain() {

		detailForm.fromMenu = DetailForm.FromMenuKbn.SCOUT_MAIL;
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);
		return show();
	}


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", urlPattern = "indexFromDetailList/{id}/{sendKbn}", input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String indexFromDetailList() {
		detailForm.fromMenu = DetailForm.FromMenuKbn.SCOUT_MAIL;
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.DETAIL_LIST);
		return show();
	}


	/**
	* 初期表示(メールBOXからの遷移)
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", urlPattern = "indexFromMail/{id}/{sendKbn}", input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String indexFromMail() {
		detailForm.fromMenu = DetailForm.FromMenuKbn.MAIL_BOX;
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.LIST);
        this.menuInfo = MenuInfo.mailInstance();
		return show();
	}


	/**
	 * 初期表示(メールBOXからの遷移)
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", urlPattern = "indexFromDetailListFromMail/{id}/{sendKbn}", input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String indexFromDetailListFromMail() {
		detailForm.fromMenu = DetailForm.FromMenuKbn.MAIL_BOX;
		session.setAttribute(MAIL_LIST_KBN, MailListKbn.DETAIL_LIST);
		return show();
	}


	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		session.removeAttribute("originaMailId");
		session.removeAttribute("scoutMailLogId");
		session.removeAttribute("memberId");

		try {

			// スカウト者情報を取得する
			ScoutmailSearchProperty prop = new ScoutmailSearchProperty();
			prop.maxRow = getInitMaxRow();
			prop.scountId = Integer.parseInt(detailForm.scoutMailLogId);

			List<ScoutMailTargetDto> dtoList = scoutMailLogic.selectScoutTargetList(prop);

			scoutMailTargetDto = Beans.createAndCopy(ScoutMailTargetDto.class, dtoList.get(0)).execute();

			scoutMailTargetDto.age = scoutMailTargetDto.getAge();

			createHistories(scoutMailTargetDto.memberId);

			session.setAttribute("memberId", scoutMailTargetDto.memberId);

			// スカウトメールを取得する 全件取得するために0-200で設定
			PagerProperty property = new PagerProperty();
			property.targetPage = 0;
			property.maxRow = 200;

			// 応募IDをキーに応募者のメール（送受信）を取得する
			List<MailForApplicationIdDto> tmpList = new ArrayList<MailForApplicationIdDto>();
			ApplicationMailRetDto retDto = applicationLogic.getApplicationIdEachMail(property, Integer.parseInt(detailForm.scoutMailLogId), MAIL_KBN.SCOUT_MAIL);

			for (MailSelectDto entity : retDto.retList) {

				MailForApplicationIdDto mMailForApplicationIdDto = new MailForApplicationIdDto();
				Beans.copy(entity, mMailForApplicationIdDto).execute();

				mMailForApplicationIdDto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(entity.sendDatetime);

				if(entity.sendKbn == MTypeConstants.SendKbn.RECEIVE &&
						(entity.senderKbn == MTypeConstants.SenderKbn.MEMBER || entity.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER)) {
					session.setAttribute("originaMailId", entity.id);
					detailForm.subject = "re:" + entity.subject;
				}

				if(entity.sendKbn == MTypeConstants.SendKbn.RECEIVE
						&& (entity.senderKbn == MTypeConstants.SenderKbn.MEMBER || entity.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER)
						&& scoutMailTargetDto.exitMemberFlg) {
					detailForm.isSendMailFlg = true;
				}
				// メールステータスの更新
				if(entity.sendKbn == MTypeConstants.SendKbn.RECEIVE
						&& (entity.senderKbn == MTypeConstants.SenderKbn.MEMBER || entity.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER)
						&& entity.mailStatus == MTypeConstants.MailStatus.UNOPENED
						&& !userDto.isMasqueradeFlg()) {
					scoutMailLogic.changeMailToOpened(entity.id);
				}

				tmpList.add(mMailForApplicationIdDto);
			}

			dataList = tmpList;


		} catch (NumberFormatException | WNoResultException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		session.setAttribute("scoutMailLogId", NumberUtils.toInt(detailForm.scoutMailLogId));
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE02R01;
	}

	/**
	 * 一覧に戻る
	 * @return
	 */
	@Execute(validator = false)
	public String back() {

		Object obj = session.getAttribute(MAIL_LIST_KBN);
		if (obj == null || !(obj instanceof MailListKbn)) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST_SEARCH_AGAIN;
		}

		MailListKbn kbn = (MailListKbn) obj;

		if (kbn == MailListKbn.DETAIL_LIST) {
			return TransitionConstants.Application.REDIRECT_SCOUT_DETAIL_LIST_SHOW_LIST;
		} else {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST_SEARCH_AGAIN;
		}
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String submit() {

		Integer originaMailId = (Integer)session.getAttribute("originaMailId");
		Integer scoutMailLogId = (Integer)session.getAttribute("scoutMailLogId");
		Integer memberId = (Integer)session.getAttribute("memberId");

		if(originaMailId == null || scoutMailLogId == null || memberId == null || !scoutMailLogId.equals(NumberUtils.toInt(detailForm.scoutMailLogId))) {
			throw new FraudulentProcessException("不正な操作が行われました。" + detailForm);
		}

		if(detailForm.mailBody.isEmpty() || detailForm.subject.isEmpty()) {
			detailForm.errorMessage = "件名と本文を入力してください。";
			return "/scoutMail/detail/indexAgain/" + scoutMailLogId;
		}

		sendMail(originaMailId, scoutMailLogId, memberId);

		session.removeAttribute("originaMailId");
		session.removeAttribute("scoutMailLogId");
		session.removeAttribute("memberId");

		return "/scoutMail/detail/index/" + scoutMailLogId +"?redirect=true";
	}

	/**
	 * メールの送信処理
	 */
	private void sendMail(Integer originaMailId, Integer scoutMailLogId, Integer memberId) {

		MMember member = memberLogic.findById(memberId);

		ScoutMailProperty property = new ScoutMailProperty();
		if(isConvertMemberName(scoutMailLogId)) {
			property.subject = PatternSentenceUtil.replacePattern(detailForm.subject, ShopServiceConstants.PatternReplace.MEMBER_NAME, member.memberName);
			property.body = PatternSentenceUtil.replacePattern(detailForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, member.memberName);
		} else {
			property.subject = PatternSentenceUtil.replacePattern(detailForm.subject, ShopServiceConstants.PatternReplace.MEMBER_NAME, String.valueOf(member.id));
			property.body = PatternSentenceUtil.replacePattern(detailForm.mailBody, ShopServiceConstants.PatternReplace.MEMBER_NAME, String.valueOf(member.id));
		}
		property.mailId = originaMailId;
		property.customerId = userDto.customerId;
		property.memberIdList = new ArrayList<Integer>();
		property.sendKbn = NumberUtils.toInt(GourmetCareeConstants.SEND_KBN_RETURN);
		property.scoutMailLogId = scoutMailLogId;
		property.memberIdList.add(memberId);

		scoutMailLogic.doReturnMail(property);
	}

	/**
	 * 詳細画面表示データを生成
	 */
	private void createDispData() {

		try {
			// メールデータを取得
			ScoutMailProperty  property = new ScoutMailProperty();
			property.customerId = userDto.customerId;
			property.tMail = mailService.getMailDataCustomer(Integer.parseInt(detailForm.id),
					property.customerId,
					MTypeConstants.MailKbn.SCOUT,
					NumberUtils.toInt(detailForm.sendKbn));

			// 表示データに変換
			ScoutMailDetailDto dto = scoutMailLogic.convertScoutMailDetailData(property);
			Beans.copy(dto, detailForm).execute();

			detailForm.scoutMailRemainDto = memberLogic.getRemainScoutMail(userDto.customerId);
		} catch (NumberFormatException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * 返信
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String returnMail() {
		return returnMailExecute(TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_INPUT_RETURN_MAIL);
	}

	/**
	 * 返信(メールBOXからの遷移)
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String returnMailFromMail() {
		return returnMailExecute(TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_INPUT_RETURN_MAIL_FROM_MAIL);
	}

	private String returnMailExecute(String constants) {

		// 返信可能かどうかチェック
		isPossibleReturnMail(detailForm);

		// 返信元データをセッションに保持
		session.setAttribute(InputAction.RETURN_MAIL_ID_SESSION_KEY, detailForm.id);
		session.setAttribute("displayId", DISP_MAIL_DETAIL);

		return constants;
	}

	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String deleteMail() {

		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.sendKbn);

		StringBuilder sb = new StringBuilder(0);
		sb.append(TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_DELETE_INDEX);
		sb.append("&id=");
		sb.append(detailForm.id);
		sb.append("&sendKbn=");
		sb.append(detailForm.sendKbn);

		return sb.toString();
	}

	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String deleteMailFromMail() {

		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.sendKbn);

		StringBuilder sb = new StringBuilder(0);
		sb.append(TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_DELETE_INDEX_FROM_MAIL);
		sb.append("&id=");
		sb.append(detailForm.id);
		sb.append("&sendKbn=");
		sb.append(detailForm.sendKbn);

		return sb.toString();
	}

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.application.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	private void createHistories(int memberId) {

		List<TCareerHistory> entityList;
		List<CareerHistoryDto> careerHistoryDtoList = new ArrayList<CareerHistoryDto>();
		try {
			entityList = careerHistoryService.getCareerHistoryDataByMemberId(memberId);
		} catch (WNoResultException e) {
			detailForm.careerHistoryList = new ArrayList<>();
			return;
		}

		for (TCareerHistory entity : entityList) {
			CareerHistoryDto dto = new CareerHistoryDto();
			Beans.copy(entity, dto).execute();
			try {
				entity.tCareerHistoryAttributeList = careerHistoryAttributeService.findByCareerHistoryId(entity.id);
				List<String> jobList = new ArrayList<String>();
				List<String> industryList = new ArrayList<String>();

				for (TCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {

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

				careerHistoryDtoList.add(dto);
			} catch (WNoResultException e) {
				careerHistoryDtoList.add(dto);
			}
		}

		detailForm.careerHistoryList = careerHistoryDtoList;
	}

	private boolean isConvertMemberName(Integer scoutMailLogId) {

		try {
			List<TMail> mailList = mailService.findByCondition(new SimpleWhere().eq("scoutMailLogId", scoutMailLogId));
			for(TMail mail : mailList) {
				if(mail.scoutReceiveKbn != null && MTypeConstants.ScoutReceiveKbn.REFUSAL == mail.scoutReceiveKbn) {
					return false;
				}
			}
		} catch (WNoResultException e) {
			return false;
		}

		return true;
	}


}
