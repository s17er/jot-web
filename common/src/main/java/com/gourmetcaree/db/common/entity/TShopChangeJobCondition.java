package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータ職種のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_shop_change_job_condition")
public class TShopChangeJobCondition extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_change_job_condition_id_gen")
	@SequenceGenerator(name="t_shop_change_job_condition_id_gen", sequenceName="t_shop_change_job_condition_id_seq", allocationSize=1)
	@Column(name=ID)
	public Integer id;

	/** 店舗ID */
	@Column(name=SHOP_LIST_ID)
	public Integer shopListId;
	/** 雇用形態区分 */
	@Column(name=EMPLOY_PTN_KBN)
	public Integer employPtnKbn;
	/** 職種区分 */
	@Column(name=JOB_KBN)
	public Integer jobKbn;
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
	@JoinColumn(name="shop_list_id")
	public TShopList tShopList;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_change_job_condition";

	/** ID */
	public static final String ID ="id";
	/** 店舗ID */
	public static final String SHOP_LIST_ID = "shop_list_id";
	/** 雇用形態区分 */
	public static final String EMPLOY_PTN_KBN = "employ_ptn_kbn";
	/** 職種区分 */
	public static final String JOB_KBN = "job_kbn";
	/** 給与体系区分 */
	public static final String SALERY_STRUCTURE_KBN = "salery_structure_kbn";
	/** 下限給与金額 */
	public static final String LOWER_SALARY_PRICE = "lower_salary_price";
	/** 上限給与金額 */
	public static final String UPPER_SALARY_PRICE = "upper_salary_price";
	/** 給与 */
	public static final String SALARY = "salary";
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

	/** 店舗エンティティ */
	public static final String T_SHOP_LIST ="t_shop_list";
}