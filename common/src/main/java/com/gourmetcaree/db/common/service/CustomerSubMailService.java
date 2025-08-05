package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 顧客サブメールのサービスクラスです。
 * @version 1.0
 */
public class CustomerSubMailService extends AbstractGroumetCareeBasicService<MCustomerSubMail> {


	/**
	 * 受信可能なサブメールアドレスを取得する
	 * @param customerId
	 * @return 受信可能なサブメールアドレスを返す。取得できない場合は空のリスト
	 */
	public List<String> getReceptionSubMail(int customerId) {
		try {
			List<MCustomerSubMail> list = findByCondition(
					new SimpleWhere()
						.eq(toCamelCase(MCustomerSubMail.CUSTOMER_ID), customerId)
						.eq(toCamelCase(MCustomerSubMail.SUBMAIL_RECEPTION_FLG), MTypeConstants.SubmailReceptionFlg.RECEIVE)
						, toCamelCase(MCustomerSubMail.SUB_MAIL));
			return list.stream().map(s -> s.subMail).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<String>(0);
		}
	}

	/**
	 * サブメールエンティティを顧客IDで取得
	 * @param customerId
	 * @return サブメールアドレスのリストを返す。取得出来ない場合は空のリスト
	 */
	public List<MCustomerSubMail> findByCustomerId(int customerId) {
		try {
		return findByCondition(
				new SimpleWhere()
					.eq(toCamelCase(MCustomerSubMail.CUSTOMER_ID), customerId)
					, toCamelCase(MCustomerSubMail.ID));
		} catch (WNoResultException e) {
			return new ArrayList<MCustomerSubMail>(0);
		}
	}

	/**
	 * 削除登録する
	 * @param customerId
	 * @param list
	 */
	public void deleteInsert(int customerId, List<MCustomerSubMail> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(MCustomerSubMail.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(MCustomerSubMail.CUSTOMER_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(customerId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (MCustomerSubMail entity : list) {
			if (StringUtil.isEmpty(entity.subMail) || entity.submailReceptionFlg == null) {
				continue;
			}
			MCustomerSubMail insertEntity = Beans.createAndCopy(MCustomerSubMail.class, entity).execute();
			insertEntity.customerId = customerId;
			insertEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(insertEntity);
		}
	}
}