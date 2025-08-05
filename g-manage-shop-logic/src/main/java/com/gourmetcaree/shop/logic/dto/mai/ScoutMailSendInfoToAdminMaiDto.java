package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.BaseSysMaiDto;


/**
 * スカウトメール送信のお知らせ(管理者宛て)Dto
 * @author Takahoro Kimura
 * @version 1.0
 */
public class ScoutMailSendInfoToAdminMaiDto extends BaseSysMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1882547118260670296L;

	/** 顧客ID */
	private String customerId;

	/** 顧客名 */
	private String customerName;

	/** 会員ID */
	private String memberId;

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

}
