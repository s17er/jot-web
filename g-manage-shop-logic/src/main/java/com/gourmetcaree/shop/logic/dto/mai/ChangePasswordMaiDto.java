package com.gourmetcaree.shop.logic.dto.mai;

import com.gourmetcaree.common.dto.FooterMaiDto;

/**
 * パスワード変更完了メールDto
 * @author Makoto Otani
 * @version 1.0
 */
public class ChangePasswordMaiDto extends FooterMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3084682328241234561L;

	/** 顧客名 */
	private String customerName;

	/** PC版パス */
	private String pcPath;

	/** 携帯版パス */
	private String mobilePath;

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
	 * PC版パスを取得します。
	 * @return PC版パス
	 */
	public String getPcPath() {
	    return pcPath;
	}

	/**
	 * PC版パスを設定します。
	 * @param pcPath PC版パス
	 */
	public void setPcPath(String pcPath) {
	    this.pcPath = pcPath;
	}

	/**
	 * 携帯版パスを取得します。
	 * @return 携帯版パス
	 */
	public String getMobilePath() {
	    return mobilePath;
	}

	/**
	 * 携帯版パスを設定します。
	 * @param mobilePath 携帯版パス
	 */
	public void setMobilePath(String mobilePath) {
	    this.mobilePath = mobilePath;
	}

}
