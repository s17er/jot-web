package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.arbeitsys.entity.MstArea;
import com.gourmetcaree.db.common.entity.TWebShopList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEBデータ系列店舗のサービスクラスです。
 * @version 1.0
 */
public class WebShopListService extends AbstractGroumetCareeBasicService<TWebShopList> {


	/**
	 * 削除登録する
	 * @param webId
	 * @param list
	 */
	public void deleteInsert(int webId, List<TWebShopList> list) {
		// SQLの作成
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(TWebShopList.TABLE_NAME);
		sql.append(" WHERE ");
		sql.append(TWebShopList.WEB_ID).append(" = ? ");

		// WEBIDで物理削除を実行
		jdbcManager.updateBatchBySql(
			    sql.toString(), Integer.class)
				.params(webId)
				.execute();

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (TWebShopList entity : list) {
			if (entity.shopListId == null) {
				continue;
			}
			entity.webId = webId;
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			insert(entity);
		}
	}

	/**
	 * WEBデータIDで系列店舗一覧を取得
	 * @param webId
	 * @return WEBデータに紐づく系列店舗のリスト。取得できない場合は空のリストを返す
	 */
	public List<TWebShopList> findByWebId(int webId) {
		try {
			return findByCondition(
					new SimpleWhere()
						.eq(toCamelCase(TWebShopList.WEB_ID), webId)
						.eq(toCamelCase(TWebShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
					toCamelCase(TWebShopList.ID));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 系列店舗の数を返す
	 * @param webId
	 * @return WEBデータに紐づいている系列店舗の数
	 */
	public int countByWebId(int webId) {
			return (int) countRecords(
					new SimpleWhere()
						.eq(toCamelCase(TWebShopList.WEB_ID), webId)
						.eq(toCamelCase(TWebShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED));
	}

	/**
     * WEBデータIDと店舗IDで系列店舗を取得
     * @param webId
     * @return WEBデータに紐づく系列店舗情報。取得できない場合はnullを返す
     */
    public TWebShopList findByWebIdAndShopId(int webId, int shopId) {
		return jdbcManager.from(TWebShopList.class)
				.where(new SimpleWhere()
					.eq(toCamelCase(TWebShopList.WEB_ID), webId)
					.eq(toCamelCase(TWebShopList.SHOP_LIST_ID), shopId)
					.eq(toCamelCase(TWebShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.disallowNoResult()
				.getSingleResult();
	}

	/**
     * WEBデータIDと店舗IDでインディード出力対象の系列店舗を取得
     * @param webId
	 * @param shopIds
     * @return WEBデータに紐づく系列店舗情報。取得できない場合は空のリストを返す
     */
    public List<TWebShopList> findByByWebIdAndShopIdsAndIndeedFeedFlg(int webId, List<Integer> shopIds) {
		List<TWebShopList> list = jdbcManager.from(TWebShopList.class)
				.where(new SimpleWhere()
					.eq(toCamelCase(TWebShopList.WEB_ID), webId)
					.in(toCamelCase(TWebShopList.SHOP_LIST_ID), shopIds)
					.eq(toCamelCase(TWebShopList.INDEED_FEED_FLG), TWebShopList.INDEED_FEED)
					.eq(toCamelCase(TWebShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.getResultList();
		// 取得結果が空の場合
		if (list == null) {
			return new ArrayList<>(0);
		}

		return list;
	}
}