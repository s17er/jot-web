package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryCareerHistoryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;


/**
 * 事前登録用職歴属性サービス
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationEntryCareerHistoryAttributeService extends AbstractGroumetCareeBasicService<TAdvancedRegistrationEntryCareerHistoryAttribute> {


	/**
	 * 職歴IDと属性コードから属性値のString配列を作成します。
	 * @param careerHistoryId 職歴ID
	 * @param attributeCd 属性コード
	 * @return 属性値のString配列
	 */
	public String[] selectAttributeValueStringArrayFromCareerHistoryIdAndAttributeCd(int careerHistoryId, String attributeCd) {

		return selectAttributeValueStringListFromCareerHistoryIdAndAttributeCd(careerHistoryId, attributeCd).toArray(new String[0]);
	}

	/**
	 * 職歴IDと属性コードから属性値のリストを作成します。
	 * @param careerHistoryId 職歴ID
	 * @param attributeCd 属性コード
	 * @return 属性値のString配列
	 */
	public List<String> selectAttributeValueStringListFromCareerHistoryIdAndAttributeCd(int careerHistoryId, String attributeCd) {

		List<String> valueList =
			createSelectByCareerHistoryIdAndAttributeCd(careerHistoryId, attributeCd)
				.iterate(new IterationCallback<TAdvancedRegistrationEntryCareerHistoryAttribute, List<String>>() {
					private List<String> retList = new ArrayList<String>(0);

					@Override
					public List<String> iterate(TAdvancedRegistrationEntryCareerHistoryAttribute entity,
							IterationContext context) {
						if (entity == null) {
							return retList;
						}

						retList.add(String.valueOf(entity.attributeValue));
						return retList;
					}
				});

		if (valueList == null) {
			return new ArrayList<String>(0);
		}

		return valueList;
	}

	/**
	 * 職歴IDと属性コードから属性値を作成します。(Integer)
	 * @param careerHistoryId 職歴ID
	 * @param attributeCd 属性コード
	 * @return 属性値
	 */
	public Integer selectAttributeValueIntegerByCreateHistoryIdAndAttributeCd(int careerHistoryId, String attributeCd) {
		TAdvancedRegistrationEntryCareerHistoryAttribute entity = createSelectByCareerHistoryIdAndAttributeCd(careerHistoryId, attributeCd).limit(1).getSingleResult();

		if (entity == null) {
			return null;
		}

		return entity.attributeValue;
	}

	/**
	 * 職歴IDと属性コードから属性値を作成します。(String)
	 * @param careerHistoryId 職歴ID
	 * @param attributeCd 属性コード
	 * @return 属性値
	 */
	public String selectAttributeValueStringByCreateHistoryIdAndAttributeCd(int careerHistoryId, String attributeCd) {

		Integer typeValue = selectAttributeValueIntegerByCreateHistoryIdAndAttributeCd(careerHistoryId, attributeCd);

		if (typeValue == null) {
			return "";
		}

		return String.valueOf(typeValue);
	}

	/**
	 * 職歴IDと属性コードからセレクトを作成します。
	 * @param careerHistoryId 職歴ID
	 * @param attributeCd 属性コード
	 * @return セレクト
	 */
	public AutoSelect<TAdvancedRegistrationEntryCareerHistoryAttribute> createSelectByCareerHistoryIdAndAttributeCd(int careerHistoryId, String attributeCd) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistoryAttribute.ADVANCED_REGISTRATION_ENTRY_CAREER_HISTORY_ID), careerHistoryId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistoryAttribute.ATTRIBUTE_CD), attributeCd);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistoryAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(TAdvancedRegistrationEntryCareerHistoryAttribute.class)
							.where(where);
	}

	/**
	 * WEB履歴書履歴IDをキーに属性リストを取得します。
	 * @param advancedRegistrationEntryCareerHistoryId WEB履歴書履歴ID
	 * @return
	 * @throws WNoResultException
	 * @author Yamane
	 */
	public List<TAdvancedRegistrationEntryCareerHistoryAttribute> findByCareerHistoryId(Integer advancedRegistrationEntryCareerHistoryId) throws WNoResultException {

		if (advancedRegistrationEntryCareerHistoryId == null) {
			throw new WNoResultException();
		}
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistoryAttribute.ADVANCED_REGISTRATION_ENTRY_CAREER_HISTORY_ID), advancedRegistrationEntryCareerHistoryId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryCareerHistoryAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return findByCondition(where, TAdvancedRegistrationEntryCareerHistoryAttribute.ID);
	}

	/**
	 * WEB履歴書履歴IDをキーに属性を削除
	 * @param advancedRegistrationEntryCareerHistoryId WEB履歴書履歴ID
	 * @author Yamane
	 */
	public void deleteByCareerHistoryId(Integer advancedRegistrationEntryCareerHistoryId) {

		if (advancedRegistrationEntryCareerHistoryId == null) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TAdvancedRegistrationEntryCareerHistoryAttribute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TAdvancedRegistrationEntryCareerHistoryAttribute.ADVANCED_REGISTRATION_ENTRY_CAREER_HISTORY_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(advancedRegistrationEntryCareerHistoryId)
				.execute();

	}
}
