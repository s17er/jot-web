package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.admin.service.property.IpPhoneHistoryCsvProperty;
import com.gourmetcaree.common.csv.IpPhoneHistoryCsv;

/**
 * IP電話応募用のCSVロジックです
 * @author Makoto Otani
 *
 */
public class IpPhoneHistoryCsvLogic extends AbstractAdminLogic {

	/** CSVコントローラファクトリ */
	@Resource
	protected S2CSVCtrlFactory s2CSVCtrlFactory;

	/**
	 * CSVを出力
	 * @param property
	 */
	public void outPutCsv(List<IpPhoneHistoryCsv> csvList, IpPhoneHistoryCsvProperty property) {
		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property.fileName));
		PrintWriter out = null;
		S2CSVWriteCtrl<IpPhoneHistoryCsv> csvWriter = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));
			csvWriter = s2CSVCtrlFactory.getWriteController(IpPhoneHistoryCsv.class, out);

			int count = 0;
			for (IpPhoneHistoryCsv csv : csvList) {
				csvWriter.write(csv);
				count ++;
			}
			property.count = count;

		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 * @throws IOException
	 * @throws IOException
	 */
	private static String createOutputFilePath(String fileName) {

		String dateStr = null;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(fileName)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}
}
