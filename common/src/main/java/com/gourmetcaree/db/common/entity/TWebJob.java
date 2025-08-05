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

/**
 * WEBデータ職種のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_job")
public class TWebJob extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_job_id_gen")
	@SequenceGenerator(name="t_web_job_id_gen", sequenceName="t_web_job_id_seq", allocationSize=1)
	@Column(name=ID)
	public Integer id;

	/** WEBデータID */
	@Column(name=WEB_ID)
	public Integer webId;
	/** 雇用形態区分 */
	@Column(name=EMPLOY_PTN_KBN)
	public Integer employPtnKbn;
	/** 職種区分 */
	@Column(name=JOB_KBN)
	public Integer jobKbn;
	/** 掲載フラグ */
	@Column(name=PUBLICATION_FLG)
	public Integer publicationFlg;
	/** 仕事内容 */
	@Column(name=WORK_CONTENTS)
	public String workContents;
	/** 給与体系区分 */
	@Column(name=SALERY_STRUCTURE_KBN)
	public Integer saleryStructureKbn;
	/** 下限給与金額 */
	@Column(name=LOWER_SALARY_PRICE)
	public String lowerSalaryPrice;
	/** 上限給与金額 */
	@Column(name=UPPER_SALARY_PRICE)
	public String upperSalaryPrice;
	/** 給与 */
	@Column(name=SALARY)
	public String salary;
	/** 求める人物像 */
	@Column(name=PERSON_HUNTING)
	public String personHunting;
	/** 勤務時間 */
	@Column(name=WORKING_HOURS)
	public String workingHours;
	/** 待遇 */
	@Column(name=TREATMENT)
	public String treatment;
	/** 休日休暇 */
	@Column(name=HOLIDAY)
	public String holiday;
	/** 表示順 */
	@Column(name=DISPLAY_ORDER)
	public Integer displayOrder;
	/** 加入保険 */
	@Column(name = INSURANCE)
	public String insurance;
	/** 契約期間 */
	@Column(name = CONTRACT_PERIOD)
	public String contractPeriod;
	/** 給与詳細 */
	@Column(name = SALARY_DETAIL)
	public String salaryDetail;
	/** 想定初年度年収給与下限 */
	@Column(name = ANNUAL_LOWER_SALARY_PRICE)
	public String annualLowerSalaryPrice;
	/** 想定初年度年収給与上限 */
	@Column(name = ANNUAL_UPPER_SALARY_PRICE)
	public String annualUpperSalaryPrice;
	/** 想定初年度年収フリー欄 */
	@Column(name = ANNUAL_SALARY)
	public String annualSalary;
	/** 想定初年度年収給与詳細 */
	@Column(name = ANNUAL_SALARY_DETAIL)
	public String annualSalaryDetail;
	/** 想定初年度月収給与下限 */
	@Column(name = MONTHLY_LOWER_SALARY_PRICE)
	public String monthlyLowerSalaryPrice;
	/** 想定初年度月収給与上限 */
	@Column(name = MONTHLY_UPPER_SALARY_PRICE)
	public String monthlyUpperSalaryPrice;
	/** 想定初年度月収フリー欄 */
	@Column(name = MONTHLY_SALARY)
	public String monthlySalary;
	/** 想定初年度月収給与詳細 */
	@Column(name = MONTHLY_SALARY_DETAIL)
	public String monthlySalaryDetail;

	/** WEBデータエンティティ */
	@ManyToOne
	@JoinColumn(name="web_id")
	public TWeb tWeb;

	/** WEBデータ職種属性エンティティリスト */
	@OneToMany(mappedBy = "tWebJob")
	public List<TWebJobAttribute> tWebJobAttributeList;

	/** WEBデータ職種店舗エンティティリスト */
	@OneToMany(mappedBy = "tWebJob")
	public List<TWebJobShopList> tWebJobShopList;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_job";

	/** ID */
	public static final String ID ="id";
	/** WEBデータID */
	public static final String WEB_ID = "web_id";
	/** 雇用形態区分 */
	public static final String EMPLOY_PTN_KBN = "employ_ptn_kbn";
	/** 職種区分 */
	public static final String JOB_KBN = "job_kbn";
	/** 掲載フラグ */
	public static final String PUBLICATION_FLG = "publication_flg";
	/** 仕事内容 */
	public static final String WORK_CONTENTS = "work_contents";
	/** 給与体系区分 */
	public static final String SALERY_STRUCTURE_KBN = "salery_structure_kbn";
	/** 下限給与金額 */
	public static final String LOWER_SALARY_PRICE = "lower_salary_price";
	/** 上限給与金額 */
	public static final String UPPER_SALARY_PRICE = "upper_salary_price";
	/** 給与 */
	public static final String SALARY = "salary";
	/** 求める人物像 */
	public static final String PERSON_HUNTING = "person_hunting";
	/** 勤務時間 */
	public static final String WORKING_HOURS = "working_hours";
	/** 待遇 */
	public static final String TREATMENT = "treatment";
	/** 休日休暇 */
	public static final String HOLIDAY = "holiday";
	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";
	/** 加入保険 */
	public static final String INSURANCE = "insurance";
	/** 契約期間 */
	public static final String CONTRACT_PERIOD = "contract_period";
	/** 給与詳細 */
	public static final String SALARY_DETAIL = "salary_detail";
	/** 想定初年度年収給与下限 */
	public static final String ANNUAL_LOWER_SALARY_PRICE = "annual_lower_salary_price";
	/** 想定初年度年収給与上限 */
	public static final String ANNUAL_UPPER_SALARY_PRICE = "annual_upper_salary_price";
	/** 想定初年度年収フリー欄 */
	public static final String ANNUAL_SALARY = "annual_salary";
	/** 想定初年度年収給与詳細 */
	public static final String ANNUAL_SALARY_DETAIL = "annual_salary_detail";
	/** 想定初年度月収給与下限 */
	public static final String MONTHLY_LOWER_SALARY_PRICE = "monthly_lower_salary_price";
	/** 想定初年度月収給与上限 */
	public static final String MONTHLY_UPPER_SALARY_PRICE = "monthly_upper_salary_price";
	/** 想定初年度年収フリー欄 */
	public static final String MONTHLY_SALARY = "monthly_salary";
	/** 想定初年度月収給与詳細 */
	public static final String MONTHLY_SALARY_DETAIL = "monthly_salary_detail";


	/** WEBデータエンティティ */
	public static final String T_WEB ="t_web";
	/** WEBデータ職種属性エンティティ */
	public static final String T_WEB_JOB_ATTRIBUTE_LIST ="t_web_job_attribute_list";
	/** WEBデータ職種店舗エンティティ */
	public static final String T_WEB_JOB_SHOP_LIST ="t_web_job_shop_list";
}