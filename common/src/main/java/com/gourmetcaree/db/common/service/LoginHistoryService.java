package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.db.common.entity.TLoginHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.enums.MTypeEnum.TerminalKbnEnum;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.property.WriteLoginHistoryProperty;

/**
 * ログイン履歴のサービスクラスです。
 * @version 1.0
 */
public class LoginHistoryService extends AbstractGroumetCareeBasicService<TLoginHistory> {

	/**
	 * 会員IDを元にログイン履歴のレコードを取得します。
	 * @param memberId
	 * @return
	 * @throws WNoResultException
	 */
	public TLoginHistory getEntityByMemberId(int memberId) throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TLoginHistory.MEMBER_ID), memberId)
								.eq(toCamelCase(TLoginHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
								;

		List<TLoginHistory> retList = findByCondition(where);
		return retList.get(0);
	}


	/**
	 * 会員IDを条件に、ログイン履歴のデータを物理削除します。
	 * @param memberId 会員ID
	 */
	public void deleteTLoginHistoryByMemberId(int memberId) {

		// SQLの作成
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(TLoginHistory.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TLoginHistory.MEMBER_ID).append(" = ? ");

		// 会員IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(memberId)
				.execute();
	}

	/**
	 * ログイン履歴を更新します。
	 * データがすでに存在すればUPDATEし、存在しなければINSERTを行います。
	 * @param property
	 * @param tarminalKbn
	 */
	public void writeLoginHistory(WriteLoginHistoryProperty property, TerminalKbnEnum tarminalKbn) {

		try {
			TLoginHistory entity = getEntityByMemberId(property.memberId);
			Beans.copy(property, entity).execute();
			entity.terminalKbn = tarminalKbn.getValue();

			//ログイン情報をUPDATE（後勝ちとする。）
			updateIncludesVersion(entity);

		} catch (WNoResultException e) {
			//データが存在しない場合はINSERT
			TLoginHistory entity = new TLoginHistory();
			Beans.copy(property, entity).execute();
			entity.terminalKbn = tarminalKbn.getValue();

			insert(entity);
		}
	}
}

