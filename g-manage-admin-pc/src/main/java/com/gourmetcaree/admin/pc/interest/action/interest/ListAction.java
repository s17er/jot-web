package com.gourmetcaree.admin.pc.interest.action.interest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.gourmetcaree.admin.pc.interest.dto.interest.InterestDto;
import com.gourmetcaree.admin.pc.interest.form.interest.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.builder.sql.InterestSearchSqlBuilder;
import com.gourmetcaree.admin.service.logic.InterestLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.VInterest;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MemberAttributeService;

/**
 * 気になる通知一覧アクションクラス
 * @author t_shiroumaru
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction{

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "inte.interest_datetime desc, inte.id desc";

	/** リストフォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 気になるロジック */
	@Resource
	private InterestLogic interestLogic;

	/** 会員属性サービス */
	@Resource
	private MemberAttributeService memberAttributeService;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 気になる通知リスト */
	public List<InterestDto> interestList;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input =TransitionConstants.Interest.JSP_APS01L01)
	public String index() {
		return show();
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input =TransitionConstants.Interest.JSP_APS01L01)
	@MethodAccess(accessCode="INTEREST_LIST_SEARCH")
	public String search() {

		//ページナビゲータを初期化
		listForm.maxRow = String.valueOf(getMaxRowNum());

		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Interest.JSP_APS01L01;
	}

	/**
	 * リセットして検索
	 */
	@Execute(validator = true, reset = "resetForm", validate = "validate", input =TransitionConstants.Interest.JSP_APS01L01)
	@MethodAccess(accessCode="INTEREST_LIST_SEARCH")
	public String clearSearch() {
		return search();
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator=false, input = TransitionConstants.Interest.JSP_APS01L01)
	@MethodAccess(accessCode="INTEREST_LIST_SEARCH")
	public String searchAgain() {

		doSearch(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Interest.JSP_APS01L01;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		listForm.setExistDataFlgNg();

		// 一覧画面へ遷移
		return TransitionConstants.Interest.JSP_APS01L01;
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		try {
			return Integer.parseInt(listForm.maxRow);
		} catch (NumberFormatException e) {
			listForm.maxRow = getCommonProperty("gc.application.initMaxRow");
			return NumberUtils.toInt(listForm.maxRow, DEFAULT_MAX_ROW);
		}
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Interest.JSP_APS01L01)
	@MethodAccess(accessCode="INTEREST_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Interest.JSP_APS01L01;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.Interest.JSP_APS01L01)
	@MethodAccess(accessCode="INTEREST_LIST_CHANGEPAGE")
	public String changePage() {

		doSearch(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Interest.JSP_APS01L01;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		try {
			List<VInterest> entityList;

			entityList = interestLogic.doSearchInterest(pageNavi, targetPage, map);

			log.debug("気になる通知情報を取得");

			// 表示用にデータを生成
			convertShowList(entityList);

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (ParseException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.invalidSearchParameter.");
		}
	}

	/**
	 * 検索条件をMapにセット
	 * @param map 検索条件Map
	 */
	private void createParams(Map<String, String> map) {
		map.put("areaCd", StringUtils.defaultString(listForm.where_areaCd, ""));
		map.put("industryKbn", StringUtils.defaultString(listForm.where_industryCd, ""));
		map.put("prefecturesCd", StringUtils.defaultString(listForm.where_prefecturesCd, ""));
		map.put("lowerAge", StringUtils.defaultString(listForm.where_lowerAge, ""));
		map.put("upperAge", StringUtils.defaultString(listForm.where_upperAge, ""));
		map.put("sexKbn", StringUtils.defaultString(listForm.where_sexKbn, ""));
		map.put("empPtnKbn", StringUtils.defaultString(listForm.where_empPtnKbn, ""));
		map.put("customerName", StringUtils.defaultString(listForm.where_customerName, ""));
		map.put("memberName", StringUtils.defaultString(listForm.where_memberName, ""));
		map.put("loginId", StringUtils.defaultString(listForm.where_loginId, ""));
		map.put("subMailAddress", StringUtils.defaultString(listForm.where_subMailAddress, ""));
		map.put("interestStartDate", StringUtils.defaultString(listForm.where_interestStartDate, ""));
		map.put("interestStartHour", StringUtils.defaultString(listForm.where_interestStartHour, ""));
		map.put("interestStartMinute", StringUtils.defaultString(listForm.where_interestStartMinute, ""));
		map.put("interestEndDate", StringUtils.defaultString(listForm.where_interestEndDate, ""));
		map.put("interestEndHour", StringUtils.defaultString(listForm.where_interestEndHour, ""));
		map.put("interestEndtMinute", StringUtils.defaultString(listForm.where_interestEndMinute, ""));
		map.put("manuscriptName", StringUtils.defaultString(listForm.where_manuscriptName, ""));
		map.put("webId", StringUtils.defaultString(listForm.where_webId, ""));
		map.put(InterestSearchSqlBuilder.SALES_ID, StringUtils.defaultString(listForm.where_salesId));
		map.put(InterestSearchSqlBuilder.COMPANY_ID, StringUtils.defaultString(listForm.where_companyId));
		map.put(InterestSearchSqlBuilder.HOPE_JOB, StringUtils.defaultString(listForm.where_hopeJob));
	}

	/**
	 * 表示データを生成
	 * @param entityList 応募エンティティリスト
	 */
	private void convertShowList(List<VInterest> entityList) {

		List<InterestDto> dtoList = new ArrayList<InterestDto>();
		for (VInterest entity : entityList) {
			InterestDto dto = new InterestDto();

			Beans.copy(entity, dto)
					.excludes("address", "municipality")
					.dateConverter(GourmetCareeConstants.DATE_TIME_FORMAT, "interestDateTime")
					.execute();

			if(entity.memberId == null) {
				dto.memberName = "削除されたユーザ";
			}else {
				dto.hopeEmployPtnKbnList = Lists.transform(memberAttributeService.getMemberAttributeValueList(entity.memberId, MTypeConstants.EmployPtnKbn.TYPE_CD), Functions.toStringFunction());
				dto.hopeIndustryKbnList = Lists.transform(memberAttributeService.getMemberAttributeValueList(entity.memberId, MTypeConstants.IndustryKbn.TYPE_CD), Functions.toStringFunction());
				dto.hopeJobKbnList = Lists.transform(memberAttributeService.getMemberAttributeValueList(entity.memberId, MTypeConstants.JobKbn.TYPE_CD), Functions.toStringFunction());
				dto.detailPath = GourmetCareeUtil.makePath("/member/detail/", "interestIndex", String.valueOf(entity.memberId));
			}

			dto.municipality = GourmetCareeUtil.toMunicipality(entity.municipality);
			dto.interestDatetime = DateUtils.getDateStr(entity.interestDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

			if (entity.birthday != null) {
				dto.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			}

			dtoList.add(dto);
		}

		interestList = dtoList;
	}
}
