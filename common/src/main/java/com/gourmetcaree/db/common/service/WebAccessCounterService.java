package com.gourmetcaree.db.common.service;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants.TerminalKbn;
import com.gourmetcaree.db.common.entity.TWebAccessCounter;

/**
 * ウェブアクセスカウンターサービス
 * @author Takehiro Nakamori
 *
 */
public class WebAccessCounterService extends AbstractGroumetCareeBasicService<TWebAccessCounter>{

	/**
	 * 指定原稿のカウンタをインクリメントします。
	 */
	public void increment(int webId, int terminalKbn, int areaCd) {
		String yearStr = DateUtils.getTodayDateStr("yyyy");
		String monthStr = DateUtils.getTodayDateStr("MM");
		String dayStr = DateUtils.getTodayDateStr("dd");

		BeanMap map = new BeanMap();
		map.put(WztStringUtil.toCamelCase(TWebAccessCounter.WEB_ID), webId);
		map.put(WztStringUtil.toCamelCase(TWebAccessCounter.TERMINAL_KBN), terminalKbn);
		map.put(WztStringUtil.toCamelCase(TWebAccessCounter.AREA_CD), areaCd);
		map.put(WztStringUtil.toCamelCase(TWebAccessCounter.YEAR), yearStr);
		map.put(WztStringUtil.toCamelCase(TWebAccessCounter.MONTH), monthStr);
		map.put(WztStringUtil.toCamelCase(TWebAccessCounter.DAY), dayStr);

		List<TWebAccessCounter> entityList = jdbcManager.from(TWebAccessCounter.class)
									.where(map)
									.forUpdate()
									.orderBy(SqlUtils.asc(TWebAccessCounter.ID))
									.getResultList();


		TWebAccessCounter twebAccessCounter = new TWebAccessCounter();
		twebAccessCounter.webId = NumberUtils.toInt(map.get(WztStringUtil.toCamelCase(TWebAccessCounter.WEB_ID)).toString());
		twebAccessCounter.terminalKbn = map.get(WztStringUtil.toCamelCase(TWebAccessCounter.TERMINAL_KBN)).toString();
		twebAccessCounter.areaCd = NumberUtils.toInt(map.get(WztStringUtil.toCamelCase(TWebAccessCounter.AREA_CD)).toString());
		twebAccessCounter.year = map.get(WztStringUtil.toCamelCase(TWebAccessCounter.YEAR)).toString();
		twebAccessCounter.month = map.get(WztStringUtil.toCamelCase(TWebAccessCounter.MONTH)).toString();
		twebAccessCounter.day = map.get(WztStringUtil.toCamelCase(TWebAccessCounter.DAY)).toString();
		twebAccessCounter.lastAccessDatetime = new Timestamp(new Date().getTime());

		if (entityList == null || entityList.isEmpty()) {
			twebAccessCounter.accessCount = 1;
			this.insert(twebAccessCounter);
		} else {
			TWebAccessCounter entity = entityList.get(0);
			twebAccessCounter.accessCount = entity.accessCount + 1;
			twebAccessCounter.id = entity.id;
			this.update(twebAccessCounter);
		}
	}

	/**
	 * 指定原稿のカウンタをインクリメントします。(PC)
	 */
	public void incrementPC(int webId, int areaCd) {
		increment(webId, TerminalKbn.PC_VALUE, areaCd);
	}

	/**
	 * 指定原稿のカウンタをインクリメントします。(モバイル)
	 */
	public void incrementMobile(int webId, int areaCd) {
		increment(webId, TerminalKbn.MOBILE_VALUE, areaCd);
	}

	/**
	 * 指定原稿のカウンタをインクリメントします。(スマホ)
	 */
	public void incrementSmart(int webId, int areaCd) {
		increment(webId, TerminalKbn.SMART_VALUE, areaCd);
	}

