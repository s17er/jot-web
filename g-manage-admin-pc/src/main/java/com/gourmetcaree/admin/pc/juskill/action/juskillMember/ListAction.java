package com.gourmetcaree.admin.pc.juskill.action.juskillMember;


import static com.gourmetcaree.db.common.entity.MJuskillMember.*;
import static com.gourmetcaree.db.common.service.JuskillMemberListService.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.juskill.form.juskillMember.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.JuskillMemberCsvLogic;
import com.gourmetcaree.admin.service.property.JuskillMemberCsvProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.TJuskillMemberMaterial;
import com.gourmetcaree.db.common.entity.VJuskillMemberList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.JuskillMemberListService;
import com.gourmetcaree.db.common.service.JuskillMemberMaterialService;


/**
 * ジャスキル会員一覧アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	static final int DEFAULT_MAX_ROW = 20;

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** ジャスキル会員サービス */
	@Resource
	private JuskillMemberListService juskillMemberListService;

	/** ジャスキル会員CSVロジック */
	@Resource
	private JuskillMemberCsvLogic juskillMemberCsvLogic;

	/** ジャスキル会員素材サービス */
	@Resource
	protected JuskillMemberMaterialService juskillMemberMaterialService;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** ジャスキル会員一覧 */
	public List<VJuskillMemberList> juskillMemberList;

	/** 履歴書マップ */
	public Map<Integer, String> resumeMap;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input =TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_SEARCH")
	public String search() {

		//ページナビゲータを初期化
		listForm.maxRow = String.valueOf(getMaxRowNum());

		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Juskill.JSP_APQ01L01;
	}

	/**
	 * メルマガ送信
	 * @return ジャスキル向けメルマガ登録初期表示メソッド
	 */
	@Execute(validator = false, input = TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_MAILMAGAZINE")
	public String mailMagazine() {

		// 会員向けメルマガ登録初期表示メソッドへ遷移
		return TransitionConstants.Juskill.JUSKILL_MEMBERMAILMAG_INPUT_INDEX;
	}

	/**
	 * CSV出力
	 * @return 一覧画面のパス
	 */
	@Execute(validator = false, input =TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_OUTPUT")
	public String output() {

		JuskillMemberCsvProperty property = new JuskillMemberCsvProperty();
		// CSV出力に必要な値をセット
		setProperty(property);

		// CSV出力
		try {
			juskillMemberCsvLogic.outPutCsv(property);

			ResponseUtil.write(String.valueOf(property.count));

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvDataNotFound");

		} catch (ParseException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");

		} catch (IOException e) {
			log.fatal("入出力エラーが発生しました。", e);
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");

		}

		return null;

//		return TransitionConstants.Member.JSP_APH01L01;
	}



	/**
	 * 検索件数の切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_CHANGEPAGE")
	public String changePage() {

		int targetPage = NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE);
		doSearch(targetPage);

		return TransitionConstants.Juskill.JSP_APQ01L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator = false, input = TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		int targetPage = DEFAULT_PAGE;

		doSearch(targetPage);

		return TransitionConstants.Juskill.JSP_APQ01L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator=false, input = TransitionConstants.Juskill.JSP_APQ01L01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_LIST_SEARCHAGAIN")
	public String searchAgain() {


		doSearch(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Juskill.JSP_APQ01L01;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		pageNavi = new PageNavigateHelper("", NumberUtils.toInt(listForm.maxRow, getMaxRowNum()));

		try {

			juskillMemberList = juskillMemberListService.doSearch(pageNavi, map, targetPage);

			resumeMap = new HashMap<Integer, String>();

			for(VJuskillMemberList entity : juskillMemberList) {
				TJuskillMemberMaterial tJuskillMemberMaterial = juskillMemberMaterialService.findJuskillMemberMaterialByJuskillMemberId(entity.id.toString());
				if(tJuskillMemberMaterial != null) {
					StringBuilder fileUrl = new StringBuilder("");
					fileUrl.append(getCommonProperty("gc.sslDomain"));
					fileUrl.append("/kanto");
					fileUrl.append(String.format(getCommonProperty("gc.url.juskill.resume"), tJuskillMemberMaterial.juskillMemberId, tJuskillMemberMaterial.materialData));
					resumeMap.put(entity.id, fileUrl.toString());
				}
			}

			log.debug("ジャスキル会員一覧を取得");

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (ParseException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.invalidSearchParameter");
		}
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		listForm.setExistDataFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01L01;
	}

	/**
	 * 検索条件をMapにセット
	 * @param map 検索条件Map
	 */
	private void createParams(Map<String, String> map) {

		// ID
		map.put(ID, StringUtils.defaultString(listForm.where_id));
		// ジャスキルNo
		map.put(JUSKILL_MEMBER_NO, StringUtils.defaultString(listForm.where_juskillMemberNo));
		// ジャスキル会員名前
		map.put(JUSKILL_MEMBER_NAME, StringUtils.defaultString(listForm.where_juskillMemberName));
		// 都道府県コード
		map.put(PREFECTURES_CD, StringUtils.defaultString(listForm.where_prefecturesCd, ""));
		// 希望業態
		map.put(HOPE_INDUSTRY, StringUtils.defaultString(listForm.where_hopeIndustry, ""));
		// 希望職種
		map.put(HOPE_JOB, StringUtils.defaultString(listForm.where_hopeJob, ""));
		// 性別区分
		map.put(SEX_KBN, StringUtils.defaultString(listForm.where_sexKbn, ""));
		// 上限年齢
		map.put(LOWER_AGE, StringUtils.defaultString(listForm.where_lowerAge, ""));
		// 下限年齢
		map.put(UPPER_AGE, StringUtils.defaultString(listForm.where_upperAge, ""));
		// 登録日(開始)
		map.put(FROM_INSERT_DATE, StringUtils.defaultString(listForm.where_fromJuskillEntryDate, ""));
		// 登録日(終了)
		map.put(TO_INSERT_DATE, StringUtils.defaultString(listForm.where_toJuskillEntryDate, ""));
		// 会員登録フラグ
		map.put(MEMBER_REGISTERED_FLG, StringUtils.defaultString(listForm.where_memberRegisteredFlg, ""));
		// メールアドレス
		map.put(MAIL, StringUtils.defaultString(listForm.where_mail, ""));
		//フリーワード
		map.put(FREE_WORD, StringUtils.defaultString(listForm.free_word, ""));

		// 検索条件Mapをフォームに保持
		listForm.whereMap = map;
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		return NumberUtils.toInt(getCommonProperty("gc.member.initMaxRow"), DEFAULT_MAX_ROW);
	}

	/**
	 * CSV出力処理に必要なプロパティをセット
	 * @param property
	 */
	private void setProperty(JuskillMemberCsvProperty property) {

		property.pass = getFilePass();
		property.faileName = getFileName();
		property.encode = getEncode();
		property.header = getHeader();

		// 検索用パラメータを生成
		property.map = new HashMap<String, String>();
		createParams(property.map);
	}
	/**
	 * CSV出力パスを取得する
	 * @return
	 */
	private String getFilePass() {
		return getCommonProperty("gc.csv.filepass");
	}

	/**
	 * CSVファイル名を取得する
	 * @return
	 */
	private String getFileName() {
		return getCommonProperty("gc.juskillmember.csv.filename");
	}

	/**
	 * CSV出力文字コードを取得する
	 * @return
	 */
	private String getEncode() {
		return getCommonProperty("gc.csv.encoding");
	}

	/**
	 * CSV出力ヘッダーを取得する
	 * @return
	 */
	private String getHeader() {
		return getCommonProperty("gc.juskillmember.csv.header");
	}
}