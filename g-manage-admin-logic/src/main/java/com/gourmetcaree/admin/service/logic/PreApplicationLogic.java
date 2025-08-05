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
import com.gourmetcaree.admin.service.builder.sql.impl.ApplicationSearchSqlBuilderImpl;
import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

public class PreApplicationLogic extends AbstractAdminLogic {

	/**
	 * 応募の検索を行う
	 * @param pageNavi
	 * @param map
	 * @return
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<TPreApplication> doSearchPreApplication(PageNavigateHelper pageNavi, int targetPage, Map<String, String> map) throws WNoResultException, ParseException {
		List<TPreApplication> entityList = null;

		try {
			StringBuilder sb = new StringBuilder("");
			List<Object> params = new ArrayList<Object>();
			createApplicationSearchSql(sb, params, map);

			//ページナビゲータを使用
			int count = (int) jdbcManager.getCountBySql(sb.toString(), params.toArray());
			pageNavi.changeAllCount(count);
			pageNavi.setPage(targetPage);
			addSort(sb, pageNavi.sortKey);
			addLimitAndOffset(sb, params, pageNavi.limit, pageNavi.offset);

			entityList = jdbcManager
							.selectBySql(TPreApplication.class, sb.toString(), params.toArray())
							.disallowNoResult()
							.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	public List<TPreApplication> getPreApplicationList(Map<String, String> map, String sortKey) throws ParseException, WNoResultException{
		List<TPreApplication> entityList = null;
		try {
			StringBuilder sb = new StringBuilder("");
			List<Object> params = new ArrayList<Object>();
			createApplicationSearchSql(sb, params, map);
			addSort(sb, sortKey);

			entityList = jdbcManager
					.selectBySql(TPreApplication.class, sb.toString(), params.toArray())
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
	private void createApplicationSearchSql(StringBuilder sb, List<Object> params, Map<String, String> map ) throws ParseException {
		sb.append(" SELECT ");
		sb.append("     APP.* ");
		sb.append(" FROM ");
		sb.append("    t_pre_application APP");
		sb.append(" INNER JOIN ");
		sb.append("    t_web WEB");
		sb.append(" ON APP.web_id = WEB.id ");
		sb.append(" INNER JOIN ");
		sb.append("    m_customer CUS");
		sb.append(" ON APP.customer_id = CUS.id ");
		sb.append(" INNER JOIN ");
		sb.append("    m_member MEM");
		sb.append(" ON APP.member_id = MEM.id ");
		sb.append(" WHERE 1=1 ");

		// エリアコードが指定されている場合
		if (StringUtils.isNotBlank(map.get("areaCd"))) {
			sb.append(" AND APP.area_cd = ? ");
			params.add(NumberUtils.toInt(map.get("areaCd")));
		}

		// 応募IDが指定されている場合
		if (StringUtils.isNotBlank(map.get("id"))) {
			sb.append(" AND APP.id = ? ");
			params.add(NumberUtils.toInt(map.get("id")));
		}

		// webIDが指定されている場合
		if (StringUtils.isNotBlank(map.get("webId"))) {
			sb.append(" AND APP.web_id = ? ");
			params.add(NumberUtils.toInt(map.get("webId")));
		}

		// 応募先名が指定されている場合
		if (StringUtils.isNotBlank(map.get("applicationName"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("applicationName"),
					params,
					" WEB.manuscript_name LIKE ? "));
			sb.append(" ) ");
		}

		// 下限年齢が指定されている場合
		if (StringUtils.isNotBlank(map.get("lowerAge"))) {
			sb.append(" AND APP.age >= ? ");
			params.add(NumberUtils.toInt(map.get("lowerAge")));
		}

		// 上限年齢が指定されている場合
		if (StringUtils.isNotBlank(map.get("upperAge"))) {
			sb.append(" AND APP.age <= ? ");
			params.add(NumberUtils.toInt(map.get("upperAge")));
		}

		// 性別が指定されている場合
		if (StringUtils.isNotBlank(map.get("sexKbn"))) {
			sb.append(" AND APP.sex_kbn = ? ");
			params.add(NumberUtils.toInt(map.get("sexKbn")));
		}

		// 端末区分が指定されている場合
		if (StringUtils.isNotBlank(map.get("terminalKbn"))) {
			sb.append(" AND APP.terminal_kbn = ? ");
			params.add(NumberUtils.toInt(map.get("terminalKbn")));
		}

		// 顧客名が指定されている場合
		if (StringUtils.isNotBlank(map.get("customerName"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("customerName"),
					params,
					" CUS.customer_name LIKE ? "));
			sb.append(" ) ");
		}

		// 業態が指定されている場合
		if (StringUtils.isNotBlank(map.get("industryKbn"))) {
			sb.append(" AND ( " );
			// リニューアル前の業態
			sb.append("    ( " );
			sb.append("      WEB.industry_kbn1 = ? ");
			sb.append("      OR WEB.industry_kbn2 = ? ");
			sb.append("      OR WEB.industry_kbn3 = ? ");
			sb.append("   )");
			int industryKbn = NumberUtils.toInt(map.get("industryKbn"));
			for (int i = 0; i < 3; i++) {
				params.add(industryKbn);
			}
			// リニューアル後の業態
			sb.append("   OR ( " );
			sb.append("       EXISTS (");
			sb.append("       SELECT 1 FROM v_web_industry_kbn IND WHERE ");
			sb.append("       WEB.id = IND.web_id");
			sb.append("       AND IND.industry_kbn = ? ");
			sb.append("       )");
			sb.append("   )");
			params.add(industryKbn);
			sb.append(" )");
		}

		// 氏名が指定されている場合
		if (StringUtils.isNotBlank(map.get("name"))) {
			String name = map.get("name").replaceAll("　| ", "");

			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					name,
					params,
					" REPLACE(REPLACE(APP.name, '　', ''), ' ', '') LIKE ? "));
			sb.append(" ) ");
		}

		// フリガナが指定されている場合
		if (StringUtils.isNotBlank(map.get("nameKana"))) {
			String nameKana = map.get("nameKana").replaceAll("　| ", "");

			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					nameKana,
					params,
					" REPLACE(REPLACE(MEM.member_name_kana, '　', ''), ' ', '') LIKE ? "));
			sb.append(" ) ");
		}

		// PCメールが指定されている場合
		if (StringUtils.isNotBlank(map.get("pcMail"))) {
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					map.get("pcMail"),
					params,
					" MEM.login_id LIKE ? "));
			sb.append(" ) ");
		}

		// モバイルメールが指定されている場合
		if (StringUtils.isNotBlank(map.get("mobileMail"))) {
			sb.append(" AND (");
			sb.append(SqlUtils.createLikeSearch(
					map.get("mobileMail"),
					params,
					" MEM.mobile_mail LIKE ? "));
			sb.append(" ) ");
		}

		// 応募日（開始）が指定されている場合
		if (StringUtils.isNotBlank(map.get("applicationStartDate"))) {
			sb.append(" AND APP.application_datetime >= ? ");
			params.add(new Timestamp(DateUtils.formatDate(map.get("applicationStartDate"), map.get("applicationStartHour"), map.get("applicationStartMinute")).getTime()));

		}

		// 都道府県が指定されている場合
		if (StringUtils.isNotBlank(map.get("prefecturesCd"))) {
			sb.append(" AND APP.prefectures_cd = ? ");
			params.add(NumberUtils.toInt(map.get("prefecturesCd")));
		}

		// 雇用形態が指定されている場合
		if (StringUtils.isNotBlank(map.get("empPtnKbn"))) {
			sb.append(" AND APP.employ_ptn_kbn = ? ");
			params.add(NumberUtils.toInt(map.get("empPtnKbn")));
		}

		// 応募日（終了）が指定されている場合
		if (StringUtils.isNotBlank(map.get("applicationEndDate"))) {

			Calendar cal = Calendar.getInstance();
			Date endDate = DateUtils.formatDate(map.get("applicationEndDate"), map.get("applicationEndHour"), map.get("applicationEndtMinute"));
			cal.setTime(endDate);
			if (StringUtils.isBlank(map.get("applicationEndHour"))) {
				cal.add(Calendar.DATE, 1);
			} else {
				cal.add(Calendar.MINUTE, 1);
			}

			sb.append(" AND APP.application_datetime < ?");
			params.add(new Timestamp(cal.getTimeInMillis()));
		}

		// リニューアル後の職種
		if (StringUtils.isNotBlank(map.get(ApplicationSearchSqlBuilder.HOPE_JOB))) {
			sb.append(" AND APP.job_kbn = ? ");
			params.add(NumberUtils.toInt(map.get(ApplicationSearchSqlBuilder.HOPE_JOB)));
		}


		// 会社ID・営業ID・希望職種の条件を追加。
		SqlBuilder builder = new ApplicationSearchSqlBuilderImpl(sb, params, map);
		// 既存のやつに接ぎ木方式で追加するのでビルド結果は使わない。
		builder.build();
	}

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
