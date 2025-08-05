package com.gourmetcaree.db.common.service;


import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;

import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 営業担当者マスタのサービスクラスです。
 * @version 1.0
 */
public class SalesService extends AbstractGroumetCareeBasicService<MSales> {


	/**
	 * ログインIDの重複チェック
	 * @param loginId ログインID
	 * @return ログインIDが重複していない場合、trueを返す
	 */
	public boolean existSalesDataByLoginId(String loginId) {

		MSales entity = jdbcManager.from(MSales.class)
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

		MSales entity = jdbcManager.from(MSales.class)
						.where(new SimpleWhere()
						.ne("id", id)
						.eq("loginId", loginId)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		return entity == null ? true : false;
	}

	/**
	 * 指定した条件で検索し、会社マスタのデータ共に取得します。
	 * @param conditions 条件
	 * @return 検索結果
	 */
	public List<MSales> findByConditionWithCompany(BeanMap conditions) {
		return select()
				.innerJoin("MCompany")
				.where(conditions)
				.where(new SimpleWhere().eq("MCompany.deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.getResultList();
	}

	/**
	 * IDをキーにして営業担当者の情報を取得
	 * @param id 営業担当者ID
	 * @return 営業担当者エンティティ
	 */
	public MSales getCompanyDataById(int id) {

		MSales entity = jdbcManager.from(MSales.class)
						.where(new SimpleWhere()
						.eq("id", id)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		return entity;
	}

	/**
	 * 営業担当者IDをキーに会社IDを取得
	 * @param id 営業担当者ID
	 * @return 会社ID
	 */
	public int getCompanyIdBySalesId(int id) {

		MSales entity = jdbcManager.from(MSales.class)
							.where(new SimpleWhere()
							.eq("id", id)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getSingleResult();

		return entity.companyId;
	}

	/**
	 * 旧システムに登録されているジェイオフィスの営業担当者一覧を取得します。
	 * @return 営業担当者のリスト
	 */
	public List<MSales> getOldSystemJofficeSalesList() {
		return select()
		.where(new SimpleWhere()
			.eq("companyId", 1)
			.isNotNull("oldSystemSalesId", true)
			.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
		).getResultList();

	}

}