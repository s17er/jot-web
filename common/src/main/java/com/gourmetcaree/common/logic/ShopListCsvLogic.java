package com.gourmetcaree.common.logic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.common.csv.ShopListArbeitCsv;
import com.gourmetcaree.common.csv.ShopListCsv;
import com.gourmetcaree.common.csv.ShopListJobCsv;
import com.gourmetcaree.common.csv.StationGroupCsv;
import com.gourmetcaree.common.dto.CopyShopListCsvDto;
import com.gourmetcaree.common.dto.CopyShopListJobCsvDto;
import com.gourmetcaree.common.parser.csv.FromCsvParser;
import com.gourmetcaree.common.property.ShopListCsvProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.CustomerImageService;
import com.gourmetcaree.db.common.service.PrefecturesService;
import com.gourmetcaree.db.common.service.ShopListAttributeService;
import com.gourmetcaree.db.common.service.ShopListLineService;
import com.gourmetcaree.db.common.service.ShopListMaterialService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.ShopListTagMappingService;
import com.gourmetcaree.db.common.service.StationGroupService;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 店舗一覧CSV用ロジックです。
 * @author Takehiro Nakamori
 *
 */
public class ShopListCsvLogic extends AbstractGourmetCareeLogic {

	/** ログオブジェクト */
	private final static Logger log = Logger.getLogger(ShopListCsvLogic.class);

	/** 空の電話番号 */
	private static final String BLANK_PHONE_NO = "000-0000-0000";

	/** 緯度経度をコピーするパラメータ */
	private static final String[] LAT_LNG_COPY_PARAMS = new String[] {"longitude", "latitude"};

	/** バイト用カラム */
	private static final String[] ARBEIT_COLUMNS = new String[] {
		"arbeitJob", "arbeitHourSalary", "arbeitWorkingHour", "arbeitTreatment",
		"arbeitRiseSalaryTiming", "arbeitPayDay", "arbeitTodouhukenId",
		"arbeitAreaId", "arbeitAddress", "arbeitPhoneNo", "arbeitPhoneReceptionTime",
		"arbeitApplicationMethod", "arbeitApplicationCapacity",
		"arbeitShopSingleWord", "arbeitShopSingleWordKbn", "arbeitFreeComment"
	};

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	/** 店舗一覧属性サービス */
	@Resource
	private ShopListAttributeService shopListAttributeService;

	@Resource
	private ShopListRouteService shopListRouteService;

	@Resource
	private ShopListLineService shopListLineService;

	@Resource
	private StationGroupService stationGroupService;

	@Resource
	private PrefecturesService prefecturesService;

	@Resource
	private CityService cityService;

	@Resource
	private CustomerImageService customerImageService;

	@Resource
	private ShopListMaterialService shopListMaterialService;

	@Resource
	private ShopListTagMappingService shopListTagMappingService;

