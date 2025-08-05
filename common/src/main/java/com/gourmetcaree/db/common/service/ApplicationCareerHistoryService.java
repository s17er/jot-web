package com.gourmetcaree.db.common.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TApplicationCareerHistory;

/**
 * 応募職歴サービス
 *
 */
public class ApplicationCareerHistoryService extends AbstractGroumetCareeBasicService<TApplicationCareerHistory>{

	/**
	 * 応募ＩＤから応募職歴を取得します。
	 * @param id
	 * @return
	 */
	public List<TApplicationCareerHistory> getApplicationCareerHistoryByApplicationId(int id) {
		try {
			List<TApplicationCareerHistory> entity = jdbcManager.from(TApplicationCareerHistory.class)
												.where(new SimpleWhere()
												.eq("applicationId", id))
												.orderBy("id")
												.disallowNoResult()
												.getResultList();

			return entity;
		}catch (NoResultException e) {
			return null;
		}
	}
}
