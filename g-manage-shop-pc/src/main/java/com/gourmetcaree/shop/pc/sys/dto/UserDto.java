package com.gourmetcaree.shop.pc.sys.dto;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.service.dto.ServiceShopAccessUser;

/**
 * ログインユーザを表すクラスです。<br>
 * このオブジェクトはログイン時に生成され、セッションで管理されます。
 * @author Takahiro Ando
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class UserDto extends BaseDto implements ServiceShopAccessUser {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6215934970939971615L;

	/** UserDto判別定数(店舗側) */
	public static final int SHOP_DTO = 2;

	/** ユーザID(顧客アカウントID) */
	public String userId;

	/** エリアコード */
	public int areaCd;

	/** 顧客ID */
	public int customerId;

	/** 顧客名 */
	public String customerName;

	/** ユーザID（なりすましログインID） */
	public String masqueradeUserId;


	/**
	 * 顧客名を取得します
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}

	/*
	 * （非JavaDoc）
	 * ユーザIDを取得します。
	 * @return ユーザID
	 */
	@Override
	public String getUserId() {

		if (StringUtils.isNotBlank(masqueradeUserId)) {
			return masqueradeUserId;
		}

		return userId;
	}

	/*
	 * （非JavaDoc）
	 * エリアコードを取得します。
	 * @return
	 */
	@Override
	public int getAreaCd() {
		return areaCd;
	}

	/*
	 * （非JavaDoc）
	 * 顧客IDを取得します。
	 */
	@Override
	public int getCustomerCd() {
		return customerId;
	}

	/*
	 * (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder
				.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * なりすましユーザID
	 * @return
	 */
	public String getMasqueradeUserId() {
		return masqueradeUserId;
	}

	/**
	 * なりすましログイン中のフラグを返す。
	 */
	public boolean isMasqueradeFlg() {
		if (StringUtils.isNotBlank(masqueradeUserId)) {
			return true;
		}

		return false;
	}
}
