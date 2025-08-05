package com.gourmetcaree.arbeitsys.service;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.arbeitsys.entity.MstTodouhuken;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * バイト都道府県サービス
 * @author nakamori
 *
 */
public class ArbeitTodouhukenService extends AbstractArbeitSystemService<MstTodouhuken> {

	public String convertIdToName(Integer id) {
		if (id == null) {
			return "";
		}
		MstTodouhuken entity = jdbcManager.from(MstTodouhuken.class)
									.where(new SimpleWhere()
									.eq(MstTodouhuken.ID, id)
									).getSingleResult();

		return entity == null ? "" : entity.name;
	}

	@Override
	public List<MstTodouhuken> findAll() {
		return jdbcManager.from(entityClass)
				.orderBy(MstTodouhuken.ID)
				.getResultList();
	}

	public List<MstTodouhuken> findByIds(Collection<Integer> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		return jdbcManager.from(entityClass)
					.where(new SimpleWhere().in("id", ids))
					.orderBy(MstTodouhuken.ID)
					.getResultList();
	}
}
