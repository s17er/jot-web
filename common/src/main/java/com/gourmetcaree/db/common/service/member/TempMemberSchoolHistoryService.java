package com.gourmetcaree.db.common.service.member;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.member.TTempMemberSchoolHistory;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 仮会員学歴サービス
 * @author nakamori
 *
 */
public class TempMemberSchoolHistoryService extends BaseTempMemberSubDataService<TTempMemberSchoolHistory> {

	/**
	 * 仮会員IDをキーにエンティティを取得
	 */
	public TTempMemberSchoolHistory findOneByTempMemberId(Integer tempMemberId) {
		if (tempMemberId == null) {
			return null;
		}

		Where where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TTempMemberSchoolHistory.TEMP_MEMBER_ID), tempMemberId)
							.eq(WztStringUtil.toCamelCase(TTempMemberSchoolHistory.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
							.where(where)
							.getSingleResult();
	}
}
