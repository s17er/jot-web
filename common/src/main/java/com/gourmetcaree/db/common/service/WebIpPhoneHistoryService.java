package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;

import com.gourmetcaree.common.builder.sql.IpPhoneApplicationSearchSqlBuilderImpl;
import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TWebIpPhoneHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * IP通話応募履歴 サービス
 * @author nakamori
 *
 */
public class WebIpPhoneHistoryService extends AbstractGroumetCareeBasicService<TWebIpPhoneHistory>{


	/**
	 * 電話応募履歴をセレクトします。
	 */
	public <RESULT> void select(SearchProperty property, IterationCallback<ResultDto, RESULT> callback) throws WNoResultException {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		createSearchSql(sql, params, property, false);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			throw new WNoResultException("IP電話統計がありません。");
		}

		property.pageNavi = new PageNavigateHelper(property.maxRow);
		property.pageNavi.changeAllCount((int) count);
		property.pageNavi.setPage(property.targetPage);

		if("2".equals(property.sort)) {
			sql.append(" ORDER BY IP.tel_status_detail DESC, IP.customer_id, IP.web_id, IP.id ");
		} else if("3".equals(property.sort)) {
			sql.append(" ORDER BY IP.member_tel_second DESC, IP.customer_id, IP.web_id, IP.id ");
		} else if("4".equals(property.sort)) {
			sql.append(" ORDER BY IP.tel_status_name DESC, IP.customer_id, IP.web_id, IP.id ");
		} else {
			sql.append(" ORDER BY IP.call_note_caught_member_tel_datetime DESC, IP.customer_id, IP.web_id, IP.id ");
		}

