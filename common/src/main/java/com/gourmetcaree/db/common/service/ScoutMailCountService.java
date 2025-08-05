package com.gourmetcaree.db.common.service;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TScoutMailCount;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * スカウトメール数のサービスクラスです。
 * @version 1.0
 */
public class ScoutMailCountService extends AbstractGroumetCareeBasicService<TScoutMailCount> {


	/**
	 * 顧客IDをキーにスカウトメール数テーブルデータを取得
	 * @param customerId 顧客ID
	 * @return スカウトメール数エンティティ
	 */
	public TScoutMailCount getTScoutMailCountByCustomerid(int customerId) {

		TScoutMailCount entity = jdbcManager.from(TScoutMailCount.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.disallowNoResult()
										.getSingleResult();

		return entity;
	}

	/**
	 * 顧客IDをキーにスカウトメール数を取得
	 * @param customerId 顧客ID
	 * @return スカウトメール数
	 */
	public int getScoutCountByCustomerId(int customerId) {

		TScoutMailCount entity = jdbcManager.from(TScoutMailCount.class)
									.where(new SimpleWhere()
									.eq("customerId", customerId)
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.getSingleResult();

		return entity != null ? entity.scoutCount : 0;
	}
}
