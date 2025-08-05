package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.admin.service.property.ApplicationCsvProperty;
import com.gourmetcaree.common.csv.PreApplicationCsv;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.PreApplicationAttributeService;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.db.common.service.WebIndustryKbnService;
import com.gourmetcaree.db.common.service.WebService;

public class PreApplicationCsvLogic extends AbstractAdminLogic  {

	/** CSVコントローラファクトリ */
	@Resource
	protected S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 応募サービスクラス */
	@Resource
	protected PreApplicationService preApplicationService;

	/** 応募ロジック */
	@Resource
	protected PreApplicationLogic preApplicationLogic;

	/** 応募属性サービス */
	@Resource
	protected PreApplicationAttributeService preApplicationAttributeService;

	/** 名前変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 顧客サービス */
	@Resource
	private CustomerService customerService;

	/** WEBデータサービス */
	@Resource
	private WebService webService;

	/** 会員サービス */
	@Resource
	private MemberService memberService;

	/** WEBデータに設定された店舗の業態サービス */
	@Resource
	private WebIndustryKbnService webIndustryKbnService;

	/**
	 * DeptテーブルのデータをCSV形式で出力する
	 * @param fileName
	 * @throws IOException
	 */
	public void outPutPreApplicationCsv(ApplicationCsvProperty property) throws WNoResultException, ParseException, IOException {

		// 応募データを取得
		List<TPreApplication> entityList = preApplicationLogic.getPreApplicationList(property.map, property.sortKey);

		// CSVエンティティへデータをセット
		List<PreApplicationCsv> csvEntityList = convertCsvData(entityList);

		// CSVを出力
		outPutCsv(csvEntityList, property);

	}

	/**
	 * プレ応募のデータをCSVデータに変換する
	 * @param entityList
	 * @return
	 */
	private List<PreApplicationCsv> convertCsvData(List<TPreApplication> entityList) {

		List<PreApplicationCsv> csvEntityList = new ArrayList<>();
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");

		for (TPreApplication entity : entityList) {
			PreApplicationCsv csv = new PreApplicationCsv();
			Beans.copy(entity, csv).execute();

			// 応募日、応募時間をセット
			csv.applicationDate = date.format(entity.applicationDatetime);
			csv.applicationTime = time.format(entity.applicationDatetime);

			// エリアをセット
			csv.areaName = valueToNameConvertLogic.convertToAreaName(new String[] {String.valueOf(entity.areaCd)});

			// 顧客名をセット
			csv.customerName = customerService.getCustomerName(entity.customerId);

			// 応募先名をセット
			csv.manuscriptName = webService.findById(entity.webId).manuscriptName;

			// 会員名をセット
			csv.memberName = entity.name;

			// 性別をセット
			csv.sex = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, new String[] {String.valueOf(entity.sexKbn)}) + (entity.sexKbn == 3 ? "" : "性");

			// 都道府県をセット
			csv.prefectuerName = valueToNameConvertLogic.convertToPrefecturesName(entity.prefecturesCd);

			// 飲食業界経験をセット
			if(entity.foodExpKbn != null) {
				csv.foodExp = valueToNameConvertLogic.convertToTypeName(MTypeConstants.FoodExpKbn.TYPE_CD, entity.foodExpKbn);
			}

			// 希望年収をセット
			if(entity.salaryKbn != null) {
				csv.salary = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalaryKbn.TYPE_CD, entity.salaryKbn);
			}

			// 転勤をセット
			if(entity.transferFlg != null) {
				csv.transfer = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TransferFlg.TYPE_CD, entity.transferFlg);
			}

			// 深夜勤務をセット
			if(entity.midnightShiftFlg != null) {
				csv.midnightShift = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MidnightShiftFlg.TYPE_CD, entity.midnightShiftFlg);
			}

			// 雇用形態をセット
			csv.employPtn = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, entity.employPtnKbn);

			// 希望職種をセット
			csv.hopeJob = valueToNameConvertLogic.convertToJobName(new String[] {String.valueOf(entity.jobKbn)});

			// 端末区分をセット
			if(entity.terminalKbn != null) {
				csv.terminal = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TerminalKbn.TYPE_CD, entity.terminalKbn);
			}

			csvEntityList.add(csv);
		}

		return csvEntityList;

	}

	/**
	 * CSVを出力する
	 * @param csvEntityList 応募CSVエンティティリスト
	 * @throws IOException
	 */
	private void outPutCsv(List<PreApplicationCsv> csvEntityList, ApplicationCsvProperty property) throws IOException {

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));

		S2CSVWriteCtrl<PreApplicationCsv> csv_writer =
			s2CSVCtrlFactory.getWriteController(PreApplicationCsv.class, out);

		int count = 0;
		for(PreApplicationCsv csvEntity : csvEntityList){

			csv_writer.write(csvEntity);
			count++;
		}

		property.count = count;
		csv_writer.close();
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 * @throws IOException
	 * @throws IOException
	 */
	private static String createOutputFilePath(ApplicationCsvProperty property) {

		String filename = null;
		String dateStr = null;
		filename = property.faileName;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(filename)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}
}