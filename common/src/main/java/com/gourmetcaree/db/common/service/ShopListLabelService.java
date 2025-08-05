package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TShopListLabel;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 店舗ラベルサービス
 *
 */
public class ShopListLabelService extends AbstractGroumetCareeBasicService<TShopListLabel> {


	/**
	 * 顧客IDで取得する
	 * @param customerId
	 * @return エンティティのリスト
	 * @throws WNoResultException
	 */
	public List<TShopListLabel> findByCustomerId(int customerId) throws WNoResultException {
		return findByCondition(
					new SimpleWhere()
						.eq(toCamelCase(TShopListLabel.CUSTOMER_ID), customerId)
						.eq(toCamelCase(TShopListLabel.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
					SqlUtils.createCommaStr(new String[]{toCamelCase(TShopListLabel.DISPLAY_ORDER), toCamelCase(TShopListLabel.ID)})
				);
	}
}
