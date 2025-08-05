package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.FooterMaiDto;


/**
 * 店舗見学応募メール返信のお知らせ(顧客宛て)Dto
 * @author Takahoro Ando
 * @version 1.0
 */
public class ObservateApplicationMailReturnToCustomerMaiDto extends FooterMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1540670992604421598L;

	/** 顧客ID */
	private String customerId;

	/** 応募者の名前 */
	private String applicantName;

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
	 * 応募者の名前を取得します。
	 * @return 応募者の名前
	 */
	public String getApplicantName() {
	    return applicantName;
	}

	/**
	 * 応募者の名前を設定します。
	 * @param applicantName 応募者の名前
	 */
	public void setApplicantName(String applicantName) {
	    this.applicantName = applicantName;
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
