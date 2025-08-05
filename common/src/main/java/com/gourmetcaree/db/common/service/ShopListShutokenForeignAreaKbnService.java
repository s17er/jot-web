package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VShopListShutokenForeignAreaKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 系列店舗の海外エリアビューのサービスクラス
 * @author whizz
 *
 */
public class ShopListShutokenForeignAreaKbnService extends AbstractGroumetCareeBasicService<VShopListShutokenForeignAreaKbn> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ShopListShutokenForeignAreaKbnService.class);

	/**
	 * 顧客IDに紐づく海外エリア区分を取得する
	 * @param customerId
	 * @return 海外エリア区分のリスト。取得できない場合は空のリスト
	 */
	public List<Integer> findByCustomerId(int customerId) {
		try {
			List<VShopListShutokenForeignAreaKbn> list = findByCondition(new SimpleWhere().eq(toCamelCase(VShopListShutokenForeignAreaKbn.CUSTOMER_ID), customerId),
					toCamelCase(VShopListShutokenForeignAreaKbn.SHUTOKEN_FOREIGN_AREA_KBN));
			return list.stream().map(s -> s.shutokenForeignAreaKbn).collect(Collectors.toList());
		} catch (WNoResultException e) {
			// 取得できない場合は空のリストを返す
			return new ArrayList<>(0);
		}
	}
}
