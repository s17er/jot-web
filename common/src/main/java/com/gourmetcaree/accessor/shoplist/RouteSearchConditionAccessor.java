package com.gourmetcaree.accessor.shoplist;

import java.util.List;


/**
 * 店舗一覧の路線検索をする検索条件のアクセサ
 * @author nakamori
 *
 */
public interface RouteSearchConditionAccessor {

	/**
	 * 店舗一覧のIDリスト取得
	 */
	List<Integer> getShopListIdList();

	/**
	 * 鉄道会社IDの取得
	 */
	Integer getRailroadId();

	/**
	 * 路線IDの取得
	 */
	Integer getRouteId();

	/**
	 * 駅ID(FROM)の取得
	 */
	Integer getStationFromId();

	/**
	 * 駅ID(TO)の取得
	 */
	Integer getStationToIdTo();
}
