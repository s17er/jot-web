package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VSummaryRailroadPref;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 鉄道会社と都道府県のコードのサマリviewサービス
 * @author whizz
 *
 */
public class SummaryRailRoadPrefService extends AbstractGroumetCareeBasicService<VSummaryRailroadPref> {


	/**
	 * 都道府県コードで鉄道事業者コードを取得
	 * @param prefCd
	 * @return
	 */
	public List<VSummaryRailroadPref> findByPredCd(int prefCd) {
		try {
			return findByCondition(new SimpleWhere().eq(toCamelCase(VSummaryRailroadPref.PREF_CD), prefCd), toCamelCase(VSummaryRailroadPref.COMPANY_CD));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * 指定した都道府県コードの鉄道会社コードのリストを取得
	 * @param prefCd
	 * @return
	 */
	public List<Integer> getCompanyCdList(int prefCd) {
		List<VSummaryRailroadPref> list = findByPredCd(prefCd);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>(0);
		}
		return list.stream().map(s -> s.companyCd).collect(Collectors.toList());
	}
}
