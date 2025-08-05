package com.gourmetcaree.db.common.service.member;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 仮会員職歴サービス
 * @author nakamori
 *
 */
public class TempMemberCareerHistoryService extends BaseTempMemberSubDataService<TTempMemberCareerHistory> {

	/**
	 * 仮会員IDをキーにエンティティを取得
	 */
	public List<TTempMemberCareerHistory> findByTempMemberId(Integer tempMemberId) {
		if (tempMemberId == null) {
			return new ArrayList<TTempMemberCareerHistory>(0);
		}

		Where where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TTempMemberCareerHistory.TEMP_MEMBER_ID), tempMemberId)
							.eq(WztStringUtil.toCamelCase(TTempMemberCareerHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
							.where(where)
							.getResultList();
	}
}
