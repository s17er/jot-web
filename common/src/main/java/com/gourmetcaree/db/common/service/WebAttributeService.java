package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.util.SqlUtils;
import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

import freemarker.template.utility.StringUtil;

/**
 * WEBデータ属性のサービスクラスです。
 * @version 1.0
 */
public class WebAttributeService extends AbstractGroumetCareeBasicService<TWebAttribute> {


	/**
	 * 属性コードを指定して属性値の配列を取得します。
	 * @param webId
	 * @param attributeCd
	 * @return
	 */
	public int[] getWebAttributeValueArray(int webId, String attributeCd) {

		try {
			List<Integer> retList = new ArrayList<Integer>();

			SimpleWhere where = getWhereByAttributeCd(webId, attributeCd);
			List<TWebAttribute> list = findByCondition(where, asc(toCamelCase(TWebAttribute.ATTRIBUTE_VALUE)));

			for (TWebAttribute entity : list) {
				retList.add(entity.attributeValue);
			}

			return ArrayUtils.toPrimitive(retList.toArray(new Integer[0]));

		} catch (WNoResultException e) {
			//未取得の場合は空を返す
			return new int[0];
		}
	}

	/**
	 * 属性コードを指定して属性値のリストを取得します。
	 * @param webId
	 * @param attributeCd
	 * @return
	 */
	public List<String> getWebAttributeValueList(int webId, String attributeCd) {

		AutoSelect<TWebAttribute> select = getWebAttributeValueSelect(webId, attributeCd);
		List<String> list = select.iterate(
				new IterationCallback<TWebAttribute, List<String>>() {
					List<String> valueList = new ArrayList<String>();
					@Override
					public List<String> iterate(TWebAttribute entity, IterationContext context) {
						if (entity != null) {
							valueList.add(String.valueOf(entity.attributeValue));
						}
						return valueList;
					}
				});

		if (list == null) {
			return new ArrayList<String>();
		}

		return list;
	}


	/**
	 * 属性コードを指定して属性値のリストを取得します。
	 * @param webId
	 * @param attributeCd
	 * @return
	 */
	public List<Integer> getWebAttributeValueIntegerList(int webId, String attributeCd) {

		AutoSelect<TWebAttribute> select = getWebAttributeValueSelect(webId, attributeCd);
		List<Integer> list = select.iterate(
				new IterationCallback<TWebAttribute, List<Integer>>() {
					List<Integer> valueList = new ArrayList<Integer>();
					@Override
					public List<Integer> iterate(TWebAttribute entity, IterationContext context) {
						if (entity != null) {
							valueList.add(entity.attributeValue);
						}
						return valueList;
					}
				});

		if (list == null) {
			return new ArrayList<Integer>();
		}

		return list;
	}

	/**
	 * 属性コードを指定して属性値のオートセレクトを取得します。
	 * @param webId
	 * @param attributeCd
	 * @return
	 */
	private AutoSelect<TWebAttribute> getWebAttributeValueSelect(int webId, String attributeCd) {
		return jdbcManager.from(TWebAttribute.class)
					.where(getWhereByAttributeCd(webId, attributeCd))
					.orderBy(asc(toCamelCase(TWebAttribute.ATTRIBUTE_VALUE)));

	}

	/**
	 * 属性コードを指定したWebデータ属性テーブル用の検索条件です。
	 * @param webId
	 * @param attributeCd
	 * @return
	 */
	private SimpleWhere getWhereByAttributeCd(int webId, String attributeCd) {
		SimpleWhere where = new SimpleWhere()
					.eq(toCamelCase(TWebAttribute.WEB_ID), webId)
					.eq(toCamelCase(TWebAttribute.ATTRIBUTE_CD), attributeCd)
					.eq(toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		return where;
	}

	/**
	 * WEBIDをキーにWEBデータ属性を物理削除
	 * @param webId WEBID
	 */
	public void deleteTWebAttributeByWebId(int webId) {

		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebAttribute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebAttribute.WEB_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webId)
				.execute();
	}


	/**
	 * 指定した条件でサロゲートキー「id」のリストを取得します。
	 * @param webId
	 * @return
	 */
	public List<IdSelectDto> getIdList(int webId) {

		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT id FROM t_web_attribute WHERE web_id = ? AND delete_flg = ? ";

		params.add(webId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return getIdList(sql, params);
	}

	/**
	 * 属性値のリストを取得
	 * @param webId WEB_ID
	 * @param attributeCd 属性コード
	 */
	public List<Integer> getValueList(Integer webId, String attributeCd) {
		if (webId == null || attributeCd == null) {
			return new ArrayList<Integer>(0);
		}

		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebAttribute.WEB_ID), webId)
			.eq(WztStringUtil.toCamelCase(TWebAttribute.ATTRIBUTE_CD), attributeCd)
			.eq(WztStringUtil.toCamelCase(TWebAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<TWebAttribute> list = jdbcManager.from(TWebAttribute.class)
												.eager(TWebAttribute.ATTRIBUTE_VALUE)
												.where(where)
												.getResultList();

		List<Integer> valueList = new ArrayList<Integer>(list.size());
		for (TWebAttribute entity : list) {
			valueList.add(entity.attributeValue);
		}
		return valueList;
	}


	/**
	 * 属性コードと属性値をキーにWebデータIDのリストを取得
	 * 属性検索でEXISTSを指定するより、これでWEBデータIDを絞り込んで、IN指定するほうが早くなる
	 * @param attributeCd 属性コード
	 * @param attributeValues 属性値
	 * @return 属性コード・キーに対応するWEBデータIDリスト
	 */
	public List<Integer> findWebIdsByAttributeCdAndAttributeValues(String attributeCd,
																   Collection<Integer> attributeValues) {

		if (CollectionUtils.isEmpty(attributeValues)) {
			return Collections.emptyList();
		}

		String sql = String.format("SELECT %s FROM %s WHERE %s = ? AND %s IN (%s) AND %s = ?",
				TWebAttribute.WEB_ID,
				TWebAttribute.TABLE_NAME,
				TWebAttribute.ATTRIBUTE_CD,
				TWebAttribute.ATTRIBUTE_VALUE,
				SqlUtils.getQMarks(attributeValues.size()),
				TWebAttribute.DELETE_FLG);

		List<Object> params = Lists.newArrayList();
		params.add(attributeCd);
		params.addAll(attributeValues);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.selectBySql(Integer.class, sql, params.toArray())
				.getResultList();

	}
}