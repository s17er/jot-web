package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.BaseSysMaiDto;

/**
 * 応募テスト確認完了メールDto
 * @author Makoto Otani
 * @version 1.0
 */
public class AppTestConfMaiDto extends BaseSysMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8917752507958273762L;

	/** 顧客名 */
	private String customerName;

	/** 顧客ID */
	private String customerId;

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


}