	/**
	 * 店舗一覧のCSVを出力する。
	 * @param csvList
	 * @param property
	 * @throws IOException
	 */
	private void outPutShopListCsvFile(List<ShopListCsv> csvList, ShopListCsvProperty property, HttpServletResponse response) throws  IOException {
		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = null;
		S2CSVWriteCtrl<ShopListCsv> csvWriter = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));
			csvWriter =
						s2CSVCtrlFactory.getWriteController(ShopListCsv.class, out);

			int count = 0;
			for (ShopListCsv csv : csvList) {
				csvWriter.write(csv);
				count++;
			}
			property.count = count;
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
			}
		}
	}

	/**
	 * 店舗一覧の路線部分を作成します。
	 * @param csv 店舗一覧CSV
	 * @param entity 店舗一覧エンティティ
	 */
	private void createShopRouteCsv(ShopListArbeitCsv csv, ShopListArbeitCsv entity) {
		List<TShopListRoute> routeList = shopListRouteService.findByShopListId(Integer.parseInt(entity.id));
		if (CollectionUtils.isEmpty(routeList)) {
			return;
		}


		for (TShopListRoute route : routeList) {
//			if (GourmetCareeUtil.eqInt(1, route.dispOrder)) {
//				csv.arbeitRailroadId1 = GourmetCareeUtil.convertIntegerToString(route.railroadId);
//				csv.arbeitRouteId1 = GourmetCareeUtil.convertIntegerToString(route.routeId);
//				csv.arbeitStationId1 = GourmetCareeUtil.convertIntegerToString(route.stationId);
//				csv.arbeitStationComment1 = StringUtils.defaultString(route.comment);
//				continue;
//			}
//
//			if (GourmetCareeUtil.eqInt(2, route.dispOrder)) {
//				csv.arbeitRailroadId2 = GourmetCareeUtil.convertIntegerToString(route.railroadId);
//				csv.arbeitRouteId2 = GourmetCareeUtil.convertIntegerToString(route.routeId);
//				csv.arbeitStationId2 = GourmetCareeUtil.convertIntegerToString(route.stationId);
//				csv.arbeitStationComment2 = StringUtils.defaultString(route.comment);
//				continue;
//			}
//
//			if (GourmetCareeUtil.eqInt(3, route.dispOrder)) {
//				csv.arbeitRailroadId3 = GourmetCareeUtil.convertIntegerToString(route.railroadId);
//				csv.arbeitRouteId3 = GourmetCareeUtil.convertIntegerToString(route.routeId);
//				csv.arbeitStationId3 = GourmetCareeUtil.convertIntegerToString(route.stationId);
//				csv.arbeitStationComment3 = StringUtils.defaultString(route.comment);
//				continue;
//			}
		}
	}
	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(ShopListCsvProperty property) {
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
	 * アルバイト側の店舗一覧情報も含めてCSVをアウトプットします。
	 * @param property CSVプロパティ
	 * @param response レスポンス
	 * @throws IOException ファイル作成に失敗したらスロー
	 */
	public void outPutArbeitShopListCsv(ShopListCsvProperty property, HttpServletResponse response) throws IOException {
		response.setContentType(
				"application/octet-stream;charset=Windows-31J");
		response.setHeader(
				"Content-Disposition",
				"attachment; filename="
				+ createOutputFilePath(property));

		PrintWriter writer = null;
		S2CSVWriteCtrl<ShopListArbeitCsv> csvWriter = null;
		try {
			writer = new PrintWriter(
					new OutputStreamWriter(
							response.getOutputStream()
							, property.encode));
			csvWriter = s2CSVCtrlFactory.getWriteController(ShopListArbeitCsv.class, writer);

			synchronized (csvWriter) {
				//writeShopListArbeitCsv(csvWriter, property.shopListList);
			}
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
				csvWriter = null;
			}
		}
	}


	/**
	 * CSVのインポートを行います。
	 * @param filePath
	 * @param customerId
	 * @param areaCd
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public List<Integer> importCsv(String filePath, int customerId, int areaCd, String charSet) throws UnsupportedEncodingException, ArrayIndexOutOfBoundsException, IOException {

		List<Integer> registerIdList = new ArrayList<Integer>();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "shift-jis"))) {
			CSVParser parser = CSVFormat
					.EXCEL
					.withIgnoreEmptyLines(true)
					.withFirstRecordAsHeader()
					.withIgnoreSurroundingSpaces(true)
					.parse(br);

			FromCsvParser<ShopListCsv> csvParser = FromCsvParser.newInstance(ShopListCsv.class, parser.getRecords());

			csvParser.parse((ShopListCsv data, CSVRecord row) ->{
				CopyShopListCsvDto dto = copyCsvToEntity(data, customerId, areaCd);
				insertCsvAttributeList(data, customerId, dto);
				getStationGroupListFromCsv(data, row);
				insertShopListLineFromCsv(data,dto);
				insertShopListTagFromCsv(data,dto);

				registerIdList.add(dto.tShopList.id);
			});

		}

		return registerIdList;
	}





	/**
	 * バイトCSVをインポートします。
	 * @param filePath ファイルパス
	 * @param customerId 顧客ID
	 * @param areaCd エリアコード
	 * @param charSet 文字コード
	 * @return インポートした店舗一覧IDリスト
	 * @throws IOException インポートに失敗したらスロー
	 */
	public List<Integer> importArbeitCsv(String filePath, int customerId, int areaCd, String charSet) throws IOException, ArrayIndexOutOfBoundsException {

		List<Integer> registerIdList = new ArrayList<Integer>();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "shift-jis"))) {
			CSVParser parser = CSVFormat
					.EXCEL
					.withIgnoreEmptyLines(true)
					.withFirstRecordAsHeader()
					.withIgnoreSurroundingSpaces(true)
					.parse(br);

			FromCsvParser<ShopListArbeitCsv> csvParser = FromCsvParser.newInstance(ShopListArbeitCsv.class, parser.getRecords());

			csvParser.parse((ShopListArbeitCsv data, CSVRecord row) ->{
				CopyShopListCsvDto dto = copyCsvToEntity(data, customerId, areaCd);
				insertCsvAttributeList(data, customerId, dto);
				getStationGroupListFromCsv(data, row);
				insertShopListLineFromCsv(data,dto);
				insertShopListTagFromCsv(data,dto);

				registerIdList.add(dto.tShopList.id);
			});
		}

		return registerIdList;
	}


	/**
	 * バイトCSVをインポートします。
	 * @param filePath ファイルパス
	 * @param customerId 顧客ID
	 * @param areaCd エリアコード
	 * @param charSet 文字コード
	 * @return インポートした店舗一覧IDリスト
	 * @throws IOException インポートに失敗したらスロー
	 */
	public List<TShopChangeJobCondition> importJobCsv(String filePath, int customerId, int areaCd, String charSet) throws IOException, ArrayIndexOutOfBoundsException {

		List<TShopChangeJobCondition> tempList = new ArrayList<TShopChangeJobCondition>();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "shift-jis"))) {
			CSVParser parser = CSVFormat
					.EXCEL
					.withIgnoreEmptyLines(true)
					.withFirstRecordAsHeader()
					.withIgnoreSurroundingSpaces(true)
					.parse(br);

			FromCsvParser<ShopListJobCsv> csvParser = FromCsvParser.newInstance(ShopListJobCsv.class, parser.getRecords());

			csvParser.parse((ShopListJobCsv data, CSVRecord row) ->{
				TShopChangeJobCondition entity = copyJobCsvToEntity(data);
				tempList.add(entity);
			});
		}

		return tempList;
	}




	/**
	 * CSVをエンティティにコピーします。
	 * @param csv CSVエンティティ
	 * @param customerId 顧客ID
	 * @param areaCd エリアコード
	 * @return コピー用DTO
	 */
	private CopyShopListCsvDto copyCsvToEntity(ShopListArbeitCsv csv, int customerId, int areaCd) {
		CopyShopListCsvDto dto = new CopyShopListCsvDto();
		dto.tShopList = Beans.createAndCopy(TShopList.class, csv)
								.excludes(TShopList.ID,
										WztStringUtil.toCamelCase(TShopList.INDUSTRY_KBN1),
										WztStringUtil.toCamelCase(TShopList.INDUSTRY_KBN2),
										WztStringUtil.toCamelCase(TShopList.JOB_OFFER_FLG),
										WztStringUtil.toCamelCase(TShopList.ARBEIT_GYOTAI_ID),
										WztStringUtil.toCamelCase(TShopList.ARBEIT_SHOP_SINGLE_WORD_KBN),
										WztStringUtil.toCamelCase(TShopList.PREFECTURES_CD),
										WztStringUtil.toCamelCase(TShopList.CITY_CD),
										WztStringUtil.toCamelCase(TShopList.SEAT_KBN),
										WztStringUtil.toCamelCase(TShopList.SALES_PER_CUSTOMER_KBN),
										WztStringUtil.toCamelCase(TShopList.OPEN_DATE_YEAR),
										WztStringUtil.toCamelCase(TShopList.OPEN_DATE_MONTH),
										WztStringUtil.toCamelCase(TShopList.OPEN_DATE_LIMIT_DISPLAY_DATE)
										)
								.execute();

		// IDが設定されている場合
		if (NumberUtils.isNumber(csv.id)) {
			dto.tShopList.targetId = Integer.parseInt(csv.id);
			try {
				TShopList existEntity = shopListService.findById(dto.tShopList.targetId);
				dto.tShopList.latLngKbn = existEntity.latLngKbn;

				if (GourmetCareeUtil.eqInt(
						MTypeConstants.ShopListLatLngKbn.LAT_LNG,
						existEntity.latLngKbn)) {

					Beans.copy(existEntity, dto.tShopList).includes(LAT_LNG_COPY_PARAMS).execute();
				}

				dto.updateFlg = true;

			} catch (SNoResultException e) {
				// 何もしない
			}
		} else {
			dto.tShopList.latLngKbn = MTypeConstants.ShopListLatLngKbn.ADDRESS;
			dto.updateFlg = false;
		}


		// 引数で受けとったエリアコードは使用しない
		dto.tShopList.areaCd = areaCd;

		if(StringUtils.isNotBlank(csv.industryKbn1)) {
			dto.tShopList.industryKbn1 = GourmetCareeUtil.convertStringToInteger(csv.industryKbn1);
		}

		if(StringUtils.isNotBlank(csv.industryKbn2)) {
			dto.tShopList.industryKbn2 = GourmetCareeUtil.convertStringToInteger(csv.industryKbn2);
		}

		dto.tShopList.status = MTypeConstants.ShopListStatus.TEMP_SAVE;
		dto.tShopList.customerId = customerId;

		if (csv instanceof ShopListArbeitCsv) {
			ShopListArbeitCsv arbeitCsv = (ShopListArbeitCsv) csv;
			if (StringUtils.isNotBlank(arbeitCsv.prefecturesCd)) {
				dto.tShopList.prefecturesCd = NumberUtils.toInt(arbeitCsv.prefecturesCd);
			}
			if (StringUtils.isNotBlank(arbeitCsv.cityCd)) {
				dto.tShopList.cityCd = arbeitCsv.cityCd;
			}
			if (StringUtils.isNotBlank(arbeitCsv.seatKbn)) {
				dto.tShopList.seatKbn = NumberUtils.toInt(arbeitCsv.seatKbn);
			}
			if (StringUtils.isNotBlank(arbeitCsv.salesPerCustomerKbn)) {
				dto.tShopList.salesPerCustomerKbn = NumberUtils.toInt(arbeitCsv.salesPerCustomerKbn);
			}
			if(StringUtils.isNotBlank(arbeitCsv.openDateYear)) {
				dto.tShopList.openDateYear = NumberUtils.toInt(arbeitCsv.openDateYear);
			}
			if(StringUtils.isNotBlank(arbeitCsv.openDateMonth)) {
				dto.tShopList.openDateMonth = NumberUtils.toInt(arbeitCsv.openDateMonth);
			}
			if(StringUtils.isNotBlank(arbeitCsv.openDateLimitDisplayDate)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				try {
					dto.tShopList.openDateLimitDisplayDate = new java.sql.Date((sdf.parse(arbeitCsv.openDateLimitDisplayDate).getTime()));
				} catch (ParseException e) {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						dto.tShopList.openDateLimitDisplayDate = new java.sql.Date((sdf.parse(arbeitCsv.openDateLimitDisplayDate).getTime()));
					}catch(ParseException e2) {
						dto.tShopList.openDateLimitDisplayDate = new java.sql.Date((new Date(1)).getTime());
					}
				}
			}
			getAddress1(dto.tShopList.prefecturesCd, dto.tShopList.cityCd, dto.tShopList.address, dto);

		}
		shopListService.insert(dto.tShopList);

		return dto;
	}

	private TShopChangeJobCondition copyJobCsvToEntity(ShopListJobCsv csv) {
		TShopChangeJobCondition entity = new TShopChangeJobCondition();
		entity = Beans.createAndCopy(TShopChangeJobCondition.class, csv)
								.execute();
		return entity;
	}

	/**
	 * CSVをエンティティにコピーします。
	 * @param csv CSVエンティティ
	 * @param customerId 顧客ID
	 * @param areaCd エリアコード
	 * @return コピー用DTO
	 */
	private CopyShopListCsvDto copyCsvToEntity(ShopListCsv csv, int customerId, int areaCd) {
		CopyShopListCsvDto dto = new CopyShopListCsvDto();
		dto.tShopList = Beans.createAndCopy(TShopList.class, csv)
								.excludes(TShopList.ID,
										WztStringUtil.toCamelCase(TShopList.INDUSTRY_KBN1),
										WztStringUtil.toCamelCase(TShopList.INDUSTRY_KBN2),
										WztStringUtil.toCamelCase(TShopList.JOB_OFFER_FLG),
										WztStringUtil.toCamelCase(TShopList.PREFECTURES_CD),
										WztStringUtil.toCamelCase(TShopList.CITY_CD),
										WztStringUtil.toCamelCase(TShopList.SEAT_KBN),
										WztStringUtil.toCamelCase(TShopList.SALES_PER_CUSTOMER_KBN),
										WztStringUtil.toCamelCase(TShopList.OPEN_DATE_YEAR),
										WztStringUtil.toCamelCase(TShopList.OPEN_DATE_MONTH),
										WztStringUtil.toCamelCase(TShopList.OPEN_DATE_LIMIT_DISPLAY_DATE)
										)
								.execute();

		TShopList existEntity = null;

		// IDが設定されている場合
		if (NumberUtils.isNumber(csv.id)) {
			dto.tShopList.targetId = Integer.parseInt(csv.id);
			try {
				existEntity = shopListService.findById(dto.tShopList.targetId);
				dto.tShopList.latLngKbn = existEntity.latLngKbn;

				if (GourmetCareeUtil.eqInt(
						MTypeConstants.ShopListLatLngKbn.LAT_LNG,
						existEntity.latLngKbn)) {

					Beans.copy(existEntity, dto.tShopList).includes(LAT_LNG_COPY_PARAMS).execute();
				}

				dto.updateFlg = true;

			} catch (SNoResultException e) {
				// 何もしない
			}
		} else {
			dto.tShopList.latLngKbn = MTypeConstants.ShopListLatLngKbn.ADDRESS;
			dto.updateFlg = false;
		}


		// 引数で受けとったエリアコードは使用しない
		dto.tShopList.areaCd = areaCd;

		if(StringUtils.isNotBlank(csv.industryKbn1)) {
			dto.tShopList.industryKbn1 = NumberUtils.toInt(csv.industryKbn1);
		}

		if(StringUtils.isNotBlank(csv.industryKbn2)) {
			dto.tShopList.industryKbn2 = NumberUtils.toInt(csv.industryKbn2);
		}

		dto.tShopList.status = MTypeConstants.ShopListStatus.TEMP_SAVE;
		dto.tShopList.customerId = customerId;

		if(StringUtils.isNotBlank(csv.prefecturesCd)) {
			dto.tShopList.prefecturesCd = NumberUtils.toInt(csv.prefecturesCd);
		}

		if(StringUtils.isNotBlank(csv.cityCd)) {
			dto.tShopList.cityCd = csv.cityCd;
		}

		if(StringUtils.isNotBlank(csv.seatKbn)) {
			dto.tShopList.seatKbn = NumberUtils.toInt(csv.seatKbn);
		}

		if(StringUtils.isNotBlank(csv.salesPerCustomerKbn)) {
			dto.tShopList.salesPerCustomerKbn = NumberUtils.toInt(csv.salesPerCustomerKbn);
		}

		if(StringUtils.isNotBlank(csv.openDateYear)) {
			dto.tShopList.openDateYear = NumberUtils.toInt(csv.openDateYear);
		}

		if(StringUtils.isNotBlank(csv.openDateMonth)) {
			dto.tShopList.openDateMonth = NumberUtils.toInt(csv.openDateMonth);
		}

		if(StringUtils.isNotBlank(csv.openDateLimitDisplayDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			try {
				dto.tShopList.openDateLimitDisplayDate = new java.sql.Date((sdf.parse(csv.openDateLimitDisplayDate).getTime()));
			} catch (ParseException e) {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					dto.tShopList.openDateLimitDisplayDate = new java.sql.Date((sdf.parse(csv.openDateLimitDisplayDate).getTime()));
				}catch(ParseException e2) {
					dto.tShopList.openDateLimitDisplayDate = new java.sql.Date((new Date(1)).getTime());
				}
			}
		}
		getAddress1(dto.tShopList.prefecturesCd, dto.tShopList.cityCd, dto.tShopList.address, dto);
		shopListService.insert(dto.tShopList);

		return dto;
	}

	/**
	 * csvから、店舗一覧属性をインサートします。
	 * @param csv CSV
	 * @param dto コピー用DTO
	 */
	private void insertCsvAttributeList(ShopListArbeitCsv csv, int customerId, CopyShopListCsvDto dto) {

		//仕事の特徴
		insertAttributeList(csv.workCharacteristicKbnArray, MTypeConstants.WorkCharacteristicKbn.TYPE_CD, dto.tShopList.id);

		//職場
		insertAttributeList(csv.shopCharacteristicKbnArray, MTypeConstants.ShopCharacteristicKbn.TYPE_CD, dto.tShopList.id);

		// 画像
		insertShopMaterial(csv, customerId, dto.tShopList.id);

	}

	/**
	 * csvから、店舗一覧属性をインサートします。
	 * @param csv CSV
	 * @param dto コピー用DTO
	 */
	private void insertCsvAttributeList(ShopListCsv csv, int customerId, CopyShopListCsvDto dto) {

		//仕事の特徴
		insertAttributeList(csv.workCharacteristicKbnArray, MTypeConstants.WorkCharacteristicKbn.TYPE_CD, dto.tShopList.id);

		//職場
		insertAttributeList(csv.shopCharacteristicKbnArray, MTypeConstants.ShopCharacteristicKbn.TYPE_CD, dto.tShopList.id);

		// 画像
		insertShopMaterial(csv, customerId, dto.tShopList.id);

	}

	/**
	 * 店舗一覧属性をインサートします。
	 * @param columnValue カラムの値
	 * @param typeCd タイプコード
	 * @param shopListId 店舗一覧ID
	 */
	private void insertAttributeList(String columnValue, String typeCd, Integer shopListId) {
		// 値がない場合は処理しない。
		if (StringUtils.isBlank(columnValue)) {
			return;
		}

		Set<Integer> attributeLValueSet = new HashSet<Integer>();
		String[] valueArray = columnValue.split(",");

		for (String value : valueArray) {
			attributeLValueSet.add(NumberUtils.toInt(GourmetCareeUtil.superTrim(value)));
		}

		for (Integer value : attributeLValueSet) {
			TShopListAttribute entity = new TShopListAttribute();
			entity.attributeCd = typeCd;
			entity.attributeValue = value;
			entity.shopListId = shopListId;
			shopListAttributeService.insert(entity);
		}

	}

	/**
	 * 店舗画像をインサートします。
	 * @param columnValue カラムの値
	 * @param typeCd タイプコード
	 * @param shopListId 店舗一覧ID
	 */
	private void insertShopMaterial(ShopListArbeitCsv csv, int customerId, Integer shopListId) {
		// 値がない場合は処理しない。
		if (StringUtils.isBlank(csv.mainImg) && StringUtils.isBlank(csv.logoImg)) {
			return;
		}

		if (StringUtils.isNotBlank(csv.mainImg)) {
			List<TCustomerImage> mainImgList;
			try {
				mainImgList = customerImageService.findListByCustomerIdAndFileName(customerId, csv.mainImg);
				if (CollectionUtils.isNotEmpty(mainImgList)) {
					TCustomerImage mainImage = mainImgList.get(0);

					TShopListMaterial entity = new TShopListMaterial();
					entity.shopListId = shopListId;
					entity.materialKbn = Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1);
					entity.fileName = mainImage.fileName;
					entity.contentType = mainImage.contentType;
					entity.materialData = mainImage.materialData;
					entity.customerImageId = mainImage.id;
					shopListMaterialService.insert(entity);
				}
			} catch (WNoResultException e) {
				//
			}
		}

		if (StringUtils.isNotBlank(csv.logoImg)) {
			List<TCustomerImage> logoImgList;
			try {
				logoImgList = customerImageService.findListByCustomerIdAndFileName(customerId, csv.logoImg);
				if (CollectionUtils.isNotEmpty(logoImgList)) {
					TCustomerImage logoImage = logoImgList.get(0);

					TShopListMaterial entity = new TShopListMaterial();
					entity.shopListId = shopListId;
					entity.materialKbn = Integer.parseInt(MTypeConstants.ShopListMaterialKbn.LOGO);
					entity.fileName = logoImage.fileName;
					entity.contentType = logoImage.contentType;
					entity.materialData = logoImage.materialData;
					entity.customerImageId = logoImage.id;
					shopListMaterialService.insert(entity);
				}
			} catch (WNoResultException e) {
				//
			}
		}
	}

	/**
	 * 店舗画像をインサートします。
	 * @param columnValue カラムの値
	 * @param typeCd タイプコード
	 * @param shopListId 店舗一覧ID
	 */
	private void insertShopMaterial(ShopListCsv csv, int customerId, Integer shopListId) {
		// 値がない場合は処理しない。
		if (StringUtils.isBlank(csv.mainImg)) {
			return;
		}

		if (StringUtils.isNotBlank(csv.mainImg)) {
			List<TCustomerImage> mainImgList;
			try {
				mainImgList = customerImageService.findListByCustomerIdAndFileName(customerId, csv.mainImg);
				if (CollectionUtils.isNotEmpty(mainImgList)) {
					TCustomerImage mainImage = mainImgList.get(0);

					TShopListMaterial entity = new TShopListMaterial();
					entity.shopListId = shopListId;
					entity.materialKbn = Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1);
					entity.fileName = mainImage.fileName;
					entity.contentType = mainImage.contentType;
					entity.materialData = mainImage.materialData;
					entity.customerImageId = mainImage.id;
					shopListMaterialService.insert(entity);
				}
			} catch (WNoResultException e) {
				//
			}
		}
	}


	/**
	 * 店舗一覧CSVから店舗一覧路線をインサートします。
	 * @param csv CSV
	 * @param dto コピーDTO
	 */
	private void insertShopListLineFromCsv(ShopListArbeitCsv csv, CopyShopListCsvDto dto) {
			if(csv.stationGroupList == null) {
				return;
			}

			List<StationGroupCsv> list = csv.stationGroupList;
			int index = 1;
			for(StationGroupCsv stationGroupCsv : list) {
				if(StringUtils.isBlank(stationGroupCsv.stationCd)) {
					insertShopListLine(dto.tShopList.id, 0, 0, 0, null, null, "", index);
				}else {
					Integer stationCd = Integer.parseInt(stationGroupCsv.stationCd);
					Integer lineCd = stationGroupService.getLineCd(stationCd);
					Integer companyCd = stationGroupService.getCompanyCd(stationCd);

					insertShopListLine(dto.tShopList.id, companyCd, lineCd, stationCd, stationGroupCsv.transportationKbn, stationGroupCsv.timeRequiredMinute, "", index);
				}
				index++;
			}
	}

	/**
	 * 店舗一覧CSVから住所を取得
	 * @param csv CSV
	 * @param dto コピーDTO
	 */
	private void getAddress1(Integer prefCd, String cityCd, String address, CopyShopListCsvDto dto) {
			//住所項目が正しく入力されていなければ何もしない
			if(prefCd == null || StringUtils.isBlank(cityCd) || StringUtils.isBlank(address)
					|| prefCd < 1 || !NumberUtils.isNumber(cityCd)) {
				return;
			}

			String prefName = prefecturesService.getName(prefCd);
			String cityName = cityService.getName(cityCd);

			if(StringUtils.isNotBlank(prefName) && StringUtils.isNotBlank(cityName)) {
				dto.tShopList.address1 = prefName + cityName + " " + address;
			}
	}

	/**
	 * 店舗一覧CSVから店舗一覧路線をインサートします。
	 * @param csv CSV
	 * @param dto コピーDTO
	 */
	private void insertShopListLineFromCsv(ShopListCsv csv, CopyShopListCsvDto dto) {
			if(csv.stationGroupList == null) {
				return;
			}

			List<StationGroupCsv> list = csv.stationGroupList;
			int index = 1;
			for(StationGroupCsv stationGroupCsv : list) {
				if(StringUtils.isBlank(stationGroupCsv.stationCd)) {
					insertShopListLine(dto.tShopList.id, 0, 0, 0, null, null, "", index);
				}else {
					Integer stationCd = Integer.parseInt(stationGroupCsv.stationCd);
					Integer lineCd = stationGroupService.getLineCd(stationCd);
					Integer companyCd = stationGroupService.getCompanyCd(stationCd);

					insertShopListLine(dto.tShopList.id, companyCd, lineCd, stationCd, stationGroupCsv.transportationKbn, stationGroupCsv.timeRequiredMinute, "", index);
				}
				index++;
			}
	}

	/**
	 * CSVに入力された駅情報から駅情報グループリストを取得する
	 * @param data
	 * @param row
	 */
	private void getStationGroupListFromCsv(ShopListArbeitCsv data, CSVRecord row) {
		List<StationGroupCsv> list = new ArrayList<>();
		try {
			for(int i = 32; i < row.size(); i = i + 3) {
				StationGroupCsv stationGroupCsv = new StationGroupCsv();
				stationGroupCsv.stationCd = row.get(i);
				stationGroupCsv.transportationKbn = row.get(i + 1);
				stationGroupCsv.timeRequiredMinute = row.get(i + 2);

				if(StringUtils.isBlank(stationGroupCsv.stationCd)
						&& StringUtils.isBlank(stationGroupCsv.transportationKbn)
						&& StringUtils.isBlank(stationGroupCsv.timeRequiredMinute)
						&& list.isEmpty()) {
					return;

				}else if(StringUtils.isBlank(stationGroupCsv.stationCd) && StringUtils.isBlank(stationGroupCsv.transportationKbn)
						&& StringUtils.isBlank(stationGroupCsv.timeRequiredMinute) && !list.isEmpty()) {
					data.stationGroupList = list;
					return;
				}

				if(StringUtils.isNotBlank(stationGroupCsv.stationCd) && NumberUtils.isNumber(stationGroupCsv.stationCd)
						&& StringUtils.isNotBlank(stationGroupCsv.transportationKbn) && NumberUtils.isNumber(stationGroupCsv.transportationKbn)
						&& StringUtils.isNotBlank(stationGroupCsv.timeRequiredMinute) && NumberUtils.isNumber(stationGroupCsv.timeRequiredMinute)) {
					list.add(stationGroupCsv);
				}else {
					List<StationGroupCsv> errorList = new ArrayList<>();
					errorList.add(new StationGroupCsv());
					data.stationGroupList = errorList;
					return;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			return;
		}
		data.stationGroupList = list;
	}

	/**
	 * CSVに入力された駅情報から駅情報グループリストを取得する
	 * @param data
	 * @param row
	 */
	private void getStationGroupListFromCsv(ShopListCsv data, CSVRecord row) {
		List<StationGroupCsv> list = new ArrayList<>();
		try {
			for(int i = 31; i < row.size(); i = i + 3) {
				StationGroupCsv stationGroupCsv = new StationGroupCsv();
				stationGroupCsv.stationCd = row.get(i);
				stationGroupCsv.transportationKbn = row.get(i + 1);
				stationGroupCsv.timeRequiredMinute = row.get(i + 2);

				if(StringUtils.isBlank(stationGroupCsv.stationCd)
						&& StringUtils.isBlank(stationGroupCsv.transportationKbn)
						&& StringUtils.isBlank(stationGroupCsv.timeRequiredMinute)
						&& list.isEmpty()) {
					return;

				}else if(StringUtils.isBlank(stationGroupCsv.stationCd) && StringUtils.isBlank(stationGroupCsv.transportationKbn)
						&& StringUtils.isBlank(stationGroupCsv.timeRequiredMinute) && !list.isEmpty()) {
					data.stationGroupList = list;
					return;
				}

				if(StringUtils.isNotBlank(stationGroupCsv.stationCd) && NumberUtils.isNumber(stationGroupCsv.stationCd)
						&& StringUtils.isNotBlank(stationGroupCsv.transportationKbn) && NumberUtils.isNumber(stationGroupCsv.transportationKbn)
						&& StringUtils.isNotBlank(stationGroupCsv.timeRequiredMinute) && NumberUtils.isNumber(stationGroupCsv.timeRequiredMinute)) {
					list.add(stationGroupCsv);
				}else {
					List<StationGroupCsv> errorList = new ArrayList<>();
					errorList.add(new StationGroupCsv());
					data.stationGroupList = errorList;
					return;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			return;
		}
		data.stationGroupList = list;
	}


	/**
	 * 店舗一覧の路線をインサートします。
	 * @param companyCd 鉄道会社ID
	 * @param lineCd 路線ID
	 * @param stationCd 駅ID
	 * @param comment コメント
	 * @param displayOrder 表示番号
	 */
	private void insertShopListLine(Integer shopListId, Integer companyCd, Integer lineCd, Integer stationCd, String transportationKbn, String timeRequiredMinute, String comment, int displayOrder) {
		TShopListLine entity = new TShopListLine();
		entity.shopListId = shopListId;
		entity.companyCd = companyCd;
		entity.lineCd = lineCd;
		entity.stationCd =stationCd;
		if(StringUtils.isNotBlank(transportationKbn)) {
			entity.transportationKbn = Integer.parseInt(transportationKbn);
		}
		if(StringUtils.isNotBlank(timeRequiredMinute)) {
			entity.timeRequiredMinute = Integer.parseInt(timeRequiredMinute);
		}
		entity.comment = comment;
		entity.displayOrder = displayOrder;

		shopListLineService.insert(entity);
	}

	/**
	 * 店舗一覧CSVから店舗一覧タグをインサートします（顧客管理側）
	 * @param csv CSV
	 * @param dto コピーDTO
	 */
	private void insertShopListTagFromCsv(ShopListArbeitCsv csv, CopyShopListCsvDto dto) {
		String tags = csv.tag;
		// タグが空の場合はスルー
		if (StringUtils.isEmpty(tags)) {
			return;
		}

		Integer shopId = dto.tShopList.id;
		List<String> tagList = Arrays.asList(tags.split("・"));

		for(String tagId : tagList) {
			if (!StringUtils.isEmpty(tagId) && tagId.matches("\\d+")) {
				insertShopListTag(tagId, shopId);
			}
		}
	}

	/**
	 * 店舗一覧CSVから店舗一覧タグをインサートします（企業管理側）
	 * @param csv CSV
	 * @param dto コピーDTO
	 */
	private void insertShopListTagFromCsv(ShopListCsv csv, CopyShopListCsvDto dto) {
		String tags = csv.tag;
		// タグが空の場合はスルー
		if (StringUtils.isEmpty(tags)) {
			return;
		}

		Integer shopId = dto.tShopList.id;
		List<String> tagList = Arrays.asList(tags.split("・"));

		for(String tagId : tagList) {
			if (!StringUtils.isEmpty(tagId) && tagId.matches("\\d+")) {
				insertShopListTag(tagId, shopId);
			}
		}
	}

	/**
	 * 店舗一覧のインディードタグをインサートします。
	 * @param tagId タグID
	 * @param shopId 店舗ID
	 */
	private void insertShopListTag(String tagId, Integer shopId) {
		MShopListTagMapping entity = new MShopListTagMapping();
		entity.shopListTagId = Integer.parseInt(tagId);
		entity.shopListId = shopId;

		shopListTagMappingService.insert(entity);
	}
}


