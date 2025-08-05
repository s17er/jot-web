package com.gourmetcaree.db.common.service;

import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 顧客アカウントマスタのサービスクラスです。
 * @version 1.0
 */
public class CustomerAccountService extends AbstractGroumetCareeBasicService<MCustomerAccount> {


	/**
	 * ログインIDの重複チェック
	 * @param loginId ログインID
	 * @return ログインIDが重複していない場合、trueを返す
	 */
	public boolean existCustomerDataByLoginId(String loginId) {

		MCustomerAccount entity = jdbcManager.from(MCustomerAccount.class)
						.where(new SimpleWhere()
						.eq("loginId", loginId)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		return entity == null ? true : false;
	}

	/**
	 * ログインIDの重複チェック
	 * 指定されたIDを除く
	 * @param loginId ログインID
	 * @return ログインIDが重複していない場合、trueを返す
	 */
	public boolean existSalesDataByIdLoginId(int id, String loginId) {

		MCustomerAccount entity = jdbcManager.from(MCustomerAccount.class)
						.where(new SimpleWhere()
						.ne("customerId", id)
						.eq("loginId", loginId)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		return entity == null ? true : false;
	}

	/**
	 * ログインIDが存在するかチェック
	 * @param loginId ログインID
	 * @return ログインIDが存在する場合、trueを返す。
	 */
	public boolean isCustomerExists(int id, String loginId) {

		try {
			MCustomerAccount entity = jdbcManager.from(MCustomerAccount.class)
				.where(new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(MCustomerAccount.CUSTOMER_ID), id)
				.eq(WztStringUtil.toCamelCase(MCustomerAccount.LOGIN_ID), loginId)
				.eq(WztStringUtil.toCamelCase(MCustomerAccount.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.disallowNoResult()
				.getSingleResult();

			return entity == null ? false : true;

		} catch (SNoResultException e) {
			// 存在しないのでfalseを返す。
			return false;
		}


	}

	/**
	 * 顧客IDをキーに顧客アカウントマスタデータを取得
	 * @param customerId 顧客ID
	 * @return 顧客アカウントマスタリスト
	 */
	public MCustomerAccount getMCustomerAccountByCustomerId(int customerId) {

		List<MCustomerAccount> entityList = jdbcManager.from(MCustomerAccount.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.disallowNoResult()
										.getResultList();

		return entityList.get(0);
	}

	/**
	 * 顧客IDをキーに顧客アカウントマスタデータを取得
	 * @param customerId 顧客ID
	 * @return 顧客アカウントマスタエンティティリスト
	 */
	public List<MCustomerAccount> getMCustomerAccountListByCustomerId(int customerId) {

		List<MCustomerAccount> entityList = jdbcManager.from(MCustomerAccount.class)
												.where(new SimpleWhere()
												.eq("customerId", customerId)
												.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
												.getResultList();

		return entityList;
	}

	/**
	 * 顧客IDをキーに顧客アカウントマスタを論理削除
	 * @param customerId 顧客ID
	 */
	public void logicalDeleteByCustomerId(int customerId) {

		// 顧客アカウントマスタを取得
		List<MCustomerAccount> entityList = getMCustomerAccountListByCustomerId(customerId);

		if (entityList != null && entityList.size() > 0) {
			logicalDeleteBatch(entityList);
		}
	}

	public void updatePassword(MCustomerAccount entity) {
		// パスワードを変換
		entity.password = DigestUtil.createDigest(entity.password);

		// バージョンチェックせずパスワードを更新
		updateIncludesVersion(entity);
	}
}
