package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.accessor.shoplist.RouteSearchConditionAccessor;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 店舗一覧路線サービス
 * @author Takehiro Nakamori
 *
 */
public class ShopListRouteService extends AbstractGroumetCareeBasicService<TShopListRoute> {

	/**
	 * 店舗一覧IDからエンティティリストを取得します。
	 * @param shopListId 店舗一覧ID
	 * @return エンティティリスト
	 */
	public List<TShopListRoute> findByShopListId(int shopListId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopListRoute.SHOP_LIST_ID), shopListId);
		where.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		try {
			return findByCondition(where, TShopListRoute.DISP_ORDER);

		} catch (WNoResultException e) {
			return new ArrayList<TShopListRoute>();
		}
	}


	/**
	 * {@link RouteSearchConditionAccessor} を条件に店舗一覧IDのリストを取得
	 * @param accessor 条件アクセサ
	 */
	public List<Integer> findShopListIdsByCondtion(RouteSearchConditionAccessor accessor) {
		if (accessor.getRailroadId() == null) {
			return new ArrayList<Integer>(0);
		}
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT ").append(TShopListRoute.SHOP_LIST_ID).append(" FROM ")
			.append(TShopListRoute.TABLE_NAME)
			.append(" WHERE ");

		createRouteSearchWhere(sql, params, accessor);

		return jdbcManager.selectBySql(Integer.class, sql.toString(), params.toArray())
										.getResultList();
	}

	/**
	 * {@link RouteSearchConditionAccessor} を条件に店舗一覧路線を検索
	 * @param accessor 条件アクセサ
	 */
	public List<TShopListRoute> findByCondtion(RouteSearchConditionAccessor accessor) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
			.append(TShopListRoute.TABLE_NAME)
			.append(" WHERE ");

		createRouteSearchWhere(sql, params, accessor);

		return jdbcManager.selectBySql(entityClass, sql.toString(), params.toArray())
							.getResultList();
	}


	private void createRouteSearchWhere(StringBuilder srcWhere, List<Object> params, RouteSearchConditionAccessor accessor) {
		StringBuilder where = new StringBuilder();
		if (CollectionUtils.isNotEmpty(accessor.getShopListIdList())) {
			where.append(TShopListRoute.SHOP_LIST_ID).append(" IN (")
						.append(StringUtils.join(accessor.getShopListIdList(), ","))
						.append(" ) ");
//			params.addAll(accessor.getShopListIdList());
		}

		// 鉄道会社 -> 路線 -> 駅 の順に入れ子にする
		if (accessor.getRailroadId() != null) {
			if (where.length() > 0) {
				where.append(" AND ");
			}

			where.append("  railroad_id = ?     ");
			params.add(accessor.getRailroadId());


			if (accessor.getRouteId() != null) {
				where.append("  AND     route_id = ?  ");
				params.add(accessor.getRouteId());

				addStationFromToCondition(where, params, accessor);
			}
		}
		where.append(" AND delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

		srcWhere.append(where);
	}

	/**
	 * FROM-TO の駅条件を追加
	 */
	private void addStationFromToCondition(StringBuilder where, List<Object> params, RouteSearchConditionAccessor accessor) {
		Integer from = accessor.getStationFromId();
		Integer to = accessor.getStationToIdTo();

		// 両方nullは何もしない。
		if (from == null && to == null) {
			return;
		}

		if (from != null) {
			where.append("       AND station_id >= ?  ");
			params.add(from);
		}
		if (to != null) {
			where.append("       AND station_id <= ? ");
			params.add(to);
		}

	}

	/**
	 * 店舗一覧IDをキーに路線を物理削除
	 * @param shopListId 店舗一覧ID
	 */
	public void deleteByShopListId(int shopListId) {
		StringBuffer sb = new StringBuffer(0);
		sb.append(" DELETE FROM ");
		sb.append(TShopListRoute.TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(TShopListRoute.SHOP_LIST_ID);
		sb.append(" = ?");

		jdbcManager.updateBatchBySql(sb.toString(), Integer.class)
				.params(shopListId)
				.execute();
	}


	@Override
	public int insert(TShopListRoute entity) {
		if (entity.deleteFlg == null) {
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
		}
		return super.insert(entity);
	}
}
