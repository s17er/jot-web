package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TShopListLabelGroup;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 店舗ラベルグループサービス
 *
 */
public class ShopListLabelGroupService extends AbstractGroumetCareeBasicService<TShopListLabelGroup> {


	/**
	 * 店舗IDで取得する
	 * @param shopListId
	 * @return エンティティのリスト
	 * @throws WNoResultException
	 */
	public List<TShopListLabelGroup> findByShopListId(int shopListId) throws WNoResultException {
		return findByCondition(
					new SimpleWhere()
						.eq(toCamelCase(TShopListLabelGroup.SHOP_LIST_ID), shopListId)
						.eq(toCamelCase(TShopListLabelGroup.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
					SqlUtils.createCommaStr(new String[]{toCamelCase(TShopListLabelGroup.DISPLAY_ORDER), toCamelCase(TShopListLabelGroup.ID)})
				);
	}

	/**
	 * 店舗リストラベルIDで取得する
	 * @param shopListLabelId
	 * @return エンティティのリスト
	 * @throws WNoResultException
	 */
	public List<TShopListLabelGroup> findByShopListLabelId(int shopListLabelId) throws WNoResultException {
		return findByCondition(
				new SimpleWhere()
					.eq(toCamelCase(TShopListLabelGroup.SHOP_LIST_LABEL_ID), shopListLabelId)
					.eq(toCamelCase(TShopListLabelGroup.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
				SqlUtils.createCommaStr(new String[]{toCamelCase(TShopListLabelGroup.DISPLAY_ORDER), toCamelCase(TShopListLabelGroup.ID)})
			);
	}

	/**
	 * 店舗リストラベルIDで削除する
	 * @param shopListLabelId
	 * @return エンティティのリスト
	 * @throws WNoResultException
	 */
	public void deleteByShopListLabelId(int shopListLabelId) throws WNoResultException {

		deleteBatchIgnoreVersion(findByShopListLabelId(shopListLabelId));
	}
}
