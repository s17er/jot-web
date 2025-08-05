package com.gourmetcaree.db.common.service.member;


import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.member.TTempMemberAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;


/**
 * 仮会員属性サービス
 * @author nakamori
 *
 */
public class TempMemberAttributeService extends BaseTempMemberSubDataService<TTempMemberAttribute>{

	/**
	 * 仮会員IDをキーにエンティティを取得
	 */
	public List<TTempMemberAttribute> findByTempMemberId(Integer tempMemberId) {
		if (tempMemberId == null) {
			return new ArrayList<TTempMemberAttribute>(0);
		}

		Where where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TTempMemberAttribute.TEMP_MEMBER_ID), tempMemberId)
							.eq(WztStringUtil.toCamelCase(TTempMemberAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
							.where(where)
							.getResultList();
	}
}
