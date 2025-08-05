package com.gourmetcaree.shop.logic.logic;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.shop.logic.dto.RailroadDto;


/**
 * 特集ロジッククラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
public class RailroadLogic extends AbstractShopLogic {


	/**
	 * 鉄道会社の名称を返却します。<br />
	 * プロパティに駅IDをセットして呼び出します。
	 * @param property WEBデータ路線プロパティ
	 * @return WEBデータ路線図DTO
	 */
	public RailroadDto getRouteName(Integer stationId) {

		// プロパティがnullの場合はエラー
		if (stationId == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		List<Object> params = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT");
		sql.append("  ST.station_name");
		sql.append("  , RO.route_name");
		sql.append("  , RA.railroad_name ");
		sql.append("FROM");
		sql.append("  m_station ST ");
		sql.append("  INNER JOIN m_route RO ");
		sql.append("    ON ST.ROute_id = RO.id ");
		sql.append("  INNER JOIN m_railroad RA ");
		sql.append("    ON RO.railroad_id = RA.id ");
		sql.append("WHERE");
		sql.append("      ST.delete_flg = 0");
		sql.append("  AND RO.delete_flg = 0");
		sql.append("  AND RA.delete_flg = 0");
		sql.append("  AND ST.id = ?");

		// 値のセット
		params.add(stationId);

		// 検索実行(IDで検索してるので１件のみ取得)
		return jdbcManager
					.selectBySql(RailroadDto.class, sql.toString(), params.toArray())
					.disallowNoResult()
					.getSingleResult();
	}

	/**
	 * 路線関連の名称を取得します。
	 * @param form WEBデータフォーム
	 * @return 路線Dto
	 */
	public RailroadDto getRailroadDto(Integer stationId) {

		// 名称の取得
		return getRouteName(stationId);
	}
}