	/**
	 * 原稿指定のカウンタを取得します。
	 * @param webId 原稿ID
	 * @param terminalKbn 端末区分
	 * @param areaCd エリア区分
	 * @param date 日付
	 * @return
	 */
	public int getCount(int webId, int terminalKbn, int areaCd, Date date) {
		String yearStr = DateUtils.getDateStr(date, "yyyy");
		String monthStr = DateUtils.getDateStr(date, "MM");
		String dayStr = DateUtils.getDateStr(date, "dd");

		try{
			TWebAccessCounter entity = jdbcManager.from(TWebAccessCounter.class)
				.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.WEB_ID), webId)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.TERMINAL_KBN), terminalKbn)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.DAY), dayStr)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.MONTH), monthStr)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.YEAR), yearStr))
				.disallowNoResult()
				.getSingleResult();

			return entity.accessCount;
		} catch (SNoResultException e) {
			return 0;
		}
	}

	/**
	 * 本日の原稿指定のカウンタを取得します。
	 * @param webId 原稿ID
	 * @param terminalKbn 端末区分
	 * @param areaCd エリア区分
	 * @param date 日付
	 * @return
	 */
	public int getTodayCount(int webId, int terminalKbn, int areaCd) {
		return getCount(webId, terminalKbn, areaCd, new Date());
	}

	/**
	 * 原稿指定のカウンタを取得します。
	 * @param webId 原稿ID
	 * @param terminalKbn 端末区分
	 * @param areaCd エリア区分
	 * @param date 日付
	 * @return
	 */
	public int getAllCount(int webId, int areaCd, Date date) {
		String yearStr = DateUtils.getDateStr(date, "yyyy");
		String monthStr = DateUtils.getDateStr(date, "MM");
		String dayStr = DateUtils.getDateStr(date, "dd");
		int count = 0;

		try{
			List<TWebAccessCounter> entityList = jdbcManager.from(TWebAccessCounter.class)
				.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.WEB_ID), webId)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.DAY), dayStr)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.MONTH), monthStr)
					.eq(WztStringUtil.toCamelCase(TWebAccessCounter.YEAR), yearStr))
				.disallowNoResult()
				.getResultList();

			for (TWebAccessCounter entity : entityList) {
				count += entity.accessCount;
			}
			return count;


		} catch (SNoResultException e) {
			return 0;
		}
	}

	/**
	 * 原稿指定のカウンタを取得します。
	 * @param webId 原稿ID
	 * @param terminalKbn 端末区分
	 * @param areaCd エリア区分
	 * @param date 日付
	 * @return
	 */
	public int getAllCountToday(int webId, int areaCd) {
		return getAllCount(webId, areaCd, new Date());
	}

	/**
	 * 原稿の全アクセス件数をカウントする
	 * @param webId
	 * @param areaCd
	 * @return 端末ごとにアクセス件数が入ったリスト
	 */
	public List<AccessCountDto> getAllAccessCount(int webId, int areaCd) {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append(" CASE WHEN ");
		sql.append("  SUM(").append(TWebAccessCounter.ACCESS_COUNT).append(") IS NULL THEN 0 ");
		sql.append("  ELSE SUM(").append(TWebAccessCounter.ACCESS_COUNT).append(") END AS access_count ");
		sql.append("  , ").append(TWebAccessCounter.TERMINAL_KBN);
		sql.append(" FROM ");
		sql.append(TWebAccessCounter.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebAccessCounter.WEB_ID).append(" = ? ");
		sql.append(" AND ").append(TWebAccessCounter.AREA_CD).append(" = ? ");
		sql.append(" GROUP BY ").append(TWebAccessCounter.TERMINAL_KBN);

		List<Object> params = new ArrayList<Object>();
		params.add(webId);
		params.add(areaCd);

		return jdbcManager.selectBySql(AccessCountDto.class, sql.toString(), params.toArray()).getResultList();
	}

	/**
	 * アクセス件数を保持するDTO
	 */
	public static class AccessCountDto implements Serializable {
		private static final long serialVersionUID = 132933628713011346L;
		/** アクセス合計 */
		public Integer accessCount;
		/** 端末区分 */
		public String terminalKbn;
	}

}
