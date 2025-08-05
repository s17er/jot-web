package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MWebTag;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * WEB用タグのサービスクラスです。
 * @version 1.0
 */
public class WebTagService extends AbstractGroumetCareeBasicService<MWebTag> {

	/**
	 * WebTagテーブルをすべて取得する
	 * @return
	 */
	public List<MWebTag> getWebTagListFindByAll() throws WNoResultException {

		try {
			List<MWebTag> entityList = jdbcManager.from(MWebTag.class)
					.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
					.orderBy(MWebTag.ID)
					.disallowNoResult()
					.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}
}