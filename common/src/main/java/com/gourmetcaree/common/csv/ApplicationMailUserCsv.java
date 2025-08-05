package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;


@CSVEntity(header = true)
public class ApplicationMailUserCsv {

    @CSVColumn(columnIndex = 0, columnName = "応募日")
    public String applicationDate;

    @CSVColumn(columnIndex = 1, columnName = "応募時間")
    public String applicationTime;

    @CSVColumn(columnIndex = 2, columnName = "エリア")
    public String areaCdName;

    @CSVColumn(columnIndex = 3, columnName = "氏名")
    public String name;

    @CSVColumn(columnIndex = 4, columnName = "氏名(カナ)")
    public String nameKana;

    @CSVColumn(columnIndex = 5, columnName = "性別")
    public String sexKbnName;

    @CSVColumn(columnIndex = 6, columnName = "年齢")
    public String age;

    @CSVColumn(columnIndex = 7, columnName = "郵便番号")
    public String zipCd;

    @CSVColumn(columnIndex = 8, columnName = "都道府県")
    public String prefecturesCdName;

    @CSVColumn(columnIndex = 9, columnName = "市区町村")
    public String municipality;

    @CSVColumn(columnIndex = 10, columnName = "住所")
    public String address;

    @CSVColumn(columnIndex = 11, columnName = "電話番号")
    public String phoneNo;

    @CSVColumn(columnIndex = 12, columnName = "メールアドレス")
    public String pcMail;

    @CSVColumn(columnIndex = 13, columnName = "希望連絡時間・連絡方法")
    public String connectTime;

    @CSVColumn(columnIndex = 14, columnName = "取得資格")
    public String qualificationName;

    @CSVColumn(columnIndex = 15, columnName = "自己PR")
    public String applicationSelfPr;

    @CSVColumn(columnIndex = 16, columnName = "応募先名（原稿名）")
    public String applicationName;

    @CSVColumn(columnIndex = 17, columnName = "希望雇用形態")
    public String employPtnKbnName;

    @CSVColumn(columnIndex = 18, columnName = "希望職種")
    public String hopeJob;

}