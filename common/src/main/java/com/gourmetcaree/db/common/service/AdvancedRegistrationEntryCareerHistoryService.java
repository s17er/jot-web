package com.gourmetcaree.db.common.service;

import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryCareerHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 事前登録用職歴サービス
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationEntryCareerHistoryService extends AbstractGroumetCareeBasicService<TAdvancedRegistrationEntryCareerHistory> {


	/**
	 * 事前登録用職歴のセレクトを作成します。
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @return 事前登録用職歴のセレクト
	 */
	public AutoSelect<TAdvancedRegistrationEntryCareerHistory>
			createSelectFromAdvancedRegistrationEntryId(int advancedRegistrationEntryId) {

		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistory.ADVANCED_REGISTRATION_ENTRY_ID),
				advancedRegistrationEntryId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);


		return jdbcManager.from(TAdvancedRegistrationEntryCareerHistory.class)
				.where(where)
				.orderBy(TAdvancedRegistrationEntryCareerHistory.ID);

	}

	/**
	 * 事前登録エントリIDをキーに職歴テーブルを取得する。
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @return 職歴情報
	 * @throws WNoResultException
	 * @throws WNoResultException
	 */
	public List<TAdvancedRegistrationEntryCareerHistory> getCareerHistoryDataByAdvancedRegistrationEntryId(int advancedRegistrationEntryId) throws WNoResultException {

		try {
			List<TAdvancedRegistrationEntryCareerHistory> entityList = jdbcManager.from(TAdvancedRegistrationEntryCareerHistory.class)
												.where(new SimpleWhere()
												.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistory.ADVANCED_REGISTRATION_ENTRY_ID) , advancedRegistrationEntryId)
												.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
												.orderBy(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistory.ID))
												.disallowNoResult()
												.getResultList();
			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 職歴IDをキーに属性を削除
	 * @param careerHistoryId 職歴ID
	 */
	public void deleteByCareerHistoryId(Integer careerHistoryId) {
		if (careerHistoryId == null) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TAdvancedRegistrationEntryCareerHistory.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TAdvancedRegistrationEntryCareerHistory.ADVANCED_REGISTRATION_ENTRY_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(careerHistoryId)
				.execute();

	}
}
