package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static com.gourmetcaree.db.common.entity.MMember.ID;
import static com.gourmetcaree.db.common.entity.VJuskillMemberList.*;
import static com.gourmetcaree.db.common.entity.VJuskillMemberList.ADDRESS;
import static com.gourmetcaree.db.common.entity.VJuskillMemberList.PHONE_NO1;
import static com.gourmetcaree.db.common.entity.VJuskillMemberList.PHONE_NO2;
import static com.gourmetcaree.db.common.entity.member.BaseMemberEntity.PREFECTURES_CD;
import static com.gourmetcaree.db.common.entity.member.BaseMemberEntity.SEX_KBN;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;
import com.gourmetcaree.db.common.entity.VJuskillMemberList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * ジャスキル一覧関連のサービスクラス
 * @author whizz
 *
 */
public class JuskillMemberListService extends AbstractGroumetCareeBasicService<VJuskillMemberList> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(JuskillMemberListService.class);

	/** 上限年齢 */
	public static final String LOWER_AGE = "lower_age";

	/** 下限年齢 */
	public static final String UPPER_AGE = "upper_age";

	/** 登録日(開始) */
	public static final String FROM_INSERT_DATE = "from_insert_Date";

	/** 登録日(終了) */
	public static final String TO_INSERT_DATE = "to_insert_date";

	/** 会員登録フラグ */
	public static final String MEMBER_REGISTERED_FLG = "member_registered_flg";

	/**
	 * 検索のメインロジック
	 * @param targetPage 表示するページ数
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<VJuskillMemberList> doSearch(PageNavigateHelper pageNavi,Map<String, String> map, int targetPage) throws WNoResultException, ParseException {


		SimpleWhere where = createWhere(map);
		ComplexWhere orWhere = addFreeWordSearch(map);

		int count = (int) countRecords(where, orWhere);
		pageNavi.changeAllCount(count);
		pageNavi.setPage(targetPage);
		if(count == 0) {
			throw new WNoResultException();
		}

		pageNavi.sortKey = SqlUtils.desc(toCamelCase(JUSKILL_MEMBER_NO));
		return findByCondition(where, orWhere, pageNavi);
	}

	/**
	 * CSVに出力する会員データを取得
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<VJuskillMemberList> getJuskillMemberDataForCsv(Map<String, String> map, String sortKey) throws WNoResultException, ParseException {

		List<VJuskillMemberList> entityList = null;

		try {
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();

			// SQL作成
			createSql(sqlStr, params, map);

			// 検索を行い該当するリストを取得
			entityList = getJuskillMemberList(sqlStr, params);
		} catch (SNoResultException e) {
			SqlLogRegistry registry = SqlLogRegistryLocator.getInstance();
			if (registry != null) {
				SqlLog sqlLog = registry.getLast();
				System.out.println(sqlLog.getCompleteSql());
			}
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		sqlStr.append("SELECT *  FROM v_juskill_member_list ");
		sqlStr.append("WHERE delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

		if (!"".equals(map.get(ID))) {
			sqlStr.append("AND id = ? ");
			params.add(NumberUtils.toInt(map.get(ID)));
		}
		// 人材紹介登録者Noが入力されている場合
		if (!"".equals(map.get(JUSKILL_MEMBER_NO))) {
			sqlStr.append("AND juskill_member_no = ? ");
			params.add(NumberUtils.toInt(map.get(JUSKILL_MEMBER_NO)));
		}

		// 氏名が入力されている場合
		if (!"".equals(map.get(JUSKILL_MEMBER_NAME))) {
			sqlStr.append("AND juskill_member_name like ? ");
			params.add("%" + map.get(JUSKILL_MEMBER_NAME) + "%");
		}

		// 都道府県が入力されている場合
		if (!"".equals(map.get(PREFECTURES_CD))) {
			sqlStr.append("AND prefectures_cd = ? ");
			params.add(NumberUtils.toInt(map.get(PREFECTURES_CD)));
		}

		// 登録開始日が入力されている場合
		if (!"".equals(map.get(FROM_INSERT_DATE))) {
			sqlStr.append("AND juskill_entry_date >= ? ");
			params.add(sdf.parse(map.get(FROM_INSERT_DATE)));
		}

		// 登録終了日が入力されている場合
		if (!"".equals(map.get(TO_INSERT_DATE))) {
			sqlStr.append("AND juskill_entry_date <= ? ");
			params.add(sdf.parse(map.get(TO_INSERT_DATE)));
		}

		// 希望業態が入力されている場合
		if (!"".equals(map.get(HOPE_INDUSTRY))) {
			sqlStr.append("AND hope_industry like ? ");
			params.add("%" + map.get(HOPE_INDUSTRY) + "%");
		}

		// 雇用形態が入力されている場合
		if (!"".equals(map.get(HOPE_JOB))) {
			sqlStr.append("AND hope_job like ? ");
			params.add("%" + map.get(HOPE_JOB) + "%");
		}

		// 性別が選択されている場合
		if (!"".equals(map.get(SEX_KBN))) {
			sqlStr.append("AND sex_kbn = ? ");
			params.add(NumberUtils.toInt(map.get(SEX_KBN)));
		}

		// 上限年齢が入力されている場合
		if (!"".equals(map.get(UPPER_AGE))) {
			sqlStr.append("AND age <= ? ");
			params.add(NumberUtils.toInt(map.get(UPPER_AGE)));
		}

		// 下限年齢が入力されている場合
		if (!"".equals(map.get(LOWER_AGE))) {
			sqlStr.append("AND age >= ? ");
			params.add(NumberUtils.toInt(map.get(LOWER_AGE)));
		}

		// 会員登録が選択されている場合
		if (!"".equals(map.get(MEMBER_REGISTERED_FLG))) {
			sqlStr.append("AND member_id IS NOT NULL ");
		}

		// メールアドレスが入力されている場合
		if (!"".equals(map.get(MAIL))) {
			sqlStr.append("AND mail like ? ");
			params.add("%" + map.get(MAIL) + "%");
		}

		// フリーワードが入力されている場合
		if (!"".equals(map.get(FREE_WORD))) {
			String[] freeWords = splitFreeWord(map.get(FREE_WORD));
			int loop = 0;
			for(String freeWord : freeWords) {
				if(loop == 0) {
					sqlStr.append("AND ( ");
					sqlStr.append("address like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR phone_no1 like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR phone_no2 like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR closest_station like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR route like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR final_school_history like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR qualification like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR final_career_history like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR notice like ? ");
					params.add("%" + freeWord + "%");
				} else {
					sqlStr.append("OR address like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR phone_no1 like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR phone_no2 like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR closest_station like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR route like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR final_school_history like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR qualification like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR final_career_history like ? ");
					params.add("%" + freeWord + "%");
					sqlStr.append("OR notice like ? ");
					params.add("%" + freeWord + "%");
				}
				loop++;
			}
			sqlStr.append(") ");
		}
	}

	/**
	 * 検索条件を作成する
	 * @param map 画面から受け渡された値
	 * @return 検索条件をセットしたSimpleWhere
	 * @throws ParseException
	 */
	private SimpleWhere createWhere(Map<String, String> map) throws ParseException {

		SimpleWhere where = new SimpleWhere();

		if (StringUtil.isNotEmpty(map.get(ID))) {
			where.eq(toCamelCase(ID), trim(map.get(ID)));
		}
		if (StringUtil.isNotEmpty(map.get(JUSKILL_MEMBER_NO))) {
			where.eq(toCamelCase(JUSKILL_MEMBER_NO), trim(map.get(JUSKILL_MEMBER_NO)));
		}
		if (StringUtil.isNotEmpty(map.get(JUSKILL_MEMBER_NAME))) {
			String trimName = GourmetCareeUtil.removeAllSpace(map.get(JUSKILL_MEMBER_NAME));
			where.contains(toCamelCase(TRIM_NAME), trimName);
		}
		if (StringUtil.isNotEmpty(map.get(VJuskillMemberList.PREFECTURES_CD))) {
			where.eq(toCamelCase(VJuskillMemberList.PREFECTURES_CD), map.get(VJuskillMemberList.PREFECTURES_CD));
		}
		if (StringUtil.isNotEmpty(map.get(HOPE_INDUSTRY))) {
			where.contains(toCamelCase(HOPE_INDUSTRY), trim(map.get(HOPE_INDUSTRY)));
		}
		if (StringUtil.isNotEmpty(map.get(HOPE_JOB))) {
			where.contains(toCamelCase(HOPE_JOB), trim(map.get(HOPE_JOB)));
		}
		if (StringUtil.isNotEmpty(map.get(VJuskillMemberList.SEX_KBN))) {
			where.eq(toCamelCase(VJuskillMemberList.SEX_KBN), map.get(VJuskillMemberList.SEX_KBN));
		}
		if (StringUtil.isNotEmpty(map.get(LOWER_AGE))) {
			where.ge(toCamelCase(AGE), Integer.parseInt(map.get(LOWER_AGE)));
		}
		if (StringUtil.isNotEmpty(map.get(UPPER_AGE))) {
			where.le(toCamelCase(AGE), Integer.parseInt(map.get(UPPER_AGE)));
		}
		if (StringUtil.isNotEmpty(map.get(FROM_INSERT_DATE))) {
			where.ge(toCamelCase(JUSKILL_ENTRY_DATE), DateUtils.convertDateStrToTimestampAddDate(map.get(FROM_INSERT_DATE), 0));
		}
		if (StringUtil.isNotEmpty(map.get(TO_INSERT_DATE))) {
			where.lt(toCamelCase(JUSKILL_ENTRY_DATE), DateUtils.convertDateStrToTimestampAddDate(map.get(TO_INSERT_DATE), 1));
		}
		if (StringUtil.isNotEmpty(map.get(MAIL))) {
			where.contains(toCamelCase(MAIL), trim(map.get(MAIL)));
		}
		if (StringUtil.isNotEmpty(map.get(MEMBER_ID))) {
			where.contains(toCamelCase(MEMBER_ID), trim(map.get(MEMBER_ID)));
		}
		if (StringUtil.isNotEmpty(map.get(MEMBER_REGISTERED_FLG))) {
			if (map.get(MEMBER_REGISTERED_FLG).equals(String.valueOf(MTypeConstants.MemberRegisteredFlg.REGISTERED))) {
				where.isNotNull(MEMBER_ID, true);
			} else {
				where.isNull(MEMBER_ID, true);
			}
		}

		return where;
	}

	private ComplexWhere addFreeWordSearch(Map<String, String> map) throws ParseException {

		ComplexWhere orWhere = new  ComplexWhere();

		if (StringUtil.isNotEmpty(map.get(FREE_WORD))) {
			String[] freeWords = splitFreeWord(map.get(FREE_WORD));
			int loop = 0;
			for(String freeWord : freeWords) {
				if(loop == 0) {
					orWhere.contains(toCamelCase(ADDRESS), freeWord)
					.or().contains(toCamelCase(PHONE_NO1), freeWord)
					.or().contains(toCamelCase(PHONE_NO2), freeWord)
					.or().contains(toCamelCase(CLOSEST_STATION), freeWord)
					.or().contains(toCamelCase(ROUTE), freeWord)
					.or().contains(toCamelCase(FINAL_SCHOOL_HISTORY), freeWord)
					.or().contains(toCamelCase(QUALIFICATION), freeWord)
					.or().contains(toCamelCase(FINAL_CAREER_HISTORY), freeWord)
					.or().contains(toCamelCase(NOTICE), freeWord);
				}else {
					orWhere.or().contains(toCamelCase(ADDRESS), freeWord)
					.or().contains(toCamelCase(PHONE_NO1), freeWord)
					.or().contains(toCamelCase(PHONE_NO2), freeWord)
					.or().contains(toCamelCase(CLOSEST_STATION), freeWord)
					.or().contains(toCamelCase(ROUTE), freeWord)
					.or().contains(toCamelCase(FINAL_SCHOOL_HISTORY), freeWord)
					.or().contains(toCamelCase(QUALIFICATION), freeWord)
					.or().contains(toCamelCase(FINAL_CAREER_HISTORY), freeWord)
					.or().contains(toCamelCase(NOTICE), freeWord);
				}
				loop++;
			}
		}else {
			return null;
		}

		return orWhere;
	}

	/**
	 * 検索結果の会員IDリストを返す
	 * @param where 検索条件
	 * @param pageNavi ﾍﾟｰｼﾞナビ
	 * @return 会員IDリスト
	 */
	private List<VJuskillMemberList> getJuskillMemberList(StringBuilder sqlStr, List<Object> params) {

		List<VJuskillMemberList> entityList = jdbcManager.selectBySql(VJuskillMemberList.class, sqlStr.toString(), params.toArray())
				.disallowNoResult()
				.getResultList();

		return entityList;
	}

	/**
	 * ジャスキル会員の詳細を取得する
	 * @param id
	 * @return 履歴がセットされたジャスキル会員エンティティ
	 * @throws WNoResultException
	 */
	public VJuskillMemberList getDetail(Integer id) throws WNoResultException {
		String joinProp = VJuskillMemberList.M_JUSKILL_MEMBER_CAREER_HISTORY_LIST;
		return findByIdLeftJoin(joinProp, id, joinProp + "." + MJuskillMemberCareerHistory.ID);
	}

	/**
	 * メルマガ送信対象の会員を取得します。
	 * @param map 検索条件を保持するMAP
	 * @return 会員一覧
	 * @throws NumberFormatException 数値変換に失敗した場合のエラー
	 * @throws ParseException 日付けが不正な場合のエラー
	 * @throws WNoResultException 検索結果が無い場合のエラー
	 */
	public List<VJuskillMemberList> getSendMailMagazineMember(Map<String, String> map) throws NumberFormatException, ParseException, WNoResultException {


		// 運営側一覧の検索条件をセット
		SimpleWhere where = createWhere(map);

		// 会員一覧を取得
		List<VJuskillMemberList> list = findByCondition(where, createAdminMemberListSortKey());

		// ログの出力
		SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
		SqlLog sqlLog = sqlLogRegistry.getLast();
		log.info("会員向けメルマガ配信先を取得するSQL：" + sqlLog.getCompleteSql());

		return list;
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
	 * フリーワードをそれぞれに分割する
	 * @param freeWord
	 * @return
	 */
	private String[] splitFreeWord(String freeWord) {
		return freeWord.replace("　", " ").split(" ");
	}
}
