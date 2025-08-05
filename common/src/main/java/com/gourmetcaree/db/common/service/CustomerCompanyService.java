package com.gourmetcaree.db.common.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.functionalInterface.JdbcIterator;
import com.gourmetcaree.functionalInterface.builder.JdbcIteratorCallback;

/**
 * 顧客担当会社マスタのサービスクラスです。
 * @version 1.0
 */
public class CustomerCompanyService extends AbstractGroumetCareeBasicService<MCustomerCompany> {


	/**
	 * 顧客IDをキーに顧客担当会社マスタデータを取得(管理者・自社スタッフ)
	 * @param customerId 顧客ID
	 * @return 顧客担当会社マスタエンティティリスト
	 */
	public List<MCustomerCompany> getMCustomerCompanyListByCustomerId(int customerId) {

		List<MCustomerCompany> entityList = jdbcManager.from(MCustomerCompany.class)
										.where(new SimpleWhere()
										.eq("customerId", customerId)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.orderBy("companyId")
										.getResultList();

		return entityList;
	}

	/**
	 * 顧客ID・営業担当者IDをキーに顧客担当者マスタデータを取得(代理店)
	 * @param customerId 顧客ID
	 * @param salesId 営業担当者ID
	 * @return 顧客担当会社マスタエンティティリスト
	 */
	public List<MCustomerCompany> getMCustomerCompanyListByCustomerId(int customerId, int salesId) {

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlStr.append("SELECT cc.* FROM m_customer_company cc INNER JOIN m_company c ON cc.company_id = c.id ");
		sqlStr.append("INNER JOIN m_sales s ON s.company_id = c.id  ");
		sqlStr.append("WHERE cc.customer_id = ? AND s.id = ? ");
		sqlStr.append("AND cc.delete_flg = ? ");
		sqlStr.append("AND c.delete_flg = ? ");
		sqlStr.append("AND s.delete_flg = ? ");
		sqlStr.append("ORDER BY cc.id ");

		params.add(customerId);
		params.add(salesId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		List<MCustomerCompany> entityList = jdbcManager.selectBySql(MCustomerCompany.class, sqlStr.toString(), params.toArray()).getResultList();

		return entityList;
	}

	/**
	 * 顧客IDをキーに顧客担当会社マスタデータを物理削除
	 * @param customerId 顧客ID
	 */
	public void deleteCustomerCompanyByCustomerId(int customerId) {

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
			    "DELETE FROM m_customer_company WHERE customer_id = ? "
				, Integer.class);

		    batchUpdate.params(customerId);

		    batchUpdate.execute();
	}

	/**
	 * 顧客IDと営業担当者IDをキーに顧客マスタデータを検索（営業担当者が代理店の場合)
	 * @param customerId 顧客ID
	 * @param salesId 営業担当者ID
	 * @return 自分の会社の担当顧客であればtrueを返す
	 */
	public boolean isCustomerDataExistByCustomerIdSalesId(int customerId, int salesId) {

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlStr.append("SELECT cc.id FROM m_customer_company cc INNER JOIN m_company c ON cc.company_id = c.id ");
		sqlStr.append("INNER JOIN m_sales s ON s.company_id = c.id  ");
		sqlStr.append("WHERE cc.customer_id = ? AND s.id = ? ");
		sqlStr.append("AND cc.delete_flg = ? ");
		sqlStr.append("AND c.delete_flg = ? ");
		sqlStr.append("AND s.delete_flg = ? ");

		params.add(customerId);
		params.add(salesId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		List<MCustomerCompany> entityList = jdbcManager.selectBySql(MCustomerCompany.class, sqlStr.toString(), params.toArray()).getResultList();

		if (entityList == null || entityList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 顧客IDをキーに顧客マスタデータを検索（営業担当者が自社スタッフの場合)
	 * @param customerId 顧客ID
	 * @return 担当会社に代理店がない場合、trueを返す
	 */
	public boolean isCustomerDataExistByCustomerId(int customerId) {

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlStr.append("SELECT cc.id FROM m_customer_company cc INNER JOIN m_company c ON cc.company_id = c.id ");
		sqlStr.append("WHERE cc.customer_id = ? AND c.agency_flg = ? ");
		sqlStr.append("AND cc.delete_flg = ? ");
		sqlStr.append("AND c.delete_flg = ? ");

		params.add(customerId);
		params.add(MCompany.AgencyFlgValue.AGENCY);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		List<MCustomerCompany> entityList = jdbcManager.selectBySql(MCustomerCompany.class, sqlStr.toString(), params.toArray()).getResultList();

		if (entityList == null || entityList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 顧客IDから会社IDのリストを取得します。
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public List<Integer> getCompanyIdListByCustomerId(int customerId) throws WNoResultException {
		Where where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(MCustomerCompany.CUSTOMER_ID), customerId)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		List<MCustomerCompany> entityList = findByCondition(where);

		List<Integer> idList = new ArrayList<Integer>();
		for (MCustomerCompany entity : entityList) {
			idList.add(entity.companyId);
		}
		return idList;
	}

	/**
	 * customerCompanyNameの名前を取得
	 * @param customerId
	 * @param iterator
	 */
	public void selectCustomerCompanyName(Integer customerId, JdbcIterator<CompanySalesNameDto> iterator) {
		if (customerId == null) {
			return;
		}

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		CompanySalesNameDto.createSql(sql, params, customerId);
		jdbcManager.selectBySql(CompanySalesNameDto.class, sql.toString(), params.toArray())
				.iterate(JdbcIteratorCallback.callback(iterator));
	}


	public static class CompanySalesNameDto implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = -2273114904525221403L;

		public String companyName;

		public String salesName;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

		private static void createSql(StringBuilder sql, List<Object> params, Integer customerId) {
			sql.append(" SELECT ");
			sql.append("   C.company_name ");
			sql.append("   , S.sales_name  ");
			sql.append(" FROM ");
			sql.append("   m_customer_company CC  ");
			sql.append("   INNER JOIN m_company C  ");
			sql.append("     ON CC.company_id = C.id  ");
			sql.append("   INNER JOIN m_sales S  ");
			sql.append("     ON CC.sales_id = S.id  ");
			sql.append(" WHERE ");
			sql.append("   CC.customer_id = ?  ");
			sql.append("   AND CC.delete_flg = ?  ");
			sql.append("   AND C.delete_flg = ?  ");
			sql.append("   AND S.delete_flg = ?  ");
			sql.append(" ORDER BY ");
			sql.append("   CC.company_id ");

			params.add(customerId);
			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}
	}
}