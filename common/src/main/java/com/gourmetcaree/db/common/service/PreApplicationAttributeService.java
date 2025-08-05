package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TPreApplicationAttribute;

/**
 * プレ応募属性サービス
 *
 */
public class PreApplicationAttributeService extends AbstractGroumetCareeBasicService<TPreApplicationAttribute>{

	/**
	 *プレ応募IDから属性エンティティのリストを取得します。
	 * @param id
	 * @return
	 */
	public List<TPreApplicationAttribute> getPreApplicationAttrListByApplicationId(int id) {
		try {
			List<TPreApplicationAttribute> entityList = jdbcManager.from(TPreApplicationAttribute.class)
														.where(new SimpleWhere()
														.eq("applicationId", id)
														).disallowNoResult()
														.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			return new ArrayList<TPreApplicationAttribute>();
		}
	}

	public int[] getPreApplicationAttrValue(int id, String attrCd) {
		try {
			List<TPreApplicationAttribute> entityList = jdbcManager.from(TPreApplicationAttribute.class)
														.where(new SimpleWhere()
														.eq("applicationId", id)
														.eq("attributeCd", attrCd)
														).disallowNoResult()
														.getResultList();

			int[] attrValue = new int[entityList.size()];
			for(int i = 0; i < entityList.size(); i++) {
				attrValue[i] = entityList.get(i).attributeValue;
			}

			return attrValue;
		} catch (SNoResultException e) {
			return new int[0];
		}
	}
}
