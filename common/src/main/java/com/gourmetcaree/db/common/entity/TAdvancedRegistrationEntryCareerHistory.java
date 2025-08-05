package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEB履歴書職歴のエンティティクラスです。(事前登録用)
 * @version 1.0
 */
@Entity
@Table(name="t_advanced_registration_entry_career_history")
public class TAdvancedRegistrationEntryCareerHistory extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2712880290008835363L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_advanced_registration_entry_career_history_id_gen")
	@SequenceGenerator(name="t_advanced_registration_entry_career_history_id_gen", sequenceName="t_advanced_registration_entry_career_history_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 事前登録エントリID */
	@Column(name = ADVANCED_REGISTRATION_ENTRY_ID)
	public Integer advancedRegistrationEntryId;

	/** 会社名 */
	@Column(name="company_name")
	public String companyName;

	/** 勤務期間 */
	@Column(name="work_term")
	public String workTerm;

	/** 業務内容 */
	@Column(name="business_content")
	public String businessContent;

	/** 客席数・坪数 */
	@Column(name="seat")
	public String seat;

	/** 月売上 */
	@Column(name="month_sales")
	public String monthSales;

	/** 職歴属性エンティティリスト */
	@OneToMany(mappedBy = "tAdvancedRegistrationEntryCareerHistory")
	public List<TAdvancedRegistrationEntryCareerHistoryAttribute> tAdvancedRegistrationEntryCareerHistoryAttributeList;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_advanced_registration_entry_career_history";

	/** ID */
	public static final String ID ="id";

	/** 事前登録エントリID */
	public static final String ADVANCED_REGISTRATION_ENTRY_ID = "advanced_registration_entry_id";

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