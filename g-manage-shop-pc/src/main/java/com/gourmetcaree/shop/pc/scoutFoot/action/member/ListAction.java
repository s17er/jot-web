package com.gourmetcaree.shop.pc.scoutFoot.action.member;

import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;
import com.gourmetcaree.shop.logic.dto.MemberInfoDto;
import com.gourmetcaree.shop.logic.logic.CustomerSearchConditionDto;
import com.gourmetcaree.shop.logic.property.MemberProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.member.ListForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * 求職者一覧を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends MemberBaseAction  {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 求職者一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "tLoginHistory.lastLoginDatetime desc, id desc, mMemberAttributeList.id";

	/** 求職者一覧画面ID */
	public static final String DISP_MEMBER_LIST = "spE01L01";

	/** 初期表示かどうか */
	public boolean initDisp = false;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String index() {

		initDisp = true;
		return show();
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(validator = true, reset = "resetMultiBox", validate="validate", input =TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String search() {

		//ページナビゲータを初期化
		listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		listForm.pageNavi.setPage(DEFAULT_PAGE);

		// スカウトメール数を取得
		// TODO スカウト残数
//		listForm.scoutCount = memberLogic.getScoutCount(userDto.customerId);

		doSearch(listForm.pageNavi);

		// スカウトメール状況をセット
		scoutMailRemainDto = memberLogic.getRemainScoutMail(userDto.customerId);
		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01L01;
	}

	/**
	 * 検索件数の切り替え
	 * @return
	 */
	@Execute(validator = false, reset = "resetCheckId",  urlPattern = "changePage/{pageNum}", input =TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String changePage() {

		if (listForm.pageNavi == null) {
			listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		} else {
			listForm.pageNavi.setPage(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));
		}

		// スカウトメール数を取得
		// TODO スカウト残数
//		listForm.scoutCount = memberLogic.getScoutCount(userDto.customerId);

		doSearch(listForm.pageNavi);

		// スカウトメール状況をセット
		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01L01;
	}

	/**
	 * 検索条件の保存
	 * @return
	 */
	@Execute(validator = true, reset = "resetMultiBox", validate="saveValidate", input = "/member/list/searchAgain", redirect = true)
	public String saveConditions() {
		CustomerSearchConditionDto dto = new CustomerSearchConditionDto();
		Beans.copy(listForm, dto).execute();
		saveConditionLogic.saveCondition(userDto.customerId, dto);
		return "/member/list/searchAgain?redirect=true";
	}

	/**
	 * 保存している検索条件で検索
	 * @return
	 */
	@Execute(validator = false, reset = "resetMultiBox", input =TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String loadConditions() {
		CustomerSearchConditionDto dto = saveConditionLogic.getSaveConditions(userDto.customerId);
		Beans.copy(dto, listForm).execute();
		listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		listForm.pageNavi.setPage(DEFAULT_PAGE);

		doSearch(listForm.pageNavi);

		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01L01;
	}

	@Execute(validator=false, reset = "resetCheckId", input = TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String searchAgainFromSendScoutMail() {
		checkUnReadMail();
		return TransitionConstants.ScoutFoot.REDIRECT_MEMBER_LIST_SEARCH_AGAIN;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return 会員検索JSPのパス
	 */
	@Execute(validator=false, reset = "resetCheckId", input =TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String searchAgain() {

		if (listForm.pageNavi == null) {
			listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		}


		doSearch(listForm.pageNavi);


		// スカウトメール状況をセット
		setScoutStatus();
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01L01;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(PageNavigateHelper pageNavi) {

		MemberProperty property = new MemberProperty();
		property.pageNavi = pageNavi;
		property.customerId = userDto.customerId;

		// 検索用パラメータを生成
		Beans.copy(listForm, property).execute();

		try {
			// 検索
			property.memberEntityList = memberLogic.doSearch(property);

			log.debug("顧客情報を取得");
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("顧客情報を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			}

			// 表示用にデータを生成
			memberDtoList = memberLogic.convertShowList(property);

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			setScoutStatus();
			checkUnReadMail();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (ParseException e) {
			listForm.setExistDataFlgNg();
			setScoutStatus();
			checkUnReadMail();
			throw new ActionMessagesException("errors.app.dataNotFound.");
		}
	}

	/**
	 * 一括送信
	 * @return
	 */
	@Execute(validator = false, reset = "resetCheckId", validate = "validateLumpSend", input = "/member/list/searchAgain")
	public String lumpSend() {

		if (!isPossibleScout(listForm)) {
			return TransitionConstants.ScoutFoot.JSP_SPE01L01;
		}

		// セッションにチェックしたID、画面区分を保持
		session.setAttribute("checkId", listForm.checkId);
		session.setAttribute("displayId", DISP_MEMBER_LIST);

		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_INPUT;
	}

	/**
	 * キープBOXに追加
	 * @return
	 */
	@Execute(validator = false,  input = TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String addKeepBox() {

		try {
			// キープBOXに追加する
			keepBoxLogic.doAddkeepBox(userDto.customerId, Integer.parseInt(listForm.limitValue));

			// キープBOXに追加した会員情報を変更
			changeMemberKeeped();

			ResponseUtil.write(listForm.targetId);

		} catch (NumberFormatException e) {
			// 会員IDが不正な場合、キープBOX追加に失敗のメッセージを表示
			ResponseUtil.write("errorAddKeepBox");
		}

		return null;
	}

	/**
	 * キープBOXから削除
	 * @return
	 */
	@Execute(validator = false,  input = TransitionConstants.ScoutFoot.JSP_SPE01L01)
	public String deleteKeepBox() {

		try {
			// キープBOXに追加する
			keepBoxLogic.doDeletekeepBox(userDto.customerId, Integer.parseInt(listForm.limitValue));

			// キープBOXから削除した会員情報を変更
			changeMemberUnKeeped();

			ResponseUtil.write(listForm.targetId);

		} catch (NumberFormatException e) {
			// 会員IDが不正な場合、キープBOX追加に失敗のメッセージを表示
			ResponseUtil.write("errorDeleteKeepBox");
		}

		return null;
	}

	@Execute(validator = false,  reset = "resetCheckId", validate = "validateLumpAdd", input = "/member/list/searchAgain")
	public String lumpAddKeepBox() {

		try {
			for(String id : listForm.checkId) {
				keepBoxLogic.doAddkeepBox(userDto.customerId, Integer.parseInt(id));
				changeMemberKeeped();
			}
		} catch (NumberFormatException e) {
			// 会員IDが不正な場合、キープBOX追加に失敗のメッセージを表示
			ResponseUtil.write("errorAddKeepBox");
		}

		return "/member/list/searchAgain?redirect=true";
	}

	/**
	 * キープBOXに追加した会員をキープ済みに変更
	 */
	private void changeMemberKeeped() {

		if(listForm != null && memberDtoList != null) {
			for (MemberInfoDto dto : memberDtoList) {
				if (String.valueOf(dto.id).equals(listForm.limitValue)) {
					dto.considerationFlg = MemberStatusDto.ConsiderationFlgKbn.KEEP;
				}
			}
		}
	}

	/**
	 * キープBOXに追加した会員をキープ済みに変更
	 */
	private void changeMemberUnKeeped() {

		if(listForm != null && memberDtoList != null) {
			for (MemberInfoDto dto : memberDtoList) {
				if (String.valueOf(dto.id).equals(listForm.limitValue)) {
					dto.considerationFlg = MemberStatusDto.ConsiderationFlgKbn.NOT_KEEP;
				}
			}
		}
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// スカウトメール数を取得
		scoutMailRemainDto = memberLogic.getRemainScoutMail(userDto.customerId);

		listForm.setExistDataFlgNg();
		listForm.searchConditionSavedFlg = saveConditionLogic.existSearchCondition(userDto.customerId);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE01L01;
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		try {
			return Integer.parseInt(listForm.maxRow);
		} catch (NumberFormatException e) {
			return NumberUtils.toInt(getCommonProperty("gc.member.initMaxRow"), DEFAULT_MAX_ROW);
		}
	}
}
