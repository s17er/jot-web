package com.gourmetcaree.shop.logic.csv;

import java.io.Serializable;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * スカウトメール用CSV
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header=true)
public class ScoutMailCsv implements Serializable {

	private static final long serialVersionUID = -2697388002494435071L;

	/** ID */
	@CSVColumn(columnIndex = 0, columnName = "ID")
	public Integer id;


	/** 最終ログイン日 */
	@CSVColumn(columnIndex = 1, columnName = "最終ログイン日")
	public String lastLoginDate;


	/** 住所 */
	@CSVColumn(columnIndex = 2, columnName = "住所")
	public String address;


	/** 年令 */
	@CSVColumn(columnIndex = 3, columnName = "年令")
	public Integer age;


	/** 性別 */
	@CSVColumn(columnIndex = 4, columnName = "性別")
	public String sex;


	/** 取得資格 */
	@CSVColumn(columnIndex = 5, columnName = "取得資格")
	public String qualificationKbnStr;


	/** 飲食業界経験年数（アルバイト含む） */
	@CSVColumn(columnIndex = 6, columnName = "飲食業界経験年数（アルバイト含む）")
	public String foodExpKbnStr;


	/** 海外勤務経験 */
	@CSVColumn(columnIndex = 7, columnName = "海外勤務経験")
	public String foreignWorkFlgStr;


	/** 自己PR */
	@CSVColumn(columnIndex = 8, columnName = "自己PR")
	public String scoutSelfPr;


	/** 希望職種 */
	@CSVColumn(columnIndex = 9, columnName = "希望職種")
	public String jobKbnKbnStr;


	/** 希望業種 */
	@CSVColumn(columnIndex = 10, columnName = "希望業種")
	public String industryKbnStr;


	/** 希望勤務地 */
	@CSVColumn(columnIndex = 11, columnName = "希望勤務地")
	public String webAreaStr;


	/** 希望雇用形態 */
	@CSVColumn(columnIndex = 12, columnName = "希望雇用形態")
	public String employPtnKbnStr;


	/** 転勤の可・不可 */
	@CSVColumn(columnIndex = 13, columnName = "転勤の可・不可")
	public String transferFlgStr;


	/** 深夜勤務の可・不可 */
	@CSVColumn(columnIndex = 14, columnName = "深夜勤務の可・不可")
	public String midnightShiftFlgStr;


	/** 希望年収 */
	@CSVColumn(columnIndex = 15, columnName = "希望年収")
	public String salaryKbnStr;


}
