package com.gourmetcaree.admin.pc.sys.dto.mai;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseSysMaiDto;

/**
 * 掲載依頼完了通知メールDto
 * @author Takahoro Kimura
 * @version 1.0
 */
public class ScoutMailAddMaiDto extends BaseSysMaiDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8809695232072543146L;

	/** 顧客名 */
	private String customerName;

	/** スカウトメール追加数 */
	private String scoutCount;

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
	 * スカウトメール追加数を取得します。
	 * @return スカウトメール追加数
	 */
	public String getScoutCount() {
		return scoutCount;
	}

	/**
	 * スカウトメール追加数を設定します。
	 * @param scoutCount スカウトメール追加数
	 */
	public void setScoutCount(String scoutCount) {
		this.scoutCount = scoutCount;
	}

}
