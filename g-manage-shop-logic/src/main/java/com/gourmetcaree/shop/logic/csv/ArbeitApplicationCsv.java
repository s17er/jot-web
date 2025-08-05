package com.gourmetcaree.shop.logic.csv;

import java.io.Serializable;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * グルメdeバイト 応募用CSV
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header=true)
public class ArbeitApplicationCsv implements Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = -1576921307461942416L;


	@CSVColumn(columnIndex = 0, columnName = "応募日時")
	public String applicationDatetimeStr;

	@CSVColumn(columnIndex = 1, columnName = "応募ID")
	public String id;

	@CSVColumn(columnIndex = 2, columnName = "掲載店名")
	public String shopName;

	/** 氏名(漢字) */
	@CSVColumn(columnIndex = 3, columnName = "氏名(漢字)")
	public String name;


	/** 氏名(カナ) */
	@CSVColumn(columnIndex = 4, columnName = "氏名(カナ)")
	public String nameKana;


	/** 性別 */
	@CSVColumn(columnIndex = 5, columnName = "性別")
	public String sexKbnStr;


	/** 年令 */
	@CSVColumn(columnIndex = 6, columnName = "年令")
	public String age;


	/** 郵便番号 */
	@CSVColumn(columnIndex = 7, columnName = "郵便番号")
	public String zipCd;


	/** 住所 */
	@CSVColumn(columnIndex = 8, columnName = "住所")
	public String address;


	/** 電話番号 */
	@CSVColumn(columnIndex = 9, columnName = "電話番号")
	public String phoneNo;


	/** メールアドレス */
	@CSVColumn(columnIndex = 10, columnName = "メールアドレス")
	public String mailAddress;


	/** 現在の職業 */
	@CSVColumn(columnIndex = 11, columnName = "現在の職業")
	public String currentJobKbnStr;


	/** 勤務可能時期 */
	@CSVColumn(columnIndex = 12, columnName = "勤務可能時期")
	public String possibleEntryTermKbnStr;


	/** 飲食店勤務の経験 */
	@CSVColumn(columnIndex = 13, columnName = "飲食店勤務の経験")
	public String foodExpKbnStr;


	/** 応募職種 */
	@CSVColumn(columnIndex = 14, columnName = "応募職種")
	public String applicationJob;


	/** 希望連絡時間・連絡方法 */
	@CSVColumn(columnIndex = 15, columnName = "希望連絡時間・連絡方法")
	public String connectionTime;


	/** 自己PR・要望 */
	@CSVColumn(columnIndex = 16, columnName = "自己PR・要望")
	public String applicationSelfPr;



}
