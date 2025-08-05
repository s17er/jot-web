package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TInterest;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.InterestService;
import com.gourmetcaree.db.common.service.InterestService.InterestSearchProperty;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.MemberAttributeService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.ScoutMailLogService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.dto.ScoutMemberInfoDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutMailTargetDto;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutmailSearchProperty;
import com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail.InputAction;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember.ListForm;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember.ListForm.FromMenuKbn;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * スカウト一覧を表示するアクションクラスです。
 * @author Motoaki Hara
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends ScoutBaseAction {

	/** ログ */
	private static final Logger log = Logger.getLogger(ListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** メールの会員リスト */
	public static final String MAIL_MEMBER_LIST = ListAction.class.getPackage().getName().concat("MAIL_MEMBER_LIST");


	/** スカウトメール一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** スカウトメールログサービス */
	@Resource
	private ScoutMailLogService scoutMailLogService;


	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** スカウト会員情報リスト */
	public List<ScoutMemberInfoDto> scoutMemberInfoDtoList;

	/** スカウトメール対象リスト */
	private List<ScoutMailTargetDto> targetList;

	/** メールサービス */
	@Resource
	private MailService mailService;

	/** 気になるサービス */
	@Resource
	private InterestService interestService;

	/** 会員サービス */
	@Resource
	private MemberService memberService;

	/** 会員属性サービス */
	@Resource
	private MemberAttributeService memberAttributeService;

	/** Webサービス */
	@Resource
	private WebService webService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", input = TransitionConstants.Application.JSP_SPC06L02)
	public String index() {
		listForm.fromMenu = FromMenuKbn.SCOUT_MAIL;
		return show();
	}

	/**
	 * 初期表示(メールBOXメニューから遷移)
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", input = TransitionConstants.Application.JSP_SPC06L02)
	public String mailBox() {
		listForm.fromMenu = FromMenuKbn.MAIL_BOX;
		this.menuInfo = MenuInfo.mailInstance();
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// スカウト一覧初期表示リストを取得
		listForm.pageNum = String.valueOf(DEFAULT_PAGE);
		doCreatetList(DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC06L02;
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC06L02)
	public String outputCsv() {
		try {
			scoutMailLogic.outputCsv(response, "scoutMail");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.mailDataNotFound");
		} catch (IOException e) {
			throw new ActionMessagesException("errors.app.failedOutputCsv");
		} catch (Exception e) {
			log.warn("CSV出力時にエラーが発生しました。", e);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("CSV出力時にエラーが発生しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.warn("CSV出力時にエラーが発生しました。", e);
			}

			throw new ActionMessagesException("errors.app.failedOutputCsv");
		}
		return null;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input =TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String changePage() {

		// 表示リストを取得
		doCreatetList(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Application.JSP_SPC06L02;
	}


	/**
	 * 一覧作成
	 * @param sendKbn 送受信区分
	 * @param pageNavi ページナビヘルパー
	 */
	private void doCreatetList(int targetPage) {
		if (listForm.fromMenu == FromMenuKbn.MAIL_BOX) {
			this.menuInfo = MenuInfo.mailInstance();
		}
		ScoutmailSearchProperty prop = new ScoutmailSearchProperty();
		prop.maxRow = getInitMaxRow();
		prop.targetPage = targetPage;
		targetList = scoutMailLogic.selectScoutTargetList(prop);

		this.pageNavi = prop.pageNavi;

		if (targetList == null || targetList.size() == 0) {

			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		listForm.setExistDataFlgOk();
	}


	/**
	 * 一覧作成
	 * @param targetPage
	 * @param webId
	 */
	private void doCreatetList(int targetPage, int webId){
		if (listForm.fromMenu == FromMenuKbn.MAIL_BOX) {
			this.menuInfo = MenuInfo.mailInstance();
		}
		List<TInterest> list = interestService.selectInterestByWebId(webId);
		targetList = new ArrayList<>();
		for(TInterest entity : list) {
			try {
			 MMember member = memberService.findById(entity.memberId);
			 ScoutMailTargetDto dto = convertMemberInfoToScoutMailTargetDto(member);
			 dto.sendDatetime = entity.insertDatetime;
			 targetList.add(dto);
			}catch (SNoResultException e) {
				//処理なし
			}
		}

		InterestSearchProperty prop = new InterestSearchProperty();
		prop.maxRow = getInitMaxRow();
		prop.targetPage = targetPage;

		this.pageNavi = prop.pageNavi;

		if (targetList == null || targetList.size() == 0) {

			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		listForm.setExistDataFlgOk();

	}

	/**
	 * 会員の情報をスカウトメールdtoに変換する
	 * @param member
	 * @return
	 */
	private ScoutMailTargetDto convertMemberInfoToScoutMailTargetDto(MMember member) {
		ScoutMailTargetDto dto = new ScoutMailTargetDto();
		 Beans.copy(member, dto).execute();
		 dto.memberId = member.id;
		 dto.employPtnKbnList = Lists.transform(memberAttributeService.getMemberAttributeValueList(member.id, MTypeConstants.EmployPtnKbn.TYPE_CD), Functions.toStringFunction());
		 dto.industryKbnList = Lists.transform(memberAttributeService.getMemberAttributeValueList(member.id, MTypeConstants.IndustryKbn.TYPE_CD), Functions.toStringFunction());
		 dto.jobKbnList = Lists.transform(memberAttributeService.getMemberAttributeValueList(member.id, MTypeConstants.JobKbn.TYPE_CD), Functions.toStringFunction());
		return dto;
	}


	@Execute(validator = false, reset = "resetForMemo")
	public String addMemo() {
		scoutMailLogService.changeMemo(NumberUtils.toInt(listForm.mailLogId), listForm.memo);
		return null;
	}

	@Execute(validator = false, reset = "resetForSelection")
	public String changeSelectionFlg() {

		Integer selectionFlg;
		if (StringUtils.isBlank(listForm.selectionFlg)) {
			selectionFlg = null;
		} else {
			selectionFlg = NumberUtils.toInt(listForm.selectionFlg);
		}
		scoutMailLogService.changeSelectionFlg(NumberUtils.toInt(listForm.mailLogId), selectionFlg);
		return null;
	}

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.scoutMail.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	public List<ScoutMailTargetDto> getTargetList() {
		if (targetList == null) {
			return new ArrayList<ScoutMailLogic.ScoutMailTargetDto>();
		}
		return targetList;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC06L02)
	public String showList() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		doCreatetList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPC06L02;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.SCOUT_MAIL;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return
	 */
	@Execute(validator=false, input = TransitionConstants.Application.JSP_SPC06L02)
	public String searchAgain() {
		doCreatetList(Integer.parseInt(listForm.pageNum));
		return TransitionConstants.Application.JSP_SPC06L02;
	}


	/**
	 * 一括送信
	 * @return
	 */
	@Execute(validator = true, reset = "resetForLumpSend", input = TransitionConstants.Application.FWD_SCOUT_SEARCH_AGAIN)
	public String lumpSend() {
		return lumpSendMain();
	}

	/**
	 * 一括送信(メールBOXからの遷移)
	 * @return
	 */
	@Execute(validator = true, reset = "resetForLumpSend", input = TransitionConstants.Application.FWD_SCOUT_SEARCH_AGAIN)
	public String lumpSendFromMail() {
		lumpSendMain();
		return TransitionConstants.Application.REDIRECT_SCOUT_MAIL_RETURN_LUMP_SEND_FROM_MAILBOX;
	}

	/**
	 * 気になる応募者一覧画面に遷移する
	 * @return
	 */
	@Execute(validator = false, urlPattern = "focusInterest/{webId}", reset ="resetForm", input = TransitionConstants.Application.JSP_SPC06L02)
	public String focusInterest() {
		int webId = Integer.parseInt(listForm.webId);

		try {
			TWeb web = webService.findById(webId);

			if(userDto.customerId != (int)web.customerId) {
				throw new PageNotFoundException();
			}
		}catch (SNoResultException e) {
			throw new PageNotFoundException();
		}

		listForm.fromMenu = FromMenuKbn.SCOUT_MAIL;
		// スカウト一覧初期表示リストを取得
		listForm.pageNum = String.valueOf(DEFAULT_PAGE);
		doCreatetList(DEFAULT_PAGE, webId);

		return TransitionConstants.Application.JSP_SPC07L01;
	}


	/**
	 * 一括送信(実処理)
	 * @return
	 */
	private String lumpSendMain() {
		List<Integer> failIdList = new ArrayList<Integer>();
		List<Integer> scoutMailLogIdList = new ArrayList<Integer>();
		for (String lumpId : listForm.lumpSendIdArray) {
			int id;
			try {
				id = Integer.parseInt(lumpId);
			} catch (NumberFormatException e) {
				throw new FraudulentProcessException(e);
			}


			if (!scoutMailLogic.existReceiveMail(id)) {
				failIdList.add(id);
				continue;
			}

			scoutMailLogIdList.add(id);
		}

		if (CollectionUtils.isNotEmpty(failIdList)) {
			throw new ActionMessagesException("errors.app.cannotReplyScoutMail", scoutMailLogService.convertIdToMemberId(failIdList));
		}

		List<String> list;
		try {
			list = scoutMailLogic.convertScoutMailLogIdToLatestReceivedMailIdStringList(scoutMailLogIdList);
		} catch (WNoResultException e) {
			throw new FraudulentProcessException();
		}

		session.setAttribute(InputAction.RETURN_MAIL_ID_SESSION_KEY, list);
		session.setAttribute("displayId", MAIL_MEMBER_LIST);
		session.setAttribute(InputAction.RETURN_MAIL_SCOUT_MAIL_LOG_ID_KEY, scoutMailLogIdList);
		session.setAttribute(InputAction.PAGE_NUMBER_SESSION_KEY, listForm.pageNum);

		return TransitionConstants.Application.REDIRECT_SCOUT_MAIL_RETURN_LUMP_SEND;
	}

}
