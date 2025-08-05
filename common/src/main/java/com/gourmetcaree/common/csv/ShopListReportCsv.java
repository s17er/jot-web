package com.gourmetcaree.common.csv;

import java.io.Serializable;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * 店舗一覧レポート用CSV(データのインポート/エクスポートは {@link ShopListCsv} を参照)
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header = true)
public class ShopListReportCsv implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7592539256841388885L;

	@CSVColumn(columnName = "ID", columnIndex = 0)
	public Integer id;

	/** 顧客名のこと */
	@CSVColumn(columnName = "企業名", columnIndex = 1)
	public String customerName;

	@CSVColumn(columnName = "会社名", columnIndex = 2)
	public String companyName;

	@CSVColumn(columnName = "営業担当名", columnIndex = 3)
	public String salesName;

	@CSVColumn(columnName = "店舗名", columnIndex = 4)
	public String shopName;

	@CSVColumn(columnName = "業態", columnIndex = 5)
	public String industryName;

	@CSVColumn(columnName = "住所", columnIndex = 6)
	public String address;

	@CSVColumn(columnName = "求人募集（あり・なし）", columnIndex = 7)
	public String jobOfferFlgName;

	@CSVColumn(columnName = "電話番号", columnIndex = 8)
	public String phoneNo;

	@CSVColumn(columnName = "店舗の表示/非表示", columnIndex = 9)
	public String displayFlgName;
}
