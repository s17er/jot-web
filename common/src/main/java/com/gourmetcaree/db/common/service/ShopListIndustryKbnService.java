package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VShopListIndustryKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 系列店舗の業態区分集計ビューのサービスクラス
 * @author whizz
 *
 */
public class ShopListIndustryKbnService extends AbstractGroumetCareeBasicService<VShopListIndustryKbn> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ShopListIndustryKbnService.class);

	/**
	 * 顧客IDに紐づく業態区分を取得する
	 * @param customerId
	 * @return 業態区分のリスト。取得できない場合は空のリスト
	 */
	public List<Integer> findByCustomerId(int customerId) {
		List<Integer> industryList = new ArrayList<>(0);
		try {
			List<VShopListIndustryKbn> list = findByCondition(new SimpleWhere().eq(toCamelCase(VShopListIndustryKbn.CUSTOMER_ID), customerId),
					toCamelCase(VShopListIndustryKbn.INDUSTRY_KBN));
			for (VShopListIndustryKbn entity : list) {
				industryList.add(entity.industryKbn);
			}
		} catch (WNoResultException e) {
			// 取得できない場合は空のリストを返す
		}
		return industryList;
	}
}
