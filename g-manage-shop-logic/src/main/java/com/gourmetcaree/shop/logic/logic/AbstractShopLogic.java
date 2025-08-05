package com.gourmetcaree.shop.logic.logic;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.db.common.service.dto.ServiceShopAccessUser;

/**
 *
 * @author Makoto Otani
 * @version 1.0
 *
 */
public abstract class AbstractShopLogic extends AbstractGourmetCareeLogic {

	/** アプリケーションオブジェクト */
	// XXX バッチのDIに影響があるため、取得方法変更
	@Resource
	protected ServletContext application;

	// XXX バッチのDIに影響があるため、取得方法変更
	@Resource
	protected HttpServletResponse response;

	/**
	 * UserDtoからcustomerIdを取得します。
	 * @return customerId
	 */
	protected int getCustomerId() {
		ServiceShopAccessUser accessUser = (ServiceShopAccessUser) getServiceAccessUser();
		return accessUser.getCustomerCd();
	}

	/**
	 * UserDtoからareaCdを取得します。
	 * @return areaCd
	 */
	protected int getAreaCd() {
		ServiceShopAccessUser accessUser = (ServiceShopAccessUser) getServiceAccessUser();
		return accessUser.getAreaCd();
	}

	/**
	 * 共通プロパティファイルから指定のキーの値を取得します。
	 * @param key キー
	 * @return 値
	 */
	protected String getCommonProperty(String key) {
		return (String) ((Map<?, ?>) application.getAttribute("common")).get(key);
	}
}
