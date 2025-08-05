package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEB履歴書職歴属性のサービスクラスです。
 * @version 1.0
 */
public class CareerHistoryAttributeService extends AbstractGroumetCareeBasicService<TCareerHistoryAttribute> {

	public List<Integer> getAttributeValueListByCareerHistoryId(Integer careerHistoryId)
			throws WNoResultException {
		try {
			List<Integer> valueList = jdbcManager.from(TCareerHistoryAttribute.class)
								.where(new SimpleWhere()
								.eq(WztStringUtil.toCamelCase(TCareerHistoryAttribute.CAREER_HISTORY_ID), careerHistoryId)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.iterate(new IterationCallback<TCareerHistoryAttribute, List<Integer>>() {
									private List<Integer> list = new ArrayList<Integer>();
									@Override
									public List<Integer> iterate(TCareerHistoryAttribute entity, IterationContext context) {
										if (entity != null) {
											list.add(entity.attributeValue);
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

	/**
	 * 職歴IDをキーに属性リストを取得します。
	 * @param careerHistoryId
	 * @return
	 * @throws WNoResultException
	 */
	public List<TCareerHistoryAttribute> findByCareerHistoryId(Integer careerHistoryId) throws WNoResultException {
		if (careerHistoryId == null) {
			throw new WNoResultException();
		}
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TCareerHistoryAttribute.CAREER_HISTORY_ID), careerHistoryId);
		where.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);

		return findByCondition(where, TCareerHistoryAttribute.ID);
	}

	/**
	 * 職歴IDをキーに属性を削除
	 * @param careerHistoryId
	 */
	public void deleteByCareerHistoryId(Integer careerHistoryId) {
		if (careerHistoryId == null) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TCareerHistoryAttribute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TCareerHistoryAttribute.CAREER_HISTORY_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(careerHistoryId)
				.execute();

	}
}