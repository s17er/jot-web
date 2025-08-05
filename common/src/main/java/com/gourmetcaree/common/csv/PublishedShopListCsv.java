package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * 掲載中の店舗一覧
 * @author kyamane
 *
 */
@CSVEntity(header=true)
public class PublishedShopListCsv {

	@CSVColumn(columnIndex = 0, columnName = "原稿番号")
	public String webId;

	@CSVColumn(columnIndex = 1, columnName = "原稿名")
	public String webManuscriptName;

	@CSVColumn(columnIndex = 2, columnName = "エリア")
	public String webArea;

	@CSVColumn(columnIndex = 3, columnName = "顧客ID")
	public String customerId;

	@CSVColumn(columnIndex = 4, columnName = "顧客名")
	public String customerName;

	@CSVColumn(columnIndex = 5, columnName = "顧客郵便番号")
	public String customerZipCd;

	@CSVColumn(columnIndex = 6, columnName = "顧客県")
	public String customerPrefectures;

	@CSVColumn(columnIndex = 7, columnName = "顧客住所1")
	public String customerAddress1;

	@CSVColumn(columnIndex = 8, columnName = "顧客住所2")
	public String customerAddress2;

	@CSVColumn(columnIndex = 9, columnName = "電話番号")
	public String customerTelNo;

	@CSVColumn(columnIndex = 10, columnName = "店舗ID")
	public String shopId;

	@CSVColumn(columnIndex = 11, columnName = "店舗名")
	public String shopName;

	@CSVColumn(columnIndex = 12, columnName = "店舗業態1")
	public String shopIndustryKbnName1;

	@CSVColumn(columnIndex = 13, columnName = "店舗業態2")
	public String shopIndustryKbnName2;

	@CSVColumn(columnIndex = 14, columnName = "国内海外")
	public String shopDomesticKbn;

	@CSVColumn(columnIndex = 15, columnName = "都道府県（エリア）")
	public String shopArea;

	@CSVColumn(columnIndex = 16, columnName = "店舗住所")
	public String shopAddress;

	@CSVColumn(columnIndex = 17, columnName = "店舗TEL")
	public String shopTelNo;

	@CSVColumn(columnIndex = 18, columnName = "店舗交通")
	public String shopTransit;

	@CSVColumn(columnIndex = 19, columnName = "店舗テキスト")
	public String shopInformation;

	@CSVColumn(columnIndex = 20, columnName = "店舗定休日")
	public String shopHoliday;

	@CSVColumn(columnIndex = 21, columnName = "店舗営業時間")
	public String shopBusinessHours;

	@CSVColumn(columnIndex = 22, columnName = "店舗席数")
	public String shopSeating;

	@CSVColumn(columnIndex = 23, columnName = "店舗客単価")
	public String shopUnitPrice;

	@CSVColumn(columnIndex = 24, columnName = "バイト連携")
	public String jobOffer;
}
