package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.VWebIndustryKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEBデータに設定された店舗の業態サービスクラスです。
 * @version 1.0
 */
public class WebIndustryKbnService extends AbstractGroumetCareeBasicService<VWebIndustryKbn> {

	/**
	 * WEBIDをキーに業態区分のリストを取得
	 * @param webId WEBID
	 */
	public List<Integer> getIndustryKbnList(int webId) {
		try {
			List<VWebIndustryKbn> list = findByCondition(new SimpleWhere().eq(toCamelCase(VWebIndustryKbn.WEB_ID), webId), toCamelCase(VWebIndustryKbn.INDUSTRY_KBN));
			return list.stream().map(s -> s.industryKbn).collect(Collectors.toList());
		} catch (WNoResultException e) {
			return new ArrayList<>();
		}
	}
}