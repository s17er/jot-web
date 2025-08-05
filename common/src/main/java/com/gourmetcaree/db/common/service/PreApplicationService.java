package com.gourmetcaree.db.common.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.entity.TPreApplicationAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * プレ応募のサービスクラスです。
 */
public class PreApplicationService extends AbstractGroumetCareeBasicService<TPreApplication> {


	/**
	 * IDをキーに応募情報を取得
	 * @param id 応募ID
	 * @return 応募情報
	 * @throws WNoResultException
	 */
	public TPreApplication getPreApplicationDataById(int id) throws WNoResultException {

		try {
			TPreApplication entity = jdbcManager.from(TPreApplication.class)
									.leftOuterJoin("mCustomer" , "mCustomer.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
									.where(new SimpleWhere()
									.eq("id", id)
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.disallowNoResult()
									.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * プレ応募ＩＤから属性エンティティのリストを取得します。
	 * @param id
	 * @return
	 */
	public List<TPreApplicationAttribute> getPreApplicationAttrListByApplicationId(int id) {
		try {
			List<TPreApplicationAttribute> entityList = jdbcManager.from(TPreApplicationAttribute.class)
														.where(new SimpleWhere()
														.eq("applicationId", id)
														).disallowNoResult()
														.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			return new ArrayList<TPreApplicationAttribute>();
		}
	}

	/**
	 * 検索のメインロジック
	 * @param targetPage 表示するページ数
	 * @throws WNoResultException
	 * @throws ParseException
	 */
	public List<TPreApplication> doSearch(PageNavigateHelper pageNavi,Map<String, String> map) throws WNoResultException, ParseException {

		List<TPreApplication> entityList = null;

		try {
			// 検索条件を生成
			Where where = createWhere(map);

			//ページナビゲータを使用
			int count = getSearchCount(where);
			pageNavi.changeAllCount(count);

			// 応募エンティティリストを取得
			entityList = getSearchResultList(where, pageNavi);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}

	/**
	 * 検索結果のカウントを取得
	 * @param where 検索条件
	 * @return 検索結果数
	 */
	public int getSearchCount(Where where) {

		Long count = jdbcManager.from(TPreApplication.class)
						.where(where)
						.disallowNoResult()
						.getCount();

		return count.intValue();
	}

	/**
	 * 検索結果の応募一覧を取得
	 * @param where 検索条件
	 * @param pageNavi ページﾞナビヘルパー
	 * @return 応募エンティティリスト
	 */
	public List<TPreApplication> getSearchResultList(Where where, PageNavigateHelper pageNavi) {

		List<TPreApplication> entityList = jdbcManager.from(TPreApplication.class)
										.where(where)
										.orderBy(pageNavi.sortKey)
										.limit(pageNavi.limit)
										.offset(pageNavi.offset)
										.disallowNoResult()
										.getResultList();

		return entityList;
	}

	/**
	 * 検索条件を生成
	 * @param where SELECT用検索条件
	 * @param map 画面で入力された検索条件
	 * @throws ParseException
	 */
	private SimpleWhere createWhere(Map<String, String> map) throws ParseException {

		SimpleWhere sw = new SimpleWhere();

		// 削除フラグを指定
		sw.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);

		// エリアコードが指定されている時
		if (StringUtils.isNotBlank(map.get("areaCd"))) {
			sw.eq("areaCd", NumberUtils.toInt(map.get("areaCd")));
		}

		// 都道府県コードが指定されている時
		if (StringUtils.isNotBlank(map.get("prefecturesCd"))) {
			sw.eq("prefecturesCd", NumberUtils.toInt(map.get("prefecturesCd")));
		}

		// 下限年齢が指定されている場合
		if (StringUtils.isNotBlank(map.get("lowerAge"))) {
			sw.ge("age", NumberUtils.toInt(map.get("lowerAge")));
		}

		// 上限年齢が指定されている場合
		if (StringUtils.isNotBlank(map.get("upperAge"))) {
			sw.le("age", NumberUtils.toInt(map.get("upperAge")));
		}

		// 性別が指定されている場合
		if (StringUtils.isNotBlank(map.get("sexKbn"))) {
			sw.eq("sexKbn", NumberUtils.toInt(map.get("sexKbn")));
		}

		// 雇用形態が指定されている場合
		if (StringUtils.isNotBlank(map.get("empPtnKbn"))) {
			sw.eq("employPtnKbn", NumberUtils.toInt(map.get("empPtnKbn")));
		}

		// 応募IDが指定されている場合
		if (StringUtils.isNotBlank(map.get("id"))) {
			sw.eq("id", NumberUtils.toInt(map.get("id")));
		}

		// 会員フラグが指定されている場合
		if (StringUtils.isNotBlank(map.get("memberFlg"))) {
			sw.eq("memberFlg", NumberUtils.toInt(map.get("memberFlg")));
		}

		// 端末区分が指定されている場合
		if (StringUtils.isNotBlank(map.get("terminalKbn"))) {
			sw.eq("terminalKbn", map.get("terminalKbn"));
		}

		// 顧客名が指定されている場合
		if (StringUtils.isNotBlank(map.get("customerName"))) {
			sw.contains("mCustomer.customerName", map.get("customerName"));
		}

		// 氏名が指定されている場合
		if (StringUtils.isNotBlank(map.get("name"))) {
			sw.contains("name", map.get("name"));
		}

		// PCメールが指定されている場合
		if (StringUtils.isNotBlank(map.get("pcMail"))) {
			sw.contains("pcMail", map.get("pcMail"));
		}

		// モバイルメールが指定されている場合
		if (StringUtils.isNotBlank(map.get("mobileMail"))) {
			sw.contains("mobileMail", map.get("mobileMail"));
		}

		// 応募日（開始）が指定されている場合
		if (StringUtils.isNotBlank(map.get("applicationStartDate"))) {

			sw.ge("applicationDateTime", new Timestamp(DateUtils.formatDate(map.get("applicationStartDate"), map.get("applicationStartHour"), map.get("applicationStartMinute")).getTime()));
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

			sw.lt("applicationDateTime", new Timestamp(cal.getTimeInMillis()));
		}

		// 応募先名が指定されている場合
		if (StringUtils.isNotBlank(map.get("applicationName"))) {
			sw.contains("applicationName", map.get("applicationName"));
		}

		// GCWコードが指定されている場合
		if (StringUtils.isNotBlank(map.get("gcwCd"))) {
			sw.contains("gcwCd", map.get("gcwCd"));
		}


		return sw;
	}

	/**
	 * WEB IDをキーに応募数を取得
	 * @param webId webID
	 * @return 応募数
	 */
	public long countByWebId(Integer webId) {
		if (webId == null) {
			return 0;
		}
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TPreApplication.WEB_ID), webId);
		SqlUtils.addNotDeleted(where);
		return jdbcManager.from(entityClass)
					.where(where)
					.getCount();
	}

	/**
	 * 気になる件数を取得する
	 * @param webId
	 * @return
	 */
	public int getPreApplicationCount(int webId) {
		return (int)jdbcManager.from(TPreApplication.class)
				.where(new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(TPreApplication.WEB_ID), webId)
				.eq(WztStringUtil.toCamelCase(TPreApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.getCount();
	}
}
