package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.db.common.entity.TCustomerLoginHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.enums.MTypeEnum.TerminalKbnEnum;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.property.WriteCustomerLoginHistoryProperty;

/**
 * 顧客ログイン履歴のサービスクラスです。
 * @version 1.0
 */
public class CustomerLoginHistoryService extends AbstractGroumetCareeBasicService<TCustomerLoginHistory> {

	/**
	 * 顧客IDを元に顧客ログイン履歴のレコードを取得します。
	 * @param customerId
	 * @return TCustomerLoginHistory
	 * @throws WNoResultException
	 */
	public TCustomerLoginHistory getEntityByCustomerId(int customerId) throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TCustomerLoginHistory.CUSTOMER_ID), customerId)
								.eq(toCamelCase(TCustomerLoginHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
								;

		List<TCustomerLoginHistory> retList = findByCondition(where);
		return retList.get(0);
	}



	/**
	 * 顧客ログイン履歴を更新します。
	 * データがすでに存在すればUPDATEし、存在しなければINSERTを行います。
	 * @param property
	 * @param tarminalKbn
	 */
	public void writeLoginHistory(WriteCustomerLoginHistoryProperty property, TerminalKbnEnum tarminalKbn) {

		try {
			TCustomerLoginHistory entity = getEntityByCustomerId(property.customerId);
			Beans.copy(property, entity).execute();
			entity.terminalKbn = tarminalKbn.getValue();

			//ログイン情報をUPDATE（後勝ちとする。）
			updateIncludesVersion(entity);

		} catch (WNoResultException e) {
			//データが存在しない場合はINSERT
			TCustomerLoginHistory entity = new TCustomerLoginHistory();
			Beans.copy(property, entity).execute();
			entity.terminalKbn = tarminalKbn.getValue();

			insert(entity);
		}
	}
}

