package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

@CSVEntity(header = true)
public class WebdataListCsv {

	@CSVColumn(columnIndex=0, columnName="掲載期間")
	public String postDate;

	@CSVColumn(columnIndex=1, columnName="原稿番号")
	public Integer webId;

	@CSVColumn(columnIndex=2, columnName="原稿名")
	public String manuscriptName;

	@CSVColumn(columnIndex=3, columnName="顧客名")
	public String customerName;

	@CSVColumn(columnIndex=4, columnName="サイズ")
	public String sizeName;

	@CSVColumn(columnIndex=5, columnName="営業担当")
	public String salesName;

	@CSVColumn(columnIndex=6, columnName="募集職種")
	public String employJob;

	@CSVColumn(columnIndex=7, columnName="選択店舗数")
	public String selectShops;

	@CSVColumn(columnIndex=8, columnName="総店舗数")
	public String allShops;

}
