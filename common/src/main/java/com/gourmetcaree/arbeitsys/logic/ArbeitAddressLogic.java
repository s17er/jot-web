package com.gourmetcaree.arbeitsys.logic;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.db.common.entity.TShopList;

/**
 * バイト側住所ロジック
 * @author Takehiro Nakamori
 *
 */
public class ArbeitAddressLogic extends AbstractArbeitLogic {

	/** エリアとサブエリアのJOINSQL */
	private static final String AREA_SUBAREA_JOIN_SQL = "SELECT * FROM mst_area AREA INNER JOIN mst_subarea SUB ON AREA.id = SUB.area_id WHERE SUB.id = ? AND AREA.id = ?";


	/**
	 * 都道府県・市区が存在するかどうか
	 * @param entity 店舗一覧
	 * @return 存在すればtrue
	 */
	public boolean existPrefectureMunicipality(TShopList entity) {
		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>();
		createExistPrefectureMunicipality(sql, params, entity);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		return count > 0l;
	}


	/**
	 * 都道府県・市区の存在確認用SQLを作成します。
	 * @param sb SQL文
	 * @param params 条件
	 * @param entity 店舗一覧
	 */
	private static void createExistPrefectureMunicipality(StringBuffer sb, List<Object> params, TShopList entity) {

		sb.append(" SELECT * FROM mst_todouhuken PREF ");
		sb.append(" WHERE PREF.id = ? ");
		sb.append(" AND EXISTS (SELECT * FROM mst_area MUN WHERE MUN.id = ? AND MUN.todouhuken_id = PREF.id) ");

		params.add(entity.arbeitTodouhukenId);
		params.add(entity.arbeitAreaId);
	}



	/**
	 * サブエリアが存在するかどうか
	 * @param entity
	 * @return
	 */
	public boolean existSubArea(TShopList entity) {
		long count = jdbcManager.getCountBySql(AREA_SUBAREA_JOIN_SQL, entity.arbeitSubAreaId, entity.arbeitAreaId);
		return count > 0l;
	}
}
