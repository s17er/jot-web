package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_application_career_history")
public class TApplicationCareerHistory extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1211544517486176895L;

	/** ID */
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_application_career_history_id_gen")
	@SequenceGenerator(name="t_application_career_history_id_gen", sequenceName="t_application_career_history_id_seq", allocationSize=1)
	@Column(name="id")
	@Id
	public Integer id;

	/** 応募ID */
	@Column(name="application_id")
	public Integer applicationId;

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

	/** 応募テーブルのリスト */
    @ManyToOne
    @JoinColumn(name="application_id")
    public TApplication tApplication;

    /** 応募職歴属性エンティティリスト */
	@OneToMany(mappedBy = "tApplicationCareerHistory")
	public List<TApplicationCareerHistoryAttribute> tApplicationCareerHistoryAttributeList;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_application_career_history";

	/** 応募ID */
	public static final String APPLICATION_ID = "application_id";

	/** 会社名 */
	public static final String COMPANY_NAME = "company_name";

	/** 勤務期間 */
	public static final String WORK_TERM = "work_term";

	/** 業務内容 */
	public static final String BUSSINESS_CONTENT = "business_content";

	/** 客席数・坪数 */
	public static final String SEAT = "seat";

	/** 月売上 */
	public static final String MONTH_SALES = "month_sales";

	/** 応募テーブルのリスト */
	public static final String T_APPLICATION = "t_application";



}
