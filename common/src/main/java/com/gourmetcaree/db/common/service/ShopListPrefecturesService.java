package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VShopListPrefectures;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 系列店舗の都道府県ビューのサービスクラス
 * @author whizz
 *
 */
public class ShopListPrefecturesService extends AbstractGroumetCareeBasicService<VShopListPrefectures> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ShopListPrefecturesService.class);

	/**
	 * 顧客IDに紐づく都道府県を取得する
	 * @param customerId
	 * @return 都道府県のリスト。取得できない場合は空のリスト
	 */
	public List<Integer> findByCustomerId(int customerId) {
		try {
			List<VShopListPrefectures> list = findByCondition(new SimpleWhere().eq(toCamelCase(VShopListPrefectures.CUSTOMER_ID), customerId),
					toCamelCase(VShopListPrefectures.PREFECTURES_CD));
			return list.stream().map(s -> s.prefecturesCd).collect(Collectors.toList());
		} catch (WNoResultException e) {
			// 取得できない場合は空のリストを返す
			return new ArrayList<>(0);
		}
	}
}
