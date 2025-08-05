package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 事前登録エントリ属性サービス
 * @author Makoto Otani
 *
 */
public class AdvancedRegistrationEntryAttributeService extends AbstractGroumetCareeBasicService<TAdvancedRegistrationEntryAttribute> {


	/**
	 * 事前登録エントリ属性を削除登録します。
	 * @param advancedRegistrationEntryId 親になる事前登録エントリID
	 * @param advancedRegistrationEntryAttributeList 事前登録エントリ属性エンティティリスト
	 */
	public void deleteInsert(int advancedRegistrationEntryId, List<TAdvancedRegistrationEntryAttribute> advancedRegistrationEntryAttributeList) {

		// 削除
		deleteEntityForadvancedRegistrationEntryId(advancedRegistrationEntryId);

		// リストが空の場合は処理しない
		if (CollectionUtils.isEmpty(advancedRegistrationEntryAttributeList)) return;


		for (TAdvancedRegistrationEntryAttribute entryAttribute : advancedRegistrationEntryAttributeList) {
			// 事前登録エントリIDをセット
			entryAttribute.advancedRegistrationEntryId = advancedRegistrationEntryId;
			// 値を登録
			insert(entryAttribute);
		}
	}

	/**
	 * 事前登録エントリIDに紐付くデータを削除する
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 */
	public void deleteEntityForadvancedRegistrationEntryId(int advancedRegistrationEntryId) {

		// データを削除する
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(TAdvancedRegistrationEntryAttribute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TAdvancedRegistrationEntryAttribute.ADVANCED_REGISTRATION_ENTRY_ID);
		sql.append(" = ? ");

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
			    sql.toString()
				, Integer.class);

		batchUpdate.params(advancedRegistrationEntryId);

		batchUpdate.execute();
	}


	/**
	 * 属性値リストをセレクトします。
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @param attributeCd 属性コード
	 * @return 属性値リスト
	 */
	public List<Integer> selectAttributeValueList(int advancedRegistrationEntryId, String attributeCd) {
		List<Integer> list = createAttributeValueSelect(advancedRegistrationEntryId, attributeCd).getResultList();
		if (list == null) {
			return new ArrayList<Integer>(0);
		}

		return list;
	}


	/**
	 * 事前登録属性値をString[]で取得
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @param attributeCd 属性コード
	 * @return 属性値リスト
	 */
	public String[] selectAttributeValueStringArray(int advancedRegistrationEntryId, String attributeCd) {
		List<String> list = selectAttributeValueStringList(advancedRegistrationEntryId, attributeCd);
		if (CollectionUtils.isEmpty(list)) {
			return new String[0];
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 事前登録属性値をList&lt;String&gt;で取得
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @param attributeCd 属性コード
	 * @return 属性値リスト
	 */
	public List<String> selectAttributeValueStringList(int advancedRegistrationEntryId, String attributeCd) {
		List<String> list =
				createAttributeValueSelect(advancedRegistrationEntryId, attributeCd)
				.iterate(new IterationCallback<Integer, List<String>>() {
					List<String> cbList = new ArrayList<String>();
					@Override
					public List<String> iterate(Integer value, IterationContext context) {
						if (value == null) {
							return cbList;
						}
						cbList.add(String.valueOf(value));
						return cbList;
					}
				});

		if (list == null) {
			return new ArrayList<String>(0);
		}

		return list;
	}

	/**
	 * 属性値をセレクトします。
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @param attributeCd 属性コード
	 * @return 属性値
	 */
	public Integer selectAttributeValue(int advancedRegistrationEntryId, String attributeCd) {
		return createAttributeValueSelect(advancedRegistrationEntryId, attributeCd).getSingleResult();
	}

	/**
	 * 属性値のセレクトを作成します
	 * @param advancedRegistrationEntryId 事前登録エントリID
	 * @param attributeCd 属性コード
	 * @return 属性値のセレクト
	 */
	private SqlSelect<Integer> createAttributeValueSelect(int advancedRegistrationEntryId, String attributeCd) {
		String sql = "SELECT attribute_value FROM t_advanced_registration_entry_attribute WHERE advanced_registration_entry_id = ? AND attribute_cd = ? AND delete_flg = ?";

		return jdbcManager.selectBySql(Integer.class, sql, advancedRegistrationEntryId, attributeCd, DeleteFlgKbn.NOT_DELETED);
	}


	public List<TAdvancedRegistrationEntryAttribute> findByAdvancedRegistrationEntryId(Integer advancedRegistrationEntryId) {
		if (advancedRegistrationEntryId == null) {
			return new ArrayList<TAdvancedRegistrationEntryAttribute>(0);
		}

		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryAttribute.ADVANCED_REGISTRATION_ENTRY_ID), advancedRegistrationEntryId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntryAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(TAdvancedRegistrationEntryAttribute.class)
					.where(where)
					.orderBy(TAdvancedRegistrationEntryAttribute.ID)
					.getResultList();

	}
}
