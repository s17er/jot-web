package com.gourmetcaree.db.common.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 系列店舗B用タグのマッピングのサービスクラスです。
 * @version 1.0
 */
public class ShopListTagMappingService extends AbstractGroumetCareeBasicService<MShopListTagMapping> {

	/**
	 * 店舗一覧IDに紐づくタグIDと店舗一覧IDのマッピングを取得する
	 * @param shopListTagId
	 * @param shopListId
	 * @return
	 */
	public MShopListTagMapping findByShopListTagIdAndShopListId(Integer shopListTagId, Integer shopListId) {

		try {
			return jdbcManager
				.from(MShopListTagMapping.class)
				.where(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(MShopListTagMapping.SHOP_LIST_ID), shopListId)
						.eq(WztStringUtil.toCamelCase(MShopListTagMapping.SHOP_LIST_TAG_ID), shopListTagId)
						.eq(WztStringUtil.toCamelCase(MShopListTagMapping.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						)
				.disallowNoResult()
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 店舗一覧IDに紐づくすべてのタグを削除する
	 * @param shopListId
	 */
	public void deleteByShopListId(Integer shopListId) {

		try {
			List<MShopListTagMapping> result = jdbcManager.from(MShopListTagMapping.class)
					.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(MShopListTagMapping.SHOP_LIST_ID), shopListId)
							.eq(WztStringUtil.toCamelCase(MShopListTagMapping.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							)
					.disallowNoResult()
					.getResultList();
			for (MShopListTagMapping entity : result) {
				delete(entity);
			}
		} catch (NoResultException e) {
			return;
		}
	}

	/**
	 * 店舗IDに紐づくタグマッピングを取得
	 * @param shopListId
	 * @return
	 */
	public List<MShopListTagMapping> findByShopListId(Integer shopListId) {
		try {
			return jdbcManager
				.from(MShopListTagMapping.class)
				.where(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(MShopListTagMapping.SHOP_LIST_ID), shopListId)
						.eq(WztStringUtil.toCamelCase(MShopListTagMapping.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						)
				.disallowNoResult()
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 削除と登録をする
	 * @param shopId
	 * @param list
	 */
	public void deleteInsert(int shopListId, List<MShopListTagMapping> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(MShopListTagMapping.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(MShopListTagMapping.SHOP_LIST_ID).append(" = ? ");

		// 店舗IDで物理削除を実行
		jdbcManager.updateBatchBySql(
				sql.toString(), Integer.class)
				.params(shopListId)
				.execute();
		// 登録内容が空の場合はデータの削除のみ
		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (MShopListTagMapping entity : list) {
			// タグIDが空の場合スルー
			if (entity.shopListTagId == null) {
				continue;
			}
			entity.shopListId = shopListId;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(entity);
		}
	}

	@Override
	public int insert(MShopListTagMapping entity) {
		return super.insert(entity);
	}
}