package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEBデータ職種サービス
 *
 */
public class WebJobService extends AbstractGroumetCareeBasicService<TWebJob> {

	/**
	 * WEBデータIDで取得
	 * @param webId
	 * @return web職種一覧のリスト。取得できない場合は空のリストを返す
	 */
	public List<TWebJob> findByWebId(int webId) {
		try {
			return findByCondition(
				new SimpleWhere()
					.eq(toCamelCase(TWebJob.WEB_ID), webId)
					.eq(toCamelCase(TWebJob.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
					SqlUtils.createCommaStr(
							new String[]{
									toCamelCase(TWebJob.DISPLAY_ORDER),
									toCamelCase(TWebJob.ID)
							})
					);
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * WEBデータIDで属性を含めて取得
	 * @param webId
	 * @return web職種一覧のリスト。取得できない場合は空のリストを返す
	 */
	public List<TWebJob> findByWebIdWithAttribute(int webId) {
		try {
			return findByConditionLeftJoin(toCamelCase(TWebJob.T_WEB_JOB_ATTRIBUTE_LIST),
				new SimpleWhere()
					.eq(toCamelCase(TWebJob.WEB_ID), webId)
					.eq(toCamelCase(TWebJob.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
					SqlUtils.createCommaStr(
							new String[]{
									toCamelCase(TWebJob.DISPLAY_ORDER),
									toCamelCase(TWebJob.ID),
									SqlUtils.dot(toCamelCase(TWebJob.T_WEB_JOB_ATTRIBUTE_LIST), toCamelCase(TWebAttribute.ATTRIBUTE_CD)),
									SqlUtils.dot(toCamelCase(TWebJob.T_WEB_JOB_ATTRIBUTE_LIST), toCamelCase(TWebAttribute.ATTRIBUTE_VALUE)),
							})
					);
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * WEBデータIDで属性と店舗を含めて取得
	 * @param webId
	 * @return web職種一覧のリスト。取得できない場合は空のリストを返す
	 */
	public List<TWebJob> findByWebIdWithAttributeAndShopList(int webId) {
		try {
			return findByConditionLeftJoinTwoTables(
					toCamelCase(TWebJob.T_WEB_JOB_ATTRIBUTE_LIST),
					toCamelCase(TWebJob.T_WEB_JOB_SHOP_LIST),
					new SimpleWhere()
						.eq(toCamelCase(TWebJob.WEB_ID), webId)
						.eq(toCamelCase(TWebJob.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
						SqlUtils.createCommaStr(
								new String[]{
										toCamelCase(TWebJob.DISPLAY_ORDER),
										toCamelCase(TWebJob.ID),
										SqlUtils.dot(toCamelCase(TWebJob.T_WEB_JOB_ATTRIBUTE_LIST), toCamelCase(TWebAttribute.ATTRIBUTE_CD)),
										SqlUtils.dot(toCamelCase(TWebJob.T_WEB_JOB_ATTRIBUTE_LIST), toCamelCase(TWebAttribute.ATTRIBUTE_VALUE)),
								})
					);
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * WEBIDをキーに物理削除
	 * @param webJobId WEB職種ID
	 */
	public void deleteByWebId(int webId) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebJob.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebJob.WEB_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webId)
				.execute();
	}
}
