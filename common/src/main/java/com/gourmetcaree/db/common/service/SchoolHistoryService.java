package com.gourmetcaree.db.common.service;

import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEB履歴書学歴のサービスクラスです。
 * @version 1.0
 */
public class SchoolHistoryService extends AbstractGroumetCareeBasicService<TSchoolHistory> {


	/**
	 * 会員IDをキーに学歴情報を取得する。
	 * @param memberId 会員ID
	 * @return 学歴情報
	 * @throws WNoResultException データが見つからなかった場合にスロー
	 */
	public TSchoolHistory getTSchoolHistoryDataByMemberId(int memberId) throws WNoResultException {

		try {
			TSchoolHistory entity = jdbcManager.from(TSchoolHistory.class)
										.where(new SimpleWhere()
										.eq("memberId", memberId)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.disallowNoResult()
										.getSingleResult();

			return entity;
		} catch (SNoResultException e) {
			throw new WNoResultException(String.format("データが見つかりませんでした。 会員ID:[%d]", memberId), e);
		}
	}

	/**
	 * 会員IDをキーに学歴データを物理削除
	 * @param memberId 会員ID
	 */
	public void deleteSchoolHistoryByMemberId(int memberId) {

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
			    "DELETE FROM t_school_history WHERE member_id = ? "
				, Integer.class);

		    batchUpdate.params(memberId);

		    batchUpdate.execute();
	}
}