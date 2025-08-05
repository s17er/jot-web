package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

@CSVEntity(header = true)
public class IpPhoneHistoryCsv {

	@CSVColumn(columnIndex=0, columnName="顧客ID")
	public String customerId;

	@CSVColumn(columnIndex=1, columnName="顧客名")
	public String customerName;

	@CSVColumn(columnIndex=2, columnName="原稿番号")
	public String webId;

	@CSVColumn(columnIndex=3, columnName="原稿名")
	public String manuscriptName;

	@CSVColumn(columnIndex=4, columnName="応募日時")
	public String callNoteCaughtMemberTelDatetime;

	@CSVColumn(columnIndex=5, columnName="通話時間")
	public String telTime;

	@CSVColumn(columnIndex=6, columnName="通話ステータス")
	public String telStatusName;

	@CSVColumn(columnIndex=7, columnName="応募者番号")
	public String memberTel;

	@CSVColumn(columnIndex=8, columnName="営業担当")
	public String salesName;

	@CSVColumn(columnIndex=9, columnName="掲載期間（開始）")
	public String postStartDatetime;

	@CSVColumn(columnIndex=10, columnName="掲載期間（終了）")
	public String postEndDatetime;

}
