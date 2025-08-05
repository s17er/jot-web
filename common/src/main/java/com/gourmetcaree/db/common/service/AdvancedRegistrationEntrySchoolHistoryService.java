package com.gourmetcaree.db.common.service;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntrySchoolHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 事前登録用学歴サービス
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationEntrySchoolHistoryService extends AbstractGroumetCareeBasicService<TAdvancedRegistrationEntrySchoolHistory> {


	/**
	 * 事前登録エントリIDをキーに学歴情報を取得する。
	 * @param id 会員ID
	 * @return 学歴情報
	 * @throws WNoResultException
	 * @throws WNoResultException
	 * @author Yamane
	 */
	public TAdvancedRegistrationEntrySchoolHistory getTSchoolHistoryDataByMemberId(int advancedRegistrationEntryId) throws WNoResultException {

		try {
			TAdvancedRegistrationEntrySchoolHistory entity = jdbcManager.from(TAdvancedRegistrationEntrySchoolHistory.class)
										.where(new SimpleWhere()
										.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntrySchoolHistory.ADVANCED_REGISTRATION_ENTRY_ID), advancedRegistrationEntryId)
										.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntrySchoolHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
										.disallowNoResult()
										.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 事前登録エントリIDをキーに学歴データを物理削除
	 * @param advancedRegistrationEntryId 会員ID
	 * @author Yamane
	 */
	public void deleteSchoolHistoryByAdvancedRegistrationEntryId(int advancedRegistrationEntryId) {

		StringBuilder sb = new StringBuilder(0);
		sb.append("DELETE FROM ").append(TAdvancedRegistrationEntrySchoolHistory.TABLE_NAME).append(" WHERE ");
		sb.append(TAdvancedRegistrationEntrySchoolHistory.ADVANCED_REGISTRATION_ENTRY_ID).append(" = ?");

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
				sb.toString(), Integer.class);

		    batchUpdate.params(advancedRegistrationEntryId);

		    batchUpdate.execute();
	}


	/**
	 * 事前登録エントリIDをキーに検索します。
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @return 事前登録用学歴
	 */
	public TAdvancedRegistrationEntrySchoolHistory findByAdvancedRegistrationEntryId(int advancedRegistrationEntryId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntrySchoolHistory.ADVANCED_REGISTRATION_ENTRY_ID), advancedRegistrationEntryId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntrySchoolHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(TAdvancedRegistrationEntrySchoolHistory.class)
						.where(where)
						.disallowNoResult()
						.limit(1)
						.getSingleResult();
	}

}
