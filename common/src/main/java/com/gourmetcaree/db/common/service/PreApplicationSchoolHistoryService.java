package com.gourmetcaree.db.common.service;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TPreApplicationSchoolHistory;

/**
 * プレ応募学歴サービス
 *
 */
public class PreApplicationSchoolHistoryService extends AbstractGroumetCareeBasicService<TPreApplicationSchoolHistory>{

	/**
	 * プレ応募ＩＤからプレ応募学歴を取得します。
	 * @param id
	 * @return
	 */
	public TPreApplicationSchoolHistory getPreApplicationSchoolHistoryByApplicationId(int id) {
		try {
			TPreApplicationSchoolHistory entity = jdbcManager.from(TPreApplicationSchoolHistory.class)
												.where(new SimpleWhere()
												.eq("applicationId", id))
												.disallowNoResult()
												.getSingleResult();

			return entity;
		}catch (NoResultException e) {
			return null;
		}
	}
}
