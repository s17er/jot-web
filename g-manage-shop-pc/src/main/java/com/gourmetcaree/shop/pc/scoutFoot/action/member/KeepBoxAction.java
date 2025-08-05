package com.gourmetcaree.shop.pc.scoutFoot.action.member;

import java.util.ArrayList;
import java.util.List;

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
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.shop.logic.logic.MemberLogic;
import com.gourmetcaree.shop.logic.property.MemberProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.member.KeepBoxForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * キープボックスのアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class KeepBoxAction extends MemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(KeepBoxAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** キープボックスフォーム */
	@ActionForm
	@Resource
	protected KeepBoxForm keepBoxForm;

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "tLoginHistory.lastLoginDatetime desc, id desc, mMemberAttributeList.id";

	/** ソートキー */
	private static final String SORT_KEY = "tLoginHistory.lastLoginDatetime desc, id desc";

	/** キープBOX画面ID */
	public static final String DISP_KEEPBOX_LIST = "spE03L01";

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE03L01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		doCreatetList(pageNavi, DEFAULT_PAGE);

		// スカウトメール状況をセット
		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE03L01;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, reset = "resetId", urlPattern = "changePage/{pageNum}", input =TransitionConstants.ScoutFoot.JSP_SPE03L01)
	public String changePage() {

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		doCreatetList(pageNavi,
				NumberUtils.toInt(keepBoxForm.pageNum, DEFAULT_PAGE));

		// スカウトメール状況をセット
		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE03L01;
	}


	@Execute(validator=false, reset = "resetId", input =TransitionConstants.ScoutFoot.JSP_SPE03L01)
	public String searchAgainFromSendScoutMail() {
		return TransitionConstants.ScoutFoot.REDIRECT_MEMBER_KEEPBOX_SEARCH_AGAIN;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return
	 */
	@Execute(validator=false, input =TransitionConstants.ScoutFoot.JSP_SPE03L01)
	public String searchAgain() {

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		doCreatetList(pageNavi,
				NumberUtils.toInt(keepBoxForm.pageNum, DEFAULT_PAGE));

		// スカウトメール状況をセット
		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE03L01;
	}


	/**
	 * 表示数変更
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE03L01)
	public String changeMaxRow() {
		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		doCreatetList(pageNavi,
				NumberUtils.toInt(keepBoxForm.pageNum, DEFAULT_PAGE));

		// スカウトメール状況をセット
		setScoutStatus();
		checkUnReadMail();
		return TransitionConstants.ScoutFoot.JSP_SPE03L01;
	}

	/**
	 * 一覧作成
	 * @param pageNavi ページナビヘルパー
	 */
	private void doCreatetList(PageNavigateHelper pageNavi, int targetPage) {

		MemberProperty property = new MemberProperty();
		property.pageNavi = pageNavi;
		property.customerId = userDto.customerId;
		property.sortKey = SORT_KEY;
		property.targetPage = targetPage;

		try {
			// 検索
			property.memberEntityList = keepBoxLogic.doSelectKeepList(property);

			log.debug("キープBOXデータ取得");
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("キープBOXデータ取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			}

			// 表示用にデータを生成
			property.pageKbn = MemberLogic.KEEPBOX_PAGE_KBN;
			memberDtoList = keepBoxLogic.convertShowList(property);

			keepBoxForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			keepBoxForm.setExistDataFlgNg();
			//throw new ActionMessagesException("errors.app.keepDataNotFound");
		}
	}

	/**
	 * 一括送信
	 * @return
	 */
	@Execute(validator = false, reset = "resetId", validate = "validateLumpSend", input = TransitionConstants.ScoutFoot.RETURN_TO_MEMBER_KEEPBOX_LIST)
	public String lumpSend() {

		if (!isPossibleScout(keepBoxForm)) {
			return TransitionConstants.ScoutFoot.JSP_SPE01L01;
		}

		// セッションにチェックしたID、画面区分を保持
		session.setAttribute("checkId", keepBoxForm.checkId);
		session.setAttribute("displayId", DISP_KEEPBOX_LIST);

		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_INPUT_FROM_KEEP_BOX;
	}

	/**
	 * 一括削除
	 * @return
	 */
	@Execute(validator = false, reset = "resetId", validate = "validateLumpDelete", input = TransitionConstants.ScoutFoot.RETURN_TO_MEMBER_KEEPBOX_LIST)
	public String lumpDelete() {

		// 削除処理
		doDelete();

		// 削除した旨のメッセージ
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.member.deleteKeepBox"));
		ActionMessagesUtil.addMessages(request, messages);

		// 表示リスト生成
		createListAfterDelete();
		setScoutStatus();

		return TransitionConstants.ScoutFoot.JSP_SPE03L01;
	}

	/**
	 * 削除後のリストを生成
	 */
	private void createListAfterDelete() {

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		try {
			// リストの生成
			doCreatetList(pageNavi,
					NumberUtils.toInt(keepBoxForm.pageNum, DEFAULT_PAGE));
		} catch (ActionMessagesException e) {
			ActionMessagesUtil.addMessages(request, e.getMessages());
		}
	}

	/**
	 * 削除処理
	 */
	private void doDelete() {

		try {
			List<Integer> idList = new ArrayList<Integer>();
			for (String id : keepBoxForm.deleteId) {
				idList.add(Integer.parseInt(id));
			}

			scoutConsiderationService.deleteKeepData(idList);
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		try {
			return Integer.parseInt(keepBoxForm.maxRow);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}

}
