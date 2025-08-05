package com.gourmetcaree.admin.pc.sys.dto.mai;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseSysMaiDto;

/**
 * 掲載確定完了通知メールDto
 * @author Makoto Otani
 * @version 1.0
 */
public class FixedMaiDto extends BaseSysMaiDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1747094569250170481L;

	/** 代理店名 */
	private String companyName;

	/** 原稿番号 */
	private String id;

	/** 顧客名 */
	private String customerName;

	/** 顧客ID */
	private String customerId;

	/**
	 * 会社名を取得します。
	 * @return 会社名
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * 会社名を設定します。
	 * @param companyName 会社名
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 原稿番号を取得します。
	 * @return 原稿番号
	 */
	public String getId() {
		return id;
	}

	/**
	 * 原稿番号を設定します。
	 * @param id 原稿番号
	 */
	public void setId(String id) {
		this.id = id;
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
