package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ScoutMailLogService;
import com.gourmetcaree.shop.logic.dto.ScoutMailDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutmailSearchProperty;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ListForm;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ListForm.FromMenuKbn;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ListForm.MailTypeKbn;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

import net.arnx.jsonic.JSON;


/**
 * スカウトメール一覧を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends ScoutMailBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 10;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "send_datetime desc, id desc";

	/** ステータスのデフォルト値 */
	public static final int STATUS_DEFAULT_VALUE = -1;

	/** スカウトメール一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	@Resource
	protected ScoutMailLogService scoutMailLogService;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** スカウトメールリスト */
	public List<ScoutMailDto> scoutMailDtoList;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String index() {
		listForm.mailType = MailTypeKbn.SCOUT_MAIL;
		listForm.fromMenu = FromMenuKbn.SCOUT_MAIL;
		return show();
	}

	/**
	 * 検索 (何故かsearch()を作成しても動かないため)
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String mailBox() {

		doCreatetList(MTypeConstants.SendKbn.RECEIVE,NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));
		listForm.sendKbn = String.valueOf(SendKbn.RECEIVE);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// スカウトメール数を取得
		// TODO スカウト残数
		listForm.scoutMailRemainDto = memberLogic.getRemainScoutMail(userDto.customerId);

		listForm.where_displayCount = "20";
		listForm.sendKbn = String.valueOf(SendKbn.RECEIVE);

		// スカウトメール初期表示リストを取得
		doCreatetList(MTypeConstants.SendKbn.RECEIVE, DEFAULT_PAGE);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * 再表示
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String showList() {

		listForm.sendKbn = String.valueOf(SendKbn.RECEIVE);
		doCreatetList(MTypeConstants.SendKbn.RECEIVE,NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input =TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String changePage() {

		if (listForm.sendKbn == null) {
			listForm.sendKbn = String.valueOf(SendKbn.RECEIVE);
		}

		// 表示リストを取得
		doCreatetList(
				replaceSendKbn(StringUtils.defaultString(listForm.sendKbn)),
				NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * 受信箱・送信箱の切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changeBox/{sendKbn}", input =TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String changeBox() {

		// 表示リストを取得
		doCreatetList(replaceSendKbn(StringUtils.defaultString(listForm.sendKbn)),
				DEFAULT_PAGE);
		checkUnReadMail();

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	@Execute(validator = false, reset = "resetChangeIdArray", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String doLumpChangeRead() {

		if(listForm.changeIdArray != null) {
			for(String scoutMailId : listForm.changeIdArray) {
				try {
					scoutMailLogic.changeMailToOpened(NumberUtils.toInt(scoutMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "会員を選択してください。";
		}

		return "/scoutMail/list/showList?redirect=true";
	}

	@Execute(validator = false, reset = "resetChangeIdArray", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String doLumpChangeUnRead() {

		if(listForm.changeIdArray != null) {
			for(String scoutMailId : listForm.changeIdArray) {
				try {
					scoutMailLogic.changeMailToUnOpened(NumberUtils.toInt(scoutMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "会員を選択してください。";
		}

		return "/scoutMail/list/showList?redirect=true";
	}

	@Execute(validator = false, reset = "resetChangeIdArray", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String doLumpChangeUnDisplay() {

		if(listForm.changeIdArray != null) {
			List<Integer> mailIds = Arrays.asList(listForm.changeIdArray).stream()
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());

			scoutMailLogic.changeMailToUnDisplay(mailIds);
		} else {
			listForm.errorMessage = "会員を選択してください。";
		}

		return "/scoutMail/list/showList?redirect=true";
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String ajaxSelectionFlg() {

		TScoutMailLog entity = new TScoutMailLog();
		entity.id = listForm.scoutMailLogId;

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;
		if (StringUtils.isNotBlank(selectionMap.get(listForm.selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(listForm.selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}

		scoutMailLogService.updateIncludesVersion(entity);

		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}

	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String editMemo() {

		TScoutMailLog entity = new TScoutMailLog();
		entity.id = listForm.scoutMailLogId;
		entity.memo = listForm.memo;

		scoutMailLogService.updateIncludesVersion(entity);

		return null;
	}

	/**
	 * 一覧作成
	 * @param sendKbn 送受信区分
	 */
	private void doCreatetList(int sendKbn, int targetPage) {

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		if(StringUtils.isNotBlank(listForm.where_displayCount)) {
			pageNavi.maxRow = Integer.parseInt(listForm.where_displayCount);
		}

		ScoutMailProperty property = new ScoutMailProperty();
		property.pageNavi = pageNavi;
		property.customerId = userDto.customerId;
		property.sendKbn = sendKbn;
		property.targetPage = targetPage;

		Map<String,Object> searchRequestsMap = new HashMap<>();

		if(sendKbn == SendKbn.RECEIVE) {
			if(GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(listForm.where_areaCd)).size() > 0) {
				searchRequestsMap.put("where_areaCd", listForm.where_areaCd);
			}
			if(GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(listForm.where_mailStatus)).size() > 0) {
				searchRequestsMap.put("where_mailStatus", listForm.where_mailStatus);
			}
			if(StringUtils.isNotBlank(listForm.where_selectionKbn)) {
				searchRequestsMap.put("where_selectionKbn", listForm.where_selectionKbn);
			}
			if(StringUtils.isNotBlank(listForm.where_keyword)) {
				searchRequestsMap.put("where_keyword", listForm.where_keyword);
			}
		}


		try {
			// 検索
			property.scoutMailListDtoList = scoutMailLogic.getScoutMailList(property, searchRequestsMap);

			log.debug("スカウトメールリストを取得");

			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("スカウトメールリストを取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			}

			// 表示用にデータを生成
			scoutMailDtoList = new ArrayList<ScoutMailDto>();
			List<ScoutMailDto> tmpDtoList = new ArrayList<ScoutMailDto>();
			tmpDtoList = scoutMailLogic.convertScoutMailList(property);
			for(ScoutMailDto dto : tmpDtoList){

				ScoutmailSearchProperty prop = new ScoutmailSearchProperty();
				// メールが存在するかがわかればいいので、値はなんでもいい。
				prop.maxRow = 1;
				prop.scountId = dto.scoutMailLogId;
				dto.isFirstScoutAccessible = scoutMailLogic.existsFirstScoutMail(prop);
				scoutMailDtoList.add(dto);
			}

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.mailDataNotFound");
		}
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		return NumberUtils.toInt(getCommonProperty("gc.scoutMail.initMaxRow"), DEFAULT_MAX_ROW);
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.SCOUT_MAIL;
	}

	@Override
	public MenuInfo getMenuInfo() {
		if (listForm.fromMenu == null) {
			return super.getMenuInfo();
		}

		switch (listForm.fromMenu) {
			case MAIL_BOX:
				return MenuInfo.mailInstance();
			case SCOUT_MAIL:
			default:
				return MenuInfo.scoutInstance();
		}
	}
}

