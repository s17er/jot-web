package com.gourmetcaree.db.common.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TApplicationCareerHistoryAttribute;

/**
 * 応募職歴属性サービス
 *
 */
public class ApplicationCareerHistoryAttributeService extends AbstractGroumetCareeBasicService<TApplicationCareerHistoryAttribute>{

	/**
	 * 応募職歴ＩＤから応募職歴属性を取得します。
	 * @param id
	 * @return
	 */
	public List<TApplicationCareerHistoryAttribute> getApplicationCareerHistoryByApplicationCareerHistoryId(int id) {
		try {
			List<TApplicationCareerHistoryAttribute> entity = jdbcManager.from(TApplicationCareerHistoryAttribute.class)
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
