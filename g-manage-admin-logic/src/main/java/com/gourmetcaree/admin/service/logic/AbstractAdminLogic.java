package com.gourmetcaree.admin.service.logic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.db.common.service.dto.ServiceAdminAccessUser;

/**
 *
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
public abstract class AbstractAdminLogic extends AbstractGourmetCareeLogic {

	/**
	 * UserDtoからcompanyIdを取得します。
	 * @return companyId
	 */
	protected String getCompanyId() {
		ServiceAdminAccessUser accessUser = (ServiceAdminAccessUser) getServiceAccessUser();
		return accessUser.getCompanyId();
	}

	/**
	 * UserDtoからauthLevelを取得します。
	 * @return authLevel
	 */
	protected String getAuthLevel() {
		ServiceAdminAccessUser accessUser = (ServiceAdminAccessUser) getServiceAccessUser();
		return accessUser.getAuthLevel();
	}

	/** レスポンスオブジェクト */
	@Resource
	protected HttpServletResponse response;
}
