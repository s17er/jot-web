package com.gourmetcaree.db.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * エリアマスタのサービスクラスです。
 * @version 1.0
 */
public class AreaService extends AbstractGroumetCareeBasicService<MArea> {


	public List<MArea> findAllList() {
		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<MArea> list = jdbcManager
				.from(MArea.class)
				.where(where)
				.getResultList();

		return list;
	}

	public Map<Integer, String> getAllLinkNameMap() {
		List<MArea> list = findAllList();

		Map<Integer, String> valueMap = new HashMap<>();
		list.stream().forEach(action -> valueMap.put(action.areaCd, action.linkName));

		return valueMap;
	}

	/**
	 * エリアコードから値を取得
	 * @param areaCd エリアコード
	 * @return エリアマスタのリスト
	 * @throws WNoResultException データが取得できなかった場合はエラー
	 */
	public List<MArea> getMAreaList(int areaCd) throws WNoResultException {

		// 検索条件の作成
		Where where = new SimpleWhere()
							.eq(StringUtil.camelize(MArea.AREA_CD), areaCd)
							.eq(StringUtil.camelize(MArea.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		// 値を取得して返却
		return findByCondition(where, StringUtil.camelize(MArea.DISPLAY_ORDER));
	}

	/**
	 * エリアコードをキーにエリア名をセットしたMapを返します。<br />
	 * 取得できない場合は空のMapを返します。
	 * @return エリア名を保持したMap
	 */
	public Map<Integer, String> getAreaNameMap() {

		List<MArea> list = findAll();

		Map<Integer, String> map = new HashMap<Integer, String>();
		// 取得できない場合は空のmapを返す
		if (list == null || list.isEmpty()) {
			return map;
		}

		for (MArea entity : list) {
			map.put(entity.areaCd, entity.areaName);
		}
		return map;
	}

	public List<MArea> getAllArea() throws WNoResultException{
		Where where = new SimpleWhere().eq(StringUtil.camelize(MArea.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return findByCondition(where, StringUtil.camelize(MArea.DISPLAY_ORDER));
	}
}