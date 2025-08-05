package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

@CSVEntity(header=true)
public class ArbeitApplicationMailUserCsv {

	@CSVColumn(columnIndex=0,columnName="応募日")
	public String applicationDate;

	@CSVColumn(columnIndex=1,columnName="応募時間")
	public String applicationTime;

	@CSVColumn(columnIndex=2, columnName="業態")
	public String arbeitGyotaiName;

	@CSVColumn(columnIndex=3, columnName="エリア")
	public String areaCd;

	@CSVColumn(columnIndex=4,columnName="氏名")
	public String name;

	@CSVColumn(columnIndex=5,columnName="氏名(カナ)")
	public String nameKana;

	@CSVColumn(columnIndex=6,columnName="性別")
	public String sexKbnName;

	@CSVColumn(columnIndex=7, columnName="年齢")
	public String age;

	@CSVColumn(columnIndex=8, columnName="都道府県")
	public String prefecturesCdName;

	@CSVColumn(columnIndex=9, columnName="市区町村")
	public String municipality;

	@CSVColumn(columnIndex=10, columnName="住所")
	public String address;

	@CSVColumn(columnIndex=11, columnName="電話番号")
	public String phoneNo;

	@CSVColumn(columnIndex=12, columnName="メールアドレス")
	public String mailAddress;

	@CSVColumn(columnIndex=13, columnName="希望連絡時間・連絡方法")
	public String connectionTime;

	@CSVColumn(columnIndex=14, columnName="勤務可能時期")
	public String possibleEntryTermKbnName;

	@CSVColumn(columnIndex=15, columnName="現在の職業")
	public String currentJobKbnName;

	@CSVColumn(columnIndex=16, columnName="飲食店の経験")
	public String foodExpKbnName;

	@CSVColumn(columnIndex=17, columnName="自己PR")
	public String applicationSelfPr;

	@CSVColumn(columnIndex=18, columnName="応募先名（店舗名）")
	public String applicationName;

	@CSVColumn(columnIndex=19, columnName="希望職種")
	public String applicationJob;
}