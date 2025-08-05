package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * 事前登録用会員のCSVです。
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header=true)
public class AdvancedRegistrationMemberCsv {

	/** 開催年 */
	@CSVColumn(columnIndex = 0, columnName = "開催年")
	public String advancedRegistrationShortName;

	@CSVColumn(columnIndex = 1, columnName = "登録ID")
	public Integer id;

	@CSVColumn(columnIndex = 2, columnName = "登録日時")
	public String registrationDatetimeStr;

	@CSVColumn(columnIndex = 3, columnName = "最終ログイン日時")
	public String lastLoginDatetime;


	/** 会員名 */
	@CSVColumn(columnIndex = 4, columnName = "来場ステータス")
	public String attendedStatus;

	/** 会員名 */
	@CSVColumn(columnIndex = 5, columnName = "氏名（漢字）")
	public String memberName;

	/** 会員名（カナ） */
	@CSVColumn(columnIndex = 6, columnName = "氏名（フリガナ）")
	public String memberNameKana;

	/** ログインメールアドレス */
	@CSVColumn(columnIndex = 7, columnName = "ログインメールアドレス")
	public String loginId;

	/** 携帯メールアドレス */
	@CSVColumn(columnIndex = 8, columnName = "メールアドレス（携帯）")
	public String mobileMail;

	/** 性別 */
	@CSVColumn(columnIndex = 9, columnName = "性別")
	public String sex;

	/** 生年月日 */
	@CSVColumn(columnIndex = 10, columnName = "生年月日")
	public String birthdayStr;

	/** 住所 */
	@CSVColumn(columnIndex = 11, columnName = "住所")
	public String addressStr;

	/** 電話番号 */
	@CSVColumn(columnIndex = 12, columnName = "電話番号")
	public String phoneNo;

	@CSVColumn(columnIndex = 13, columnName = "経歴や資格など、PRがあればご自由にご記入ください")
	public String advancedRegistrationSelfPr;

	@CSVColumn(columnIndex = 14, columnName = "転職先に望むことがあればご自由にご記入ください")
	public String hopeCareerChangeText;

	@CSVColumn(columnIndex = 15, columnName = "現在(前職)の年収")
	public String workSalary;

	@CSVColumn(columnIndex = 16, columnName = "取得資格")
	public String qualificationStr;

	@CSVColumn(columnIndex = 17, columnName = "現在の状況")
	public String currentStatus;

	@CSVColumn(columnIndex = 18, columnName = "事前登録")
	public String advancedRegistrationKbnStr;

	@CSVColumn(columnIndex = 19, columnName = "メルマガ")
	public String advancedMailMagazineReceptionFlgStr;

	@CSVColumn(columnIndex = 20, columnName = "スカウトメール")
	public String scoutMail;

	@CSVColumn(columnIndex = 21, columnName = "希望する業態")
	public String hopeIndustries;

	@CSVColumn(columnIndex = 22, columnName = "希望する職種・ポジション")
	public String hopeJobs;

	@CSVColumn(columnIndex = 23, columnName = "転職希望時期")
	public String hopeCareerChangeTermStr;

	@CSVColumn(columnIndex = 24, columnName = "学校名")
	public String shoolName;

	@CSVColumn(columnIndex = 25, columnName = "学部・学科")
	public String department;

	/** 学校の卒業状況 */
	@CSVColumn(columnIndex = 26, columnName = "状況")
	public String graduationKbnStr;

	@CSVColumn(columnIndex = 27, columnName = "PCメール配信")
	public String pcMailReceiveFlg;

	@CSVColumn(columnIndex = 28, columnName = "モバイルメール配信")
	public String mobileMailReceiveFlg;

	@CSVColumn(columnIndex = 29, columnName = "会員区分")
	public String memberKbn;
}
