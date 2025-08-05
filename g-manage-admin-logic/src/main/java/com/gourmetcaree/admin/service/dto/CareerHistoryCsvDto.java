package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;

/**
 * 職歴のCSV情報を保持するクラスです。
 * @author Takahiro Kimura
 */
public class CareerHistoryCsvDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -364666503663996427L;

    /** 会社名 */
    public String companyName;

    /** 勤務期間 */
    public String workTerm;

    /** 職種 */
    public String job;

    /** 職種コード配列 */
    public String[] jobArray;

    /** 業態 */
    public String industry;

    /** 業態コード配列 */
    public String[] industryArray;

    /** 業務内容 */
    public String businessContent;

    /** 客席数・坪数 */
    public String seat;

    /** 月売上 */
    public String monthSales;


}
