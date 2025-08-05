package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * IP電話サービス
 * @author nakamori
 *
 */
public class WebIpPhoneService extends AbstractGroumetCareeBasicService<TWebIpPhone> {

	/**
	 * 指定のIP電話番号以外のデータを削除
	 * @param noDeleteIpTelList 削除しないIP電話リスト
	 */
	public void deleteOtherData(int webId, int customerId, List<String> noDeleteIpTelList) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), webId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), customerId);
		where.notIn(WztStringUtil.toCamelCase(TWebIpPhone.IP_TEL), noDeleteIpTelList);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		jdbcManager.from(entityClass)
					.where(where)
					.iterate(new IterationCallback<TWebIpPhone, Void>() {
						@Override
						public Void iterate(TWebIpPhone entity,
								IterationContext context) {
							if (entity != null) {
								logicalDelete(entity);
							}
							return null;
						}
					});
	}


	/**
	 * 原稿IDをキーに削除
	 * @author hara
	 */
	public void deleteByWebId(int webId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), webId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		jdbcManager.from(entityClass)
		.where(where)
		.iterate(new IterationCallback<TWebIpPhone, Void>() {
			@Override
			public Void iterate(TWebIpPhone entity,
					IterationContext context) {
				if (entity != null) {
					logicalDelete(entity);
				}
				return null;
			}
		});
	}
	/**
	 * 次の枝番を取得
	 * @param webId
	 * @param customerId
	 * @return
	 */
	public int createNextEdaNo(int webId, int customerId) {
		StringBuilder sql = new StringBuilder(" SELECT MAX( ")
			.append(TWebIpPhone.EDA_NO)
			.append(" FROM ")
			.append(TWebIpPhone.TABLE_NAME)
			.append(" WHERE ")
			.append(SqlUtils.eqNoCamelize(TWebIpPhone.WEB_ID))
			.append(SqlUtils.andEqNoCamelize(TWebIpPhone.CUSTOMER_ID))
			.append(SqlUtils.andEqNoCamelize(TWebIpPhone.DELETE_FLG));

		Object[] params = {webId, customerId, DeleteFlgKbn.NOT_DELETED};

		Integer maxValue = jdbcManager.selectBySql(Integer.class, sql.toString(), params)
										.getSingleResult();

		if (maxValue == null || maxValue.intValue() == 0) {
			return 1;
		}

		return maxValue + 1;
	}



	/**
	 * WEBデータをキーにIP情報を取得
	 */
	public List<TWebIpPhone> findByWebData(TWeb web) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), web.id);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), web.customerId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
						.where(where)
						.orderBy(TWebIpPhone.EDA_NO)
						.getResultList();
	}


	/**
	 * WEBIDと顧客IDをキーにセレクトを行います
	 */
	public <RESULT> void selectDataByWebIdAndCustomerId(Integer webId, Integer customerId, IterationCallback<TWebIpPhone, RESULT> iterationCallback) {
		if (webId == null || customerId == null) {
			return;
		}

		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), webId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), customerId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		jdbcManager.from(entityClass)
						.where(where)
						.orderBy(TWebIpPhone.EDA_NO)
						.iterate(iterationCallback);
	}

	/**
	 * IP電話番号から、IDのリストをセレクト
	 * @param ipPhone
	 * @return
	 * @throws WNoResultException
	 */
	public List<Integer> selectIdListFromIpPhoneNumber(String ipPhone) throws WNoResultException {
		if (StringUtils.isBlank(ipPhone)) {
			return new ArrayList<Integer>(0);
		}
		SimpleWhere where = new SimpleWhere();
		where.contains(WztStringUtil.toCamelCase(TWebIpPhone.IP_TEL), ipPhone.replace("-", ""));
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		final List<Integer> list = new ArrayList<Integer>();
		jdbcManager.from(entityClass)
				.where(where)
				.iterate(new IterationCallback<TWebIpPhone, Void>() {
					@Override
					public Void iterate(TWebIpPhone entity,
							IterationContext context) {
						if (entity != null) {
							list.add(entity.id);
						}
						return null;
					}
				});

		if (CollectionUtils.isEmpty(list)) {
			throw new WNoResultException(String.format("電話番号[%s]のIP電話が見つかりませんでした。", ipPhone));
		}
		return list;
	}
}
