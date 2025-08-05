package com.gourmetcaree.shop.logic.csv;

import java.io.Serializable;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * 質問・店舗見学CSV
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header=true)
public class ObservateApplicationCsv implements Serializable{



	/**
	 *
	 */
	private static final long serialVersionUID = -4830546237619644090L;


	/** 氏名(漢字) */
	@CSVColumn(columnIndex = 0, columnName = "氏名(漢字)")
	public String name;


	/** 氏名(カナ) */
	@CSVColumn(columnIndex = 1, columnName = "氏名(カナ)")
	public String nameKana;


	/** 性別 */
	@CSVColumn(columnIndex = 2, columnName = "性別")
	public String sexKbnStr;


	/** 年令 */
	@CSVColumn(columnIndex = 3, columnName = "年令")
	public String age;


	/** 電話番号 */
	@CSVColumn(columnIndex = 4, columnName = "電話番号")
	public String phoneNo;


	/** PCメールアドレス */
	@CSVColumn(columnIndex = 5, columnName = "PCメールアドレス")
	public String pcMail;


	/** 携帯メールアドレス */
	@CSVColumn(columnIndex = 6, columnName = "携帯メールアドレス")
	public String mobileMail;


	/** 内容 */
	@CSVColumn(columnIndex = 7, columnName = "内容")
	public String contents;



}
