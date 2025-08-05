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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.Copy;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.admin.service.property.ApplicationCsvProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ApplicationCsv;
import com.gourmetcaree.common.csv.ObservateApplicationCsv;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.db.common.service.WebIndustryKbnService;
import com.gourmetcaree.db.common.service.WebService;

public class ApplicationCsvLogic extends AbstractAdminLogic  {

	/** CSVコントローラファクトリ */
	@Resource
	protected S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 応募サービスクラス */
	@Resource
	protected ApplicationService applicationService;

	/** 応募ロジック */
	@Resource
	protected ApplicationLogic applicationLogic;

	/** 店舗見学サービス */
	@Resource
	private ObservateApplicationService observateApplicationService;

	/** 応募属性サービス */
	@Resource
	protected ApplicationAttributeService applicationAttributeService;

	/** 名前変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 顧客サービス */
	@Resource
	private CustomerService customerService;

	/** WEBデータサービス */
	@Resource
	private WebService webService;

	/** WEBデータに設定された店舗の業態サービス */
	@Resource
	private WebIndustryKbnService webIndustryKbnService;

	/**
	 * DeptテーブルのデータをCSV形式で出力する
	 * @param fileName
	 * @throws IOException
	 */
	public void outPutApplicationCsv(ApplicationCsvProperty property) throws WNoResultException, ParseException, IOException {

		// 応募データを取得
		List<TApplication> entityList = applicationLogic.getApplicationList(property.map, property.sortKey);

		// CSVエンティティへデータをセット
		List<ApplicationCsv> csvEntityList = convertCsvData(entityList);

		// CSVを出力
		outPutCsv(csvEntityList, property);

	}


	/**
	 * DeptテーブルのデータをCSV形式で出力する
	 * @param fileName
	 * @throws IOException
	 */
	public void outPutObservateApplicationCsv(ApplicationCsvProperty property) throws WNoResultException, ParseException, IOException {

		// 応募データを取得
		List<TObservateApplication> entityList = applicationLogic.getObservateApplicationList(property.map, property.sortKey);

		// CSVエンティティへデータをセット
		List<ObservateApplicationCsv> csvEntityList = convertObservateCsvData(entityList);

		// CSVを出力
		outPutObservateCsv(csvEntityList, property);

	}

