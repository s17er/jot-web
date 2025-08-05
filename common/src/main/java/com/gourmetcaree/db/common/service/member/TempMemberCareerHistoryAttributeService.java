package com.gourmetcaree.db.common.service.member;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistoryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService;

/**
 * 仮会員職歴属性サービス
 * @author nakamori
 *
 */
public class TempMemberCareerHistoryAttributeService extends AbstractGroumetCareeBasicService<TTempMemberCareerHistoryAttribute> {

	/**
	 * 仮会員職歴IDをキーにエンティティを取得
	 */
	public List<TTempMemberCareerHistoryAttribute> findByTempMemberCareerHistoryId(Integer tempMemberCareerHistoryId) {
		if (tempMemberCareerHistoryId == null) {
			return new ArrayList<TTempMemberCareerHistoryAttribute>(0);
		}

		Where where = new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TTempMemberCareerHistoryAttribute.TEMP_MEMBER_CAREER_HISTORY_ID), tempMemberCareerHistoryId)
							.eq(WztStringUtil.toCamelCase(TTempMemberCareerHistoryAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
							.where(where)
							.getResultList();
	}


	/**
	 * 仮会員職歴IDをキーにデータを物理削除
	 */
	public void deleteByTempMemberCareerHistoryIds(List<Integer> tempMemberCareerHistoryIdList) {
		if (CollectionUtils.isEmpty(tempMemberCareerHistoryIdList)) {
			return;
		}
		String sql = String.format("DELETE FROM %s WHERE %s IN (%s)",
				getTableName(),
				TTempMemberCareerHistoryAttribute.TEMP_MEMBER_CAREER_HISTORY_ID,
				SqlUtils.getQMarks(tempMemberCareerHistoryIdList.size()));

		List<Class<?>> classes = Lists.newArrayList();
		for (int i = 0; i < tempMemberCareerHistoryIdList.size(); i++) {
			classes.add(Integer.class);
		}

		jdbcManager.updateBySql(sql, classes.toArray(new Class<?>[0]))
					.params(tempMemberCareerHistoryIdList.toArray())
					.execute();

		serviceLog.info(String.format("仮会員職歴属性データを削除しました。仮会員職歴ID:[%s]", StringUtils.join(tempMemberCareerHistoryIdList, ",")));

	}
}
