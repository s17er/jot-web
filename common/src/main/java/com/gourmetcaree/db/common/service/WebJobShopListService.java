package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWebJobShopList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEB職種店舗サービス
 *
 */
public class WebJobShopListService extends AbstractGroumetCareeBasicService<TWebJobShopList> {
	/**
	 * WEB職種IDで店舗を取得
	 * @param webJobId
	 * @return web職種店舗一覧のリスト。取得できない場合は空のリストを返す
	 */
	public List<TWebJobShopList> findByWebJobId(int webJobId) {
		try {
			return findByCondition(
					new SimpleWhere()
						.eq(toCamelCase(TWebJobShopList.WEB_JOB_ID), webJobId)
						.eq(toCamelCase(TWebJobShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
					toCamelCase(TWebJobShopList.ID));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * WEB職種IDをキーに物理削除
	 * @param webJobId WEB職種ID
	 */
	public void deleteByWebJobId(int webJobId) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebJobShopList.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebJobShopList.WEB_JOB_ID).append(" = ? ");

		// WEB職種IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webJobId)
				.execute();
	}

	/**
	 * 店舗IDをキーに物理削除
	 * @param webJobId 店舗ID
	 */
	public void deleteByShopListId(int shopListid) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebJobShopList.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebJobShopList.SHOP_LIST_ID).append(" = ? ");

		// WEB職種IDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(shopListid)
				.execute();
	}

	/**
	 * 店舗リストで複数物理削除
	 * @param tShopList 店舗エンティティリスト
	 */
	public void deleteByShopList(List<TShopList> tShopList) {
//		リストが空ならなにもしない
		if (CollectionUtils.isEmpty(tShopList)) {
			return;
		}
//		IDのリストを取得
        List<String> shopIdList = tShopList.stream().map(TShopList -> TShopList.id.toString()).collect(Collectors.toList());
//        店舗IDのリストで削除するSQLを作成
		String sql = String.format("DELETE FROM %s WHERE %s IN (%s)",
				TWebJobShopList.TABLE_NAME,
				TWebJobShopList.SHOP_LIST_ID,
				SqlUtils.getQMarks(tShopList.size()));

		List<Class<?>> classes = Lists.newArrayList();
		for (int i = 0; i < shopIdList.size(); i++) {
			classes.add(Integer.class);
		}

		jdbcManager.updateBySql(sql, classes.toArray(new Class<?>[0]))
					.params(shopIdList.toArray())
					.execute();
	}
}
