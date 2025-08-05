package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 顧客画像サービス
 * @author hara
 *
 */
public class CustomerImageService extends AbstractGroumetCareeBasicService<TCustomerImage> {

	public List<TCustomerImage> findListByCustomerId(int customerId) throws WNoResultException {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(TCustomerImage.CUSTOMER_ID), customerId)
				.eq(WztStringUtil.toCamelCase(TCustomerImage.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return findByCondition(where);
	}

	public List<TCustomerImage> findListByCustomerIdAndFileName(int customerId, String fileName) throws WNoResultException {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(TCustomerImage.CUSTOMER_ID), customerId)
				.eq(WztStringUtil.toCamelCase(TCustomerImage.FILE_NAME), fileName)
				.eq(WztStringUtil.toCamelCase(TCustomerImage.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return findByCondition(where);
	}

}