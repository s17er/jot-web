package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VCustomerImageNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

public class CustomerImageNoDataService extends AbstractGroumetCareeBasicService<VCustomerImageNoData> {

	public List<VCustomerImageNoData> findListByCustomerId(int customerId) throws WNoResultException {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(VCustomerImageNoData.CUSTOMER_ID), customerId);

		return findByCondition(where);
	}

}
