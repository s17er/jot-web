package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TWebJobAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * WEB職種属性サービス
 *
 */
public class WebJobAttributeService extends AbstractGroumetCareeBasicService<TWebJobAttribute> {

	/**
	 * webJobIdとコードで絞り込み
	 *
	 * @param webJobId
	 * @param attributeCd
	 * @return
	 */
	public List<TWebJobAttribute> findByWebJobIdAndAttributeCd(int webJobId, String attributeCd) {

		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(TWebJobAttribute.WEB_JOB_ID), webJobId)
				.eq(WztStringUtil.toCamelCase(TWebJobAttribute.ATTRIBUTE_CD), attributeCd)
				.eq(WztStringUtil.toCamelCase(TWebJobAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(TWebJobAttribute.class)
				.where(where)
				.getResultList();
	}

	/**
	 * WEB職種IDをキーに物理削除
	 * @param webJobId WEB職種ID
	 */
	public void deleteByWebJobId(int webJobId) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebJobAttribute.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebJobAttribute.WEB_JOB_ID).append(" = ? ");

		// WEB職種IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webJobId)
				.execute();
	}

	/**
	 * 属性値リストを作成します。
	 * @param webJobId 店舗一覧ID
	 * @param attributeCd 属性コード
	 * @return 属性値リスト
	 */
	public List<Integer> createAttributeValueList(int webJobId, String attributeCd) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebJobAttribute.WEB_JOB_ID), webJobId);
		where.eq(WztStringUtil.toCamelCase(TWebJobAttribute.ATTRIBUTE_CD), attributeCd);
		where.eq(WztStringUtil.toCamelCase(TWebJobAttribute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<Integer> valueList;

		valueList = jdbcManager.from(TWebJobAttribute.class)
								.where(where)
								.iterate(new IterationCallback<TWebJobAttribute, List<Integer>>() {
									List<Integer> list = new ArrayList<Integer>();
									@Override
									public List<Integer> iterate(TWebJobAttribute entity, IterationContext context) {
										if (entity != null) {
											list.add(entity.attributeValue);
										}
										return list;
									}
								});

		if (valueList == null) {
			return new ArrayList<Integer>();
		}

		return valueList;
	}
}
