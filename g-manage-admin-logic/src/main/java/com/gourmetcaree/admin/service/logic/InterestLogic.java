package com.gourmetcaree.admin.service.logic;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;

import com.gourmetcaree.admin.service.builder.sql.ApplicationSearchSqlBuilder;
import com.gourmetcaree.admin.service.builder.sql.impl.InterestSearchSqlBuilderImpl;
import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.VInterest;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 気になる通知に関するロジッククラスです。
 * @author t_shiroumaru
 *
 */
public class InterestLogic extends AbstractAdminLogic {


	/**
	 * 応募の検索を行う
	 * @param pageNavi
	 * @param map
	 * @return
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<VInterest> doSearchInterest(PageNavigateHelper pageNavi, int targetPage, Map<String, String> map) throws WNoResultException, ParseException {
		List<VInterest> entityList = null;

		try {
			StringBuilder sb = new StringBuilder("");
			List<Object> params = new ArrayList<Object>();
			createVInterestSearchSql(sb, params, map);

			//ページナビゲータを使用
			int count = (int) jdbcManager.getCountBySql(sb.toString(), params.toArray());
			pageNavi.changeAllCount(count);
			pageNavi.setPage(targetPage);
			addSort(sb, pageNavi.sortKey);
			addLimitAndOffset(sb, params, pageNavi.limit, pageNavi.offset);

			entityList = jdbcManager
					.selectBySql(VInterest.class, sb.toString(), params.toArray())
					.disallowNoResult()
					.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	/**
	 * 検索条件mapをもとにＳＱＬ文を作成します。
	 * @param sb
	 * @param params
	 * @param map
	 * @throws ParseException
	 */
	private void createVInterestSearchSql(StringBuilder sb, List<Object> params, Map<String, String> map ) throws ParseException {
		sb.append(" SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append("    v_interest inte");
		sb.append(" WHERE 1=1 ");

		// エリアコードが指定されている場合
		if (StringUtils.isNotBlank(map.get("areaCd"))) {
			sb.append(" AND inte.area_cd = ? ");
			params.add(NumberUtils.toInt(map.get("areaCd")));
		}

		// webIDが指定されている場合
		if (StringUtils.isNotBlank(map.get("webId"))) {
			sb.append(" AND inte.web_id = ? ");
			params.add(NumberUtils.toInt(map.get("webId")));
		}

		// 応募先名が指定されている場合
		if (StringUtils.isNotBlank(map.get("manuscriptName"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("manuscriptName"),
					params,
					" inte.manuscript_name LIKE ? "));
			sb.append(" ) ");
		}

		// 下限年齢が指定されている場合
		if (StringUtils.isNotBlank(map.get("lowerAge"))) {
			sb.append(" AND date_part('year',age(current_date, inte.birthday)) >= ?  ");
			params.add(NumberUtils.toInt(map.get("lowerAge")));
		}

		// 上限年齢が指定されている場合
		if (StringUtils.isNotBlank(map.get("upperAge"))) {
			sb.append(" AND date_part('year',age(current_date, inte.birthday)) <= ?  ");
			params.add(NumberUtils.toInt(map.get("upperAge")));
		}

		// 性別が指定されている場合
		if (StringUtils.isNotBlank(map.get("sexKbn"))) {
			sb.append(" AND inte.sex_kbn = ? ");
			params.add(NumberUtils.toInt(map.get("sexKbn")));
		}

		// 顧客名が指定されている場合
		if (StringUtils.isNotBlank(map.get("customerName"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("customerName"),
					params,
					" inte.customer_name LIKE ? "));
			sb.append(" ) ");
		}

		// 業態が指定されている場合
		if (StringUtils.isNotBlank(map.get("industryKbn"))) {
			sb.append(" AND ( ");
			sb.append(" EXISTS(");
			sb.append("        SELECT 1 FROM m_member_attribute ma WHERE");
			sb.append("        inte.member_id = ma.member_id");
			sb.append("        AND ma.attribute_cd = 'industry_kbn' AND ma.attribute_value = ?");
			sb.append("       ) ");
			sb.append(" ) ");
			params.add(NumberUtils.toInt(map.get("industryKbn")));
		}

		// 氏名が指定されている場合
		if (StringUtils.isNotBlank(map.get("memberName"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("memberName"),
					params,
					" inte.member_name LIKE ? "));
			sb.append(" ) ");
		}

		// PCメールが指定されている場合
		if (StringUtils.isNotBlank(map.get("loginId"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("loginId"),
					params,
					" inte.login_id LIKE ? "));
			sb.append(" ) ");
		}

		// モバイルメールが指定されている場合
		if (StringUtils.isNotBlank(map.get("subMailAddress"))) {
			sb.append(" AND (");
			sb.append(SqlUtils.createLikeSearch(
					map.get("subMailAddress"),
					params,
					" inte.sub_mail_address LIKE ? "));
			sb.append(" ) ");
		}

		// 応募日（開始）が指定されている場合
		if (StringUtils.isNotBlank(map.get("interestStartDate"))) {

			sb.append(" AND inte.interest_datetime >= ? ");
			params.add(new Timestamp(DateUtils.formatDate(map.get("interestStartDate"), map.get("interestStartHour"), map.get("interestStartMinute")).getTime()));

		}

		// 都道府県が指定されている場合
		if (StringUtils.isNotBlank(map.get("prefecturesCd"))) {
			sb.append(" AND inte.prefectures_cd = ? ");
			params.add(NumberUtils.toInt(map.get("prefecturesCd")));
		}

		// 雇用形態が指定されている場合
		if (StringUtils.isNotBlank(map.get("empPtnKbn"))) {
			sb.append(" AND ( ");
			sb.append(" EXISTS(");
			sb.append("        SELECT 1 FROM m_member_attribute ma WHERE");
			sb.append("        inte.member_id = ma.member_id");
			sb.append("        AND ma.attribute_cd = 'employ_ptn_kbn' AND ma.attribute_value = ?");
			sb.append("       ) ");
			sb.append(" ) ");
			params.add(NumberUtils.toInt(map.get("empPtnKbn")));
		}

		// 応募日（終了）が指定されている場合
		if (StringUtils.isNotBlank(map.get("interestEndDate"))) {

			Calendar cal = Calendar.getInstance();
			Date endDate = DateUtils.formatDate(map.get("interestEndDate"), map.get("interestEndHour"), map.get("interestEndtMinute"));
			cal.setTime(endDate);
			if (StringUtils.isBlank(map.get("interestEndHour"))) {
				cal.add(Calendar.DATE, 1);
			} else {
				cal.add(Calendar.MINUTE, 1);
			}

			sb.append(" AND inte.interest_datetime < ?");
			params.add(new Timestamp(cal.getTimeInMillis()));
		}

		// リニューアル後の職種
		if (StringUtils.isNotBlank(map.get(ApplicationSearchSqlBuilder.HOPE_JOB))) {
			sb.append(" AND ( ");
			sb.append(" EXISTS(");
			sb.append("        SELECT 1 FROM m_member_attribute ma WHERE");
			sb.append("        inte.member_id = ma.member_id");
			sb.append("        AND ma.attribute_cd = 'job_kbn' AND ma.attribute_value = ?");
			sb.append("       ) ");
			sb.append(" ) ");
			params.add(NumberUtils.toInt(map.get(ApplicationSearchSqlBuilder.HOPE_JOB)));
		}


		// 会社ID・営業ID・希望職種の条件を追加。
		SqlBuilder builder = new InterestSearchSqlBuilderImpl(sb, params, map);
		// 既存のやつに接ぎ木方式で追加するのでビルド結果は使わない。
		builder.build();
	}

	/**
	 * order byを追加します
	 * @param sb
	 * @param sort
	 */
	private void addSort(StringBuilder sb, String sort) {
		if (StringUtils.isBlank(sort)) {
			return;
		}

		sb.append(" ORDER BY ");
		sb.append(sort);
	}

	/**
	 * offsetとlimitを追加します。
	 * @param sb
	 * @param params
	 * @param limit
	 * @param offset
	 */
	private void addLimitAndOffset(StringBuilder sb, List<Object> params, int limit, int offset) {
		if (offset > 0) {
			sb.append(" OFFSET ? ");
			params.add(offset);
		}

		if (limit > 0) {
			sb.append(" LIMIT ? ");
			params.add(limit);
		}
	}
}
