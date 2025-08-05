package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * 事前登録に仮登録をした結果を出力するCSV
 * @author nakamori
 *
 */
@CSVEntity(header=true)
public class AdvancedRegistrationTempSignInCsv {

	@CSVColumn(columnIndex = 0, columnName = "メールアドレス")
	public String mail;

	@CSVColumn(columnIndex = 1, columnName = "端末")
	public String terminall;

	@CSVColumn(columnIndex = 2, columnName = "本登録の有無")
	public String registeredFlg;

	@CSVColumn(columnIndex = 3, columnName = "本登録日時")
	public String registeredDate;

}
