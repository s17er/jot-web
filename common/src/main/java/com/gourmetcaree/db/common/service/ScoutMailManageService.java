package com.gourmetcaree.db.common.service;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TScoutMailManage;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * スカウトメール管理サービス
 * @author Takehiro Nakamori
 *
 */
public class ScoutMailManageService extends AbstractGroumetCareeBasicService<TScoutMailManage> {

	/**
	 * 顧客ID、原稿IDからスカウト管理を取得します。
	 * @param customerId
	 * @param webId
	 * @return
	 * @throws WNoResultException
	 */
	public TScoutMailManage findByCustomerIdAndWebId(int customerId, int webId) throws WNoResultException {

		try {
			return jdbcManager.from(TScoutMailManage.class)
							.where(new SimpleWhere()
								.eq(WztStringUtil.toCamelCase(TScoutMailManage.WEB_ID), webId)
								.eq(WztStringUtil.toCamelCase(TScoutMailManage.CUSTOMER_ID), customerId)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException(e);

		// 同じwebIdは存在させないため、システム上ありえない。
		} catch (SNonUniqueResultException e) {
			throw new FraudulentProcessException(e);
		}
	}

	/**
	 * 同じ有効期限のスカウト管理を取得。
	 * web原稿確定による管理はこのメソッドでは取れない。
	 * @param fromEntity
	 * @return
	 * @throws WNoResultException
	 */
	public TScoutMailManage getSameUseEndDatetimeEntity(TScoutMailManage fromEntity) throws WNoResultException {
		if (GourmetCareeUtil.eqInt(MTypeConstants.ScoutMailKbn.FIX_WEB, fromEntity.scoutMailKbn)) {
			throw new IllegalArgumentException("WEB原稿確定の無料分は取得出来ません。");
		}
		try {
			return jdbcManager.from(TScoutMailManage.class)
								.where(new SimpleWhere()
									.eq(WztStringUtil.toCamelCase(TScoutMailManage.CUSTOMER_ID),
										fromEntity.customerId)
									.eq(WztStringUtil.toCamelCase(TScoutMailManage.SCOUT_MAIL_KBN),
										fromEntity.scoutMailKbn)
									.eq(WztStringUtil.toCamelCase(TScoutMailManage.USE_END_DATETIME),
										fromEntity.useEndDatetime)
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);

		// 同じ顧客・有効期限・スカウトメール区分の条件が揃うことはない
		} catch (SNonUniqueResultException e) {
			throw new FraudulentProcessException(e);
		}
	}
}
