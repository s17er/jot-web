package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.shop.logic.dto.ScoutMailDto;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ListForm;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ListForm.MailTypeKbn;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * スカウトメール一覧を表示するアクションクラスです。
 * @author Yamane
 * @version 1.0
 */
@ManageLoginRequired
public class DetailMailListAction extends ScoutMailBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailMailListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 10;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "TM.send_datetime desc, TM.id desc";

	/** スカウトメール一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;


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
		return show();
	}

	/**
	 * メールボックスからの遷移
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", input = TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String mailBox() {
		listForm.mailType = MailTypeKbn.MAIL_BOX;
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// スカウトメール数を取得
		// TODO スカウト残数
		listForm.scoutMailRemainDto = memberLogic.getRemainScoutMail(userDto.customerId);

		// スカウトメール初期表示リストを取得
		doCreatetList(MTypeConstants.SendKbn.RECEIVE, DEFAULT_PAGE);

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input =TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String changePage() {


		// スカウトメール数を取得
		// TODO スカウト残数
//		listForm.scoutCount = memberLogic.getScoutCount(userDto.customerId);

		// 表示リストを取得
		doCreatetList(
				replaceSendKbn(StringUtils.defaultString(listForm.sendKbn)),
				NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * 受信箱・送信箱の切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changeBox/{sendKbn}", input =TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String changeBox() {

		// スカウトメール数を取得
		// TODO スカウト残数
//		listForm.scoutCount = memberLogic.getScoutCount(userDto.customerId);


		// 表示リストを取得
		doCreatetList(replaceSendKbn(StringUtils.defaultString(listForm.sendKbn)),
				DEFAULT_PAGE);

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * 別画面から再び一覧に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return スカウトメール一覧JSPのパス
	 */
	@Execute(validator=false, input =TransitionConstants.ScoutFoot.JSP_SPE02L01)
	public String searchAgain() {

		// スカウトメール数を取得
		// TODO スカウト残数
//		listForm.scoutCount = memberLogic.getScoutCount(userDto.customerId);


		// 表示リストを取得
		doCreatetList(replaceSendKbn(StringUtils.defaultString(listForm.sendKbn)),
				NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.ScoutFoot.JSP_SPE02L01;
	}

	/**
	 * 一覧作成
	 * @param sendKbn 送受信区分
	 * @param pageNavi ページナビヘルパー
	 */
	private void doCreatetList(int sendKbn, int targetPage) {

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		ScoutMailProperty property = new ScoutMailProperty();
		property.pageNavi = pageNavi;
		property.customerId = userDto.customerId;
		property.sendKbn = sendKbn;
		property.targetPage = targetPage;

		try {
			// 検索 第２引数は仮でnull
			property.scoutMailListDtoList = scoutMailLogic.getScoutMailList(property, null);

			log.debug("スカウトメールリストを取得");
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("スカウトメールリストを取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			}

			// 表示用にデータを生成
			scoutMailDtoList = new ArrayList<ScoutMailDto>();
			List<ScoutMailDto> tmpDtoList = new ArrayList<ScoutMailDto>();
			tmpDtoList = scoutMailLogic.convertScoutMailList(property);
			for(ScoutMailDto dto : tmpDtoList){
				if (listForm.fromMenu.equals(ListForm.FromMenuKbn.MAIL_BOX)) {
					dto.mailDetailPath = GourmetCareeUtil.makePath("/scoutMail/detail/", "indexFromMail", String.valueOf(dto.id), String.valueOf(property.sendKbn));
				} else {
					dto.mailDetailPath = GourmetCareeUtil.makePath("/scoutMail/detail/", "index", String.valueOf(dto.id), String.valueOf(property.sendKbn));
				}
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
}
