package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

public class ShopChangeJobConditionService extends AbstractGroumetCareeBasicService<TShopChangeJobCondition> {

	/**
	 * 削除登録する
	 * @param shopId
	 * @param list
	 */
	public void deleteInsert(int shopListId, List<TShopChangeJobCondition> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TShopChangeJobCondition.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TShopChangeJobCondition.SHOP_LIST_ID).append(" = ? ");

		// 店舗IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(shopListId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (TShopChangeJobCondition entity : list) {
			entity.shopListId = shopListId;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(entity);
		}
	}

	/**
	 * 削除登録する
	 * @param shopId
	 * @param list
	 */
	public void allDeleteInsert(int customerId, List<TShopChangeJobCondition> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TShopChangeJobCondition.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TShopChangeJobCondition.SHOP_LIST_ID);
		sql.append(" IN (SELECT id FROM ");
		sql.append(TShopList.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TShopList.CUSTOMER_ID).append(" = ? )");


		// 店舗IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(customerId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (TShopChangeJobCondition entity : list) {
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(entity);
		}
	}


	/**
	 * 店舗ID、雇用形態区分、職種区分で取得する
	 * @param shopListId
	 * @return
	 */
	public TShopChangeJobCondition findByShopListIdAndJob(int shopListId, int employPtnKbn, int jobKbn) {
		try {
			 List<TShopChangeJobCondition> list = findByCondition(
					new SimpleWhere()
					.eq(toCamelCase(TShopChangeJobCondition.SHOP_LIST_ID), shopListId)
					.eq(toCamelCase(TShopChangeJobCondition.EMPLOY_PTN_KBN), employPtnKbn)
					.eq(toCamelCase(TShopChangeJobCondition.JOB_KBN), jobKbn));
			 return list.get(0);
		} catch (WNoResultException e) {
			return null;
		}
	}
}