		jdbcManager.selectBySql(ResultDto.class, sql.toString(), params.toArray())
				.limit(property.pageNavi.limit)
				.offset(property.pageNavi.offset)
				.iterate(callback);
	}

	/**
	 * 電話応募CSV出力情報をセレクトします。
	 */
	public <RESULT> void selectCsv(SearchProperty property, IterationCallback<ResultCSVDto, RESULT> callback) throws WNoResultException {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		createSearchSql(sql, params, property ,true);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			throw new WNoResultException("IP電話統計がありません。");
		}

		sql.append(" ORDER BY IP.call_note_caught_member_tel_datetime DESC, IP.customer_id, IP.web_id, IP.id ");

		jdbcManager.selectBySql(ResultCSVDto.class, sql.toString(), params.toArray())
				.iterate(callback);
	}

	/**
	 * 検索SQLの生成
	 */
	private void createSearchSql(StringBuilder sql, List<Object> params, SearchProperty property, boolean csvFlg) {
		sql.append(" SELECT ");
		sql.append("   IP.id ");
		sql.append("   , IP.web_id ");
		sql.append("   , IP.customer_id ");
		sql.append("   , IP.member_tel ");
		sql.append("   , IP.ip_phone_tel ");
		sql.append("   , IP.customer_display_tel ");
		sql.append("   , IP.customer_tel ");
		sql.append("   , IP.response_customer_datetime ");
		sql.append("   , IP.member_tel_second ");
		sql.append("   , IP.customer_tel_second ");
		sql.append("   , IP.tel_status_name ");
		sql.append("   , IP.hang_up_reason ");
		sql.append("   , IP.tel_status_detail ");
		sql.append("   , IP.client_cd ");
		sql.append("   , IP.call_note_caught_member_tel_datetime ");
		sql.append("   , CUS.customer_name ");
		sql.append("   , WEB.manuscript_name  ");
		// CSVの時だけ出力
		if (csvFlg) {
			sql.append("   , SALES.sales_name  ");
			sql.append("   , VOLUME.post_start_datetime  ");
			sql.append("   , VOLUME.post_end_datetime  ");
		}
		sql.append(" FROM ");
		sql.append("   t_web_ip_phone_history IP ");
		sql.append("   INNER JOIN m_customer CUS ON IP.customer_id = CUS.id ");
		sql.append("   INNER JOIN t_web WEB ON IP.web_id = WEB.id ");
		// CSVの時だけ設定
		if (csvFlg) {
			sql.append("   LEFT JOIN m_sales SALES ON WEB.sales_id = SALES.id ");
			sql.append("   LEFT JOIN m_volume VOLUME ON WEB.volume_id = VOLUME.id ");
		}
		sql.append(" WHERE ");
		sql.append("   IP.delete_flg = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);


		if (property.webId != null) {
			sql.append("   AND IP.web_id = ? ");
			params.add(property.webId);
		}
		if (property.customerId != null) {
			sql.append("   AND IP.customer_id = ? ");
			params.add(property.customerId);
		}


		if (StringUtils.isNotBlank(property.manuscriptName)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.manuscriptName,
					params,
					"    WEB.manuscript_name LIKE ?   "));
			sql.append(" ) ");
		}

		if (StringUtils.isNotBlank(property.customerName)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.customerName,
					params,
					"    CUS.customer_name LIKE ?   "));
			sql.append(" ) ");
		}

		if (StringUtils.isNotBlank(property.memberTel)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.memberTel,
					params,
					"    IP.member_tel LIKE ?   "));
			sql.append(" ) ");
		}

		if (StringUtils.isNotBlank(property.ipPhoneTel)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.ipPhoneTel,
					params,
					"    IP.ip_phone_tel LIKE ? "));
			sql.append(" ) ");
		}

		if (property.searchStartDatetime != null) {
			sql.append("   AND IP.call_note_caught_member_tel_datetime >= ? ");
			params.add(property.searchStartDatetime);
		}

		if (property.searchEndDatetime != null) {
			sql.append("   AND IP.call_note_caught_member_tel_datetime <= ? ");
			params.add(property.searchEndDatetime);

		}

		// 会社ID・営業ID・希望職種の条件を追加。
		SqlBuilder builder = new IpPhoneApplicationSearchSqlBuilderImpl(sql, params, property);
		// 既存のやつに接ぎ木方式で追加するのでビルド結果は使わない。
		builder.build();
	}

	/**
	 * IP電話の履歴からWebId単位に件数を保持したMAPにセットして返します
	 * @param ignoreIpPhoneList カウント対象外にする電話番号
	 * @return Map<webId, 件数>
	 */
	public Map<Integer, Integer> getHistoryCountMap(List<String> ignoreIpPhoneList) {

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" SELECT COUNT(*) AS count ");
		sql.append(", ").append(TWebIpPhoneHistory.WEB_ID);
		sql.append(" FROM ").append(TWebIpPhoneHistory.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(eqNoCamelize(TWebIpPhoneHistory.DELETE_FLG));
		params.add(DeleteFlgKbn.NOT_DELETED);
		// 無効リストがあれば条件に追加
		if (CollectionUtils.isNotEmpty(ignoreIpPhoneList)) {
			sql.append(andNotInNoCamelize(TWebIpPhoneHistory.MEMBER_TEL, ignoreIpPhoneList.size()));
			params.addAll(ignoreIpPhoneList);
		}
		sql.append(" GROUP BY ").append(TWebIpPhoneHistory.WEB_ID);

		final Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		jdbcManager.selectBySql(CountDto.class, sql.toString(), params.toArray())
			.iterate(new IterationCallback<CountDto, Void>() {
				@Override
				public Void iterate(CountDto entity, IterationContext context) {
					countMap.put(entity.webId, entity.count);
					return null;
				}
			});
		return countMap;
	}

	/**
	 * 電話応募の件数を取得する
	 *
	 * @param webId
	 * @return
	 */
	public int getIpPhoneApplicationCount(int webId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		sql.append(" SELECT COUNT(*) FROM ");
		sql.append(TWebIpPhoneHistory.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(eqNoCamelize(TWebIpPhoneHistory.DELETE_FLG));
		params.add(DeleteFlgKbn.NOT_DELETED);
		sql.append(andEqNoCamelize(TWebIpPhoneHistory.WEB_ID));
		params.add(webId);

		return jdbcManager.selectBySql(CountDto.class, sql.toString(), params.toArray()).getSingleResult().count;
	}

	/**
	 * カウント件数を保持するDTO
	 */
	public static class CountDto implements Serializable {
		private static final long serialVersionUID = 439933628713011346L;
		public Integer webId;
		public Integer count;
	}

	/**
	 * 検索プロパティ
	 * @author nakamori
	 *
	 */
	public static class SearchProperty extends PagerProperty {

		/**
		 *
		 */
		private static final long serialVersionUID = -5067394319100559649L;

		/** 顧客ID */
		public Integer customerId;

		/** webId */
		public Integer webId;

		/** 原稿名 */
		public String manuscriptName;

		/** 顧客名 */
		public String customerName;

		/** 応募者電話番号 */
		public String memberTel;

		/** IP電話番号(顧客電話) */
		public String ipPhoneTel;

		/** 会社ID */
		public String where_companyId;

		/** 営業ID */
		public String where_salesId;

		/** 検索開始日時 */
		public Timestamp searchStartDatetime;

		/** 検索終了日時 */
		public Timestamp searchEndDatetime;

		/** ページナビ */
		public PageNavigateHelper pageNavi;

		public String sort;
	}

	/**
	 * 検索結果用DTO
	 * @author nakamori
	 *
	 */
	public static class ResultDto extends TWebIpPhoneHistory {

		/**
		 *
		 */
		private static final long serialVersionUID = -8547537644479033611L;

		/** 原稿名 */
		public String manuscriptName;

		/** 通話時間 */
		public String telTime;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}

	/**
	 * CSV出力結果用DTO
	 * @author otani
	 *
	 */
	public static class ResultCSVDto extends ResultDto {

		private static final long serialVersionUID = -1547537614679033611L;

		/** 営業担当者名 */
		public String salesName;

		/** 掲載開始日時 */
		public Timestamp postStartDatetime;

		/** 掲載終了日時 */
		public Timestamp postEndDatetime;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}
}
