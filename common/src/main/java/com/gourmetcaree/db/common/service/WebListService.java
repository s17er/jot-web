package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static org.seasar.framework.util.StringUtil.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.common.builder.sql.web.WebDataSearchSqlBuilderImpl;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.RenewalFlg;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.entity.TWebJobAttribute;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;
import com.gourmetcaree.valueobject.PublicationEndStatus;

import jp.co.whizz_tech.commons.WztJaStringUtil;
import jp.co.whizz_tech.commons.WztKatakanaUtil;
import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * WEBデータ一覧ビューのサービスクラスです。
 * @author Makoto Otani
 * @version 1.0
 */
public class WebListService extends AbstractGroumetCareeReferenceService<VWebList> {

	/** 求人原稿の表示期間 */
	private static final int YEAR = -1;

	/**
	 * WEBDataカラム名の配列
	 */
	private static final String[] webDataColumnNameArray = {VWebList.MANUSCRIPT_NAME
		,VWebList.RECRUITMENT_JOB
		,VWebList.WORK_CONTENTS
		,VWebList.SALARY
		,VWebList.PERSON_HUNTING
		,VWebList.WORKING_HOURS
		,VWebList.WORKING_PLACE
		,VWebList.WORKING_PLACE_DETAIL
		,VWebList.TREATMENT
		,VWebList.HOLIDAY
		,VWebList.SEATING
		,VWebList.UNIT_PRICE
		,VWebList.BUSINESS_HOURS
		,VWebList.OPENING_DAY
		,VWebList.SHOP_INFORMATION
		,VWebList.HOMEPAGE1
		,VWebList.HOMEPAGE2
		,VWebList.HOMEPAGE3
		,VWebList.PHONE_RECEPTIONIST
		,VWebList.MAIL
		,VWebList.APPLICATION_METHOD
		,VWebList.ADDRESS_TRAFFIC
		,VWebList.MAP_TITLE
		,VWebList.MAP_ADDRESS
		,VWebList.MESSAGE
		,VWebList.CATCH_COPY1
		,VWebList.SENTENCE1
		,VWebList.CATCH_COPY2
		,VWebList.SENTENCE2
		,VWebList.CATCH_COPY3
		,VWebList.SENTENCE3
		,VWebList.SENTENCE4
		,VWebList.CAPTIONA
		,VWebList.CAPTIONB
		,VWebList.CAPTIONC
		,VWebList.COMMENT
		,VWebList.ATTENTION_SHOP_SENTENCE
		,VWebList.MEMO
		,VWebList.CUSTOMER_NAME
		,VWebList.CUSTOMER_NAME_KANA
		,VWebList.ATTENTION_HERE_TITLE
		,VWebList.ATTENTION_HERE_SENTENCE
		,VWebList.TOP_INTERVIEW_URL
		,VWebList.HOMEPAGE_COMMENT1
		,VWebList.HOMEPAGE_COMMENT2
		,VWebList.HOMEPAGE_COMMENT3,
	};

	/**
	 * IDをキーに検索をします。
	 * @param id ID
	 */
	public VWebList findById(Integer id) {
		SimpleWhere where = new SimpleWhere();
		where.eq("id", id);

		return jdbcManager.from(entityClass)
							.where(where)
							.disallowNoResult()
							.getSingleResult();
	}

