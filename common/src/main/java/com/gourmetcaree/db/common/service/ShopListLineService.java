package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 店舗一覧路線サービス
 *
 */
public class ShopListLineService extends AbstractGroumetCareeBasicService<TShopListLine> {

	/**
	 * 店舗IDで路線リストを取得
	 * @param shopListId
	 * @return 店舗路線リスト。取得できない場合は空のリストを返す
	 */
	public List<TShopListLine> findByShopListId(int shopListId) {
		try {
			return findByCondition(
				createNotDeleteWhere()
					.eq(toCamelCase(TShopListLine.SHOP_LIST_ID), shopListId)
				, toCamelCase(toCamelCase(TShopListLine.DISPLAY_ORDER))
			);
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 削除登録する
	 * @param shopId
	 * @param list
	 */
	public void deleteInsert(int shopListId, List<TShopListLine> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TShopListLine.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TShopListLine.SHOP_LIST_ID).append(" = ? ");

		// 店舗IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(shopListId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (TShopListLine entity : list) {
			if (entity.stationCd == null) {
				continue;
			}
			entity.shopListId = shopListId;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(entity);
		}
	}

	/**
	 * 店舗一覧IDをキーに路線を物理削除
	 * @param shopListId 店舗一覧ID
	 */
	public void deleteByShopListId(int shopListId) {
		StringBuffer sb = new StringBuffer(0);
		sb.append(" DELETE FROM ");
		sb.append(TShopListLine.TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(TShopListLine.SHOP_LIST_ID);
		sb.append(" = ?");

		jdbcManager.updateBatchBySql(sb.toString(), Integer.class)
				.params(shopListId)
				.execute();
	}

	@Override
	public int insert(TShopListLine entity) {
		return super.insert(entity);
	}
}
