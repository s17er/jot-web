package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;


@CSVEntity(header = true)
public class PreApplicationCsv {

    @CSVColumn(columnIndex = 0, columnName = "ID")
    public Integer id;

    @CSVColumn(columnIndex = 1, columnName = "プレ応募日")
    public String applicationDate;

    @CSVColumn(columnIndex = 2, columnName = "プレ応募時間")
    public String applicationTime;

    @CSVColumn(columnIndex = 3, columnName = "エリア")
    public String areaName;

    @CSVColumn(columnIndex = 4, columnName = "顧客名")
    public String customerName;

    @CSVColumn(columnIndex = 5, columnName = "応募先名")
    public String manuscriptName;

    @CSVColumn(columnIndex = 6, columnName = "会員名")
    public String memberName;

    @CSVColumn(columnIndex = 7, columnName = "年齢")
    public String age;

    @CSVColumn(columnIndex = 8, columnName = "性別")
    public String sex;

    @CSVColumn(columnIndex = 9, columnName = "都道府県")
    public String prefectuerName;

    @CSVColumn(columnIndex = 10, columnName = "市区町村")
    public String municipality;

    @CSVColumn(columnIndex = 11, columnName = "飲食業界経験年数")
    public String foodExp;

    @CSVColumn(columnIndex = 12, columnName = "希望年収")
    public String salary;

    @CSVColumn(columnIndex = 13, columnName = "転勤")
    public String transfer;

    @CSVColumn(columnIndex = 14, columnName = "深夜勤務")
    public String midnightShift;

    @CSVColumn(columnIndex = 15, columnName = "雇用形態")
    public String employPtn;

    @CSVColumn(columnIndex = 16, columnName = "希望職種")
    public String hopeJob;

    @CSVColumn(columnIndex = 17, columnName = "希望業態")
    public String hopeIndustry;

    @CSVColumn(columnIndex = 18, columnName = "端末区分")
    public String terminal;


}