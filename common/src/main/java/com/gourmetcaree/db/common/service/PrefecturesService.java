package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MPrefectures;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 都道府県マスタのサービスクラスです。
 * @version 1.0
 */
public class PrefecturesService extends AbstractGroumetCareeBasicService<MPrefectures> {

	public List<MPrefectures> findAllList() {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(MPrefectures.class)
				.where(where)
				.getResultList();
	}

	public Map<Integer, String> getAllNameMap() {
		List<MPrefectures> list = findAllList();

		Map<Integer, String> valueMap = new HashMap<>();
		list.stream().forEach(action -> valueMap.put(action.prefecturesCd, action.prefecturesName));

		return valueMap;
	}

 	/**
	 * 都道府県コードから都道府県名を取得します
	 * @param prefectureCode 都道府県コード
	 * @return 都道府県名
	 */
	public String  getName(int prefectureCd) {
		try {
			String prefectureName = findByPrefectureCd(prefectureCd).prefecturesName;
			return StringUtils.isEmpty(prefectureName) ? "" : prefectureName;
		} catch (WNoResultException e) {
			return "";
		}
	}

	/**
	 * 都道府県コードが存在するかチェックします
	 * @param prefectureCd
	 * @return
	 */
	public boolean checkFindByPrefectureCd(int prefectureCd) {
		try {
			findByPrefectureCd(prefectureCd);
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
	public MPrefectures findByPrefectureCd(int prefectureCd) throws WNoResultException {

		try {
			return select().where(
					createNotDeleteWhere()
					.eq(toCamelCase(MPrefectures.PREFECTURES_CD), prefectureCd))
			.disallowNoResult()
			.getSingleResult();

		} catch (NoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 都道府県コード、都道府県名をセットしたMapを取得
	 * @return 取得できない場合は空のMapを返すが、都道府県マスタのためほぼありえない
	 */
	public Map<Integer, String> getCdNameMap() {
		try {
			List<MPrefectures> list = findByCondition(
					createNotDeleteWhere()
					,toCamelCase(MPrefectures.PREFECTURES_CD));
		    Map<Integer, String> map = new HashMap<>();
    		list.stream().forEach(s -> map.put(s.prefecturesCd, s.prefecturesName));
    		return map;

		} catch (WNoResultException e) {
			return new HashMap<>();
		}
	}

}