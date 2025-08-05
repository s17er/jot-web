package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TApplicationAttribute;

/**
 * 応募属性サービス
 * @author takehiro nakamori
 *
 */
public class ApplicationAttributeService extends AbstractGroumetCareeBasicService<TApplicationAttribute>{

	/**
	 * 応募ＩＤから属性エンティティのリストを取得します。
	 * @param id
	 * @return
	 */
	public List<TApplicationAttribute> getApplicationAttrListByApplicationId(int id) {
		try {
			List<TApplicationAttribute> entityList = jdbcManager.from(TApplicationAttribute.class)
														.where(new SimpleWhere()
														.eq("applicationId", id)
														).disallowNoResult()
														.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			return new ArrayList<TApplicationAttribute>();
		}
	}

	public int[] getApplicationAttrValue(int id, String attrCd) {
		try {
			List<TApplicationAttribute> entityList = jdbcManager.from(TApplicationAttribute.class)
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
