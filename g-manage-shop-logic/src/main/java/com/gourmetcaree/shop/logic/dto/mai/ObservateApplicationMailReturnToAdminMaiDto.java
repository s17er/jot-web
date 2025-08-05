package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.BaseSysMaiDto;


/**
 * 応募メール送信のお知らせ(管理者宛て)Dto
 * @author Takahoro Ando
 * @version 1.0
 */
public class ObservateApplicationMailReturnToAdminMaiDto extends BaseSysMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3247645312679370818L;

	/** 顧客ID */
	private String customerId;

	/** 顧客名 */
	private String customerName;

	/** 応募ID */
	private String applicationId;

	/** 応募者名 */
	private String applicantName;

	/** メールID */
	private String mailId;

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
	 * 応募IDを取得します。
	 * @return 応募ID
	 */
	public String getApplicationId() {
	    return applicationId;
	}

	/**
	 * 応募IDを設定します。
	 * @param applicationId 応募ID
	 */
	public void setApplicationId(String applicationId) {
	    this.applicationId = applicationId;
	}

	/**
	 * 応募者名を取得します。
	 * @return 応募者名
	 */
	public String getApplicantName() {
	    return applicantName;
	}

	/**
	 * 応募者名を設定します。
	 * @param applicantName 応募者名
	 */
	public void setApplicantName(String applicantName) {
	    this.applicantName = applicantName;
	}

	/**
	 * メールIDを取得します。
	 * @return メールID
	 */
	public String getMailId() {
	    return mailId;
	}

	/**
	 * メールIDを設定します。
	 * @param mailId メールID
	 */
	public void setMailId(String mailId) {
	    this.mailId = mailId;
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
