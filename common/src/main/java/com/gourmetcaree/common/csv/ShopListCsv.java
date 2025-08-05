package com.gourmetcaree.common.csv;

import java.util.List;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

@CSVEntity(header=true)
public class ShopListCsv{
	@CSVColumn(columnIndex=0, columnName="系列店舗ID")
	public String id;

	@CSVColumn(columnIndex=1, columnName="表示状態")
	public String displayFlg;

	@CSVColumn(columnIndex=2, columnName="※店舗名")
	public String shopName;

	@CSVColumn(columnIndex=3, columnName="※業態【HP表示用】")
	public String industryText;

	@CSVColumn(columnIndex=4, columnName="※業態1")
	public String industryKbn1;

	@CSVColumn(columnIndex=5, columnName="業態2")
	public String industryKbn2;

	@CSVColumn(columnIndex=6, columnName="※キャッチコピー")
	public String catchCopy;

	@CSVColumn(columnIndex=7, columnName="テキスト")
	public String shopInformation;

	@CSVColumn(columnIndex=8, columnName="仕事の特徴")
	public String workCharacteristicKbnArray;

	@CSVColumn(columnIndex=9, columnName="職場")
	public String shopCharacteristicKbnArray;

	@CSVColumn(columnIndex=10, columnName="交通")
	public String transit;

	@CSVColumn(columnIndex=11, columnName="TEL")
	public String csvPhoneNo;

	@CSVColumn(columnIndex=12, columnName="席数(区分)")
	public String seatKbn;

	@CSVColumn(columnIndex=13, columnName="スタッフ")
	public String staff;

	@CSVColumn(columnIndex=14, columnName="客単価(区分)")
	public String salesPerCustomerKbn;

	@CSVColumn(columnIndex=15, columnName="定休日")
	public String holiday;

	@CSVColumn(columnIndex=16, columnName="営業時間")
	public String businessHours;

	@CSVColumn(columnIndex=17, columnName="オープン日(年)")
	public String openDateYear;

	@CSVColumn(columnIndex=18, columnName="オープン日(月)")
	public String openDateMonth;

	@CSVColumn(columnIndex=19, columnName="オープン日備考")
	public String openDateNote;

	@CSVColumn(columnIndex=20, columnName="オープン日表示期限")
	public String openDateLimitDisplayDate;

	@CSVColumn(columnIndex=21, columnName="URL")
	public String url1;

	@CSVColumn(columnIndex=22, columnName="インディードタグ")
	public String tag;

	@CSVColumn(columnIndex=23, columnName="国内外")
	public String domesticKbn;

	@CSVColumn(columnIndex=24, columnName="海外エリア")
	public String shutokenForeignAreaKbn;

	@CSVColumn(columnIndex=25, columnName="海外住所")
	public String foreignAddress;

	@CSVColumn(columnIndex=26, columnName="都道府県コード")
	public String prefecturesCd;

	@CSVColumn(columnIndex=27, columnName="市区コード")
	public String cityCd;

	@CSVColumn(columnIndex=28, columnName="その他住所")
	public String address;

	@CSVColumn(columnIndex=29, columnName="画像名")
	public String mainImg;

	@CSVColumn(columnIndex=30, columnName="受動喫煙対策")
	public String preventSmoke;

	public List<StationGroupCsv> stationGroupList;
}
