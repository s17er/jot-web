package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TCustomerSearchCondition;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

public class CustomerSearchConditionService extends AbstractGroumetCareeBasicService<TCustomerSearchCondition> {

	/**
	 * 顧客IDで会員条件を検索します
	 * @param customerId
	 * @return
	 */
	public List<TCustomerSearchCondition> findByCustomerId(int customerId) {
		List<TCustomerSearchCondition> list = new ArrayList<>();
		try {
			list = jdbcManager.from(TCustomerSearchCondition.class)
							.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TCustomerSearchCondition.CUSTOMER_ID), customerId)
							.eq(WztStringUtil.toCamelCase(TCustomerSearchCondition.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getResultList();
		}catch (SNoResultException e) {
			return null;
		}
		return list;
	}

}
