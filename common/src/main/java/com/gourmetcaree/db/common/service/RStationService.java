package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MRStation;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * リニューアル後の駅マスタのサービスクラスです。
 * @version 1.0
 */
public class RStationService extends AbstractGroumetCareeBasicService<MRStation> {

	public List<MRStation> findAllList() {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(MRStation.class)
				.where(where)
				.getResultList();
	}

	public Map<Integer, String> getAllNameMap() {
		List<MRStation> list = findAllList();

		Map<Integer, String> valueMap = new HashMap<>();
		list.stream().forEach(action -> valueMap.put(action.stationCd, action.stationName));

		return valueMap;
	}

	/**
	 * 駅コードから検索をする
	 * @param stationCd 駅コード
	 * @return
	 */
	public MRStation findByStationCd(int stationCd) {

		return jdbcManager.from(MRStation.class)
				.where(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(MRStation.STATION_CD), stationCd)
						.eq(WztStringUtil.toCamelCase(MRStation.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						)
				.getSingleResult();
	}

	/**
	 * 路線コードで駅一覧を取得する
	 * @param lineCd
	 * @return 駅一覧のエンティティ
	 * @throws WNoResultException
	 */
	public List<MRStation> findByLineCd(int lineCd) throws WNoResultException {
		return findByCondition(
				createNotDeleteWhere()
				.eq(toCamelCase(MRStation.LINE_CD), lineCd)
				.eq(toCamelCase(MRStation.E_STATUS), MRStation.IN_OPERATIONAL)	// 運用中
				, toCamelCase(MRStation.E_SORT));
	}
}
