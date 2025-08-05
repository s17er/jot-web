package com.gourmetcaree.db.common.service.dto;

import com.gourmetcaree.db.common.service.dto.ServiceAccessUser;

/**
 * サービスにアクセスする運営者側ユーザのインタフェースです。
 * @author Makoto Otani
 * @version 1.0
 */
public interface ServiceAdminAccessUser extends ServiceAccessUser {

	/**
	 * ユーザ権限を取得します。
	 * @return
	 */
	public String getAuthLevel();

	/**
	 * ユーザ会社を取得します。
	 * @return
	 */
	public String getCompanyId();
}
