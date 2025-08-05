package com.gourmetcaree.admin.pc.member.action.member;

import static com.gourmetcaree.db.common.entity.MMember.*;
import static com.gourmetcaree.db.common.entity.member.BaseMemberEntity.*;
import static com.gourmetcaree.db.common.service.MemberService.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.member.dto.member.AttrDto;
import com.gourmetcaree.admin.pc.member.dto.member.MemberInfoDto;
import com.gourmetcaree.admin.pc.member.form.member.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MemberCsvLogic;
import com.gourmetcaree.admin.service.property.MemberCsvProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.IndustryKbn;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.entity.VMemberHopeCity;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 会員一覧アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class ListAction extends MemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "id desc, mMemberAttributeList.id";

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 会員CSVロジック */
	@Resource
	protected MemberCsvLogic memberCsvLogic;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 会員一覧 */
	public List<MemberInfoDto> memberInfoDtoList;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input =TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_SEARCH")
	public String search() {

		//ページナビゲータを初期化
		listForm.maxRow = String.valueOf(getMaxRowNum());

		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Member.JSP_APH01L01;
	}

	/**
	 * 検索件数の切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_CHANGEPAGE")
	public String changePage() {

		int targetPage = NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE);
		doSearch(targetPage);

		return TransitionConstants.Member.JSP_APH01L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator = false, input = TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		int targetPage = DEFAULT_PAGE;

		doSearch(targetPage);

		return TransitionConstants.Member.JSP_APH01L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator=false, input = TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_SEARCHAGAIN")
	public String searchAgain() {


		doSearch(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Member.JSP_APH01L01;
	}

	/**
	 * メルマガ送信
	 * @return 会員向けメルマガ登録初期表示メソッド
	 */
	@Execute(validator = false, input = TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_MAILMAGAZINE")
	public String mailMagazine() {

		// 会員向けメルマガ登録初期表示メソッドへ遷移
		return TransitionConstants.Member.MEMBERMAILMAG_INPUT_INDEX;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, NumberUtils.toInt(listForm.maxRow, getMaxRowNum()));

		try {
			List<MMember> entityList;

			entityList = memberService.doSearch(pageNavi, map, targetPage);

			log.debug("顧客情報を取得");

			// 表示用にデータを生成
			convertShowList(entityList);

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
		return TransitionConstants.Member.JSP_APH01L01;
	}

	/**
	 * CSV出力
	 * @return 一覧画面のパス
	 */
	@Execute(validator = false, input =TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="MEMBER_LIST_OUTPUT")
	public String output() {

		MemberCsvProperty property = new MemberCsvProperty();
		// CSV出力に必要な値をセット
		setProperty(property);

		// CSV出力
		try {
			memberCsvLogic.outPutCsv(property);

			ResponseUtil.write(String.valueOf(property.count));

			// 出力件数を表示するメッセージをセット
//			ActionMessages messages = new ActionMessages();
//			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.csv.outputCount", property.count));
//			ActionMessagesUtil.addMessages(request, messages);

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
	 * CSV出力処理に必要なプロパティをセット
	 * @param property
	 */
	private void setProperty(MemberCsvProperty property) {

		property.pass = getFilePass();
		property.faileName = getFileName();
		property.encode = getEncode();
		property.header = getHeader();
		property.careerHeader = getCareerHeader();

		// 検索用パラメータを生成
		property.map = new HashMap<String, String>();
		createParams(property.map);
		property.sortKey = DEFAULT_SORT_KEY;
	}

// 定数を利用して汎用的に変更
//	/**
//	 * 検索条件をMapにセット
//	 * @param map 検索条件Map
//	 */
//	private void createParams(Map<String, String> map) {
//
//		map.put("id", StringUtils.defaultString(listForm.where_id, ""));
//		map.put("name", StringUtils.defaultString(listForm.where_name, ""));
//		map.put("areaCd", StringUtils.defaultString(listForm.where_areaCd, ""));
//		map.put("prefecturesCd", StringUtils.defaultString(listForm.where_prefecturesCd, ""));
//		map.put("industryCd", StringUtils.defaultString(listForm.where_industryCd, ""));
//		map.put("employPtnKbn", StringUtils.defaultString(listForm.where_empPtnKbn, ""));
//		map.put("sexKbn", StringUtils.defaultString(listForm.where_sexKbn, ""));
//		map.put("lowerAge", StringUtils.defaultString(listForm.where_lowerAge, ""));
//		map.put("upperAge", StringUtils.defaultString(listForm.where_upperAge, ""));
//		map.put("fromUpdateDate", StringUtils.defaultString(listForm.where_fromUpdateDate, ""));
//		map.put("toUpdateDate", StringUtils.defaultString(listForm.where_toUpdateDate, ""));
//		map.put("juskillFlg", StringUtils.defaultString(listForm.where_juskillFlg, ""));
//		map.put("mail", StringUtils.defaultString(listForm.where_mail, ""));
//	}

	/**
	 * 検索条件をMapにセット
	 * @param map 検索条件Map
	 */
	private void createParams(Map<String, String> map) {

		// ID
		map.put(ID, StringUtils.defaultString(listForm.where_id, ""));
		// 名前
		map.put(MEMBER_NAME, StringUtils.defaultString(listForm.where_name, ""));
		// 名前フリガナ
		map.put(MEMBER_NAME_KANA, StringUtils.defaultString(listForm.where_nameKana, ""));
		// エリアコード
		map.put(AREA_CD, StringUtils.defaultString(listForm.where_areaCd, ""));
		// 希望勤務地
		map.put("hope_area", StringUtils.defaultString(listForm.where_hope_area, ""));
		// 都道府県コード
		map.put(PREFECTURES_CD, StringUtils.defaultString(listForm.where_prefecturesCd, ""));
		// 業種区分
		map.put(IndustryKbn.TYPE_CD, StringUtils.defaultString(listForm.where_industryCd, ""));
		// 雇用形態区分
		map.put(EMPLOY_PTN_KBN, StringUtils.defaultString(listForm.where_empPtnKbn, ""));
		// 性別区分
		map.put(SEX_KBN, StringUtils.defaultString(listForm.where_sexKbn, ""));
		// 上限年齢
		map.put(LOWER_AGE, StringUtils.defaultString(listForm.where_lowerAge, ""));
		// 下限年齢
		map.put(UPPER_AGE, StringUtils.defaultString(listForm.where_upperAge, ""));
		// 更新日(開始)
		map.put(FROM_UPDATE_DATE, StringUtils.defaultString(listForm.where_fromUpdateDate, ""));
		// 更新日(終了)
		map.put(TO_UPDATE_DATE, StringUtils.defaultString(listForm.where_toUpdateDate, ""));
		// 登録日(開始)
		map.put(FROM_INSERT_DATE, StringUtils.defaultString(listForm.where_fromInsertDate, ""));
		// 登録日(終了)
		map.put(TO_INSERT_DATE, StringUtils.defaultString(listForm.where_toInsertDate, ""));
		// ジャスキルフラグ
		map.put(JUSKILL_FLG, StringUtils.defaultString(listForm.where_juskillFlg, ""));
		// ジャスキル連絡フラグ（転職相談窓口からの求人情報）
		map.put(JUSKILL_CONTACT_FLG, StringUtils.defaultString(listForm.where_juskillContactFlg, ""));
		// メールアドレス(ログインID)
		map.put(LOGIN_ID, StringUtils.defaultString(listForm.where_mail, ""));
		// 雑誌受け取りフラグ
		map.put(GOURMET_MAGAZINE_RECEPTION_FLG, StringUtils.defaultString(listForm.where_gourmetMagazineReceptionFlg, ""));
		// 配送状況
		map.put(DELIVERY_STATUS, StringUtils.defaultString(listForm.where_delivery_status, ""));

		// 検索条件Mapをフォームに保持
		listForm.whereMap = map;
	}

	/**
	 * 表示用リストを生成
	 * @param entityList GCWコードエンティティリスト
	 */
	private void convertShowList(List<MMember> entityList) {

		List<MemberInfoDto> dtoList = new ArrayList<MemberInfoDto>();

		for (MMember entity : entityList) {
			MemberInfoDto dto = new MemberInfoDto();

			Beans.copy(entity, dto).execute();
			dto.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			dto.lastUpdateDate = DateUtils.getDateStr(entity.lastUpdateDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
			dto.detailPath = GourmetCareeUtil.makePath("/member/detail/", "index", String.valueOf(entity.id));

			dto.industryList = new ArrayList<AttrDto>();
			dto.jobList = new ArrayList<AttrDto>();
			dto.employPtnKbnList = new ArrayList<Integer>();
			for (MMemberAttribute attrEntity : entity.mMemberAttributeList) {
				if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
					AttrDto attrDto = new AttrDto();
					Beans.copy(attrEntity, attrDto).execute();
					dto.industryList.add(attrDto);
				} else if (MTypeConstants.JobKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
					AttrDto attrDto = new AttrDto();
					Beans.copy(attrEntity, attrDto).execute();
					dto.jobList.add(attrDto);
				} else if (MTypeConstants.EmployPtnKbn.TYPE_CD.equals(attrEntity.attributeCd)) {
					dto.employPtnKbnList.add(attrEntity.attributeValue);
				}
			}

			// 希望勤務地（リニューアル後からは希望都道府県を設定）
			Set<String> prefSet = new LinkedHashSet<>();
			for (VMemberHopeCity city : entity.vMemberHopeCityList) {
				prefSet.add(String.valueOf(city.prefecturesCd));
			}
			dto.memberAreaList = prefSet.toArray(new String[prefSet.size()]);
			dtoList.add(dto);
		}
		memberInfoDtoList = dtoList;
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		return NumberUtils.toInt(getCommonProperty("gc.member.initMaxRow"), DEFAULT_MAX_ROW);
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
		return getCommonProperty("gc.member.csv.filename");
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
		return getCommonProperty("gc.member.csv.header");
	}

	/**
	 * CSV出力職歴ヘッダーを取得する
	 * @return
	 */
	private String getCareerHeader() {
		return getCommonProperty("gc.member.csv.careerHeader");
	}
}