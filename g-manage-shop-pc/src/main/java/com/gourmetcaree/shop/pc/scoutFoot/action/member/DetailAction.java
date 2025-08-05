package com.gourmetcaree.shop.pc.scoutFoot.action.member;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ReleaseWebService;
import com.gourmetcaree.shop.logic.logic.FootprintLogic;
import com.gourmetcaree.shop.logic.property.MemberProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.member.DetailForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * 求職者詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class DetailAction extends MemberBaseAction  {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 求職者詳細画面ID */
	public static final String DISP_MEMBER_DETAIL = "spE01R01";

	/** FromPageのセッションキー */
	public static final String FROM_PAGE_SESSION_KEY =
			"scoutFoot.action.member.FROM_PAGE_SESSION_KEY";

	/**
	 * どこから来たかの列挙型
	 * @author Takehiro Nakamori
	 *
	 */
	public static enum FromPage {
		MEMBER,
		KEEP_BOX,
		SCOUT_MAIL,
		MAIL_BOX_SCOUT_MAIL,
		PRE_APPLICATION_MAIL;
	}

	/** 求職者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 足あとロジック */
	@Resource
	protected FootprintLogic footprintLogic;

	/** グルメキャリーに掲載中のWebデータサービス */
	@Resource
	private ReleaseWebService releaseWebService;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String index() {
		session.setAttribute(FROM_PAGE_SESSION_KEY, FromPage.MEMBER);
		return show();
	}

	/**
	 * 初期表示(キープボックスから)
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexFromKeepBox/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String indexFromKeepBox() {
		session.setAttribute(FROM_PAGE_SESSION_KEY, FromPage.KEEP_BOX);
		return show();
	}

	/**
	 * 初期表示(スカウトメールから)
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexFromScoutMail/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String indexFromScoutMail() {
		session.setAttribute(FROM_PAGE_SESSION_KEY, FromPage.SCOUT_MAIL);
		return show();
	}

	/**
	 * 初期表示(メールBOX→スカウトメールから)
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexFromMailBoxScoutMail/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String indexFromMailBoxScoutMail() {
		session.setAttribute(FROM_PAGE_SESSION_KEY, FromPage.MAIL_BOX_SCOUT_MAIL);
        this.menuInfo = MenuInfo.scoutInstance();
		return show();
	}

	/**
	 * 初期表示(メールBOX→プレ応募メールから)
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexFromMailBoxPreApplicationMail/{id}", input = TransitionConstants.Application.JSP_SPP01L01)
	public String indexFromMailBoxPreApplicationMail() {
		session.setAttribute(FROM_PAGE_SESSION_KEY, FromPage.PRE_APPLICATION_MAIL);
        this.menuInfo = MenuInfo.emptyInstance();
		return show();
	}



	/**
	 * 一覧に戻る
	 * @return
	 */
	@Execute(validator = false)
	public String backList() {

		return TransitionConstants.ScoutFoot.REDIRECT_MEMBER_LIST_SEARCH_AGAIN;
	}

	/**
	 * 足あとをつける
	 * @return
	 */
	@Execute(validator = false, urlPattern = "footPrint/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String footPrint() {

		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		convertDispData();

		//掲載中のデータが存在するかを確認
		if (!releaseWebService.isPsotCustomerExists(userDto.customerId)) {
			throw new ActionMessagesException("errors.app.noReleaseWebdata");
		}

		// 足あとをつける
		footprintLogic.doFootPrint(userDto.customerId, NumberUtils.toInt(detailForm.id));

		detailForm.dto.footPrintFlg = true;

		// 足あとをつけた旨のメッセージ
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.member.footprint"));
		ActionMessagesUtil.addMessages(request, messages);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01R01;
	}

	/**
	 * キープBOXに追加
	 */
	@Execute(validator = false, urlPattern = "addKeepBox/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String addKeepBox() {

//		// ブラウザの戻るで不正な処理をした場合のチェック
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);
		// キープする
		keepBoxLogic.doAddkeepBox(userDto.customerId, NumberUtils.toInt(detailForm.id));

		convertDispData();

		// キープした旨のメッセージ
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.member.addKeepBox"));
		ActionMessagesUtil.addMessages(request, messages);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01R01;
	}

	@Execute(validator = false, urlPattern = "deleteKeepBox/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String deleteKeepBox() {

//		// ブラウザの戻るで不正な処理をした場合のチェック
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);
		// キープする
		keepBoxLogic.doDeletekeepBox(userDto.customerId, Integer.parseInt(detailForm.id));

		convertDispData();

		// キープから削除した旨のメッセージ
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.member.deleteKeepBox"));
		ActionMessagesUtil.addMessages(request, messages);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01R01;
	}

	/**
	 * スカウトメール送信
	 * @return
	 */
	@Execute(validator = false, urlPattern = "sendMail/{id}", input = TransitionConstants.ScoutFoot.JSP_SPE01R01)
	public String sendMail() {


		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		// スカウトメール送信可能かチェック
		detailForm.checkId = new String[] {detailForm.id};
		if (!isPossibleScout(detailForm)) {
			return TransitionConstants.ScoutFoot.JSP_SPE01R01;
		}

		// セッションにチェックしたID、画面区分を保持
		session.setAttribute("checkId", detailForm.checkId);
		session.setAttribute("displayId", DISP_MEMBER_DETAIL);


		if (isFromPageKeepbox()) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_INPUT_FROM_KEEP_BOX;
		}
		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_INPUT;
	}

	/**
	 * キープボックスからの遷移かどうか
	 * @return
	 */
	private boolean isFromPageKeepbox() {
		Object obj = session.getAttribute(FROM_PAGE_SESSION_KEY);
		if (obj == null) {
			return false;
		}

		if (obj instanceof FromPage) {
			FromPage from = (FromPage) obj;
			if (FromPage.KEEP_BOX == from) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		// 表示データを生成
		convertDispData();
		log.debug("詳細データ取得");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("求職者詳細を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01R01;
	}

	/**
	 * 表示データをフォームにセットする
	 * @param form 会員フォーム
	 */
	private void convertDispData() {

		try {
			MemberProperty property = new MemberProperty();
			property.memberId = Integer.parseInt(detailForm.id);
			property.customerId = userDto.customerId;

			// 会員情報・会員属性情報をフォームにセット
			detailForm.dto = memberLogic.convertDispData(property);

		} catch (NumberFormatException e) {
			detailForm.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			// データなしのエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}
}
