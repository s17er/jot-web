package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.BaseSysMaiDto;


/**
 * 顧客からの問い合わせメールのお知らせ
 * @author Aquarius
 *
 */
public class ContactMaiDto extends BaseSysMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 328802821807990124L;

	/** 顧客ID */
	private String customerId;

	/** 企業名 */
	private String customerName;

	/** お名前 */
	private String contactName;

	/** メールアドレス */
	private String sender;

	/** 電話番号 */
	private String phoneNo;

	/** 内容 */
	private String contents;

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


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
