package com.gourmetcaree.db.common.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

public class ActiveScoutMailService extends AbstractGroumetCareeReferenceService<VActiveScoutMail> {

	/** スカウトメール残りのキー */
	private static final String REMAIN_COUNT_KEY = "remainCount";
	/** スカウトメール残りのカラム名 */
	private static final String REMAIN_COUNT_COLUMN_NAME = "remain_count";

	/**
	 * 使用可能なスカウトメール数を取得します。
	 * @param customerId
	 * @return
	 */
	public int getUsableScoutMailCount(int customerId, int...scoutMailKbns) {
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		createCountScoutRemainSql(sb, params, customerId, scoutMailKbns);

		BeanMap map = jdbcManager.selectBySql(BeanMap.class, sb.toString(), params.toArray())
								.getSingleResult();

		if (map.containsKey(REMAIN_COUNT_KEY)) {
			Object sum = map.get("remainCount");
			if (sum == null) {
				return 0;
			}
			if (sum instanceof Long) {
				return ((Long) sum).intValue();
			}
		}

		return 0;
	}

	public List<VActiveScoutMail> getPaidScoutMail(int customerId) {

		return jdbcManager.from(VActiveScoutMail.class)
				.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(
							VActiveScoutMail.CUSTOMER_ID),
							customerId)
					.eq(WztStringUtil.toCamelCase(
							VActiveScoutMail.SCOUT_MAIL_KBN), MTypeConstants.ScoutMailKbn.BOUGHT)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
					)
				.orderBy("use_end_datetime asc")
				.getResultList();
	}

	public VActiveScoutMail exitUnlimitScoutMail(int customerId) {

		return jdbcManager.from(VActiveScoutMail.class)
				.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(
							VActiveScoutMail.CUSTOMER_ID),
							customerId)
					.eq(WztStringUtil.toCamelCase(
							VActiveScoutMail.SCOUT_MAIL_KBN), MTypeConstants.ScoutMailKbn.UNLIMITED)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
					).getSingleResult();
	}

	/**
	 * 使用可能なスカウトメール数を取得します。
	 * @param customerId
	 * @return
	 */
	public int getUsableScoutMailCount(int customerId) {
		return getUsableScoutMailCount(customerId,
									MTypeConstants.ScoutMailKbn.FIX_WEB,
									MTypeConstants.ScoutMailKbn.BOUGHT,
									MTypeConstants.ScoutMailKbn.UNLIMITED);
	}


	/**
	 * 使用可能な有料スカウトメール数を取得します。
	 * @param customerId
	 * @return
	 */
	public int getUsablePaiedScoutMailCount(int customerId) {
		return getUsableScoutMailCount(customerId,
									MTypeConstants.ScoutMailKbn.BOUGHT,
									MTypeConstants.ScoutMailKbn.UNLIMITED);
	}

	/**
	 * 使用可能な無料スカウトメール数を取得します。
	 * @param customerId
	 * @return
	 */
	public int getUsableFreeScoutMailCount(int customerId) {
		return getUsableScoutMailCount(customerId,
									MTypeConstants.ScoutMailKbn.FIX_WEB);
	}


	/**
	 * 同じ有効期限のスカウトメールが存在するかどうか
	 * @param customerId
	 * @param useEndDatetime
	 * @return
	 */
	public boolean existSameEndDatetimeScoutMail(int customerId, Date useEndDatetime, int scoutMailKbn) {
		long count = jdbcManager.from(VActiveScoutMail.class)
								.where(new SimpleWhere()
									.eq(WztStringUtil.toCamelCase(
											VActiveScoutMail.CUSTOMER_ID),
											customerId)
									.eq(WztStringUtil.toCamelCase(
											VActiveScoutMail.SCOUT_MAIL_KBN), scoutMailKbn)
									.eq(WztStringUtil.toCamelCase(
											VActiveScoutMail.USE_END_DATETIME),
											new Timestamp(DateUtils.getJustDate(useEndDatetime).getTime()))
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
									)
								.getCount();

		return count > 0;
	}

	/**
	 * 同じ有効期限のメールが存在するかどうか
	 * @param customerId
	 * @param useEndDatetime
	 * @return
	 */
	public boolean existSameEndDatetimeScoutMail(int customerId, Date useEndDatetime) {
		return existSameEndDatetimeScoutMail(customerId, useEndDatetime, MTypeConstants.ScoutMailKbn.BOUGHT);
	}


	/**
	 * 減算後のスカウトメールが0以上かどうか
	 * @param customerId
	 * @param useEndDatetime
	 * @param minusScoutMail
	 * @return
	 */
	public boolean existSubstractedScoutMail(int customerId, Date useEndDatetime, int minusScoutMail) {


		long count = jdbcManager.from(VActiveScoutMail.class)
						.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(
									VActiveScoutMail.CUSTOMER_ID),
									customerId)
							.eq(WztStringUtil.toCamelCase(
									VActiveScoutMail.SCOUT_MAIL_KBN),
									MTypeConstants.ScoutMailKbn.BOUGHT)
							.eq(WztStringUtil.toCamelCase(
									VActiveScoutMail.USE_END_DATETIME),
									new Timestamp(DateUtils.getJustDate(useEndDatetime).getTime()))
							.ge(WztStringUtil.toCamelCase(
									VActiveScoutMail.SCOUT_REMAIN_COUNT),
									Math.abs(minusScoutMail))
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
							)
						.getCount();

		return count > 0;
	}

	/**
	 * スカウトメール残数を取得します
	 * @param customerId
	 * @param scoutMailKbn
	 * @return
	 */
	public long getScoutRemainCount(int customerId, int scoutMailKbn) {

		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		createCountScoutRemainSql(sb, params, customerId, scoutMailKbn);
		BeanMap map = jdbcManager.selectBySql(BeanMap.class, sb.toString(), params.toArray())
					.getSingleResult();

		if (map.containsKey(REMAIN_COUNT_KEY)) {
			Object sum = map.get("remainCount");
			if (sum == null) {
				return 0L;
			}
			if (sum instanceof Long) {
				return (Long) sum;
			}
		}

		return 0L;
	}

	/**
	 * 無制限スカウトメールを取得する
	 * @param customerId
	 * @return
	 */
	public VActiveScoutMail getUnlimitScoutMail(int customerId) {
		return jdbcManager.from(VActiveScoutMail.class)
				.where(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(
								VActiveScoutMail.CUSTOMER_ID),
								customerId)
						.eq(WztStringUtil.toCamelCase(
								VActiveScoutMail.SCOUT_MAIL_KBN),
								MTypeConstants.ScoutMailKbn.UNLIMITED)
						).getSingleResult();
	}

	/**
	 * 最新の購入分のスカウトメールを取得する
	 * @param customerId
	 * @return
	 */
	public VActiveScoutMail getLastedBoughtScoutMail(int customerId) {
		return jdbcManager.from(VActiveScoutMail.class)
				.where(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(
								VActiveScoutMail.CUSTOMER_ID),
								customerId)
						.eq(WztStringUtil.toCamelCase(
								VActiveScoutMail.SCOUT_MAIL_KBN),
								MTypeConstants.ScoutMailKbn.BOUGHT)
						).orderBy("id DESC").getResultList().get(0);
	}

	/**
	 * 無制限スカウトメールの使用期限を取得する
	 * @param customerId
	 * @return
	 */
	public String getUnlimitScoutMailUseEndDateTime(int customerId) {
		VActiveScoutMail entity  = getUnlimitScoutMail(customerId);

		if(entity != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(entity.useEndDatetime);
		} else {
			return null;
		}
	}


	/**
	 * スカウトメール残数を取得するSQLを作成します。
	 * @param sb
	 * @param params
	 * @param customerId
	 * @param scoutMailKbnArray
	 */
	private void createCountScoutRemainSql(StringBuilder sb, List<Object> params, int customerId, int... scoutMailKbnArray) {
		sb.append(" SELECT ");
		sb.append("     SUM(scout_remain_count) AS ");
		sb.append(          REMAIN_COUNT_COLUMN_NAME);
		sb.append(" FROM ");
		sb.append("     v_active_scout_mail ");
		sb.append(" WHERE ");
		sb.append("     customer_id = ? ");
		sb.append("     AND scout_mail_kbn in ( ");
		sb.append(			SqlUtils.getQMarks(scoutMailKbnArray.length));
		sb.append("     )");
		sb.append("     AND delete_flg = ? ");

		params.add(customerId);
		for (int scoutMailKbn : scoutMailKbnArray) {
			params.add(scoutMailKbn);
		}
		params.add(DeleteFlgKbn.NOT_DELETED);
	}

}
