package com.gourmetcaree.admin.service.logic.shoplist;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.seasar.struts.util.RequestUtil;

import com.gourmetcaree.common.constants.Constants;

public class ShopListCsvLogic {

	public void importCsv() {

		try (Reader reader = new InputStreamReader(
		RequestUtil.getRequest().getInputStream(), Constants.CSV_ENCODING)) {

			List<CSVRecord> recordList = CSVFormat.EXCEL
			.parse(reader)
			.getRecords();


		} catch (IOException e) {

		}
	}
}