	/**
	 * WEB一覧を検索してリストを返す
	 * XXX 使ってない？
	 * @param dto 検索条件
	 * @param pageNavi ページナビ
	 * @param targetPage 対象ページ
	 * @return WEB一覧ビューのリスト
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public List<VWebList> getWebdataList(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		return getWebdataListSelect(dto, pageNavi, targetPage)
					.getResultList();
	}


	/**
	 * WEB一覧を検索してリストを返す
	 * XXX 使ってない？
	 * @param dto 検索条件
	 * @param pageNavi ページナビ
	 * @param targetPage 対象ページ
	 * @return WEB一覧ビューのリスト
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public AutoSelect<VWebList> getWebdataListAutoSelect(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		return getWebdataListSelect(dto, pageNavi, targetPage);
	}

	/**
	 * WEB一覧を検索してリストを返す
	 * <P>キーワード検索対応版
	 * XXX 使ってない？
	 * @param dto 検索条件
	 * @param pageNavi ページナビ
	 * @param targetPage 対象ページ
	 * @return WEB一覧ビューのリスト
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public List<VWebList> getWebdataListA(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		return getWebdataListSelectA(dto, pageNavi, targetPage)
					.getResultList();
	}


	/**
	 * WEB一覧を検索してリストを返す
	 * <P>キーワード検索対応版
	 * @param dto 検索条件
	 * @param pageNavi ページナビ
	 * @param targetPage 対象ページ
	 * @return WEB一覧ビューのリスト
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public SqlSelect<VWebList> getWebdataListAutoSelectA(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		return getWebdataListSelectA(dto, pageNavi, targetPage);
	}




	/**
	 * WEB一覧を検索してリストを返す
	 * @param dto 検索条件
	 * @param pageNavi ページナビ
	 * @param targetPage 対象ページ
	 * @return WEB一覧ビューのリスト
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public AutoSelect<VWebList> getWebdataListSelect(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		// 引数チェック
		checkEmpty(dto);

		List<Object> params = new ArrayList<Object>();

		// 検索条件作成
		StringBuilder whereStr = createInputValueWhere(dto, params);
		addIpPhoneCondition("T1_", whereStr, params, dto.webIpPhoneIdList);

		new WebDataSearchSqlBuilderImpl("T1_", whereStr, params, dto)
				.build();

		// レコードの件数を取得
		int count = (int) countRecords(whereStr.toString(), params.toArray());

		//ページナビゲータのセット
		pageNavi.changeAllCount(count);

		// 検索結果が0件の場合エラー
		if (count == 0) {
			throw new WNoResultException();
		}

		pageNavi.setPage(targetPage);
		pageNavi.setSortKey(createListSort());

		return jdbcManager
				.from(entityClass)
				.where(whereStr.toString(), params.toArray())
				.orderBy(pageNavi.sortKey)
				.offset(pageNavi.offset)
				.limit(pageNavi.limit);

	}

	/**
	 * WEB一覧を検索してリストを返す
	 * <P>キーワード検索対応版
	 * @param dto 検索条件
	 * @param pageNavi ページナビ
	 * @param targetPage 対象ページ
	 * @return WEB一覧ビューのリスト
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public SqlSelect<VWebList> getWebdataListSelectA(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		// 引数チェック
		checkEmpty(dto);

		List<Object> params = new ArrayList<Object>();

		// 検索条件作成（キーワード検索以外）
		StringBuilder termsStr = createInputValueWhereA(dto, params);

		addKeywordCondition(termsStr, params, dto.keyword);
		addIpPhoneCondition(termsStr, params, dto.webIpPhoneIdList);

		// レコードの件数を取得
		int count = (int) jdbcManager.getCountBySql(termsStr.toString(), params.toArray());

		//ページナビゲータのセット
		pageNavi.changeAllCount(count);

		// 検索結果が0件の場合エラー
		if (count == 0) {
			throw new WNoResultException();
		}

		pageNavi.setPage(targetPage);
		pageNavi.setSortKey(createListSortNoCamelize());

		termsStr.append(" ORDER BY ").append(pageNavi.sortKey);

		return jdbcManager.selectBySql(VWebList.class, termsStr.toString(), params.toArray())
				.offset(pageNavi.offset)
				.limit(pageNavi.limit);

	}

	/**
	 * CSV用のWEBデータ一覧を取得
	 * <P>キーワード検索対応版
	 * @param dto
	 * @return
	 * @throws WNoResultException
	 */
	public SqlSelect<VWebList> getWebdataListForCsvA(VWebListDto dto) throws WNoResultException {

		// 引数チェック
		checkEmpty(dto);

		List<Object> params = new ArrayList<Object>();

		// 検索条件作成（キーワード検索以外）
		StringBuilder termsStr = createInputValueWhereA(dto, params);
		addKeywordCondition(termsStr, params, dto.keyword);
		addIpPhoneCondition(termsStr, params, dto.webIpPhoneIdList);

		termsStr.append(" ORDER BY ").append(VWebList.ID);

		return jdbcManager.selectBySql(VWebList.class, termsStr.toString(), params.toArray()).disallowNoResult();

	}

	/**
	 * CSV用のWEBデータ一覧を取得
	 * @param dto
	 * @return
	 * @throws WNoResultException
	 */
	public AutoSelect<VWebList> getWebdataListForCsv(VWebListDto dto) throws WNoResultException {
		// 引数チェック
		checkEmpty(dto);

		List<Object> params = new ArrayList<Object>();

		// 検索条件作成
		StringBuilder whereStr = createInputValueWhere(dto, params);

		return jdbcManager
				.from(entityClass)
				.where(whereStr.toString(), params.toArray())
				.orderBy(VWebList.ID)
				.disallowNoResult();

	}

