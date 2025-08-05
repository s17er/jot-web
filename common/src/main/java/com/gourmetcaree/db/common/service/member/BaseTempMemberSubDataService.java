package com.gourmetcaree.db.common.service.member;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.google.common.collect.Lists;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.accessor.TempMemberSubDataAccessor;
import com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService;

/**
 * 仮会員のサブデータを扱うサービスの親クラス
 * @author nakamori
 *
 */
public abstract class BaseTempMemberSubDataService<E extends AbstractCommonEntity & TempMemberSubDataAccessor> extends AbstractGroumetCareeBasicService<E>{


	/**
	 * DELETE INSERTを使ったアップデート
	 * @param tempMemberId
	 * @param entities
	 */
	public List<Integer> deleteInsert(int tempMemberId, E... entities) {
		List<E> entityList;
		if (entities == null) {
			entityList = Lists.newArrayList();
		} else {
			entityList = Arrays.asList(entities);
		}

		return deleteInsert(tempMemberId, entityList);
	}

	/**
	 * DELETE INSERTを使ったアップデート
	 * @return 削除したIDのリスト
	 */
	public List<Integer> deleteInsert(int tempMemberId, List<E> entities) {
		 List<Integer> idList = deleteByTempMemberId(tempMemberId);

		if (CollectionUtils.isEmpty(entities)) {
			return idList;
		}
		for (E entity : entities) {
			entity.setTempMemberId(tempMemberId);
			int result = insert(entity);
			serviceLog.info(String.format("テーブル[%s]のデータをINSERTしました。仮会員ID:[%d] 結果:[%d] エンティティ：[%s]", getTableName(), tempMemberId, result, entity));
		}
		return idList;
	}


	/**
	 * 仮会員IDを指定してデータを物理削除
	 */
	public List<Integer> deleteByTempMemberId(int tempMemberId) {
		List<E> entityList = findByTempMemberId(tempMemberId);

		List<Integer> idList = Lists.newArrayList();
		for (E entity : entityList) {
			idList.add(entity.getId());
			int result = delete(entity);
			serviceLog.info(String.format("テーブル[%s]のデータを削除しました。仮会員ID:[%d] 結果:[%d]", getTableName(), tempMemberId, result));
		}
		return idList;
	}


	public List<E> findByTempMemberId(Integer tempMemberId) {
		SimpleWhere where = new SimpleWhere();
		where.eq("tempMemberId", tempMemberId);

		return jdbcManager.from(entityClass)
							.where(where)
							.getResultList();
	}

	public List<E> findByTempMemberIdIgnoreDeleteFlg(Integer tempMemberId) {
		SimpleWhere where = new SimpleWhere();
		where.eq("tempMemberId", tempMemberId);

		return jdbcManager.from(entityClass)
							.where(where)
							.getResultList();
	}
}
