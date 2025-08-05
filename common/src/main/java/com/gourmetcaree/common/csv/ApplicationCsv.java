package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;


@CSVEntity(header = true)
public class ApplicationCsv {

    @CSVColumn(columnIndex = 0, columnName = "ID")
    public Integer id;

    @CSVColumn(columnIndex = 1, columnName = "応募日")
    public String applicationDate;

    @CSVColumn(columnIndex = 2, columnName = "応募時間")
    public String applicationTime;

    @CSVColumn(columnIndex = 3, columnName = "応募区分")
    public String applicationKbnName;

    @CSVColumn(columnIndex = 4, columnName = "顧客ID")
    public Integer customerId;

    @CSVColumn(columnIndex = 5, columnName = "原稿ID")
    public Integer webId;

    @CSVColumn(columnIndex = 6, columnName = "業態")
    public String industryName;

    @CSVColumn(columnIndex = 7, columnName = "エリア")
    public String areaCdName;

    @CSVColumn(columnIndex = 8, columnName = "顧客名")
    public String customerName;

    @CSVColumn(columnIndex = 9, columnName = "氏名")
    public String name;

    @CSVColumn(columnIndex = 10, columnName = "氏名(カナ)")
    public String nameKana;

    @CSVColumn(columnIndex = 11, columnName = "性別")
    public String sexKbnName;

    @CSVColumn(columnIndex = 12, columnName = "年齢")
    public String age;

    @CSVColumn(columnIndex = 13, columnName = "郵便番号")
    public String zipCd;

    @CSVColumn(columnIndex = 14, columnName = "都道府県")
    public String prefecturesCdName;

    @CSVColumn(columnIndex = 15, columnName = "市区町村")
    public String municipality;

    @CSVColumn(columnIndex = 16, columnName = "住所")
    public String address;

    @CSVColumn(columnIndex = 17, columnName = "電話番号")
    public String phoneNo;

    @CSVColumn(columnIndex = 18, columnName = "メールアドレス")
    public String pcMail;

    @CSVColumn(columnIndex = 19, columnName = "携帯メールアドレス")
    public String mobileMail;

    @CSVColumn(columnIndex = 20, columnName = "希望連絡時間・連絡方法")
    public String connectTime;

    @CSVColumn(columnIndex = 21, columnName = "入社可能時期")
    public String possibleEntryTermKbnName;

    @CSVColumn(columnIndex = 22, columnName = "現在の状況")
    public String currentEmployedSituationKbnName;

    @CSVColumn(columnIndex = 23, columnName = "取得資格")
    public String qualificationName;

    @CSVColumn(columnIndex = 24, columnName = "自己PR")
    public String applicationSelfPr;

    @CSVColumn(columnIndex = 25, columnName = "応募先名（原稿名）")
    public String applicationName;

    @CSVColumn(columnIndex = 26, columnName = "希望雇用形態")
    public String employPtnKbnName;

    @CSVColumn(columnIndex = 27, columnName = "希望職種")
    public String hopeJob;

    @CSVColumn(columnIndex = 28, columnName = "会員登録")
    public String memberFlgName;

    @CSVColumn(columnIndex = 29, columnName = "端末")
    public String terminalKbnName;


}