package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VSummaryRoutePref;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 路線と都道府県のコードのサマリviewサービス
 * @author whizz
 *
 */
public class SummaryRoutePrefService extends AbstractGroumetCareeBasicService<VSummaryRoutePref> {

	/**
	 * 都道府県コードで鉄道事業者コードを取得
	 * @param prefCd
	 * @return
	 */
	public List<VSummaryRoutePref> findByPredCd(int prefCd) {
		try {
			return findByCondition(new SimpleWhere().eq(toCamelCase(VSummaryRoutePref.PREF_CD), prefCd), toCamelCase(VSummaryRoutePref.LINE_CD));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}
	/**
	 * 指定した都道府県コードの路線コードのリストを取得
	 * @param prefCd
	 * @return
	 */
	public List<Integer> getLineCdList(int prefCd) {
		List<VSummaryRoutePref> list = findByPredCd(prefCd);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>(0);
		}
		return list.stream().map(s -> s.lineCd).collect(Collectors.toList());
	}
}
