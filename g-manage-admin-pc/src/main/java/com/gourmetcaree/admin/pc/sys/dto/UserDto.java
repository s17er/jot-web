package com.gourmetcaree.admin.pc.sys.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.service.dto.ServiceAdminAccessUser;

/**
 * ログインユーザを表すクラスです。<br>
 * このオブジェクトはログイン時に生成され、セッションで管理されます。
 * @author Takahiro Ando
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class UserDto implements ServiceAdminAccessUser, Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6215934970939971615L;

	/** UserDto判別定数(運営側) */
	public static final int ADMIN_DTO = 1;

	/** ユーザID */
	public String userId;

	/** 権限レベル */
	public String authLevel;

	/** 会社 */
	public String company;

	/** 氏名 */
	public String name;

	/** ログインID */
	public String loginId;

	/** 所属会社ID */
	public String companyId;

	/*
	 * (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder
				.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getAuthLevel() {
		return authLevel;
	}

	@Override
	public String getCompanyId() {
		return companyId;
	}
}
