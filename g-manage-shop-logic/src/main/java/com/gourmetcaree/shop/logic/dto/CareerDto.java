package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;

/**
 * 職歴DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class CareerDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1480213429871590734L;

	/** 会社名 */
	public String companyName;

	/** 勤務期間 */
	public String workTerm;

	/** 職種 */
	public String[] job;

	/** 業態 */
	public String[] industry;

	/** 業務内容 */
	public String businessContent;

	/** 客席数・坪数 */
	public String seat;

	/** 月売上 */
	public String monthSales;

}