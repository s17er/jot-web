package com.gourmetcaree.common.csv;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;
import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

@CSVEntity(header=true)
public class ShopListJobCsv{
	@CSVColumn(columnIndex=0, columnName="店舗ID")
	public String shopListId;

	@CSVColumn(columnIndex=1, columnName="雇用形態")
	public String employPtnKbn;

	@CSVColumn(columnIndex=2, columnName="職種ID")
	public String jobKbn;

	@CSVColumn(columnIndex=3, columnName="給与体系")
	public String saleryStructureKbn;

	@CSVColumn(columnIndex=4, columnName="給与下限1")
	public String lowerSalaryPrice;

	@CSVColumn(columnIndex=5, columnName="給与上限1")
	public String upperSalaryPrice;

	@CSVColumn(columnIndex=6, columnName="詳細1")
	public String salaryDetail;

	@CSVColumn(columnIndex=7, columnName="備考1")
	public String salary;

	@CSVColumn(columnIndex=8, columnName="給与下限2(年収)")
	public String annualLowerSalaryPrice;

	@CSVColumn(columnIndex=9, columnName="給与上限2(年収)")
	public String annualUpperSalaryPrice;

	@CSVColumn(columnIndex=10, columnName="詳細2")
	public String annualSalaryDetail;

	@CSVColumn(columnIndex=11, columnName="備考2")
	public String annualSalary;

	@CSVColumn(columnIndex=12, columnName="給与下限3(月収)")
	public String monthlyLowerSalaryPrice;

	@CSVColumn(columnIndex=13, columnName="給与上限3(月収)")
	public String monthlyUpperSalaryPrice;

	@CSVColumn(columnIndex=14, columnName="詳細3")
	public String monthlySalaryDetail;

	@CSVColumn(columnIndex=15, columnName="備考3")
	public String monthlySalary;
}
