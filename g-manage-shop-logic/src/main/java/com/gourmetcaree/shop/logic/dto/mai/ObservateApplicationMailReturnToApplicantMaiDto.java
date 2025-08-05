package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.FooterMaiDto;


/**
 * 店舗見学応募メール返信のお知らせ(求職者宛て)Dto
 * @author Takahoro Ando
 * @version 1.0
 */
public class ObservateApplicationMailReturnToApplicantMaiDto extends FooterMaiDto {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4815758968550215251L;

	/** 顧客ID */
	private String customerId;

	/** 顧客名 */
	private String customerName;

	/** 会員ID */
	private String memberId;

	/** ログインURL */
	private String loginUrl;

	/** ナビゲーション */
	private String navigation;

	/** スマートフォン用ログインURL */
	private String loginUrlForSmart;

	/** PC/モバイル判別フラグ */
	private boolean mobileMailFlg;

	/** 店舗見学名 */
	private String observationName;

	/**
	 * 顧客IDを取得します。
	 * @return 顧客ID
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * 顧客IDを設定します。
	 * @param customerId 顧客ID
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

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
	 * 会員IDを取得します。
	 * @return 会員ID
	 */
	public String getMemberId() {
		return memberId;
	}

	/**
	 * 会員IDを設定します。
	 * @param memberId 会員ID
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	 * ナビゲーションを取得します。
	 * @return ナビゲーション
	 */
	public String getNavigation() {
	    return navigation;
	}

	/**
	 * ナビゲーションを設定します。
	 * @param navigation ナビゲーション
	 */
	public void setNavigation(String navigation) {
	    this.navigation = navigation;
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

	/**
	 * 店舗見学名を取得します
	 * @return
	 */
	public String getObservationName() {
		return observationName;
	}

	/**
	 * 店舗見学名を設定します。
	 * @param observationName
	 */
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}
}
