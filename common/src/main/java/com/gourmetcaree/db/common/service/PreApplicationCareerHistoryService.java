package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TPreApplicationCareerHistory;
import com.gourmetcaree.db.common.entity.TPreApplicationCareerHistoryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * プレ応募職歴サービス
 *
 */
public class PreApplicationCareerHistoryService extends AbstractGroumetCareeBasicService<TPreApplicationCareerHistory>{

	/**
	 * プレ応募ＩＤから応募職歴を取得します。
	 * @param id
	 * @return
	 */
	public List<TPreApplicationCareerHistory> getPreApplicationCareerHistoryByApplicationId(int id) {
		try {
			List<TPreApplicationCareerHistory> entity = jdbcManager.from(TPreApplicationCareerHistory.class)
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

	public List<Integer> getAttributeValueListByPreApplicationCareerHistoryId(Integer careerHistoryId, String attributeCd)
			throws WNoResultException {
		try {
			List<Integer> valueList = jdbcManager.from(TPreApplicationCareerHistoryAttribute.class)
								.where(new SimpleWhere()
								.eq(WztStringUtil.toCamelCase(TPreApplicationCareerHistoryAttribute.APPLICATION_CAREER_HISTORY_ID), careerHistoryId)
								.eq("attributeCd", attributeCd)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.iterate(new IterationCallback<TPreApplicationCareerHistoryAttribute, List<Integer>>() {
									private List<Integer> list = new ArrayList<Integer>();
									@Override
									public List<Integer> iterate(TPreApplicationCareerHistoryAttribute entity, IterationContext context) {
										if (entity != null) {
											list.add(Integer.valueOf(entity.attributeValue));
										}
										return list;
									}
								});
			if (CollectionUtils.isEmpty(valueList)) {
				throw new WNoResultException();
			}

			return valueList;
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}
}
