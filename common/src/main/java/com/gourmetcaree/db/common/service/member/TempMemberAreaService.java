package com.gourmetcaree.db.common.service.member;


import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.google.common.collect.Lists;
import com.gourmetcaree.db.common.entity.member.TTempMemberArea;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 仮会員エリアサービス
 * @author nakamori
 *
 */
public class TempMemberAreaService extends BaseTempMemberSubDataService<TTempMemberArea> {

	/**
	 * 仮会員IDをキーにエンティティを取得
	 */
	public List<TTempMemberArea> findByTempMemberId(Integer tempMemberId) {
		if (tempMemberId == null) {
			return new ArrayList<TTempMemberArea>(0);
		}

		Where where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TTempMemberArea.TEMP_MEMBER_ID), tempMemberId)
							.eq(WztStringUtil.toCamelCase(TTempMemberArea.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
							.where(where)
							.getResultList();
	}



	/**
	 * 仮会員IDをキーにエンティティを取得
	 */
	public List<Integer> findAreaListByTempMemberId(Integer tempMemberId) {
		List<TTempMemberArea> entityList = findByTempMemberId(tempMemberId);
		List<Integer> list = Lists.newArrayListWithCapacity(entityList.size());

		for (TTempMemberArea entity : entityList) {
			list.add(entity.areaCd);
		}

		return list;
	}
}
