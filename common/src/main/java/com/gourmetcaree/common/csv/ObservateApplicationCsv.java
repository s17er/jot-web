package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;


@CSVEntity(header=true)
public class ObservateApplicationCsv {

	@CSVColumn(columnIndex=0,columnName="ID")
	public Integer id;

	@CSVColumn(columnIndex=1, columnName="お問い合わせの種類")
	public String observationKbnName;

	@CSVColumn(columnIndex=2,columnName="応募日")
	public String applicationDate;

	@CSVColumn(columnIndex=3,columnName="応募時間")
	public String applicationTime;

	@CSVColumn(columnIndex=4, columnName="業態")
	public String industryName;

	@CSVColumn(columnIndex=5,columnName="エリア")
	public String areaCdName;

	@CSVColumn(columnIndex=6,columnName="顧客名")
	public String customerName;

	@CSVColumn(columnIndex=7,columnName="氏名")
	public String name;

	@CSVColumn(columnIndex=8,columnName="氏名(カナ)")
	public String nameKana;

	@CSVColumn(columnIndex=9,columnName="性別")
	public String sexKbnName;

	@CSVColumn(columnIndex=10, columnName="年齢")
	public String age;

	@CSVColumn(columnIndex=11, columnName="電話番号")
	public String phoneNo;

	@CSVColumn(columnIndex=12, columnName="メールアドレス")
	public String pcMail;

	@CSVColumn(columnIndex=13, columnName="携帯メールアドレス")
	public String mobileMail;

	@CSVColumn(columnIndex=14, columnName="内容")
	public String contents;

	@CSVColumn(columnIndex=15, columnName="応募先")
	public String applicationName;

	@CSVColumn(columnIndex=16, columnName="会員登録")
	public String memberFlgName;

	@CSVColumn(columnIndex=17, columnName="端末")
	public String terminalKbnName;

}