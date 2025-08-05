package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryLoginHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.enums.MTypeEnum.TerminalKbnEnum;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.property.WriteLoginHistoryProperty;

/**
 * 事前登録ログイン履歴サービス
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationEntryLoginHistoryService extends AbstractGroumetCareeBasicService<TAdvancedRegistrationEntryLoginHistory> {

	/**
	 * 事前登録のユーザIDを元にログイン履歴のレコードを取得します。
	 * @param advancedRegistrationEntryId 事前登録のユーザID
	 * @return
	 * @throws WNoResultException
	 * @author Yamane
	 */
	public TAdvancedRegistrationEntryLoginHistory getEntityByMemberId(int advancedRegistrationEntryId) throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TAdvancedRegistrationEntryLoginHistory.ADVANCED_REGISTRATION_ENTRY_ID), advancedRegistrationEntryId)
								.eq(toCamelCase(TAdvancedRegistrationEntryLoginHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
								;

		List<TAdvancedRegistrationEntryLoginHistory> retList = findByCondition(where);
		return retList.get(0);
	}

	/**
	 * 事前登録のユーザIDを条件に、ログイン履歴のデータを物理削除します。
	 * @param advancedRegistrationEntryId 事前登録のユーザID
	 * @author Yamane
	 */
	public void deleteTLoginHistoryByMemberId(int advancedRegistrationEntryId) {

		// SQLの作成
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(TAdvancedRegistrationEntryLoginHistory.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TAdvancedRegistrationEntryLoginHistory.ADVANCED_REGISTRATION_ENTRY_ID).append(" = ? ");

		// 会員IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(advancedRegistrationEntryId)
				.execute();
	}

	/**
	 * ログイン履歴を更新します。
	 * データがすでに存在すればUPDATEし、存在しなければINSERTを行います。
	 * @param property
	 * @param tarminalKbn
	 * @author Yamane
	 */
	public void writeLoginHistory(WriteLoginHistoryProperty property, TerminalKbnEnum tarminalKbn) {

		try {
			TAdvancedRegistrationEntryLoginHistory entity = getEntityByMemberId(property.memberId);
			Beans.copy(property, entity).execute();
			entity.terminalKbn = tarminalKbn.getValue();

			//ログイン情報をUPDATE（後勝ちとする。）
			updateIncludesVersion(entity);

		} catch (WNoResultException e) {
			//データが存在しない場合はINSERT
			TAdvancedRegistrationEntryLoginHistory entity = new TAdvancedRegistrationEntryLoginHistory();
			Beans.copy(property, entity).execute();
			entity.terminalKbn = tarminalKbn.getValue();
			entity.advancedRegistrationEntryId = property.memberId;

			insert(entity);
		}
	}

	/**
	 * 事前登録エントリIDからデータをセレクトします。
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @return エンティティ
	 * @author Takehiro Nakamori
	 */
	public TAdvancedRegistrationEntryLoginHistory findByAdvancedRegistrationEntryId(int advancedRegistrationEntryId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryLoginHistory.ADVANCED_REGISTRATION_ENTRY_ID), advancedRegistrationEntryId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryLoginHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);


		return jdbcManager.from(TAdvancedRegistrationEntryLoginHistory.class)
						.where(where)
						.limit(1)
						.getSingleResult();
	}
}
