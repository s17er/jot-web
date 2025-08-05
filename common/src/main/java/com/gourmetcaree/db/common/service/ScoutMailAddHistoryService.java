package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TScoutMailAddHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * スカウトメール追加履歴のサービスクラスです。
 * @version 1.0
 */
public class ScoutMailAddHistoryService extends AbstractGroumetCareeBasicService<TScoutMailAddHistory> {


	/**
	 * 顧客IDをキーにスカウトメール追加履歴データを取得
	 * @param customerId 顧客ID
	 * @return スカウトメール追加履歴エンティティリスト
	 */
	public List<TScoutMailAddHistory> getTScoutMailAddHistoryListByCustomerid(int customerId) {

		List<TScoutMailAddHistory> entityList = jdbcManager.from(TScoutMailAddHistory.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.orderBy("add_date desc, id desc")
										.getResultList();

		return entityList;
	}
}