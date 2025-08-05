package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.MCustomerHomepage;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 顧客ホームページのサービスクラスです。
 * @version 1.0
 */
public class CustomerHomepageService extends AbstractGroumetCareeBasicService<MCustomerHomepage> {

	/**
	 * 顧客ホームページエンティティを顧客IDで取得
	 * @param customerId
	 * @return 顧客ホームページのリストを返す。取得出来ない場合は空のリスト
	 */
	public List<MCustomerHomepage> findByCustomerId(int customerId) {
		try {
		return findByCondition(
				new SimpleWhere()
					.eq(toCamelCase(MCustomerHomepage.CUSTOMER_ID), customerId)
					, toCamelCase(MCustomerHomepage.ID));
		} catch (WNoResultException e) {
			return new ArrayList<MCustomerHomepage>(0);
		}
	}

	/**
	 * 削除登録する
	 * @param customerId
	 * @param list
	 */
	public void deleteInsert(int customerId, List<MCustomerHomepage> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(MCustomerHomepage.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(MCustomerHomepage.CUSTOMER_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(customerId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (MCustomerHomepage entity : list) {
			if (StringUtil.isEmpty(entity.url)) {
				continue;
			}
			MCustomerHomepage insertEntity = Beans.createAndCopy(MCustomerHomepage.class, entity).execute();
			insertEntity.customerId = customerId;
			insertEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(insertEntity);
		}
	}
}