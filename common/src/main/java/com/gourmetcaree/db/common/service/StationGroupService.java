package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.List;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VStationGroup;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * StationGroupViewのサービスクラスです。
 * @version 1.0
 */
public class StationGroupService extends AbstractGroumetCareeBasicService<VStationGroup> {

	/**
	 * 駅コードから路線コードを取得
	 * @param stationCd
	 * @return 路線コード。取得できない場合はnull
	 */
	public Integer getLineCd(Integer stationCd) {
		try {
			return findByStationCd(stationCd).lineCd;
		} catch (WNoResultException e) {
			return 0;
		}
	}

	/**
	 * 駅コードから鉄道会社コードを取得
	 * @param stationCd
	 * @return 鉄道会社コード。取得できない場合はnull
	 */
	public Integer getCompanyCd(Integer stationCd) {
		try {
			return findByStationCd(stationCd).companyCd;
		} catch (WNoResultException e) {
			return 0;
		}
	}

	/**
	 * 指定した駅コードが存在するかチェックする
	 * @param stationCd
	 * @return
	 */
	public boolean checkStationCd(Integer stationCd) {
		return countRecords(new SimpleWhere().eq(toCamelCase(VStationGroup.STATION_CD), stationCd)) > 0;
	}


	/**
	 * 駅コードからグループデータを取得する
	 * @param stationCd
	 * @return グループデータ
	 * @throws WNoResultException
	 */
	public VStationGroup findByStationCd(Integer stationCd) throws WNoResultException {
		try {
			return select().where(
					new SimpleWhere()
					.eq(toCamelCase(VStationGroup.STATION_CD), stationCd))
			.disallowNoResult()
			.getSingleResult();

		} catch (NoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 都道府県コードと駅名から検索する
	 * @param prefCd
	 * @param stationName
	 * @return
	 * @throws WNoResultException
	 */
	public List<VStationGroup> findByPrefecturesCdAndStationName(String prefCd, String stationName) throws WNoResultException{
		try {
			 return select().where(
					new SimpleWhere()
					.eq(toCamelCase(VStationGroup.PREFECTURES_CD), prefCd)
					.like(toCamelCase(VStationGroup.STATIN_NAME), "%"+stationName+"%"))
			 		.disallowNoResult()
			 		.getResultList();
		} catch (NoResultException e) {
			throw new WNoResultException();
		}
	}
}