	/**
	 * WEBデータ一覧で入力された検索条件をセットしたStringBuilderを返す。
	 * 値は引数のリストにセットする。
	 * <P>キーワード検索対応版
	 * @param dto WEBデータ一覧Dto
	 * @param params 検索する値をセットするリスト
	 * @return 検索条件
	 */
	private StringBuilder createInputValueWhereA(VWebListDto dto, List<Object> params) {

		// 検索条件の設定
		StringBuilder whereStr = new StringBuilder(0);

		whereStr.append("SELECT * FROM ").append(VWebList.TABLE_NAME).append(" WHERE 1=1 ");

        new WebDataSearchSqlBuilderImpl(null, whereStr, params, dto)
                .build();

        // リニューアルフラグ
        whereStr.append(andEqNoCamelize(VWebList.RENEWAL_FLG));
		params.add(RenewalFlg.TARGET);

		// エリアをセット
		if (dto.areaCd != null) {
			whereStr.append(andEqNoCamelize(VWebList.AREA_CD));
			params.add(dto.areaCd);
		}

		//原稿番号
		if (dto.whereWebId != null) {
			whereStr.append(andEqNoCamelize(VWebList.ID));
			params.add(dto.whereWebId);
		}

		// 号数ID
		if (dto.volumeId != null) {
			whereStr.append(andEqNoCamelize(VWebList.VOLUME_ID));
			params.add(dto.volumeId);
		}
		// 画面表示ステータス
		if (dto.displayStatusList != null && !dto.displayStatusList.isEmpty()) {
			whereStr.append(andInNoCamelize(VWebList.DISPLAY_STATUS, dto.displayStatusList.size()));
			for (String displayStatus : dto.displayStatusList) {
				params.add(Integer.parseInt(displayStatus));
			}
		}

		// チェックステータス
		if (dto.checkedStatus != null) {
			whereStr.append(andEqNoCamelize(VWebList.CHECKED_STATUS));
			params.add(dto.checkedStatus);
		}

		// 担当会社ID
		if (dto.companyId != null) {
			whereStr.append(andEqNoCamelize(VWebList.COMPANY_ID));
			params.add(dto.companyId);
		}
		// 営業担当者ID
		if (dto.salesId != null) {
			whereStr.append(andEqNoCamelize(VWebList.SALES_ID));
			params.add(dto.salesId);
		}
		// 応募フォーム
		if (dto.applicationFormKbn != null) {
			whereStr.append(andEqNoCamelize(VWebList.APPLICATION_FORM_KBN));
			params.add(dto.applicationFormKbn);
		}
		// 誌面号数
		if (!StringUtil.isEmpty(dto.magazineVolume)) {
			whereStr.append(andEqNoCamelize(VWebList.MAGAZINE_VOLUME));
			params.add(dto.magazineVolume);
		}

		try {
			// 掲載開始日
			if (dto.postToDate != null) {
				whereStr.append(andLeNoCamelize(VWebList.POST_START_DATETIME));
				params.add(DateUtils.getEndDatetime(dto.postToDate));
			}
			// 掲載終了日
			if (dto.postFromDate != null) {
				whereStr.append(andGeNoCamelize(VWebList.POST_END_DATETIME));
				params.add(DateUtils.getStartDatetime(dto.postFromDate));
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("日付が不正です。");
		}

		// %顧客名%
		if (!StringUtil.isEmpty(dto.customerName)) {
			whereStr.append(andLikeNoCamelize(VWebList.CUSTOMER_NAME));
			params.add(containPercent(dto.customerName));
		}
		// %原稿名%
		if (!StringUtil.isEmpty(dto.manuscriptName)) {
			whereStr.append(andLikeNoCamelize(VWebList.MANUSCRIPT_NAME));
			params.add(containPercent(dto.manuscriptName));
		}

		// 業種区分がセットされていれば、検索条件にセット
		if (dto.industryKbn != null) {
			// 系列店舗から取得
			whereStr.append(" AND EXISTS ( ");
			whereStr.append(" SELECT 1 FROM v_web_industry_kbn vi WHERE vi.web_id = v_web_list.id");
			whereStr.append(" AND vi.industry_kbn = ? ");
			whereStr.append(" ) ");
			params.add(dto.industryKbn);
		}

		// サイズ区分
		if (dto.sizeKbn != null) {
			whereStr.append(andEqNoCamelize(VWebList.SIZE_KBN));
			params.add(dto.sizeKbn);
		}


		//顧客ID
		if (dto.customerId != null) {
			whereStr.append(andEqNoCamelize(VWebList.CUSTOMER_ID));
			params.add(dto.customerId);
		}

		//勤務地エリア(WEBエリアから名称変更)
		if (dto.webAreaKbn != null && dto.areaCd != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_attribute ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = v_web_list.id ");
			whereStr.append(" AND attribute_cd = ? ");
			whereStr.append(" AND attribute_value = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(MTypeConstants.WebAreaKbn.getTypeCd(dto.areaCd));
			params.add(dto.webAreaKbn);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		//海外エリア
		if (dto.foreignAreaKbn != null && dto.areaCd != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_attribute ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = v_web_list.id ");
			whereStr.append(" AND attribute_cd = ? ");
			whereStr.append(" AND attribute_value = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(MTypeConstants.ForeignAreaKbn.getTypeCd(dto.areaCd));
			params.add(dto.foreignAreaKbn);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		//特集
		if (dto.specialId != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_special ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = v_web_list.id ");
			whereStr.append(" AND special_id = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(")");

			params.add(dto.specialId);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		if (!StringUtil.isEmpty(dto.serialPublication)) {
			whereStr.append(andLikeNoCamelize(VWebList.SERIAL_PUBLICATION));
			params.add(containPercent(dto.serialPublication));
		}

		if (dto.serialPublicationKbn != null) {
			whereStr.append(" AND EXISTS( ");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_attribute ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = v_web_list.id ");
			whereStr.append(" AND attribute_cd = ? ");
			whereStr.append(" AND attribute_value = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
			params.add(dto.serialPublicationKbn);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

		}

		//注目店舗フラグ
		if (dto.attentionShopFlg != null) {
			whereStr.append(andEqNoCamelize(VWebList.ATTENTION_SHOP_FLG));
			params.add(NumberUtils.toInt(dto.attentionShopFlg));
		}

		// 検索優先フラグ
		if (dto.searchPreferentialFlg != null) {
			whereStr.append(andEqNoCamelize(VWebList.SEARCH_PREFERENTIAL_FLG));
			params.add(Integer.valueOf(dto.searchPreferentialFlg));
		}

		if(dto.movieFlg != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web ");
			whereStr.append(" WHERE ");
			whereStr.append(" t_web.id = v_web_list.id ");
			whereStr.append(" AND delete_flg = ? ");
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

			if(dto.movieFlg == 1) {
				whereStr.append(" AND ( movie_list_display_flg = ?");
				whereStr.append(" OR movie_detail_display_flg = ? )");
				params.add(dto.movieFlg);
				params.add(dto.movieFlg);
			}else {
				whereStr.append(" AND movie_list_display_flg = ?");
				whereStr.append(" AND movie_detail_display_flg = ?");
				params.add(dto.movieFlg);
				params.add(dto.movieFlg);
			}

			whereStr.append(" ) ");
		}

		//路線・最寄駅
		if (dto.railroadId != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_route ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = v_web_list.id ");
			whereStr.append(" AND delete_flg = ? ");
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

			//  鉄道会社
			whereStr.append(" AND railroad_id = ? ");
			params.add(dto.railroadId);

			// 路線
			if (dto.routeId != null) {
				whereStr.append(" AND route_id = ? ");
				params.add(dto.routeId);
			}
			// 駅
			if (dto.stationId != null) {
				whereStr.append(" AND station_id = ? ");
				params.add(dto.stationId);
			}

			whereStr.append(")");
		}

		// 店舗一覧表示区分
		if (dto.shopListDisplayKbn != null) {
			whereStr.append(" AND shop_list_display_kbn = ? ");
			params.add(dto.shopListDisplayKbn);
		}

		// 職種の検索
		if (CollectionUtils.isNotEmpty(dto.employPtnKbnList)
				|| CollectionUtils.isNotEmpty(dto.jobKbnList)
				|| CollectionUtils.isNotEmpty(dto.treatmentList)
				|| CollectionUtils.isNotEmpty(dto.otherConditionList)) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_job ");
			whereStr.append(" WHERE ");
			whereStr.append(" t_web_job.web_id = v_web_list.id ");
			// 雇用形態
			if (CollectionUtils.isNotEmpty(dto.employPtnKbnList)) {
				whereStr.append(SqlUtils.andInNoCamelize(TWebJob.EMPLOY_PTN_KBN, dto.employPtnKbnList.size()));
				params.addAll(GcCollectionUtils.toIntegerList(dto.employPtnKbnList));
			}
			// 職種
			if (CollectionUtils.isNotEmpty(dto.jobKbnList)) {
				whereStr.append(SqlUtils.andInNoCamelize(TWebJob.JOB_KBN, dto.jobKbnList.size()));
				params.addAll(GcCollectionUtils.toIntegerList(dto.jobKbnList));
			}
			// 待遇条件
			if (CollectionUtils.isNotEmpty(dto.treatmentList)) {
				whereStr.append(" AND EXISTS (");
				whereStr.append(" SELECT ");
				whereStr.append(" * ");
				whereStr.append(" FROM ");
				whereStr.append(" t_web_job_attribute ");
				whereStr.append(" WHERE ");
				whereStr.append(" t_web_job.id = t_web_job_attribute.web_job_id ");
				whereStr.append(" AND t_web_job_attribute.attribute_cd = ? ");
				whereStr.append(SqlUtils.andInNoCamelize(TWebJobAttribute.ATTRIBUTE_VALUE, dto.treatmentList.size()));
				whereStr.append(" AND t_web_job_attribute.delete_flg = ? ");
				whereStr.append(")");
				params.add(MTypeConstants.TreatmentKbn.TYPE_CD);
				params.addAll(GcCollectionUtils.toIntegerList(dto.treatmentList));
				params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
			}
			// その他条件
			if (CollectionUtils.isNotEmpty(dto.otherConditionList)) {
				whereStr.append(" AND EXISTS (");
				whereStr.append(" SELECT ");
				whereStr.append(" * ");
				whereStr.append(" FROM ");
				whereStr.append(" t_web_job_attribute ");
				whereStr.append(" WHERE ");
				whereStr.append(" t_web_job.id = t_web_job_attribute.web_job_id ");
				whereStr.append(" AND t_web_job_attribute.attribute_cd = ? ");
				whereStr.append(SqlUtils.andInNoCamelize(TWebJobAttribute.ATTRIBUTE_VALUE, dto.otherConditionList.size()));
				whereStr.append(" AND t_web_job_attribute.delete_flg = ? ");
				whereStr.append(")");
				params.add(MTypeConstants.OtherConditionKbn.TYPE_CD);
				params.addAll(GcCollectionUtils.toIntegerList(dto.otherConditionList));
				params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
			}
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(")");
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		// WEBデータ用タグ
		if (dto.webDataTagId != null && StringUtils.isNotBlank(dto.webDataTagId)) {
			whereStr.append(" AND EXISTS( ");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" m_web_tag INNER JOIN m_web_tag_mapping ON (m_web_tag.id = m_web_tag_mapping.web_tag_id) ");
			whereStr.append(" WHERE ");
			whereStr.append(" m_web_tag_mapping.web_id = v_web_list.id ");
			whereStr.append(" AND m_web_tag.id = ? ");
			whereStr.append(" AND m_web_tag.delete_flg = ? ");
			whereStr.append(" AND m_web_tag_mapping.delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(Integer.parseInt(dto.webDataTagId));
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		return whereStr;
	}

	/**
	 * WEBデータ一覧で入力された検索条件をセットしたStringBuilderを返す。
	 * 値は引数のリストにセットする。
	 * @param dto WEBデータ一覧Dto
	 * @param params 検索する値をセットするリスト
	 * @return 検索条件
	 */
	private StringBuilder createInputValueWhere(VWebListDto dto, List<Object> params) {

		// 検索条件の設定
		StringBuilder whereStr = new StringBuilder();

		// エリアをセット
		whereStr.append(eq(VWebList.AREA_CD));
		params.add(dto.areaCd);

		//原稿番号
		if (dto.whereWebId != null) {
			whereStr.append(andEq(VWebList.ID));
			params.add(dto.whereWebId);
		}

		// 号数ID
		if (dto.volumeId != null) {
			whereStr.append(andEq(VWebList.VOLUME_ID));
			params.add(dto.volumeId);
		}
		// 画面表示ステータス
		if (dto.displayStatusList != null && !dto.displayStatusList.isEmpty()) {
			whereStr.append(andIn(VWebList.DISPLAY_STATUS, dto.displayStatusList.size()));
			for (String displayStatus : dto.displayStatusList) {
				params.add(Integer.parseInt(displayStatus));
			}
		}
		// 担当会社ID
		if (dto.companyId != null) {
			whereStr.append(andEq(VWebList.COMPANY_ID));
			params.add(dto.companyId);
		}
		// 営業担当者ID
		if (dto.salesId != null) {
			whereStr.append(andEq(VWebList.SALES_ID));
			params.add(dto.salesId);
		}
		// 応募フォーム
		if (dto.applicationFormKbn != null) {
			whereStr.append(andEq(VWebList.APPLICATION_FORM_KBN));
			params.add(dto.applicationFormKbn);
		}
		// 誌面号数
		if (!StringUtil.isEmpty(dto.magazineVolume)) {
			whereStr.append(andEq(VWebList.MAGAZINE_VOLUME));
			params.add(dto.magazineVolume);
		}

		try {
			// 掲載開始日
			if (dto.postToDate != null) {
				whereStr.append(andLe(VWebList.POST_START_DATETIME));
				params.add(DateUtils.getEndDatetime(dto.postToDate));
			}
			// 掲載終了日
			if (dto.postFromDate != null) {
				whereStr.append(andGe(VWebList.POST_END_DATETIME));
				params.add(DateUtils.getStartDatetime(dto.postFromDate));
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("日付が不正です。");
		}

		// %顧客名%
		if (!StringUtil.isEmpty(dto.customerName)) {
			whereStr.append(andLike(VWebList.CUSTOMER_NAME));
			params.add(containPercent(dto.customerName));
		}
		// %原稿名%
		if (!StringUtil.isEmpty(dto.manuscriptName)) {
			whereStr.append(andLike(VWebList.MANUSCRIPT_NAME));
			params.add(containPercent(dto.manuscriptName));
		}
		// 業種区分がセットされていれば、検索条件にセット
		if (dto.industryKbn != null) {
			whereStr.append(" AND ( ");
				// 業務区分1をセット
				whereStr.append(eq(VWebList.INDUSTRY_KBN1));
				params.add(dto.industryKbn);

				// 業務区分2をセット
				whereStr.append(orEq(VWebList.INDUSTRY_KBN2));
				params.add(dto.industryKbn);

				// 業務区分3をセット
				whereStr.append(orEq(VWebList.INDUSTRY_KBN3));
				params.add(dto.industryKbn);
			whereStr.append(" ) ");
		}
		// サイズ区分
		if (dto.sizeKbn != null) {
			whereStr.append(andEq(VWebList.SIZE_KBN));
			params.add(dto.sizeKbn);
		}


		//顧客ID
		if (dto.customerId != null) {
			whereStr.append(andEq(VWebList.CUSTOMER_ID));
			params.add(dto.customerId);
		}

		//勤務地エリア(WEBエリアから名称変更)
		if (dto.webAreaKbn != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_attribute ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = T1_.id ");
			whereStr.append(" AND attribute_cd = ? ");
			whereStr.append(" AND attribute_value = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(MTypeConstants.WebAreaKbn.getTypeCd(dto.areaCd));
			params.add(dto.webAreaKbn);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		//海外エリア
		if (dto.foreignAreaKbn != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_attribute ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = T1_.id ");
			whereStr.append(" AND attribute_cd = ? ");
			whereStr.append(" AND attribute_value = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(MTypeConstants.ForeignAreaKbn.getTypeCd(dto.areaCd));
			params.add(dto.foreignAreaKbn);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		//特集
		if (dto.specialId != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_special ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = T1_.id ");
			whereStr.append(" AND special_id = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(")");

			params.add(dto.specialId);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);
		}

		if (dto.serialPublicationKbn != null) {
			whereStr.append(" AND EXISTS( ");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_attribute ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = T1_.id ");
			whereStr.append(" AND attribute_cd = ? ");
			whereStr.append(" AND attribute_value = ? ");
			whereStr.append(" AND delete_flg = ? ");
			whereStr.append(" ) ");

			params.add(MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
			params.add(dto.serialPublicationKbn);
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

		}

		//注目店舗フラグ
		if (dto.attentionShopFlg != null) {
			whereStr.append(andEq(VWebList.ATTENTION_SHOP_FLG));
			params.add(dto.attentionShopFlg);
		}

		//路線・最寄駅
		if (dto.railroadId != null) {
			whereStr.append(" AND EXISTS (");
			whereStr.append(" SELECT ");
			whereStr.append(" * ");
			whereStr.append(" FROM ");
			whereStr.append(" t_web_route ");
			whereStr.append(" WHERE ");
			whereStr.append(" web_id = T1_.id ");
			whereStr.append(" AND delete_flg = ? ");
			params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

			//  鉄道会社
			whereStr.append(" AND railroad_id = ? ");
			params.add(dto.railroadId);

			// 路線
			if (dto.routeId != null) {
				whereStr.append(" AND route_id = ? ");
				params.add(dto.routeId);
			}
			// 駅
			if (dto.stationId != null) {
				whereStr.append(" AND station_id = ? ");
				params.add(dto.stationId);
			}

			whereStr.append(")");
		}

		// 店舗一覧表示区分
		if (dto.shopListDisplayKbn != null) {
			whereStr.append(" AND shop_list_display_kbn = ? ");
			params.add(dto.shopListDisplayKbn);
		}

		// チェック状態
		if (dto.checkedStatus != null) {
			whereStr.append(" AND checked_status = ? ");
			params.add(dto.checkedStatus);
		}

		addKeywordCondition(whereStr, params, dto.keyword);

		return whereStr;
	}

	/**
	 * WEBデータ一覧一覧のソート順を返す。
	 * @return WEBデータ一覧のソート順
	 */
	private String createListSort(){

		String[] sortKey = new String[] {
			// ソート順を設定
			asc(camelize(VWebList.STATUS)),					// ステータス順
			desc(camelize(VWebList.POST_END_DATETIME)),		// 掲載終了日順
			nullsLast(desc(camelize(VWebList.VOLUME_ID))),	// 号数IDの降順(号数の入っていないnull値は最後に表示)
			asc( camelize(VWebList.DISPLAY_ORDER)),			// 表示順
			desc(camelize(VWebList.COMPANY_ID)),			// 会社IDの降順
			desc(camelize(VWebList.SALES_ID)),				// 営業担当者の降順
			desc(camelize(VWebList.ID))						// WEBIDの降順
		};
		// カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * WEBデータ一覧一覧のソート順を返す。
	 * @return WEBデータ一覧のソート順
	 */
	private String createListSortNoCamelize(){

		String[] sortKey = new String[] {
			// ソート順を設定
			nullsLast(desc(VWebList.VOLUME_ID)),	// 号数IDの降順(号数の入っていないnull値は最後に表示)
			asc( VWebList.DISPLAY_ORDER),			// 表示順
			desc(VWebList.COMPANY_ID),			// 会社IDの降順
			desc(VWebList.SALES_ID),				// 営業担当者の降順
			desc(VWebList.ID)						// WEBIDの降順
		};
		// カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * エンティティがnullの場合は、エラーを返す。
	 * @param dto 検索条件
	 */
	private void checkEmpty(VWebListDto dto) {

		// プロパティがnullの場合はエラー
		if (dto == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * WEBデータIDからWEBデータ一覧を取得する
	 * @param webId WEBデータID
	 * @return WEBデータ一覧
	 * @throws WNoResultException, NumberFormatException
	 */
	public List<VWebList> getVWebListById(String[] webId) throws WNoResultException, NumberFormatException {

		try {
			List<Integer> idList = new ArrayList<Integer>();

			for (String id : webId) {
				idList.add(Integer.parseInt(id));
			}

			String sortKey = createListSort();

			List<VWebList> entityList = jdbcManager.from(VWebList.class)
										.where(new SimpleWhere()
										.in("id", idList.toArray()))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * WEBデータIDからWEBデータ一覧を一つ取得する
	 * @param webId WEBデータID
	 * @return WEBデータ一覧
	 * @throws WNoResultException 検索結果がない場合
	 * @throws NumberFormatException webIdの変換に失敗した場合
	 */
	public VWebList getSingleVWebListById(String webId) throws WNoResultException, NumberFormatException{

		try{
			int id = Integer.parseInt(webId);
			VWebList entity = jdbcManager
									.from(VWebList.class)
									.where(new SimpleWhere()
									.eq("id", id))
									.disallowNoResult()
									.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * WEBデータIDからWEBデータ一覧を一つ取得する
	 * @param webId WEBデータID
	 * @return WEBデータ一覧
	 * @throws WNoResultException 検索結果がない場合
	 * @throws NumberFormatException webIdの変換に失敗した場合
	 */
	public VWebList getSingleVWebListById(int webId) throws WNoResultException, NumberFormatException{

		try{
			VWebList entity = jdbcManager
									.from(VWebList.class)
									.where(new SimpleWhere()
									.eq("id", webId))
									.disallowNoResult()
									.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 号数IDを元にWebデータの全件数を取得します。
	 * @param volumeId 号数ID
	 * @return 号数IDを元にしたWebデータの全件数
	 */
	public int getWebListCountByVolume(int volumeId) {

		Where where = new SimpleWhere().eq("volumeId", volumeId);
		return (int)countRecords(where);
	}

	/**
	 * 顧客ID、表示ステータスをキーにWEBデータ一覧を取得する
	 * @param customerId 顧客ID
	 * @param displayStatus 表示ステータス
	 * @return WEBデータリスト
	 * @throws WNoResultException 検索結果がない場合
	 */
	public List<VWebList> getVWebListByCustomerIdStatus(int customerId, int displayStatus, String sortKey) throws WNoResultException {

		try {
			List<VWebList> entityList = jdbcManager.from(VWebList.class)
										.where(new SimpleWhere()
										.eq(camelize(VWebList.RENEWAL_FLG), RenewalFlg.TARGET)
										.eq("customerId", customerId)
										.eq("displayStatus", displayStatus))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 顧客ID、表示ステータスをキーに「過去一年分」のWEBデータ一覧を取得する
	 * @param customerId 顧客ID
	 * @param displayStatus 表示ステータス
	 * @return WEBデータリスト
	 * @throws WNoResultException 検索結果がない場合
	 */
	public List<VWebList> getVWebListInThePastYearByCustomerIdStatus(int customerId, int displayStatus, String sortKey) throws WNoResultException {

		//現在の日付から一年前の日を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, YEAR);

		try {
			List<VWebList> entityList = jdbcManager.from(VWebList.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.eq("displayStatus", displayStatus)
										.ge(camelize(VWebList.POST_END_DATETIME), cal))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 顧客ID、表示ステータスをキーに「過去一年分」のWEBデータ一覧を取得する
	 * @param customerId
	 * @param displayStatusList
	 * @param sortKey
	 * @return
	 * @throws WNoResultException
	 */
	public List<VWebList> getVWebListInThePastYearByCustomerIdStatuses(int customerId, List<Integer> displayStatusList, String sortKey) throws WNoResultException {

		//現在の日付から一年前の日を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, YEAR);

		try {
			List<VWebList> entityList = jdbcManager.from(VWebList.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.in("displayStatus", displayStatusList)
										.ge(camelize(VWebList.POST_END_DATETIME), cal))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 顧客ID、表示ステータスをキーにWEBデータ一覧を取得する
	 * @param customerId 顧客ID
	 * @param displayStatus 表示ステータス
	 * @return WEBデータリスト
	 */
	public boolean isPostDuringDataExist(int customerId, int displayStatus) {

			Long count = jdbcManager.from(VWebList.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.eq("displayStatus", displayStatus))
										.getCount();

			return count > 0 ? true : false;

	}


	/**
	 * キーワード節を追加
	 */
	private void addKeywordCondition(StringBuilder sb, List<Object> params, String keyword) {
		if (StringUtils.isBlank(keyword)) {
			return;
		}

		Pattern pattern = Pattern.compile(GourmetCareeConstants.ZENBUN_BAD_WORD);
		Matcher matcher = pattern.matcher(keyword);
		String word = WztStringUtil.trim(
							matcher.replaceAll(keyword)
									.replaceAll("　", " "))
									.replaceAll("'", "\\\\\\'");

		String[] words = word.split("(　| )+");

		final String JA_NORMALIZE = " (%s ILIKE '%%%s%%' OR %s ILIKE '%%%s%%' ) " ;

		String f = "coalesce(%s, '')";

		// カラム
		StringBuilder columnNameStr = new StringBuilder(0);

		columnNameStr.append(String.format(f, webDataColumnNameArray[0]));
		for (int i = 1; i < webDataColumnNameArray.length; i++) {
			columnNameStr.append(" || ' ' ||  ").append(String.format(f, webDataColumnNameArray[i]));
		}

		// 条件
		sb.append(" AND ");
		sb.append(String.format(
				JA_NORMALIZE, columnNameStr,
				WztJaStringUtil.zenToHan(WztKatakanaUtil.hankakuToZenkaku(words[0]), WztJaStringUtil.ALL),
				columnNameStr,
				WztJaStringUtil.zenToHan(WztKatakanaUtil.zenkakuToHankaku(words[0]), WztJaStringUtil.ALL)));
		for (int i = 1; i < words.length; i++) {
			sb.append(String.format(" AND " +
					JA_NORMALIZE, columnNameStr,
					WztJaStringUtil.zenToHan(WztKatakanaUtil.hankakuToZenkaku(words[0]), WztJaStringUtil.ALL),
					columnNameStr,
					WztJaStringUtil.zenToHan(WztKatakanaUtil.zenkakuToHankaku(words[0]), WztJaStringUtil.ALL)));
		}

	}

	/**
	 * IP電話番号の条件を追加
	 * @param sql SQL
	 * @param params パラメータ
	 * @param webIpPhoneList {@link TWebIpPhone}のIDリスト
	 */
	private void addIpPhoneCondition(StringBuilder sql, List<Object> params, List<Integer> webIpPhoneList) {
		addIpPhoneCondition("v_web_list", sql, params, webIpPhoneList);
	}


	/**
	 * IP電話番号の条件を追加
	 * @param sql SQL
	 * @param params パラメータ
	 * @param webIpPhoneList {@link TWebIpPhone}のIDリスト
	 */
	private void addIpPhoneCondition(String aliasName, StringBuilder sql, List<Object> params, List<Integer> webIpPhoneList) {
		if (CollectionUtils.isEmpty(webIpPhoneList)) {
			return;
		}

		sql.append(" AND EXISTS ( ");
		sql.append("   SELECT ");
		sql.append("     * ");
		sql.append("   FROM ");
		sql.append("     t_web_ip_phone IP ");
		sql.append("   WHERE ");
		sql.append("     IP.id IN ( ");
			sql.append(SqlUtils.getQMarks(webIpPhoneList.size()));
		sql.append("     ) ");
		sql.append("     AND IP.web_id = ").append(aliasName).append(".id ");
		sql.append("     AND IP.customer_id =  ").append(aliasName).append(".customer_id ");
		sql.append("     AND IP.delete_flg = ? ");
		sql.append(" ) ");

		params.addAll(webIpPhoneList);
		params.add(DeleteFlgKbn.NOT_DELETED);

	}


	/**
	 * チェックステータスの変更が可能かどうか
	 *
	 * 掲載中 OR 掲載待ち
	 */
	public boolean isCheckStatusChangeable(Integer id) {
		try {
			return isCheckStatusChangeable(findById(id));
		} catch (SNoResultException e) {
			return false;
		}
	}

	/**
	 * チェックステータスの変更が可能かどうか
	 * 掲載中 OR 掲載待ち
	 */
	public boolean isCheckStatusChangeable(VWebList entity) {
		boolean changeableStatus = GourmetCareeUtil.eqInt(MStatusConstants.DisplayStatusCd.POST_WAIT, entity.displayStatus)
									|| GourmetCareeUtil.eqInt(MStatusConstants.DisplayStatusCd.POST_DURING, entity.displayStatus);

		boolean changeableCheckedStatus = GourmetCareeUtil.eqInt(MTypeConstants.WebdataCheckedStatus.UNCHECKED, entity.checkedStatus);

		return changeableStatus && changeableCheckedStatus;
	}



	/**
	 * 掲載終了用表示をするかどうか
	 * @param webId WEBID
	 */
	public boolean isDisplayPublicationEndData(int webId) {
		final VWebList webList;
		try {
			 webList = getSingleVWebListById(webId);
		} catch (NumberFormatException e) {
			return false;
		} catch (WNoResultException e) {
			return false;
		}

		PublicationEndStatus status = new PublicationEndStatus(webList);

		// 判定ができない場合はfalse
		if (status.isIllegalAccessor()) {
			return false;
		}

		return status.isDisplayEndData();
	}
}