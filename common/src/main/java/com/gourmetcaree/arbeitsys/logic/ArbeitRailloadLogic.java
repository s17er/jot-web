package com.gourmetcaree.arbeitsys.logic;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.arbeitsys.entity.AbstractMstRailload;
import com.gourmetcaree.arbeitsys.entity.AbstractMstRoute;
import com.gourmetcaree.arbeitsys.entity.AbstractMstStation;
import com.gourmetcaree.db.common.entity.TShopListRoute;

/**
 * アルバイトの路線ロジック
 * @author Takehiro Nakamori
 *
 */
public class ArbeitRailloadLogic extends AbstractArbeitLogic {

	/**
	 * 路線が存在するかどうか
	 * @param railloadId 鉄道会社ID
	 * @param routeId 路線ID
	 * @param stationId 駅ID
	 * @return 存在すればtrue
	 */
	public boolean existRoute(TShopListRoute tShopListRoute, int pref) {
		StringBuffer sb = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);

		createExistRouteSql(sb, params, tShopListRoute, pref);
		long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());


		return count > 0l;
	}


	/**
	 * 路線が存在するかどうかを調べるためのSQLを作成します。
	 * @param sb SQL文
	 * @param params 条件
	 * @param railloadId 鉄道会社ID
	 * @param routeId 路線ID
	 * @param stationId 駅ID
	 * @param pref 都道府県ID
	 */
	private static void createExistRouteSql(StringBuffer sb, List<Object> params, TShopListRoute tShopListRoute, int pref) {

		// 鉄道会社テーブルを取得
		String railload = AbstractMstRailload.getRailloadTable(pref);
		// 路線テーブルを取得
		String route = AbstractMstRoute.getRailloadTable(pref);
		// 駅テーブルを取得
		String satation = AbstractMstStation.getRailloadTable(pref);

		sb.append(" SELECT * FROM ");
		sb.append(railload.concat(" RL INNER JOIN ").concat(route).concat(" ROUTE "));
		sb.append(" ON RL.id = ? AND ROUTE.id = ? AND ROUTE.railload_id = RL.id ");
		sb.append(" INNER JOIN ".concat(satation).concat(" ST ON ST.id = ? AND ROUTE.id = ST.route_id "));

		params.add(tShopListRoute.railroadId);
		params.add(tShopListRoute.routeId);
		params.add(tShopListRoute.stationId);

	}
}
