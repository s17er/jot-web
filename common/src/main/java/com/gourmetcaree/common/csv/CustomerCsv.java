package com.gourmetcaree.common.csv;

import java.io.Serializable;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * 顧客CSV
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header = true)
public class CustomerCsv implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 3436345859982742656L;

	@CSVColumn(columnIndex = 0, columnName = "顧客ID")
	public Integer id;

	@CSVColumn(columnIndex = 1, columnName = "登録日")
	public String registrationDatetimeStr;

	@CSVColumn(columnIndex = 2, columnName = "顧客名")
	public String customerName;

	@CSVColumn(columnIndex = 3, columnName = "顧客名（カナ）")
	public String customerNameKana;

	@CSVColumn(columnIndex = 4, columnName = "担当者名")
	public String contactName;

	@CSVColumn(columnIndex = 5, columnName = "担当者名（カナ）")
	public String contactNameKana;

	@CSVColumn(columnIndex = 6, columnName = "担当者部署")
	public String contactPost;

	@CSVColumn(columnIndex = 7, columnName = "郵便番号")
	public String zipCd;

	@CSVColumn(columnIndex = 8, columnName = "住所")
	public String address;

	@CSVColumn(columnIndex = 9, columnName = "電話番号")
	public String telNo;

	@CSVColumn(columnIndex = 10, columnName = "FAX番号")
	public String faxNo;

	@CSVColumn(columnIndex = 11, columnName = "メインアドレス")
	public String mainMail;

	@CSVColumn(columnIndex = 12, columnName = "サブアドレス")
	public String subMail;

	@CSVColumn(columnIndex = 13, columnName = "担当会社 / 営業担当者")
	public String companySales;

	@CSVColumn(columnIndex = 14, columnName = "ログインID")
	public String loginId;

	@CSVColumn(columnIndex = 15, columnName = "備考")
	public String note;

	@CSVColumn(columnIndex = 16, columnName = "ログイン可否")
	public String loginFlgStr;

	@CSVColumn(columnIndex = 17, columnName = "掲載可否")
	public String publicationFlgStr;

	@CSVColumn(columnIndex = 18, columnName = "スカウトメール使用可否")
	public String scoutUseFlgStr;

	@CSVColumn(columnIndex = 19, columnName = "スカウトメール")
	public String scoutMailCountStr;

	@CSVColumn(columnIndex = 20, columnName = "スカウトメール追加履歴")
	public String scoutMailAddHistory;

	@CSVColumn(columnIndex = 21, columnName = "未読応募メール数")
	public Integer unreadApplicationMailCount;

	@CSVColumn(columnIndex = 22, columnName = "未読スカウトメール数")
	public Integer unReadScoutMailCount;

	@CSVColumn(columnIndex = 23, columnName = "最終ログイン日時")
	public String lastLoginDatetimeStr;

	@CSVColumn(columnIndex = 24, columnName = "掲載終了後のページ表示")
	public String publicationEndDisplayFlgStr;

	@CSVColumn(columnIndex = 25, columnName = "メールマガジン")
	public String mailMagazineReceptionFlgStr;

	@CSVColumn(columnIndex = 26, columnName = "顧客分類 ")
	public String testFlgStr;




}
