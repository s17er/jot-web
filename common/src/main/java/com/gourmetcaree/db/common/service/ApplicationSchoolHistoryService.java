package com.gourmetcaree.db.common.service;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TApplicationSchoolHistory;

/**
 * 応募学歴サービス
 *
 */
public class ApplicationSchoolHistoryService extends AbstractGroumetCareeBasicService<TApplicationSchoolHistory>{

	/**
	 * 応募ＩＤから応募学歴を取得します。
	 * @param id
	 * @return
	 */
	public TApplicationSchoolHistory getApplicationSchoolHistoryByApplicationId(int id) {
		try {
			TApplicationSchoolHistory entity = jdbcManager.from(TApplicationSchoolHistory.class)
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