	/**
	 * CSVを出力する
	 * @param csvEntityList 応募CSVエンティティリスト
	 * @throws IOException
	 */
	private void outPutObservateCsv(List<ObservateApplicationCsv> csvEntityList, ApplicationCsvProperty property) throws IOException {

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));

		S2CSVWriteCtrl<ObservateApplicationCsv> csv_writer =
			s2CSVCtrlFactory.getWriteController(ObservateApplicationCsv.class, out);

		int count = 0;
		for(ObservateApplicationCsv csvEntity : csvEntityList){

			csv_writer.write(csvEntity);
			count++;
		}

		property.count = count;
		csv_writer.close();

	}

	/**
	 * CSVを出力する
	 * @param csvEntityList 応募CSVエンティティリスト
	 * @throws IOException
	 */
	private void outPutCsv(List<ApplicationCsv> csvEntityList, ApplicationCsvProperty property) throws IOException {

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));

		S2CSVWriteCtrl<ApplicationCsv> csv_writer =
			s2CSVCtrlFactory.getWriteController(ApplicationCsv.class, out);

		int count = 0;
		for(ApplicationCsv csvEntity : csvEntityList){

			csv_writer.write(csvEntity);
			count++;
		}

		property.count = count;
		csv_writer.close();

	}

	/**
	 * 応募CSVエンティティリストを生成
	 * @param entityList 応募エンティティリスト
	 * @return 応募CSVエンティティリスト
	 */
	private List<ApplicationCsv> convertCsvData(List<TApplication> entityList) {

		List<ApplicationCsv> csvEntityList = new ArrayList<ApplicationCsv>();

		for (TApplication entity : entityList) {
			ApplicationCsv csvEntity = new ApplicationCsv();
			Copy copy = Beans.copy(entity, csvEntity);
			if (!ManageAuthLevel.ADMIN.value().equals(getAuthLevel())) {
				copy.excludes("address", "municipality", "name", "nameKana", "pcMail", "mobileMail");
			}

			copy.execute();

			// 応募区分（通常、気になる）
			csvEntity.applicationKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ApplicationKbn.TYPE_CD,
					new String[] { String.valueOf(entity.applicationKbn) });

			// 応募日を変換
			csvEntity.applicationDate = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);

			// 応募時間を変換
			csvEntity.applicationTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.TIME_FORMAT);

			// エリア名へ変換
			csvEntity.areaCdName = valueToNameConvertLogic.convertToAreaName(new String[] {String.valueOf(entity.areaCd)});

			// 入社可能時期名に変換
			String possibleEntryTermKbnName = "";
			if (StringUtils.isNotBlank(entity.possibleEntryTermKbn)) {
				possibleEntryTermKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PossibleEntryTermKbn.TYPE_CD, new String[]{entity.possibleEntryTermKbn});
			}
			csvEntity.possibleEntryTermKbnName = possibleEntryTermKbnName;

			// 取得資格名に変換
			int[] qualificationKbnList = applicationAttributeService.getApplicationAttrValue(entity.id, MTypeConstants.QualificationKbn.TYPE_CD);
			csvEntity.qualificationName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.QualificationKbn.TYPE_CD, GourmetCareeUtil.toIntToStringArray(qualificationKbnList));

			// 顧客名を取得
			if (entity.customerId != null) {
				csvEntity.customerName = customerService.getCustomerName(entity.customerId);
			}
			// 性別名へ変換
			csvEntity.sexKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, new String[] {String.valueOf(entity.sexKbn)});

			// 都道府県名へ変換
			if (entity.prefecturesCd != null) {
				csvEntity.prefecturesCdName = valueToNameConvertLogic.convertToPrefecturesName(new String[]{String.valueOf(entity.prefecturesCd)});
			}

			if (ManageAuthLevel.ADMIN.value().equals(getAuthLevel())) {
				// 市区町村を生成
				csvEntity.municipality = checkNull(entity.municipality);

				// 住所を生成
				csvEntity.address = checkNull(entity.address);

				// 電話番号を生成
				if (StringUtils.isNotEmpty(entity.phoneNo1)) {
					csvEntity.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
				}
			} else {
				csvEntity.municipality = GourmetCareeUtil.toMunicipality(checkNull(entity.municipality));
			}

			// 希望雇用形態を変換
			csvEntity.employPtnKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, new String[]{String.valueOf(entity.employPtnKbn)});

			// 会員区分を変換
			csvEntity.memberFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MemberFlg.TYPE_CD, new String[]{String.valueOf(entity.memberFlg)});

			// 端末区分を変換
			csvEntity.terminalKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TerminalKbn.TYPE_CD, new String[]{String.valueOf(entity.terminalKbn)});

			// 業態を変換
			if (entity.webId != null) {
				try {
					TWeb web = webService.findById(entity.webId);
					List<String> indstryNameList = new ArrayList<>();
					List<Integer> industryKbnList = webIndustryKbnService.getIndustryKbnList(web.id);
					if (CollectionUtils.isNotEmpty(industryKbnList)) {
						for (Integer kbn : industryKbnList) {
							indstryNameList.add(valueToNameConvertLogic.convertToIndustryName(new Integer[]{kbn}));
						}
					}
					if (web.industryKbn1 != null) {
						String name1 = valueToNameConvertLogic.convertToIndustryName(new Integer[]{web.industryKbn1});
						if (StringUtils.isNotEmpty(name1)) {
							indstryNameList.add(name1);
						}
					}
					if (web.industryKbn2 != null) {
						String name2 = valueToNameConvertLogic.convertToIndustryName(new Integer[]{web.industryKbn2});
						if (StringUtils.isNotEmpty(name2)) {
							indstryNameList.add(name2);
						}
					}
					if (web.industryKbn3 != null) {
						String name3 = valueToNameConvertLogic.convertToIndustryName(new Integer[]{web.industryKbn3});
						if (StringUtils.isNotEmpty(name3)) {
							indstryNameList.add(name3);
						}
					}
					csvEntity.industryName = String.join(", ", indstryNameList);

				} catch (SNoResultException e) {
					// なにもしない
				}
			}

			// 生年月日が入っていれば年齢にする（リニューアル後からの対応）
			if (entity.birthday != null) {
				csvEntity.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			}

			// リニューアル後はプルダウン
			if (entity.jobKbn != null) {
				csvEntity.hopeJob = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, entity.jobKbn);
			}

			// 現在の状況
			String currentEmployedSituationKbnName = "";
			if (entity.currentEmployedSituationKbn != null) {
				currentEmployedSituationKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.CurrentEmployedSituationKbn.TYPE_CD, entity.currentEmployedSituationKbn);
			}
			csvEntity.currentEmployedSituationKbnName = currentEmployedSituationKbnName;



			csvEntityList.add(csvEntity);
		}

		return csvEntityList;
	}


	/**
	 * 応募CSVエンティティリストを生成
	 * @param entityList 応募エンティティリスト
	 * @return 応募CSVエンティティリスト
	 */
	private List<ObservateApplicationCsv> convertObservateCsvData(List<TObservateApplication> entityList) {

		List<ObservateApplicationCsv> csvEntityList = new ArrayList<ObservateApplicationCsv>();

		for (TObservateApplication entity : entityList) {
			ObservateApplicationCsv csvEntity = new ObservateApplicationCsv();
			Copy copy = Beans.copy(entity, csvEntity);
			if (!ManageAuthLevel.ADMIN.value().equals(getAuthLevel())) {
				copy.excludes("address", "municipality", "name", "nameKana", "pcMail", "mobileMail");
			}

			copy.execute();




			// 応募日を変換
			csvEntity.applicationDate = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);

			// 応募時間を変換
			csvEntity.applicationTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.TIME_FORMAT);

			csvEntity.observationKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ObservationKbn.TYPE_CD, entity.observationKbn);

			// エリア名へ変換
			csvEntity.areaCdName = valueToNameConvertLogic.convertToAreaName(new String[] {String.valueOf(entity.areaCd)});

			// 顧客名を取得
			if (entity.customerId != null) {
				csvEntity.customerName = customerService.getCustomerName(entity.customerId);
			}
			// 性別名へ変換
			csvEntity.sexKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, new String[] {String.valueOf(entity.sexKbn)});

			if (ManageAuthLevel.ADMIN.value().equals(getAuthLevel())) {
				// 電話番号を生成
				if (StringUtils.isNotEmpty(entity.phoneNo1)) {
					csvEntity.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
				}
			}

			// 会員区分を変換
			csvEntity.memberFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MemberFlg.TYPE_CD, new String[]{String.valueOf(entity.memberFlg)});

			// 端末区分を変換
			csvEntity.terminalKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TerminalKbn.TYPE_CD, new String[]{String.valueOf(entity.terminalKbn)});

			// 生年月日が入っていれば年齢にする（リニューアル後からの対応）
			if (entity.birthday != null) {
				csvEntity.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			}

			// 業態を変換
			if (entity.webId != null) {
				try {
					TWeb web = webService.findById(entity.webId);
					List<String> indstryNameList = new ArrayList<>();
					List<Integer> industryKbnList = webIndustryKbnService.getIndustryKbnList(web.id);
					if (CollectionUtils.isNotEmpty(industryKbnList)) {
						for (Integer kbn : industryKbnList) {
							indstryNameList.add(valueToNameConvertLogic.convertToIndustryName(new Integer[]{kbn}));
						}
					}
					if (web.industryKbn1 != null) {
						String name1 = valueToNameConvertLogic.convertToIndustryName(new Integer[]{web.industryKbn1});
						if (StringUtils.isNotEmpty(name1)) {
							indstryNameList.add(name1);
						}
					}
					if (web.industryKbn2 != null) {
						String name2 = valueToNameConvertLogic.convertToIndustryName(new Integer[]{web.industryKbn2});
						if (StringUtils.isNotEmpty(name2)) {
							indstryNameList.add(name2);
						}
					}
					if (web.industryKbn3 != null) {
						String name3 = valueToNameConvertLogic.convertToIndustryName(new Integer[]{web.industryKbn3});
						if (StringUtils.isNotEmpty(name3)) {
							indstryNameList.add(name3);
						}
					}
					csvEntity.industryName = String.join(", ", indstryNameList);

				} catch (SNoResultException e) {
					// なにもしない
				}
			}

			csvEntityList.add(csvEntity);
		}

		return csvEntityList;
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

	/**
	 * 対象文字列がNUllかどうかチェックする
	 * @param target
	 * @return NULLの場合、""を返す
	 */
	private String checkNull(String target) {

		if (target == null) {
			return "";
		} else {
			return target;
		}

	}
}