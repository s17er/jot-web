package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.FooterMaiDto;


/**
 * スカウトメール返信のお知らせ送信Dto
 * @author Takahoro Kimura
 * @version 1.0
 */
public class ScoutMailSendInfoToCustomerMaiDto extends FooterMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8442398445527235542L;

	/** 会員ID */
	private String memberId;

	/** 顧客名 */
	private String customerName;

	/**
	 * 会員IDを取得します
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
	 * 顧客名を取得
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * 顧客名を設定
	 * @param customerName
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


}
