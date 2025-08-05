package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 系列店舗B用タグのサービスクラスです。
 * @version 1.0
 */
public class ShopListTagService extends AbstractGroumetCareeBasicService<MShopListTag> {

	/**
	 * ShopListTagテーブルをすべて取得する
	 * @return
	 */
	public List<MShopListTag> getShopListTagFindByAll() throws WNoResultException {

		try {
			List<MShopListTag> entityList = jdbcManager.from(MShopListTag.class)
					.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
					.orderBy(MShopListTag.ID)
					.disallowNoResult()
					.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}
}