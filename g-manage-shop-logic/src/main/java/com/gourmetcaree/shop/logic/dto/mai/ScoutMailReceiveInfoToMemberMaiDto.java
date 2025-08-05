package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.FooterMaiDto;


/**
 * スカウトメール受信のお知らせDto
 * @author Takahoro Kimura
 * @version 1.0
 */
public class ScoutMailReceiveInfoToMemberMaiDto extends FooterMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4722981585918156016L;

	/** 顧客名 */
	private String customerName;

	/** ログインURL */
	private String loginUrl;

	/** スマートフォン用ログインURL */
	private String loginUrlForSmart;

	/** PC/モバイル判別フラグ */
	private boolean mobileMailFlg;

	/**
	 * 顧客名を取得します。
	 * @return 顧客名
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * 顧客名を設定します。
	 * @param customerName 顧客名
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * ログインURLを取得します。
	 * @return ログインURL
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * ログインURLを設定します。
	 * @param loginUrl ログインURL
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * スマートフォン用ログインURLを取得します。
	 * @return ナビゲーション
	 */
	public String getLoginUrlForSmart() {
		return loginUrlForSmart;
	}

	/**
	 * スマートフォン用ログインURLを設定します。
	 * @param navigation ナビゲーション
	 */
	public void setLoginUrlForSmart(String loginUrlForSmart) {
		this.loginUrlForSmart = loginUrlForSmart;
	}

	/**
	 * PC/モバイル判別フラグを取得します。
	 * @return PC/モバイル判別フラグ
	 */
	public boolean getMobileMailFlg() {
		return mobileMailFlg;
	}

	/**
	 * PC/モバイル判別フラグを設定します。
	 * @param mobileMailFlg PC/モバイル判別フラグ
	 */
	public void setMobileMailFlg(boolean mobileMailFlg) {
		this.mobileMailFlg = mobileMailFlg;
	}
}
