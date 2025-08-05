package com.gourmetcaree.db.common.service;
import static com.gourmetcaree.common.util.SqlUtils.*;
import static com.gourmetcaree.db.common.entity.AbstractCommonEntity.*;
import static com.gourmetcaree.db.common.entity.MMember.*;
import static com.gourmetcaree.db.common.entity.member.BaseMemberEntity.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MAreaConstants.AreaCdEnum;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.AdvancedMailMagReceptionFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.IndustryKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailMagazineReceptionFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.MemberKbn;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.VMemberHopeCity;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.scoutFoot.dto.member.MemberStatusDto;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 会員マスタのサービスクラスです。
 * @version 1.0
 */
public class MemberService extends AbstractGroumetCareeBasicService<MMember> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(MemberService.class);

	/** UPDATE時にNULLへのUPDATEを許可するカラム名の保持配列 */
	private static final String[] NULL_ENABLED_COLUMN_LIST = new String[]{
																	toCamelCase(MMember.SEX_KBN),				// 性別区分
																	toCamelCase(MMember.PREFECTURES_CD),		// 都道府県コード
																	toCamelCase(MMember.EMPLOY_PTN_KBN),		// 雇用形態区分
																	toCamelCase(MMember.TRANSFER_FLG),			// 転勤フラグ
																	toCamelCase(MMember.MIDNIGHT_SHIFT_FLG),	// 深夜勤務フラグ
																	toCamelCase(MMember.FOREIGN_WORK_FLG),		// 海外勤務フラグ
																	toCamelCase(MMember.FOOD_EXP_KBN),			// 飲食業界経験区分
																	toCamelCase(MMember.SALARY_KBN),			// 希望年収区分
																	toCamelCase(MMember.DELETE_REASON_KBN),		// 削除理由区分
																	toCamelCase(MMember.DELIVERY_STATUS),		// 発送状況
																	toCamelCase(MMember.ADMIN_PASSWORD),		// 管理者用パスワード
																};

	/** 上限年齢 */
	public static final String LOWER_AGE = "lower_age";

	/** 下限年齢 */
	public static final String UPPER_AGE = "upper_age";

	/** 更新日(開始) */
	public static final String FROM_UPDATE_DATE = "from_update_Date";

	/** 更新日(終了) */
	public static final String TO_UPDATE_DATE = "to_update_date";

	/** 登録日(開始) */
	public static final String FROM_INSERT_DATE = "from_insert_Date";

	/** 登録日(終了) */
	public static final String TO_INSERT_DATE = "to_insert_date";

	/** 年齢 */
	public static final String AGE = "age";

	/* (非 Javadoc)
	 * @see com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService#setCommonInsertColmun(java.lang.Object)
	 */
	@Override
	protected void setCommonInsertColmun(MMember entity) {
		super.setCommonInsertColmun(entity);
		// 登録日時をセット
		entity.registrationDatetime = entity.insertDatetime;
		// 最終更新日時をセット
		entity.lastUpdateDatetime = entity.insertDatetime;
	}

	/* (非 Javadoc)
	 * @see com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService#setCommonUpdateColmun(java.lang.Object)
	 */
	@Override
	protected void setCommonUpdateColmun(MMember entity) {
		super.setCommonUpdateColmun(entity);
		// 最終更新日時をセット
		entity.lastUpdateDatetime = entity.updateDatetime;
	}

	/**
	 * NULLを許可するカラムを含んだテーブルに対してUPDATEを行います。
	 * @param entity 会員エンティティ
	 * @return UPDATE件数
	 */
	public int updateWithNull(MMember entity) {
		return updateWithNull(entity, NULL_ENABLED_COLUMN_LIST);
	}

	/**
	 * IDをキーに会員マスタ、会員属性マスタ情報を取得する。
	 * @param id 会員ID
	 * @return 会員情報
	 * @throws WNoResultException
	 */
	public MMember getMemberDataById(int id) throws WNoResultException {

		try {
			MMember entity = jdbcManager.from(MMember.class)
								.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.leftOuterJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.leftOuterJoin("mMemberHopeCityList", "mMemberHopeCityList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.where(new SimpleWhere()
								.eq("id", id)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.orderBy("mMemberAttributeList.id, mMemberHopeCityList.id")
								.disallowNoResult()
								.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * ログインIDから会員を取得します。
	 * @param loginId ログインID
	 * @param areaCd エリアコード NULLを渡した場合は、エリアコード判定を行わない。
	 * @param memberKbnList 会員区分一覧 NULL/空リストを渡した場合は会員区分判定を行わない
	 * @return ログインIDに該当する会員データ
	 * @throws WNoResultException 会員が見つからなかった場合にtrue
	 */
	public MMember findByLoginId(String loginId, AreaCdEnum areaCd, List<Integer> memberKbnList) throws WNoResultException {
		SimpleWhere where = new SimpleWhere();
		where.eq(toCamelCase(MMember.LOGIN_ID), loginId);
		if (areaCd != null) {
			where.eq(toCamelCase(MMember.AREA_CD), areaCd.getValue());
		}
		if (CollectionUtils.isNotEmpty(memberKbnList)) {
			where.in(toCamelCase(MMember.MEMBER_KBN), memberKbnList);
		}
		where.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);


		try {
			return jdbcManager.from(MMember.class)
							.where(where)
							.disallowNoResult()
							.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * ID、エリアコード、会員区分をキーに会員マスタ、会員属性マスタ情報を取得する。
	 * @param id 会員ID
	 * @param areaCd エリアコード
	 * @param exceptMemberKbn 対象外の会員区分
	 * @return 会員情報
	 * @throws WNoResultException
	 */
	public MMember getAreaMemberDataById(int id, int areaCd, int exceptMemberKbn) throws WNoResultException {

		try {
			MMember entity = jdbcManager.from(MMember.class)
								.leftOuterJoin("vMemberHopeCityList")
								.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.leftOuterJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.where(new SimpleWhere()
								.eq("id", id)
								.eq("areaCd", areaCd)
								.ne("memberKbn", exceptMemberKbn)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.orderBy("mMemberAttributeList.id, vMemberHopeCityList.prefecturesCd, vMemberHopeCityList.cityCd")
								.disallowNoResult()
								.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * ID、会員区分をキーに会員マスタ、会員属性マスタ情報を取得する。
	 * @param id 会員ID
	 * @param exceptMemberKbn 対象外の会員区分
	 * @return 会員情報
	 * @throws WNoResultException
	 */
	public MMember getMemberDataById(int id, int exceptMemberKbn) throws WNoResultException {

		try {
			MMember entity = jdbcManager.from(MMember.class)
								.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.leftOuterJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
								.where(new SimpleWhere()
								.eq("id", id)
								.ne("memberKbn", exceptMemberKbn)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.orderBy("mMemberAttributeList.id")
								.disallowNoResult()
								.getSingleResult();

			// 希望勤務地は大量に登録されていて同時に取得すると重いので、個別に取得する
			List<VMemberHopeCity> cityList = jdbcManager.from(VMemberHopeCity.class)
				.where(new SimpleWhere().eq(toCamelCase(VMemberHopeCity.MEMBER_ID), id))
				.orderBy(createCommaStr(new String[] {toCamelCase(VMemberHopeCity.PREFECTURES_CD), toCamelCase(VMemberHopeCity.CITY_CD)}))
				.getResultList();
			entity.vMemberHopeCityList = CollectionUtils.isNotEmpty(cityList) ? cityList : new ArrayList<>();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * CSVに出力する会員データを取得
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<MMember> getMemberDataForCsv(Map<String, String> map, String sortKey) throws WNoResultException, ParseException {

		List<MMember> entityList = null;

		try {
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();

			// SQL作成
			createSql(sqlStr, params, map);

			// 検索を行い該当する顧客のidのリストを取得
			List<Integer> idList = getMemberIdList(sqlStr, params);

			entityList = getMemberList(idList, sortKey);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	/**
	 * 検索のメインロジック
	 * @param targetPage 表示するページ数
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<MMember> doSearch(PageNavigateHelper pageNavi,Map<String, String> map, int targetPage) throws WNoResultException, ParseException {

		List<MMember> entityList = null;

		try {
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();

			// カウント用SQL作成
			createSql(sqlStr, params, map);

			//ページナビゲータを使用
			int count = (int)getCountBySql(sqlStr.toString(), params.toArray());
			pageNavi.changeAllCount(count);
			pageNavi.setPage(targetPage);

			// 検索結果のIDを取得するSQLを作成
			addSql(sqlStr, params, pageNavi);

			// 検索を行い該当する顧客のidのリストを取得
			List<Integer> idList = getMemberIdList(sqlStr, params);

			entityList = getMemberList(idList, pageNavi);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	/**
	 * 検索用SQLを生成
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 * @throws ParseException
	 */
	private void createSql(StringBuilder sqlStr, List<Object> params, Map<String, String> map ) throws ParseException {

		sqlStr.append("SELECT m.id  FROM m_member m LEFT OUTER JOIN m_member_attribute ma ON m.id = ma.member_id ");

		// 事前登録のみの会員は検索しない
		sqlStr.append("WHERE m.member_kbn != ? ");
		params.add(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER);


		// 会員IDが選択されている場合
		if (!"".equals(map.get(ID))) {
			sqlStr.append("AND m.id = ? ");
			params.add(NumberUtils.toInt(map.get(ID)));
		}

		// 会員名が選択されている場合
		if (!"".equals(map.get(MEMBER_NAME))) {
			String name = map.get(MEMBER_NAME).replaceAll("　| ", "");

            sqlStr.append("AND REPLACE(REPLACE(m.member_name, '　', ''), ' ', '') like ? ");
            params.add("%" + name + "%");
		}

		// フリガナが指定されている場合
		if (!"".equals(map.get(MEMBER_NAME_KANA))) {
			String nameKana = map.get(MEMBER_NAME_KANA).replaceAll("　| ", "");

            sqlStr.append("AND REPLACE(REPLACE(m.member_name_kana, '　', ''), ' ', '') like ? ");
            params.add("%" + nameKana + "%");

		}

		if (!"".equals(map.get(AREA_CD))) {
			sqlStr.append("AND m.area_cd = ? ");
			params.add(NumberUtils.toInt(map.get(AREA_CD)));
		}

		// 希望エリアが選択されている場合は、希望都道府県を検索
		if (!"".equals(map.get("hope_area"))) {
			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM v_member_hope_city");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append("    AND prefectures_cd = ? ");
			sqlStr.append(") ");
			params.add(NumberUtils.toInt(map.get("hope_area")));
		}

		// 都道府県が選択されている場合
		if (!"".equals(map.get(PREFECTURES_CD))) {
			sqlStr.append("AND m.prefectures_cd = ? ");
			params.add(NumberUtils.toInt(map.get(PREFECTURES_CD)));
		}

		// 業種が選択されている場合
		if (!"".equals(map.get(IndustryKbn.TYPE_CD))) {
			sqlStr.append("AND (ma.attribute_cd = ? AND ma.attribute_value = ? ) ");
			params.add(IndustryKbn.TYPE_CD);
			params.add(NumberUtils.toInt(map.get(IndustryKbn.TYPE_CD)));
		}

		// 雇用形態が選択されている場合
		if (!"".equals(map.get(EMPLOY_PTN_KBN))) {
			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM m_member_attribute");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append("    AND attribute_value = ? ");
			sqlStr.append("    AND attribute_cd = ? ");
			sqlStr.append("    AND delete_flg = ? ");
			sqlStr.append(") ");

			params.add(NumberUtils.toInt(map.get(EMPLOY_PTN_KBN)));
			params.add(MTypeConstants.EmployPtnKbn.TYPE_CD);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 性別が選択されている場合
		if (!"".equals(map.get(SEX_KBN))) {
			sqlStr.append("AND m.sex_kbn = ? ");
			params.add(NumberUtils.toInt(map.get(SEX_KBN)));
		}

		// 下限年齢が選択されている場合
		if (!"".equals(map.get(LOWER_AGE))) {

			sqlStr.append("AND m.birthday <= ? ");
			params.add(convertToTimestampDivYear(NumberUtils.toInt(map.get(LOWER_AGE)), 0));
		}

		// 上限年齢が選択されている場合
		if (!"".equals(map.get(UPPER_AGE))) {

			sqlStr.append("AND m.birthday > ? ");
			params.add(convertToTimestampDivYear(NumberUtils.toInt(map.get(UPPER_AGE)), 1));
		}

		// 更新日(開始)が選択されている場合
		if (!"".equals(map.get(FROM_UPDATE_DATE))) {
			sqlStr.append("AND m.last_update_datetime >= ? ");
			params.add(convertDateStrToTimestampAddDate(map.get(FROM_UPDATE_DATE), 0));
		}

		// 更新日(終了)が選択されている場合
		if (!"".equals(map.get(TO_UPDATE_DATE))) {
			sqlStr.append("AND m.last_update_datetime < ? ");
			params.add(convertDateStrToTimestampAddDate(map.get(TO_UPDATE_DATE), 1));
		}

		// 登録日(開始)が選択されている場合
		if (!"".equals(map.get(FROM_INSERT_DATE))) {
			sqlStr.append("AND m.insert_datetime >= ? ");
			params.add(convertDateStrToTimestampAddDate(map.get(FROM_INSERT_DATE), 0));
		}

		// 登録日(終了)が選択されている場合
		if (!"".equals(map.get(TO_INSERT_DATE))) {
			sqlStr.append("AND m.insert_datetime < ? ");
			params.add(convertDateStrToTimestampAddDate(map.get(TO_INSERT_DATE), 1));
		}

		// 転職相談窓口からの求人情報（ジャスキル連絡）が選択されている場合
		if (!"".equals(map.get(JUSKILL_CONTACT_FLG))) {
			sqlStr.append("AND m.juskill_contact_flg = ? ");
			params.add(NumberUtils.toInt(map.get(JUSKILL_CONTACT_FLG)));
		}

		// ジャスキル登録が選択されている場合
		if (!"".equals(map.get(JUSKILL_FLG))) {
			sqlStr.append("AND m.juskill_flg = ? ");
			params.add(NumberUtils.toInt(map.get(JUSKILL_FLG)));
		}

		// メールが選択されている場合
		if (!"".equals(map.get(LOGIN_ID))) {
			sqlStr.append("AND m.login_id like ? ");
			params.add("%" + map.get(LOGIN_ID) + "%");
		}

		// 雑誌受け取りフラグが選択されている場合
		if (!"".equals(map.get(GOURMET_MAGAZINE_RECEPTION_FLG))) {
			sqlStr.append("AND m.gourmet_magazine_reception_flg = ? ");
			params.add(NumberUtils.toInt(map.get(GOURMET_MAGAZINE_RECEPTION_FLG)));
		}

		// 配送状況が選択されている場合
		if (!"".equals(map.get(DELIVERY_STATUS))) {
			sqlStr.append("AND m.delivery_status = ? ");
			params.add(NumberUtils.toInt(map.get(DELIVERY_STATUS)));
		}

		sqlStr.append(" AND m.delete_flg = ?");
		params.add(DeleteFlgKbn.NOT_DELETED);

		sqlStr.append("GROUP BY m.id, ma.member_id ");

	}

	/**
	 *
	 * @param sqlStr
	 * @param params
	 * @param pageNavi
	 */
	private void addSql(StringBuilder sqlStr, List<Object> params, PageNavigateHelper pageNavi) {

		sqlStr.append("ORDER BY m.id DESC LIMIT ? OFFSET ?");
		params.add(pageNavi.limit);
		params.add(pageNavi.offset);

	}

	/**
	 * 検索結果の会員IDリストを返す
	 * @param where 検索条件
	 * @param pageNavi ﾍﾟｰｼﾞナビ
	 * @return 会員IDリスト
	 */
	private List<Integer> getMemberIdList(StringBuilder sqlStr, List<Object> params) {

		List<MMember> entityList = jdbcManager.selectBySql(MMember.class, sqlStr.toString(), params.toArray()).disallowNoResult().getResultList();


		List<Integer> idList = new ArrayList<Integer>();

		for (MMember entity : entityList) {
			idList.add(entity.id);
		}

		return idList;
	}

	/**
	 * 検索結果の会員リストを返す
	 * @param where 検索条件
	 * @param pageNavi ﾍﾟｰｼﾞナビ
	 * @return 会員リスト
	 */
	private List<MMember> getMemberList(List<Integer> idList, PageNavigateHelper pageNavi) {

		List<MMember> entityList = jdbcManager.from(MMember.class)
										.leftOuterJoin("vMemberHopeCityList")
										.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.in("id", idList.toArray()))
										.orderBy(pageNavi.sortKey + ", vMemberHopeCityList.prefecturesCd, vMemberHopeCityList.cityCd")
										.disallowNoResult()
										.getResultList();

		return entityList;
	}

	/**
	 * 検索結果の会員リストを返す
	 * @param idList 会員IDのリスト
	 * @param sortKey ソートキー
	 * @return 会員リスト
	 */
	private List<MMember> getMemberList(List<Integer> idList, String sortKey) {

		List<MMember> entityList = jdbcManager.from(MMember.class)
										.leftOuterJoin("vMemberHopeCityList")
										.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.leftOuterJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.in("id", idList.toArray()))
										.orderBy(sortKey + ", vMemberHopeCityList.prefecturesCd, vMemberHopeCityList.cityCd")
										.disallowNoResult()
										.getResultList();

		return entityList;
	}



	/**
	 * 検索のメインロジック(顧客側)
	 * @param pageNavi ページナビヘルパー
	 * @param property 検索条件プロパティ
	 * @throws WNoResultException
	 * @throws ParseException
	 */

	public List<MMember> doSearchByCustomer(PageNavigateHelper pageNavi, SearchProperty property) throws WNoResultException {
		List<MMember> entityList = null;

		try {
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();

			// カウント用SQL作成
			createSqlCustomer(sqlStr, params, property);
			StringBuilder countSqlStr = new StringBuilder(sqlStr);
			countSqlStr.append(" GROUP BY m.id, tl.last_login_datetime ");

			// 件数を取得
			int count = (int)getCountBySql(countSqlStr.toString(), params.toArray());
			pageNavi.changeAllCount(count);

			// 検索結果のIDを取得するSQLを作成
			addSqlCustomer(sqlStr, params, pageNavi);

			// 検索を行い該当する顧客のidのリストを取得
			List<Integer> idList = getMemberIdListCustomer(sqlStr, params);

			entityList = getMemberListCustomerWithAttributes(idList);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}


	/**
	 * 検索用SQLを生成
	 * @param sqlStr
	 * @param params
	 * @param map
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	private void createSqlCustomer(StringBuilder sqlStr, List<Object> params, SearchProperty property) {
		sqlStr.append("SELECT m.id  FROM m_member m ");
		sqlStr.append("INNER JOIN t_login_history tl ON tl.member_id = m.id ");
		sqlStr.append("WHERE m.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
		sqlStr.append("AND tl.last_login_datetime >= DATE_TRUNC('day', current_timestamp - interval '3 years')");

		// 業種が選択されている場合
		if (CollectionUtils.isNotEmpty(property.inductryCdList)) {
			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM m_member_attribute");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append(SqlUtils.andInNoCamelize("attribute_value", property.inductryCdList.size()));
			sqlStr.append("    AND attribute_cd = ? ");
			sqlStr.append("    AND delete_flg = ? ");
			sqlStr.append(") ");

			params.addAll(property.inductryCdList);
			params.add(MTypeConstants.IndustryKbn.TYPE_CD);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 雇用形態が選択されている場合
		if (CollectionUtils.isNotEmpty(property.employPtnKbnList)) {
			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM m_member_attribute");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append(SqlUtils.andInNoCamelize("attribute_value", property.employPtnKbnList.size()));
			sqlStr.append("    AND attribute_cd = ? ");
			sqlStr.append("    AND delete_flg = ? ");
			sqlStr.append(") ");

			params.addAll(property.employPtnKbnList);
			params.add(MTypeConstants.EmployPtnKbn.TYPE_CD);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}


		// 職種が選択されている場合
		if (CollectionUtils.isNotEmpty(property.jobKbnList)) {
			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM m_member_attribute");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append(SqlUtils.andInNoCamelize("attribute_value", property.jobKbnList.size()));
			sqlStr.append("    AND attribute_cd = ? ");
			sqlStr.append("    AND delete_flg = ? ");
			sqlStr.append(") ");

			params.addAll(property.jobKbnList);
			params.add(MTypeConstants.JobKbn.TYPE_CD);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 資格が選択されている場合
		if (CollectionUtils.isNotEmpty(property.qualificationList)) {
			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM m_member_attribute");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append(SqlUtils.andInNoCamelize("attribute_value", property.qualificationList.size()));
			sqlStr.append("    AND attribute_cd = ? ");
			sqlStr.append("    AND delete_flg = ? ");
			sqlStr.append(") ");

			params.addAll(property.qualificationList);
			params.add(MTypeConstants.QualificationKbn.TYPE_CD);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 希望勤務地が選択されている場合、市区町村を検索
		if (CollectionUtils.isNotEmpty(property.cityCdList)) {

			sqlStr.append(" AND EXISTS (");
			sqlStr.append("    SELECT 1 FROM m_member_hope_city");
			sqlStr.append("    WHERE");
			sqlStr.append("    member_id = m.id ");
			sqlStr.append(SqlUtils.andInNoCamelize("city_cd", property.cityCdList.size()));
			sqlStr.append("    AND delete_flg = ? ");
			sqlStr.append(") ");

			params.addAll(property.cityCdList);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 現住所が指定されている場合
		if (CollectionUtils.isNotEmpty(property.addressList)) {
			sqlStr.append(SqlUtils.andInNoCamelize("m.prefectures_cd", property.addressList.size()));
			params.addAll(property.addressList);
		}

		// 性別が選択されている場合
		if (CollectionUtils.isNotEmpty(property.sexKbnList)) {
			sqlStr.append(SqlUtils.andInNoCamelize("m.sex_kbn", property.sexKbnList.size()));
			params.addAll(property.sexKbnList);
		}

		// 下限年齢が選択されている場合
		if (property.lowerAge != null) {

			sqlStr.append("AND m.birthday <= ? ");
			params.add(convertToTimestampDivYear(property.lowerAge, 0));
		}

		// 上限年齢が選択されている場合
		if (property.upperAge != null) {

			sqlStr.append("AND m.birthday > ? ");
			params.add(convertToTimestampDivYear(property.upperAge, 1));
		}

		// 転勤可が選択されている場合
		if (property.transferFlg != null) {
			sqlStr.append("AND m.transfer_flg = ? ");
			params.add(property.transferFlg);
		}

		// 深夜勤務可が選択されている場合
		if (property.midnightShiftFlg != null) {
			sqlStr.append("AND m.midnight_shift_flg = ? ");
			params.add(property.midnightShiftFlg);
		}

		// スカウト済を除外が選択されている場合
		if (property.scoutedFlg != null) {
			sqlStr.append(" AND NOT EXISTS (");
			sqlStr.append("    SELECT 1 FROM t_scout_mail_manage MANAGE");
			sqlStr.append("    INNER JOIN t_scout_mail_log MAILLOG");
			sqlStr.append("    ON MANAGE.id = MAILLOG.scout_manage_id AND MAILLOG.member_id IS NOT NULL");
			sqlStr.append("    WHERE");
			sqlStr.append("    MANAGE.customer_id = ? ");
			sqlStr.append("    AND MAILLOG.member_id = m.id ");
			sqlStr.append("    AND MANAGE.delete_flg = ? ");
			sqlStr.append(") ");

			params.add(property.customerId);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 気になる済を除外が選択されている場合
		if (property.favoriteFlg != null) {
			sqlStr.append(" AND NOT EXISTS (");
			sqlStr.append("    SELECT 1 FROM t_footprint FOOT");
			sqlStr.append("    WHERE");
			sqlStr.append("    FOOT.customer_id = ? ");
			sqlStr.append("    AND FOOT.member_id = m.id ");
			sqlStr.append("    AND FOOT.delete_flg = ? ");
			sqlStr.append(") ");

			params.add(property.customerId);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 応募ありを除外が選択されている場合
		if (property.subscFlg != null) {
			sqlStr.append(" AND NOT EXISTS (");
			sqlStr.append("    SELECT 1 FROM t_application APPLICATION");
			sqlStr.append("    INNER JOIN t_web WEB");
			sqlStr.append("    ON APPLICATION.web_id = WEB.id");
			sqlStr.append("    WHERE");
			sqlStr.append("    WEB.customer_id = ? ");
			sqlStr.append("    AND APPLICATION.member_id = m.id ");
			sqlStr.append("    AND APPLICATION.delete_flg = ? ");
			sqlStr.append(") ");

			params.add(property.customerId);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 辞退を除外が選択されている場合
		if (property.refuseFlg != null) {
			sqlStr.append(" AND NOT EXISTS (");
			sqlStr.append("    SELECT 1 FROM v_member_mailbox MAILBOX");
			sqlStr.append("    WHERE");
			sqlStr.append("    MAILBOX.customer_id = ? ");
			sqlStr.append("    AND MAILBOX.member_id = m.id ");
			sqlStr.append("    AND MAILBOX.scout_receive_kbn = ? ");
			sqlStr.append(") ");

			params.add(property.customerId);
			params.add(MTypeConstants.ScoutReceiveKbn.REFUSAL);
		}

		// キーワードが選択されている場合
		if (StringUtils.isNotBlank(property.keyword)) {
			Pattern pattern = Pattern.compile(GourmetCareeConstants.ZENBUN_BAD_WORD);
			Matcher matcher = pattern.matcher(property.keyword);
			String keyword = WztStringUtil.trim(
								matcher.replaceAll(property.keyword)
										.replaceAll("　", " "));
			sqlStr.append(" AND (");
			// スカウト自己PR
			sqlStr.append(SqlUtils.createLikeSearch(
					keyword,
					params,
					" m.scout_self_pr LIKE ? "));

			sqlStr.append("  OR ");
			// 市区町村
			sqlStr.append(SqlUtils.createLikeSearch(
					keyword,
					params,
					" m.municipality LIKE ? "));

			// 都道府県名(都府県をカットして一致するか)
			String[] keywords = keyword.trim().split("[ 　]");
			final Set<Integer> prefSet = new HashSet<>();
			Stream.of(keywords)
				.map(s -> convertSearchPrefCd(s))
				.map(MAreaConstants.AreaGroup::getPrefCode)
				.filter(s -> s != null)
				.forEach(prefSet::add);
			if (CollectionUtils.isNotEmpty(prefSet)) {
				sqlStr.append(SqlUtils.orInNoCamelize("m.prefectures_cd", prefSet.size()));
				params.addAll(prefSet);
			}

			sqlStr.append(" ) ");
		}

		// スカウト受信フラグが選択されている場合
		if (property.exceptScoutKbn != null) {
			sqlStr.append(" AND m.scout_mail_reception_flg != ?");
			params.add(property.exceptScoutKbn);
		}

		sqlStr.append("  AND NOT EXISTS (");
		sqlStr.append("         SELECT * FROM ");
		sqlStr.append("                t_scout_block BLOCK ");
		sqlStr.append("         WHERE ");
		sqlStr.append("                BLOCK.customer_id = ? ");
		sqlStr.append("                AND BLOCK.member_id = m.id");
		sqlStr.append("                AND BLOCK.delete_flg = ?");
		sqlStr.append("");
		sqlStr.append(" )");

		params.add(property.customerId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 会員区分が選択されている場合
		if (CollectionUtils.isNotEmpty(property.exceptMemberKbnList)) {
			sqlStr.append("AND m.member_kbn NOT IN ( ")
				.append(SqlUtils.getQMarks(property.exceptMemberKbnList.size()))
				.append(")");
			params.addAll(property.exceptMemberKbnList);
		}

//		// 希望地域が選択されている場合(リニューアルで削除）
//		if (CollectionUtils.isNotEmpty(property.areaList)) {
//			sqlStr.append(" AND mma.area_cd IN ( ")
//				.append(SqlUtils.getQMarks(property.areaList.size()))
//				.append(")");
//			params.addAll(property.areaList);
//		}

	}

	/**
	 * 都道府県検索用にキーワードを変換する
	 * @param keyword
	 * @return 都道府県用に変換したキーワード
	 */
	private  String convertSearchPrefCd(String keyword)
	{
		if (keyword.contains("東京都")) {
			return keyword.replaceAll("東京都", "東京");
		}
		if (keyword.contains("京都府")) {
			return keyword.replaceAll("京都府", "京都");
		}
		return keyword.replaceAll("[府県]$", "");
	}

	/**
	 * メルマガ送信対象の会員を取得します。
	 * @param map 検索条件を保持するMAP
	 * @return 会員一覧
	 * @throws NumberFormatException 数値変換に失敗した場合のエラー
	 * @throws ParseException 日付けが不正な場合のエラー
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public List<MMember> getSendMailMagazineMember(Map<String, String> map) throws NumberFormatException, ParseException, WNoResultException {

		StringBuilder whereStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		// 運営側一覧の検索条件をセット
		createAdminMemberWhereSql(whereStr, params, map);

		// メルマガ送信の条件をセット
		createMailMagazineMemberWhereSql(whereStr, params);

		// エリアが選択されている場合
		if (isNotEmpty(map.get("hope_area"))) {
			// エリアコードの条件をセット
			createMemberAreaWhereSql(whereStr, params, map.get("hope_area"));
		}

		// 会員一覧を取得
		List<MMember> list = findByCondition(whereStr.toString(), params.toArray(), createAdminMemberListSortKey());

		// ログの出力
		SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
		SqlLog sqlLog = sqlLogRegistry.getLast();
		log.info("会員向けメルマガ配信先を取得するSQL：" + sqlLog.getCompleteSql());

		return list;
	}

	/**
	 * 会員エリア情報の検索条件を付与します。
	 * @param whereStr
	 * @param params
	 * @param areaCd
	 */
	private void createMemberAreaWhereSql (StringBuilder whereStr, List<Object> params, String areaCd) {

		// 希望エリアが選択されている場合は、希望都道府県を検索
		whereStr.append(" AND EXISTS (");
		whereStr.append("    SELECT 1 FROM v_member_hope_city");
		whereStr.append("    WHERE");
		whereStr.append("    member_id = T1_.id ");
		whereStr.append("    AND prefectures_cd = ? ");
		whereStr.append(") ");

		params.add(Integer.parseInt(areaCd));

	}

	/**
	 * メルマガ送信対象の事前登録会員を取得します。
	 * @param map 検索条件を保持するMAP
	 * @return 会員一覧
	 * @throws NumberFormatException 数値変換に失敗した場合のエラー
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public List<MMember> getSendMailMagazineMemberForAdvancedRegistration(Map<String, Object> map) throws WNoResultException, NumberFormatException {
		StringBuilder whereStr = new StringBuilder(0);
		List<Object> params = new ArrayList<Object>();

		// 運営側一覧の検索条件をセット
		createAdminMemberWhereSqlForAdvancedRegistration(whereStr, params, map);

		// メルマガ送信の条件をセット
		createAdvancedMailMagazineMemberWhereSql(whereStr, params);

		List<MMember> list = findByCondition(whereStr.toString(), params.toArray(), createAdminMemberListSortKey());

		// ログの出力
		SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
		SqlLog sqlLog = sqlLogRegistry.getLast();
		log.info("会員向けメルマガ配信先を取得するSQL：" + sqlLog.getCompleteSql());

		return list;
	}


	private void createAdminMemberWhereSqlForAdvancedRegistration(StringBuilder whereStr, List<Object> params, Map<String, Object> map) {
		// 削除フラグ
		whereStr.append(eq(DELETE_FLG));
		params.add(DeleteFlgKbn.NOT_DELETED);

		// IDが選択されている場合
		if (map.containsKey(ID)) {
			whereStr.append(andEq(ID));
			params.add(Integer.parseInt(String.valueOf(map.get(ID))));
		}

		// 会員名が選択されている場合
		if (map.containsKey(MEMBER_NAME)) {
			whereStr.append(andLike(MEMBER_NAME));
			params.add(containPercent(String.valueOf(map.get(MEMBER_NAME))));
		}

		// エリアが選択されている場合
		if (map.containsKey(AREA_CD)) {
			whereStr.append(andEq(AREA_CD));
			params.add(Integer.parseInt(String.valueOf(map.get(AREA_CD))));
		}

		// 都道府県が選択されている場合
		if (map.containsKey(PREFECTURES_CD)) {
			whereStr.append(andEq(PREFECTURES_CD));
			params.add(Integer.parseInt(String.valueOf(map.get(PREFECTURES_CD))));
		}

		// 性別が選択されている場合
		if (map.containsKey(SEX_KBN)) {
			whereStr.append(andEq(SEX_KBN));
			params.add(Integer.parseInt(String.valueOf(map.get(SEX_KBN))));
		}

		// 下限年齢が選択されている場合
		if (map.containsKey(LOWER_AGE)) {
			whereStr.append(andLe(BIRTHDAY));
			params.add(convertToTimestampDivYear(Integer.parseInt(String.valueOf(map.get(LOWER_AGE))), 0));
		}

		// 下限年齢が選択されている場合
		if (map.containsKey(UPPER_AGE)) {
			whereStr.append(andGt(BIRTHDAY));
			params.add(convertToTimestampDivYear(Integer.parseInt(String.valueOf(map.get(UPPER_AGE))), 1));
		}

		// メールが選択されている場合
		if (map.containsKey(LOGIN_ID)) {
			whereStr.append(andLike(LOGIN_ID));
			params.add(containPercent(String.valueOf(map.get(LOGIN_ID))));
		}

		// 登録状況が選択されている場合
		if (map.containsKey(MTypeConstants.MemberKbn.TYPE_CD)) {
			Object obj = map.get(MTypeConstants.MemberKbn.TYPE_CD);
			List<?> kbnList;
			if (obj instanceof List<?>) {
				kbnList = (List<?>) obj;
			} else {
				throw new FraudulentProcessException("登録状況が不正な値です。");
			}

			whereStr.append(andIn(MMember.MEMBER_KBN, kbnList.size()));
			for (Object kbn : kbnList) {
				params.add(Integer.parseInt(String.valueOf(kbn)));
			}
		}

		// メルマガが受信できることが大前提のため、この条件は外す
		// メルマガが選択されている場合
//		if (map.containsKey(MTypeConstants.MailMagazineReceptionFlg.TYPE_CD)) {
//			andEq(MMember.ADVANCED_MAIL_MAGAZINE_RECEPTION_FLG);
//			params.add(Integer.parseInt(String.valueOf(map.get(MTypeConstants.advance.TYPE_CD))));
//		}

		// 端末が指定されている場合
		if (map.containsKey(MTypeConstants.TerminalKbn.TYPE_CD)) {
			andEq(MMember.TERMINAL_KBN);
			params.add(Integer.parseInt(String.valueOf(map.get(MTypeConstants.TerminalKbn.TYPE_CD))));
		}
	}

	/**
	 * 運営者側システム会員一覧の検索条件を生成します。
	 * @param whereStr SQL文
	 * @param params 値
	 * @param map 検索条件を保持するMAP
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	private void createAdminMemberWhereSql(StringBuilder whereStr, List<Object> params, Map<String, String> map)
	throws ParseException, NumberFormatException {

		// 事前登録のみの会員には送信しない
		whereStr.append(ne(MMember.MEMBER_KBN));
		params.add(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER);

		// IDが選択されている場合
		if (isNotEmpty(map.get(ID))) {
			whereStr.append(andEq(ID));
			params.add(Integer.parseInt(map.get(ID)));
		}

		// 会員名が選択されている場合
		if (isNotEmpty(map.get(MEMBER_NAME))) {
			whereStr.append(andLike(MEMBER_NAME));
			params.add(containPercent(map.get(MEMBER_NAME)));
		}

		if (isNotEmpty(map.get(AREA_CD))) {
			whereStr.append(andEq(AREA_CD));
			params.add(Integer.parseInt(map.get(AREA_CD)));
		}

		// 都道府県が選択されている場合
		if (isNotEmpty(map.get(PREFECTURES_CD))) {
			whereStr.append(andEq(PREFECTURES_CD));
			params.add(Integer.parseInt(map.get(PREFECTURES_CD)));
		}

		// 雇用形態が選択されている場合
		// 業種が選択されている場合
		if (isNotEmpty(map.get(EMPLOY_PTN_KBN))) {

			whereStr.append("  AND EXISTS( ");
			whereStr.append("    SELECT");
			whereStr.append("      * ");
			whereStr.append("    FROM");
			whereStr.append("      m_member_attribute ");
			whereStr.append("    WHERE");
			whereStr.append("      attribute_cd = ?");
			whereStr.append("      AND member_id = T1_.id ");
			whereStr.append("      AND attribute_value = ?");
			whereStr.append("      AND delete_flg = ?");
			whereStr.append("  ) ");

			params.add(MTypeConstants.EmployPtnKbn.TYPE_CD);
			params.add(Integer.parseInt(map.get(EMPLOY_PTN_KBN)));
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 性別が選択されている場合
		if (isNotEmpty(map.get(SEX_KBN))) {
			whereStr.append(andEq(SEX_KBN));
			params.add(Integer.parseInt(map.get(SEX_KBN)));
		}

		// 下限年齢が選択されている場合
		if (isNotEmpty(map.get(LOWER_AGE))) {
			whereStr.append(andLe(BIRTHDAY));
			params.add(convertToTimestampDivYear(Integer.parseInt(map.get(LOWER_AGE)), 0));
		}

		// 下限年齢が選択されている場合
		if (isNotEmpty(map.get(UPPER_AGE))) {
			whereStr.append(andGt(BIRTHDAY));
			params.add(convertToTimestampDivYear(Integer.parseInt(map.get(UPPER_AGE)), 1));
		}

		// 更新日(開始)が選択されている場合
		if (isNotEmpty(map.get(FROM_UPDATE_DATE))) {
			whereStr.append(andGe(LAST_UPDATE_DATETIME));
			params.add(convertDateStrToTimestampAddDate(map.get(FROM_UPDATE_DATE), 0));
		}

		// 更新日(終了)が選択されている場合
		if (isNotEmpty(map.get(TO_UPDATE_DATE))) {
			whereStr.append(andLt(LAST_UPDATE_DATETIME));
			params.add(convertDateStrToTimestampAddDate(map.get(TO_UPDATE_DATE), 1));
		}

		// ジャスキル登録が選択されている場合
		if (isNotEmpty(map.get(JUSKILL_FLG))) {
			whereStr.append(andEq(JUSKILL_FLG));
			params.add(Integer.parseInt(map.get(JUSKILL_FLG)));
		}

		// 転職相談窓口からの求人情報（ジャスキル連絡）が選択されている場合
		if (isNotEmpty(map.get(JUSKILL_CONTACT_FLG))) {
			whereStr.append(andEq(JUSKILL_CONTACT_FLG));
			params.add(Integer.parseInt(map.get(JUSKILL_CONTACT_FLG)));
		}

		// メールが選択されている場合
		if (isNotEmpty(map.get(LOGIN_ID))) {
			whereStr.append(andLike(LOGIN_ID));
			params.add(containPercent(map.get(LOGIN_ID)));
		}

		// 業種が選択されている場合
		if (isNotEmpty(map.get(IndustryKbn.TYPE_CD))) {

			whereStr.append("  AND EXISTS( ");
			whereStr.append("    SELECT");
			whereStr.append("      * ");
			whereStr.append("    FROM");
			whereStr.append("      m_member_attribute ");
			whereStr.append("    WHERE");
			whereStr.append("      attribute_cd = ?");
			whereStr.append("      AND member_id = T1_.id ");
			whereStr.append("      AND attribute_value = ?");
			whereStr.append("      AND delete_flg = ?");
			whereStr.append("  ) ");

			params.add(MTypeConstants.IndustryKbn.TYPE_CD);
			params.add(Integer.parseInt(map.get(IndustryKbn.TYPE_CD)));
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

		// 削除フラグ
		whereStr.append(andEq(DELETE_FLG));
		params.add(DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 会員向けメルマガを送信する条件を生成します。
	 * @param whereStr 検索条件
	 * @param params 値
	 */
	private void createMailMagazineMemberWhereSql(StringBuilder whereStr, List<Object> params) {

		// メルマガ受信フラグが受け取る
		whereStr.append(andEq(MAIL_MAGAZINE_RECEPTION_FLG));
		params.add(MailMagazineReceptionFlg.RECEPTION);
	}

	/**
	 * 会員向けメルマガを送信する条件を生成します。
	 * @param whereStr 検索条件
	 * @param params 値
	 */
	private void createAdvancedMailMagazineMemberWhereSql(StringBuilder whereStr, List<Object> params) {

		// メルマガ受信フラグが受け取る
		whereStr.append(andEq(ADVANCED_MAIL_MAGAZINE_RECEPTION_FLG));
		params.add(AdvancedMailMagReceptionFlg.RECEPTION);

		// 事前登録をしている会員
		whereStr.append(andIn(MEMBER_KBN, MemberKbn.ADVANCED_REGISTRATION_MEMBER_LIST.size()));
		params.addAll(MemberKbn.ADVANCED_REGISTRATION_MEMBER_LIST);
	}

	/**
	 * 運営側システムの会員一覧表示順を返します。
	 * @return 運営側システムの会員一覧表示順
	 */
	private String createAdminMemberListSortKey() {

		String[] sortKey = new String[]{
				desc(camelize(ID))
		};
		//カンマ区切りにして返す
		return createCommaStr(sortKey);
	}

	/**
	 *
	 * @param sqlStr
	 * @param params
	 * @param pageNavi
	 */
	private void addSqlCustomer(StringBuilder sqlStr, List<Object> params, PageNavigateHelper pageNavi) {

		sqlStr.append(" GROUP BY m.id, tl.last_login_datetime ORDER BY tl.last_login_datetime DESC, m.id DESC LIMIT ? OFFSET ?");
		params.add(pageNavi.limit);
		params.add(pageNavi.offset);

	}

	/**
	 * 検索結果の顧客IDリストを返す
	 * @param where 検索条件
	 * @param pageNavi ﾍﾟｰｼﾞナビ
	 * @return 顧客IDリスト
	 */
	private List<Integer> getMemberIdListCustomer(StringBuilder sqlStr, List<Object> params) {

		List<MMember> entityList = jdbcManager.selectBySql(MMember.class, sqlStr.toString(), params.toArray()).disallowNoResult().getResultList();

		List<Integer> idList = new ArrayList<Integer>();

		for (MMember entity : entityList) {
			idList.add(entity.id);
		}

		return idList;
	}

	/**
	 * 検索結果の会員リストを返す
	 * @param where 検索条件
	 * @return 会員リスト
	 */
	private List<MMember> getMemberListCustomer(List<Integer> idList) {

		List<MMember> entityList = jdbcManager.from(MMember.class)
				.innerJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
				.where(new SimpleWhere()
				.in("id", idList.toArray()))
				.orderBy("tLoginHistory.lastLoginDatetime desc, id desc")
				.disallowNoResult()
				.getResultList();

		return entityList;
	}

	/**
	 * 検索結果の会員リストを属性付きで返す
	 * @param where 検索条件
	 * @return 会員リスト
	 */
	private List<MMember> getMemberListCustomerWithAttributes(List<Integer> idList) {

		List<MMember> entityList = jdbcManager.from(MMember.class)
				.innerJoin("tLoginHistory", "tLoginHistory.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
				.leftOuterJoin("vMemberHopeCityList")
				.leftOuterJoin("mMemberAttributeList", "mMemberAttributeList.deleteFlg = ?",
						DeleteFlgKbn.NOT_DELETED)
				.where(new SimpleWhere()
				.in("id", idList.toArray()))
				.orderBy("tLoginHistory.lastLoginDatetime desc, id desc")
				.disallowNoResult()
				.getResultList();

		return entityList;
	}

	/**
	 *
	 * @param memberId
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public MemberStatusDto getMemberStatusDto(int memberId, int customerId) throws WNoResultException {

		try {
			// SQLを生成
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			createGetStatusSql(sqlStr, params, memberId, customerId);

			// 会員の状況を取得
			MemberStatusDto dto = getMemberStatusDto(sqlStr, params);
			dto.scoutFlg = getScoutFlg(customerId, memberId);
			return dto;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	private int getScoutFlg(int customerId, int memberId) {

		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		sb.append(" SELECT * FROM t_scout_mail_log LOGS ");
		sb.append("       INNER JOIN t_scout_mail_manage MANAGE ");
		sb.append("       ON MANAGE.customer_id = ? AND LOGS.member_id = ? AND LOGS.scout_manage_id = MANAGE.id");
		sb.append(" WHERE LOGS.scout_mail_log_kbn = ? ");
		sb.append("    AND LOGS.delete_flg = ? ");
		sb.append("    AND MANAGE.delete_flg = ? ");

		params.add(customerId);
		params.add(memberId);
		params.add(MTypeConstants.ScoutMailLogKbn.USE);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		int count = (int) jdbcManager.getCountBySql(sb.toString(), params.toArray());

		if (count > 0) {
			return 1;
		}
		return 0;
	}

	/**
	 * 会員の状況を取得用SQLを生成する
	 * @param sqlStr SQL文字列
	 * @param params 条件
	 * @param memberId 会員ID
	 * @param customerId 顧客ID
	 */
	private void createGetStatusSql(StringBuilder sqlStr, List<Object> params, int memberId, int customerId) {

		sqlStr.append("SELECT M.id, ");
		sqlStr.append("(CASE (EXISTS (SELECT * FROM t_application TA WHERE TA.delete_flg = ? AND TA.customer_id = ? AND TA.member_id = ? AND TA.application_datetime > now() - interval '2 year' )) ");
		sqlStr.append("WHEN true THEN 1 ELSE 0 END) as application_flg, ");
		sqlStr.append("(CASE (EXISTS (SELECT * FROM t_footprint TF WHERE TF.delete_flg = ? AND TF.customer_id = ? AND TF.member_id = ? AND TF.access_datetime > now() - interval '3 month' )) ");
		sqlStr.append("WHEN true THEN 1 ELSE 0 END) as footprint_flg, ");
		sqlStr.append("(CASE (EXISTS (SELECT * FROM t_scout_consideration TSC WHERE TSC.delete_flg = ? AND TSC.customer_id = ? AND TSC.member_id = ? )) ");
		sqlStr.append("WHEN true THEN 1 ELSE 0 END) as consideration_flg, ");
		sqlStr.append("(CASE (EXISTS (SELECT * FROM t_scout_block TSB WHERE TSB.delete_flg = ? AND TSB.customer_id = ? AND TSB.member_id = ? )) ");
		sqlStr.append("WHEN true THEN 1 ELSE 0 END) as scout_block_flg ");
		sqlStr.append("FROM m_member M WHERE M.delete_flg = ? AND M.id = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(memberId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(memberId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(memberId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(memberId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(memberId);
	}

	/**
	 * 会員状況のDTOを取得
	 * @param sqlStr SQL文字列
	 * @param params SQLパラメータ
	 * @return 会員状況DTO
	 */
	private MemberStatusDto getMemberStatusDto(StringBuilder sqlStr, List<Object> params) {

		return jdbcManager.selectBySql
									(MemberStatusDto.class, sqlStr.toString(), params.toArray())
									.disallowNoResult()
									.getSingleResult();

	}


	/**
	 * 引数の年+1年を引いた日付を返す
	 * @param year 年
	 * @return 日付(Timestamp)
	 */
	public static Timestamp convertToTimestampDivYear(int year, int addYear) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -(year + addYear));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());

	}

	/**
	 * 日付文字列から一日足したTimestampへ変換して返す
	 * @param dateStr 日付文字列
	 * @return 日付(Timestamp)
	 * @throws ParseException
	 */
	private Timestamp convertDateStrToTimestampAddDate(String dateStr, int addDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
		Date date = sdf.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);

		return new Timestamp(cal.getTimeInMillis());

	}

	/**
	 * 会員マスタにデータを登録します。<br />
	 * パスワードの変換は登録時に行うため、変換前の値を渡します。
	 * @param entity
	 */
	public void insertMemberData(MMember entity) {

		// 登録の初期値をセットする
		setInitData(entity);

		// 登録
		insert(entity);
	}

	/**
	 * 会員マスタを登録する初期データをセットします。
	 * XXX ここを変更する場合、front-logicの MemberRegistrationLogic#insertMemberData も変更すること。
	 * @param entity 会員エンティティ
	 */
	private void setInitData(MMember entity) {

		// パスワードを変換
		if (StringUtil.isNotBlank(entity.password)) {
			entity.password = DigestUtil.createDigest(entity.password);
		}
		// メンバー区分をセット（会員移行の対応のため、チェックをしてからセット）
		if (entity.memberKbn == null) {
			entity.memberKbn = MTypeConstants.MemberKbn.NEW_MEMBER;
		}
		// メルマガ配信停止フラグを「配信」にする
		entity.pcMailStopFlg = MTypeConstants.PcMailStopFlg.DELIVERY;
		entity.mobileMailStopFlg = MTypeConstants.MobileMailStopFlg.DELIVERY;
	}

	/**
	 * 指定したログインIDが指定したエリアに登録されているか判別します。<br />
	 * すでに削除されている場合は存在しないとみなします。
	 * @param loginId ログインID
	 * @return ログインIDが存在する場合はtrue、存在しない場合はfalse
	 */
	public boolean isLoginIdExists(String loginId) {

		// データ検索
		return isMemberExists(null, loginId);

	}

	public boolean existAdvancedRegistrationLoginId(String loginId, AreaCdEnum areaCd) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(MMember.LOGIN_ID), loginId);
		where.eq(WztStringUtil.toCamelCase(MMember.AREA_CD), areaCd.getValue());
		where.in(WztStringUtil.toCamelCase(MMember.MEMBER_KBN),
				MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER_LIST);

		long count = jdbcManager.from(MMember.class)
								.where(where)
								.getCount();

		return count > 0;
	}

	/**
	 * ジャスキルフラグを希望するに更新します。<br />
	 * すでに希望するに更新されていた場合は排他制御エラーを投げます。
	 * @param id 会員ID
	 * @exception SOptimisticLockException
	 */
	public void updateJuskillFlg(int id) throws SOptimisticLockException {

		MMember entity = findById(id);

		if (MTypeConstants.JuskillFlg.NOT_HOPE != entity.juskillFlg) {
			throw new SOptimisticLockException("すでに、ジャスキル登録されています。");
		}

		MMember updateEntity = new MMember();

		updateEntity.id = id;
		updateEntity.version = entity.version;
		// ジャスキルフラグを希望するをセット
		updateEntity.juskillFlg = MTypeConstants.JuskillFlg.HOPE;

		update(updateEntity);
	}

	/**
	 * 会員IDを条件に、会員マスタのデータを物理削除します。
	 * @param memberId 会員ID
	 */
	public void deleteMMemberByMemberId(int memberId) {

		MMember entity = new MMember();
		entity.id = memberId;
		deleteIgnoreVersion(entity);
	}

	/**
	 * 指定したログインIDが指定したエリアに登録されているか判別します。<br />
	 * すでに削除されている場合は存在しないとみなします。
	 * @param memberId 会員ID
	 * @param loginId ログインID
	 * @return ログインIDが存在する場合はtrue、存在しない場合はfalse
	 */
	public Boolean isMemberExists(String memberId, String loginId) {

		// 検索条件のセット
		SimpleWhere where = createLoginIdWhere(loginId);
		// 会員IDがセットされていれば条件に追加
		if (!StringUtil.isEmpty(memberId)) {
			where.eq(toCamelCase(MMember.ID), Integer.parseInt(memberId));
		}

		// 件数検索
		int count = (int) countRecords(where);

		// データがあればtrue
		return count > 0;
	}


	/**
	 * 事前登録会員が存在するかどうか
	 * @param memberId 会員ID
	 * @param loginId ログインID
	 * @param areaCd エリアコード
	 * @return ログインIDが存在する場合はtrue、存在しない場合はfalse
	 */
	public boolean isAdvancedMemberExists(String memberId, String loginId, AreaCdEnum areaCd) {
		SimpleWhere where = new SimpleWhere();
		if (!StringUtil.isEmpty(memberId)) {
			where.eq(toCamelCase(MMember.ID), Integer.parseInt(memberId));
		}
		where
		.eq(toCamelCase(MMember.LOGIN_ID), loginId)						// ログインID
		.eq(toCamelCase(MMember.AREA_CD), areaCd.getValue())			// エリアコード
		.eq(toCamelCase(MMember.MEMBER_KBN), MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER)
		.eq(toCamelCase(MMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);	// 削除フラグ

		long count = countRecords(where);
		return count > 0;
	}
	/**
	 * ログインIDとエリアコードを条件に会員マスタを検索します。<br />
	 * データが複数件存在する場合は、SNonUniqueResultExceptionを投げる
	 * @param loginId ログインID
	 * @return 会員マスタエンティティ
	 * @throws WNoResultException データが存在しない場合はエラー
	 * @throws SNonUniqueResultException データが複数存在する場合はエラー
	 */
	public MMember getEntityByLoginId(String loginId)
		throws WNoResultException, SNonUniqueResultException {

		// 検索条件のセット
		SimpleWhere where = createLoginIdWhere(loginId);

		try {
			// データの検索(1件以上データが存在する場合はエラー)
			return jdbcManager
					.from(MMember.class)
					.where(where)
					.disallowNoResult()
					.getSingleResult();

		// データが無い場合はエラー
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	public MMember getEntityByLoginIdWithAllKbn(String loginId) throws WNoResultException {
		// 検索条件のセット
				SimpleWhere where = new SimpleWhere()
						.eq(toCamelCase(MMember.LOGIN_ID), loginId)						// ログインID
						.eq(toCamelCase(MMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);	// 削除フラグ

		try {
			// データの検索(1件以上データが存在する場合はエラー)
			return jdbcManager
					.from(MMember.class)
					.where(where)
					.disallowNoResult()
					.getSingleResult();

		// データが無い場合はエラー
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * ログインIDを条件とする検索条件を作成します。
	 * @param loginId ログインID
	 * @return ログインIDの検索条件
	 */
	private SimpleWhere createLoginIdWhere(String loginId) {

		// 検索条件のセット
		SimpleWhere where = new SimpleWhere()
				.eq(toCamelCase(MMember.LOGIN_ID), loginId)						// ログインID
				.in(toCamelCase(MMember.MEMBER_KBN), MTypeConstants.MemberKbn.GOURMETCAREE_MEMBER_LIST)
				.eq(toCamelCase(MMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);	// 削除フラグ

		return where;
	}

	/**
	 * パスワードを更新します。<br />
	 * 会員エンティティにIDと、パスワードをセットして呼び出します。
	 * @param entity 会員エンティティ
	 */
	public void updataPassword(MMember entity) {

		// パスワードを変換
		entity.password = DigestUtil.createDigest(entity.password);
		// バージョンチェックせずパスワードを更新
		updateIncludesVersion(entity);
	}

	/**
	 * パスワードを更新します。<br />
	 * 会員エンティティにIDと、パスワードをセットして呼び出します。<br />
	 * パスワードをハッシュ化しません。
	 * @param entity 会員エンティティ
	 */
	public void updataPasswordNoDigest(MMember entity) {

		// バージョンチェックせずパスワードを更新
		updateIncludesVersion(entity);
	}

	/**
	 * 未ログイン状態の旧会員かどうかを判定します。
	 * @param memberId
	 * @param areaCdEnum
	 * @return true:未ログイン状態の旧会員、false:それ以外
	 */
	public boolean isNonLoginOldMember(int memberId, AreaCdEnum areaCdEnum){

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(MMember.ID), memberId)
								.eq(toCamelCase(MMember.AREA_CD), areaCdEnum.getValue())
								.eq(toCamelCase(MMember.MEMBER_KBN), MTypeConstants.MemberKbn.NON_LOGIN_OLD_MEMBER)
								.eq(toCamelCase(MMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return countRecords(where) > 0 ? true : false;
	}

	/**
	 * 指定したメルマガトークンが使用されているかを判定します。
	 * @param token
	 * @return
	 */
	public boolean existMailMagazineToken(String token) {
		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(MMember.MAIL_MAGAZINE_TOKEN), token)
								.eq(toCamelCase(MMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return countRecords(where) > 0 ? true : false;
	}


	public static class SearchProperty extends BaseProperty {

		/**
		 *
		 */
		private static final long serialVersionUID = -3896338470592127712L;

		/** 業態コードリスト */
		public List<Integer> inductryCdList;

		/** 雇用形態リスト */
		public List<Integer> employPtnKbnList;

		/** 職種リスト */
		public List<Integer> jobKbnList;

		/** 資格リスト */
		public List<Integer> qualificationList;

		/** 希望勤務地リスト */
		public List<Integer> webAreaCdList;

		/** 希望勤務地（リニューアル後） */
		public List<String> cityCdList;

		/** 性別リスト */
		public List<Integer> sexKbnList;

		/** 下限年令 */
		public Integer lowerAge;

		/** 上限年令 */
		public Integer upperAge;

		/** 転勤可不可 */
		public Integer transferFlg;

		/** 深夜勤務可不可 */
		public Integer midnightShiftFlg;

		/** キーワード */
		public String keyword;

		/** スカウト受信フラグ */
		public Integer exceptScoutKbn;

		/** 会員区分 */
		public List<Integer> exceptMemberKbnList;

		/** 希望地域 */
		public List<Integer> areaList;

		/** 希望勤務地(都道府県) */
		public List<Integer> prefList;

		/** 現住所 */
		public List<Integer> addressList;

		public Integer scoutedFlg;

		public Integer favoriteFlg;

		public Integer subscFlg;

		public Integer refuseFlg;

		/** 顧客ID */
		public Integer customerId;
	}
}
