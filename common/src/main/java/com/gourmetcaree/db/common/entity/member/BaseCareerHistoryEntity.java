package com.gourmetcaree.db.common.entity.member;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;

/**
 * WEB履歴書職歴の基底エンティティクラス
 * @author nakamori
 *
 */
@MappedSuperclass
public abstract class BaseCareerHistoryEntity extends AbstractCommonEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -3130611525864750003L;

	/** 会社名 */
	@Column(name = COMPANY_NAME)
	public String companyName;

	/** 勤務期間 */
	@Column(name = WORK_TERM)
	public String workTerm;

	/** 業務内容 */
	@Column(name = BUSINESS_CONTENT)
	public String businessContent;

	/** 客席数・坪数 */
	@Column(name = SEAT)
	public String seat;

	/** 月売上 */
	@Column(name = MONTH_SALES)
	public String monthSales;


	/** 会社名 */
	public static final String COMPANY_NAME = "company_name";

	/** 勤務期間 */
	public static final String WORK_TERM = "work_term";

	/** 業務内容 */
	public static final String BUSINESS_CONTENT = "business_content";

	/** 客席数・坪数 */
	public static final String SEAT = "seat";

	/** 月売上 */
	public static final String MONTH_SALES = "month_sales";
}
