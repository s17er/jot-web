package com.gourmetcaree.db.common.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TPreApplicationCareerHistoryAttribute;

/**
 * プレ応募職歴属性サービス
 *
 */
public class PreApplicationCareerHistoryAttributeService extends AbstractGroumetCareeBasicService<TPreApplicationCareerHistoryAttribute>{

	/**
	 * プレ応募職歴IDからプレ応募職歴属性を取得します。
	 * @param id
	 * @return
	 */
	public List<TPreApplicationCareerHistoryAttribute> getPreApplicationCareerHistoryByApplicationCareerHistoryId(int id) {
		try {
			List<TPreApplicationCareerHistoryAttribute> entity = jdbcManager.from(TPreApplicationCareerHistoryAttribute.class)
												.where(new SimpleWhere()
												.eq("applicationCareerHistoryId", id))
												.disallowNoResult()
												.getResultList();

			return entity;
		}catch (NoResultException e) {
			return null;
		}
	}
}
