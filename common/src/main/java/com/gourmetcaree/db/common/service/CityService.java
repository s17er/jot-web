package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MCity;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 市区町村マスタのサービスクラスです。
 * @version 1.0
 */
public class CityService extends AbstractGroumetCareeBasicService<MCity> {

	public List<MCity> findAllList() {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(MCity.class)
				.where(where)
				.getResultList();
	}

	public Map<String, String> getAllNameMap() {
		List<MCity> list = findAllList();

		Map<String, String> valueMap = new HashMap<>();
		list.stream().forEach(action -> valueMap.put(action.cityCd, action.cityName));

		return valueMap;
	}

	/**
	 * 市区町村コードから市区町村名を取得
	 * @param cityCd
	 * @return 市区町村名。取得できない場合はブランク
	 */
	public String getName(String cityCd) {
		try {
			String cityName = findByCityCd(cityCd).cityName;
			return StringUtils.isEmpty(cityName) ? "" : cityName;
		} catch (WNoResultException e) {
			return "";
		}
	}


	/**
	 * 市区町村コードから都道府県コードを取得
	 * @param cityCd 市区町村コード
	 * @return 都道府県コード。取得できない場合はnull
	 */
	public Integer getPrefectureCd(String cityCd) {
		try {
			Integer prefectureCd = findByCityCd(cityCd).prefecturesCd;
			return prefectureCd;
		} catch (WNoResultException e) {
			return null;
		}
	}

	/**
	 * 市区町村コードが存在するかチェックします
	 * @param cityCd
	 * @return
	 */
	public boolean checkFindByCityCd(String cityCd) {
		try {
			findByCityCd(cityCd);
			return true;
		}catch(WNoResultException e) {
			return false;
		}
	}


	/**
	 * 市区町村コードでエンティティを取得
	 * @param cityCd
	 * @return 市区町村エンティティ
	 * @throws WNoResultException
	 */
	public MCity findByCityCd(String cityCd) throws WNoResultException {

		try {
			return select().where(
					createNotDeleteWhere()
					.eq(toCamelCase(MCity.CITY_CD), cityCd))
			.disallowNoResult()
			.getSingleResult();

		} catch (NoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 対象都道府県の市区町村を取得
	 * @param prefecturesCd 都道府県コード
	 * @return 市区町村エンティティリスト
	 * @throws WNoResultException
	 */
	public List<MCity> findByPrefecturesCd(Integer prefecturesCd) throws WNoResultException {
		return findByCondition(
				createNotDeleteWhere()
					.eq(toCamelCase(MCity.PREFECTURES_CD), prefecturesCd)
				, toCamelCase(MCity.DISPLAY_ORDER));
	}
}
