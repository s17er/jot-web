package com.gourmetcaree.db.common.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MWebTagMapping;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * WEB用タグのマッピングのサービスクラスです。
 * @version 1.0
 */
public class WebTagMappingService extends AbstractGroumetCareeBasicService<MWebTagMapping> {

	/**
	 * WEBデータIDに紐づくタグIDとWEBデータIDのマッピングを取得する
	 * @param webDataTagId
	 * @param webDataId
	 * @return
	 */
	public MWebTagMapping findByWebDataTagIdAndWebDataId(Integer webDataTagId, Integer webDataId) {

		try {
			return jdbcManager
				.from(MWebTagMapping.class)
				.where(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(MWebTagMapping.WEB_ID), webDataId)
						.eq(WztStringUtil.toCamelCase(MWebTagMapping.WEB_TAG_ID), webDataTagId)
						.eq(WztStringUtil.toCamelCase(MWebTagMapping.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						)
				.disallowNoResult()
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * WEBデータIDに紐づくすべてのタグを削除する
	 * @param webDataId
	 */
	public void deleteByWebDataId(Integer webDataId) {

		try {
			List<MWebTagMapping> result = jdbcManager.from(MWebTagMapping.class)
					.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(MWebTagMapping.WEB_ID), webDataId)
							.eq(WztStringUtil.toCamelCase(MWebTagMapping.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							)
					.disallowNoResult()
					.getResultList();
			for (MWebTagMapping entity : result) {
				delete(entity);
			}
		} catch (NoResultException e) {
			return;
		}
	}
}