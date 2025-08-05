package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.TokenProcessor;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.shop.logic.constants.ShopServiceConstants;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.pc.scoutFoot.action.member.DetailAction;
import com.gourmetcaree.shop.pc.scoutFoot.action.member.KeepBoxAction;
import com.gourmetcaree.shop.pc.scoutFoot.action.member.ListAction;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * スカウトメール入力アクションクラスです。
 * 初回送信・返信どちらもこのアクションでやっている。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class InputAction extends ScoutMailBaseAction {

	private static final String MENU_INFO_SESSION_KEY = InputAction.class.getName() + "#MENU_INFO_SESSION_KEY";

	public static final String RETURN_MAIL_ID_SESSION_KEY = "returnMailId";

	public static final String RETURN_MAIL_SCOUT_MAIL_LOG_ID_KEY = "com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail.InputAction.RETURN_MAIL_SCOUT_MAIL_LOG_ID_KEY";

	private static final String SEND_MEMBER_ID = "SEND_MEMBER_ID";

	/** リストPageNumのセッションキー */
	public static final String PAGE_NUMBER_SESSION_KEY
		= "com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputAction.PAGE_NUMBER_SESSION_KEY";

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** メール返信時に頭に付ける文字 */
	private final String RE = "re:";

	/** スカウトメール入力フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** ソートキー */
	private static final String SORT_KEY = "postStartDateTime desc, sizeKbn desc, id desc";

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String index() {
		session.setAttribute(InputForm.FROM_PAGE_SESSION_KEY, InputForm.FromPageKbn.MEMBER);
		setMenuInfo(MenuInfo.scoutInstance());
		indexLogic();
		//setPostingWebInfo();
		return show();
	}

	/**
	 * キープボックスからのインデックス
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String indexFromKeepBox() {
		session.setAttribute(InputForm.FROM_PAGE_SESSION_KEY, InputForm.FromPageKbn.KEEP_BOX);
        setMenuInfo(MenuInfo.scoutInstance());
		indexLogic();
		//setPostingWebInfo();
		return show();
	}

	/**
	 * インデックスのロジックを行います。
	 */
	private void indexLogic() {
		// セッション情報を取得
		String[] checkId = (String[]) session.getAttribute("checkId");
		if (checkId == null || checkId.length == 0) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.noCheckMember",
					MessageResourcesUtil.getMessage("labels.lumpSend"),
					MessageResourcesUtil.getMessage("labels.member"));
		}

		String dispId = (String) session.getAttribute("displayId");
		if (StringUtils.isEmpty(dispId)) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		} else {
			inputForm.displayId = dispId;
		}

		inputForm.memberIdList = new ArrayList<Integer>();
		try {
			for (String memberId : checkId) {
				inputForm.memberIdList.add(Integer.parseInt(memberId));
			}
		} catch (NumberFormatException e) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		inputForm.memberId = GourmetCareeUtil.createDelimiterStr(Arrays.asList(checkId));
	}

	/**
	 * メールを返信（メールBOXから遷移）
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String returnMailFromMail() {
		session.setAttribute(InputForm.FROM_BUTTON_SESSION_KEY, InputForm.FromButtonKbn.RETURN_MAIL);
		session.setAttribute(InputForm.FROM_MENU_SESSION_KEY, InputForm.FromMenuKbn.MAIL_BOX);
        setMenuInfo(MenuInfo.mailInstance());
        setPostingWebInfo();
		return returnMailMain();
	}

	/**
	 * メールを返信
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String returnMail() {
		session.setAttribute(InputForm.FROM_BUTTON_SESSION_KEY, InputForm.FromButtonKbn.RETURN_MAIL);
		session.setAttribute(InputForm.FROM_MENU_SESSION_KEY, InputForm.FromMenuKbn.SCOUT_MAIL);
		return returnMailMain();
	}

	/**
	 * メールを返信 実処理
	 */
	private String returnMailMain() {

		inputForm.setExistDataFlgNg();
		inputForm.returnMailBlockDispFlg = true;

		// 全画面IDを保持
		inputForm.displayId = com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail.DetailAction.DISP_MAIL_DETAIL;

		// 表示データを生成
		convertReturnInputData();
		inputForm.scoutMailLogIdList= new ArrayList<Integer>();
		inputForm.scoutMailLogIdList.add(inputForm.scoutMailLogId);

		return TransitionConstants.ScoutFoot.JSP_SPE02C01;
	}

	/**
	 * 一括送信
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String returnLumpSend() {
		session.setAttribute(InputForm.FROM_BUTTON_SESSION_KEY, InputForm.FromButtonKbn.RETURN_LUMP);
		session.setAttribute(InputForm.FROM_MENU_SESSION_KEY, InputForm.FromMenuKbn.SCOUT_MAIL);
        setMenuInfo(MenuInfo.scoutInstance());
		return returnLumpSendMain();
	}

	/**
	 * 一括送信(メールBOXから遷移)
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String returnLumpSendFromMail() {
		session.setAttribute(InputForm.FROM_BUTTON_SESSION_KEY, InputForm.FromButtonKbn.RETURN_LUMP);
		session.setAttribute(InputForm.FROM_MENU_SESSION_KEY, InputForm.FromMenuKbn.MAIL_BOX);
        setMenuInfo(MenuInfo.mailInstance());
		return returnLumpSendMain();
	}

	/**
	 * 一括送信 実処理
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String returnLumpSendMain() {

		inputForm.setExistDataFlgNg();
		inputForm.returnMailBlockDispFlg = true;
		inputForm.displayId = com.gourmetcaree.shop.pc.scoutFoot.action.scoutMember.ListAction.MAIL_MEMBER_LIST;
		inputForm.scoutMailLogIdList = (List<Integer>) session.getAttribute(RETURN_MAIL_SCOUT_MAIL_LOG_ID_KEY);

		// 表示データを生成
		convertReturnInputData();
		return TransitionConstants.ScoutFoot.JSP_SPE02C01;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// スカウトメール入力画面表示データをセット
		convertInputData();

		session.setAttribute(SEND_MEMBER_ID, inputForm.memberId);

		return TransitionConstants.ScoutFoot.JSP_SPE02C01;
	}

	/**
	 * 定型文を反映
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String addSentence() {

		// 定型文を本文へ返す
		returnSentence();

		return null;
	}

	/**
	 * メール件名を反映
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String addTitle() {

		// メール件名を本文へ返す
		retuenTitle();

		return null;
	}

	/**
	 * 掲載中のWEBデータ表示パスを本文へ反映
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String addUrl() {

		// URLを本文へ返す
		returnUrl();

		return null;
	}

	/**
	 * 詳細画面へ戻る
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String back() {

		session.removeAttribute("displayId");
		// 一括送信の場合は一覧画面に遷移
		Object fromBtn = session.getAttribute(InputForm.FROM_BUTTON_SESSION_KEY);
		if (fromBtn == null) {
		} else if (fromBtn == InputForm.FromButtonKbn.RETURN_LUMP) {
			Object ObjPageNum = session.getAttribute(PAGE_NUMBER_SESSION_KEY);
			int pageNum = NumberUtils.toInt((String) ObjPageNum, DEFAULT_PAGE);
			return TransitionConstants.Application.SCOUT_MEMBER_CHANGE_PAGE.concat(String.valueOf(pageNum));
		}

		if (StringUtils.isEmpty(inputForm.displayId)) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		} else if (ListAction.DISP_MEMBER_LIST.equals(inputForm.displayId)) {
			return TransitionConstants.ScoutFoot.REDIRECT_MEMBER_LIST_SEARCH_AGAIN;
		} else if (KeepBoxAction.DISP_KEEPBOX_LIST.equals(inputForm.displayId)) {
			return TransitionConstants.ScoutFoot.REDIRECT_MEMBER_KEEPBOX_SEARCH_AGAIN;
		} else if (DetailAction.DISP_MEMBER_DETAIL.equals(inputForm.displayId)) {
			Object fromPage = session.getAttribute(InputForm.FROM_PAGE_SESSION_KEY);
			String backPath;
			if (fromPage == null) {
				backPath = TransitionConstants.ScoutFoot.REDIRECT_MEMBER_DETAIL;
			} else if (fromPage == InputForm.FromPageKbn.MEMBER) {
				backPath = TransitionConstants.ScoutFoot.REDIRECT_MEMBER_DETAIL;
			} else {
				backPath = TransitionConstants.ScoutFoot.REDIRECT_MEMBER_DETAIL_FROM_KEEP_BOX;
			}
			return backPath + inputForm.memberId + TransitionConstants.REDIRECT_STR;
		} else if (com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail.DetailAction.DISP_MAIL_DETAIL
				.equals(inputForm.displayId)) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_DETAIL + inputForm.id + "/"
					+ String.valueOf(MTypeConstants.SendKbn.RECEIVE) + TransitionConstants.REDIRECT_STR;
		} else {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}
	}

	/**
	 * 確認画面表示
	 * @return
	 */
	@Execute(validator = true, input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String conf() {

		if (GourmetCareeConstants.SEND_KBN_SEND.equals(inputForm.sendKbn)) {
			if (!isPossibleScout()) {
				return TransitionConstants.ScoutFoot.JSP_SPE02C01;
			}

		} else if (GourmetCareeConstants.SEND_KBN_RETURN.equals(inputForm.sendKbn)) {
			returnCheck();
		} else {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		//トークンを設定。
		TokenProcessor.getInstance().saveToken(RequestUtil.getRequest());

		return TransitionConstants.ScoutFoot.JSP_SPE02C02;
	}

	/**
	 * 返信確認チェック
	 * @return
	 */
	private void returnCheck() {

		// パラメータが空の場合はエラー
		//		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.id);
		if (CollectionUtils.isEmpty(inputForm.mailIdList)) {
			throw new PageNotFoundException();
		}

		// 送信先があるかを取得できているかチェック
		if (!inputForm.existDataFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 返信可能かどうかチェック
		isPossibleReturnMail(inputForm.mailIdList, inputForm.scoutMailLogIdList, inputForm.memberIdList);
	}

	/**
	 * 訂正
	 * @return 入力画面
	 */
	@Execute(validator = false)
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.ScoutFoot.JSP_SPE02C01;
	}

	/**
	 * メール入力完了
	 * @return
	 */
	@Execute(validator = true, removeActionForm = true, input = TransitionConstants.ScoutFoot.JSP_SPE02C01)
	public String submit() {

		if (GourmetCareeConstants.SEND_KBN_SEND.equals(inputForm.sendKbn)) {
			if (!isPossibleScout()) {
				return TransitionConstants.ScoutFoot.JSP_SPE02C01;
			}

		} else if (GourmetCareeConstants.SEND_KBN_RETURN.equals(inputForm.sendKbn)) {
			returnCheck();
		} else {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		if(!inputForm.memberId.equals((String)session.getAttribute(SEND_MEMBER_ID))) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		if (GourmetCareeConstants.SEND_KBN_SEND.equals(inputForm.sendKbn)) {
			// 送信可能かチェック
			if (!isPossibleScout()) {
				return TransitionConstants.ScoutFoot.JSP_SPE02C01;
			}
			// 送信処理
			doSendMail();

			// セッションを破棄
			session.removeAttribute("checkId");

		} else if (GourmetCareeConstants.SEND_KBN_RETURN.equals(inputForm.sendKbn)) {
			// 返信可能かチェック
			returnCheck();

			if (CollectionUtils.isEmpty(inputForm.scoutMailLogIdList)) {
				throw new FraudulentProcessException("不正な操作が行われました。");
			}

			// 返信処理
			doReturnMail();
			// TODO ???何がしたいか不明。削除したい？
//			session.getAttribute("returnMailId");

			return TransitionConstants.ScoutFoot.JSP_SPE02C04;

		} else {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		Object fromPage = session.getAttribute(InputForm.FROM_PAGE_SESSION_KEY);

		if (fromPage == InputForm.FromPageKbn.KEEP_BOX) {
			return "/member/keepBox/?redirect=true";
		} else {
			return "/member/list/searchAgain?redirect=true";
		}

	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false)
	public String comp() {

		return TransitionConstants.ScoutFoot.JSP_SPE02C03;
	}

	/**
	 * 返信画面初期表示データを生成
	 */
	@SuppressWarnings("unchecked")
	private void convertReturnInputData() {

		// セッション情報を取得
		List<String> returnMailIdList;

		Object sessionId = session.getAttribute(RETURN_MAIL_ID_SESSION_KEY);

		if (sessionId == null) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		if (sessionId instanceof List) {
			returnMailIdList = (List<String>) sessionId;
		} else {
			returnMailIdList = new ArrayList<String>(1);
			returnMailIdList.add(String.valueOf(sessionId));
		}
		if (CollectionUtils.isEmpty(returnMailIdList)) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");

		}

		inputForm.mailIdList = new ArrayList<Integer>(returnMailIdList.size());
		inputForm.memberIdList = new ArrayList<Integer>();
		TMail firstMail = null;
		String firstId = null;
		for (String returnMailId : returnMailIdList) {

			int id;
			try {
				id = Integer.parseInt(returnMailId);
			} catch (NumberFormatException e) {
				inputForm.setExistDataFlgNg();
				throw new FraudulentProcessException("不正な操作が行われました。");
			}

			TMail tMail;

			try {
				tMail = mailService.getMailDataToCustomer(id, userDto.customerId, MTypeConstants.MailKbn.SCOUT);
			} catch (WNoResultException e) {
				inputForm.setExistDataFlgNg();
				throw new ActionMessagesException("errors.app.unableReturnNotExistMail");
			}

			if (firstMail == null) {
				firstMail = tMail;
				firstId = returnMailId;
			}
			inputForm.mailIdList.add(id);
			inputForm.memberIdList.add(tMail.fromId);
			inputForm.sendKbn = GourmetCareeConstants.SEND_KBN_RETURN;
			inputForm.mailStatus = String.valueOf(tMail.mailStatus);
			inputForm.urlBtnDispFlg = false;
			inputForm.scoutMailLogId = tMail.scoutMailLogId;
			inputForm.interestFlg = BooleanUtils.toBoolean(tMail.interestFlg);
		}

		if (returnMailIdList.size() == 1 && firstMail != null) {
			if(StringUtils.isBlank(inputForm.body)) {
				inputForm.body = "\n\n"
						+ WztStringUtil
								.insertQuotationMark(GourmetCareeConstants.MAIL_REPLY_QUOTATION_MARK, firstMail.body);
				if(inputForm.subject == null) {
					if(firstMail.subject.indexOf(RE) == -1) {
						inputForm.subject = RE + firstMail.subject;
					} else {
						inputForm.subject = firstMail.subject;
					}
				}
			}
			inputForm.id = firstId;
		}

		inputForm.setExistDataFlgOk();
	}

	/**
	 * メール入力画面表示データを生成
	 */
	private void convertInputData() {

		inputForm.sendKbn = GourmetCareeConstants.SEND_KBN_SEND;
		inputForm.setExistDataFlgOk();
		inputForm.urlBtnDispFlg = true;
        try {
            inputForm.body = scoutMailLogic.createAddUrlStr(userDto.customerId, SORT_KEY, false);
        } catch (WNoResultException e) {
            // 何もしない
        }
	}

	/**
	 * 定型文を本文へ返す
	 */
	private void returnSentence() {

		try {
			if (StringUtils.isEmpty(inputForm.limitValue)) {
				ResponseUtil.write("errorNoSelect");
			} else {
				MSentence mSentence = sentenceService.findById(NumberUtils.toInt(inputForm.limitValue));
				ResponseUtil.write(mSentence.body + scoutMailLogic.createAddUrlStr(userDto.customerId, SORT_KEY, true));
			}
		} catch (SNoResultException | WNoResultException e) {
			ResponseUtil.write("errorSentence");
		}
	}

	/**
	 * メール件名を返す
	 */
	private void retuenTitle() {

		try {
			if (StringUtils.isEmpty(inputForm.limitValue)) {
				ResponseUtil.write("errorNoSelect");
			} else {
				MSentence mSentence = sentenceService.findById(NumberUtils.toInt(inputForm.limitValue));
				ResponseUtil.write(mSentence.sentenceTitle);
			}
		} catch (SNoResultException e) {
			ResponseUtil.write("errorSentence");
		}
	}

	/**
	 * 掲載中のURLを本文へ返す
	 */
	private void returnUrl() {

		try {
			ResponseUtil.write(scoutMailLogic.createAddUrlStr(userDto.customerId, SORT_KEY, false));
		} catch (WNoResultException e) {
			ResponseUtil.write("errorAddUrl");
		}
	}

	/**
	 * メール返信処理
	 */
	private void doReturnMail() {

		// 返信データを生成

		// 返信処理
		int index = 0;
		for (Integer mailId : inputForm.mailIdList) {
			Integer scoutMailLogId = inputForm.scoutMailLogIdList.get(index);
			Integer memberId = inputForm.memberIdList.get(index++);

			// 気になるメール返信時スカウトメール残数を更新する
			if(inputForm.interestFlg) {
				scoutMailLogic.updateScoutMailCountWhenInterested(userDto.customerId);
			}

			if (mailId == null || scoutMailLogId == null || memberId == null) {
				throw new FraudulentProcessException();
			}
			ScoutMailProperty property = new ScoutMailProperty();
			convertReturnMailData(property, mailId, scoutMailLogId, memberId);
			scoutMailLogic.doReturnMail(property);
		}

		log.debug("返信しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("スカウトメールを返信しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}
	}

	/**
	 * メール送信処理
	 */
	private void doSendMail() {
		// 送信データを生成
		ScoutMailProperty property = new ScoutMailProperty();
		convertSendMailData(property);

		// 送信処理
		scoutMailLogic.doSendScoutMail(property);

		log.debug("送信しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("スカウトメールを送信しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

	}

	/**
	 * 返信データを生成
	 * @param property スカウトメールプロパティ
	 */
	private void convertReturnMailData(ScoutMailProperty property, int mailId, int scoutMailLogId, int memberId) {

		property.subject = inputForm.subject;
		property.body = inputForm.body;
		property.mailId = mailId;
		property.customerId = userDto.customerId;
		property.memberIdList = new ArrayList<Integer>();
		property.sendKbn = NumberUtils.toInt(inputForm.sendKbn);

		property.scoutMailLogId = scoutMailLogId;

		property.memberIdList.add(memberId);
	}

	/**
	 * 返信データを生成
	 * @param property スカウトメールプロパティ
	 */
	private void convertSendMailData(ScoutMailProperty property) {

		property.subject = inputForm.subject;
		property.body = inputForm.body;
		property.customerId = userDto.customerId;
		property.sendKbn = NumberUtils.toInt(inputForm.sendKbn);
		property.memberIdList = new ArrayList<Integer>();
		try {
			for (Integer memberId : inputForm.memberIdList) {
				property.memberIdList.add(memberId);
			}
		} catch (NumberFormatException e) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}
	}

	/**
	 * スカウトメールが送信可能かどうかチェック
	 */
	private boolean isPossibleScout() {

		try {
			ScoutMailProperty property = new ScoutMailProperty();
			property.customerId = userDto.customerId;

			// TODO スカウト残数
			//			property.scoutCount = memberLogic.getScoutCount(userDto.customerId);
			property.memberIdList = new ArrayList<Integer>();
			for (Integer memberId : inputForm.memberIdList) {
				property.memberIdList.add(memberId);
			}

			return scoutMailLogic.isPossibleScout(property);

		} catch (NumberFormatException e) {
			inputForm.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}
	}

	/**
	 * 一覧に戻るのURLを返却
	 * @return
	 */
	public String getBackListPath() {

		Object obj = session.getAttribute(InputForm.FROM_MENU_SESSION_KEY);
		if (obj == null) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST;
		} else if (obj == InputForm.FromMenuKbn.MAIL_BOX) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST_MAILBOX;
		}
		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST;
	}


	/**
	 * !!target_member!! を変換した文字を返します。
	 */
	public String getReplacedBody() {
		if (StringUtils.isBlank(inputForm.body)) {
			return "";
		}

		Set<String> idSet = new LinkedHashSet<String>();

		if (CollectionUtils.isNotEmpty(inputForm.memberIdList)) {
			for (Integer id : inputForm.memberIdList) {
				idSet.add(String.valueOf(id));
			}
		}

		int index = 0;
		StringBuilder sb = new StringBuilder();
		for (String id : idSet) {
			if (index++ > 0) {
				sb.append(",");
			}
			sb.append(id);
		}

		return inputForm.body.replace(ShopServiceConstants.PatternReplace.MEMBER_NAME, sb.toString());

	}

    private void setMenuInfo(MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
        session.setAttribute(MENU_INFO_SESSION_KEY, menuInfo);
    }

    @Override
    public MenuInfo getMenuInfo() {
	    MenuInfo menuInfo = null;
        if (this.menuInfo == null) {
            menuInfo = (MenuInfo) session.getAttribute(MENU_INFO_SESSION_KEY);
        }
        if (menuInfo == null) {
            return super.getMenuInfo();
        }
        return menuInfo;
    }

    /**
     * 本文に現在掲載中の原稿名とURLをセットする
     */
    private void setPostingWebInfo() {
    	try {
			inputForm.body = scoutMailLogic.createAddUrlStr(userDto.customerId, SORT_KEY, true);
		} catch (WNoResultException e) {
			ResponseUtil.write("errorAddUrl");
		}

    }
}
