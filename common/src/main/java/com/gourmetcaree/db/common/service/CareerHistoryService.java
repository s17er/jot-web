package com.gourmetcaree.db.common.service;

import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEB履歴書職歴のサービスクラスです。
 * @version 1.0
 */
public class CareerHistoryService extends AbstractGroumetCareeBasicService<TCareerHistory> {

	/**
	 * 会員IDをキーに職歴テーブル、職歴属性テーブル情報を取得する。
	 * @param memberId 会員ID
	 * @return 職歴情報
	 * @throws WNoResultException データが見つからなかった場合にスロー
	 */
	public List<TCareerHistory> getCareerHistoryDataByMemberId(int memberId) throws WNoResultException {

		try {
			List<TCareerHistory> entityList = jdbcManager.from(TCareerHistory.class)
												.leftOuterJoin("tCareerHistoryAttributeList", "tCareerHistoryAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
												.where(new SimpleWhere()
												.eq("memberId", memberId)
												.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
												.orderBy("id, tCareerHistoryAttributeList.id")
												.disallowNoResult()
												.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException(String.format("データが見つかりませんでした。会員ID:[%d]", memberId), e);
		}
	}


}