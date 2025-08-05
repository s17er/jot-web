package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.service.dto.CareerHistoryCsvDto;
import com.gourmetcaree.admin.service.dto.MemberCsvDto;
import com.gourmetcaree.admin.service.property.MemberCsvProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.CsvUtil;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.db.common.entity.VMemberHopeCity;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.db.common.service.MemberAttributeService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;

/**
 * 会員CSVに関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
public class MemberCsvLogic  extends AbstractAdminLogic {

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** 会員属性サービス */
	@Resource
	protected MemberAttributeService memberAttributeService;

	/** 学歴サービス */
	@Resource
	protected SchoolHistoryService schoolHistoryService;

	/** 職歴サービス */
	@Resource
	protected CareerHistoryService careerHistoryService;

	/** 職歴属性サービス */
	@Resource
	protected CareerHistoryAttributeService careerHistoryAttributeService;

	/** 名前変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 最大職歴カウント */
	private int maxCareerCount;


	/**
	 * 会員情報CSVを出力する
	 * @param property 会員情報CSVプロパティ
	 * @throws ParseException
	 * @throws WNoResultException
	 * @throws IOException
	 */
	public void outPutCsv(MemberCsvProperty property) throws WNoResultException, ParseException, IOException {

		// 会員情報を取得
		List<MemberCsvDto> memberCsvDtoList = convertCsvData(property);

		// CSVを出力する
		outputCsvFile(memberCsvDtoList, property);


	}

	/**
	 * 会員情報を取得し、DTOへ値をセット
	 * @param property 会員情報CSVプロパティ
	 * @return 会員情報DTO(CSV用)
	 * @throws ParseException
	 * @throws WNoResultException
	 */
	private List<MemberCsvDto> convertCsvData(MemberCsvProperty property) throws WNoResultException, ParseException {

		// 会員情報を取得
		List<MMember> memberList = memberService.getMemberDataForCsv(property.map, property.sortKey);

		List<MemberCsvDto> memberDtoList = new ArrayList<MemberCsvDto>();

		// 会員情報をDTOへセット
		for (MMember memberEntity : memberList) {
			MemberCsvDto memberDto = new MemberCsvDto();

			Beans.copy(memberEntity, memberDto).execute();
			memberDto.insertDateTime = DateUtils.getDateStr(memberEntity.insertDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT);
			memberDto.birthday = DateUtils.getDateStr(memberEntity.birthday, GourmetCareeConstants.DATE_FORMAT_SLASH);
			memberDto.lastUpdateDatetime = DateUtils.getDateStr(memberEntity.lastUpdateDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT);

			if (memberEntity.tLoginHistory != null && memberEntity.tLoginHistory.lastLoginDatetime != null) {
				memberDto.lastLoginDateTime = DateUtils.getDateStr(memberEntity.tLoginHistory.lastLoginDatetime, GourmetCareeConstants.MAIL_DATE_TIME_FORMAT);
			}

			// 属性をセット
			List<String> qualificationList = new ArrayList<String>();
			List<String> jobList = new ArrayList<String>();
			List<String> industryList = new ArrayList<String>();
			List<String> workLocationList = new ArrayList<String>();
			List<String> employPtnKbnList = new ArrayList<String>();

			for (MMemberAttribute memberAttrEntity : memberEntity.mMemberAttributeList) {

				if (MTypeConstants.QualificationKbn.TYPE_CD.equals(memberAttrEntity.attributeCd)) {
					qualificationList.add(String.valueOf(memberAttrEntity.attributeValue));
				} else if (MTypeConstants.JobKbn.TYPE_CD.equals(memberAttrEntity.attributeCd)) {
					jobList.add(String.valueOf(memberAttrEntity.attributeValue));
				} else if (MTypeConstants.IndustryKbn.TYPE_CD.equals(memberAttrEntity.attributeCd)) {
					industryList.add(String.valueOf(memberAttrEntity.attributeValue));
				} else if (MTypeConstants.EmployPtnKbn.TYPE_CD.equals(memberAttrEntity.attributeCd)) {
					employPtnKbnList.add(String.valueOf(memberAttrEntity.attributeValue));
				}
			}

			memberDto.qualificationArray = (String[]) qualificationList.toArray(new String[0]);
			memberDto.jobArray = (String[]) jobList.toArray(new String[0]);
			memberDto.industryArray = (String[]) industryList.toArray(new String[0]);
			memberDto.workLocationArray = (String[]) workLocationList.toArray(new String[0]);
			memberDto.employPtnKbnArray = (String[]) employPtnKbnList.toArray(new String[0]);

			// 学歴をセット
			try {
				// 学歴を取得
				TSchoolHistory schoolEntity = schoolHistoryService.getTSchoolHistoryDataByMemberId(memberEntity.id);
				Beans.copy(schoolEntity, memberDto).excludes("id", "insertDatetime").execute();

			} catch (WNoResultException e) {
				// 学歴がない場合は、処理しない。
			}

			// 職歴をセット
			List<CareerHistoryCsvDto> careerCsvDtoList = new ArrayList<CareerHistoryCsvDto>();
			try {

				// 職歴を取得
				List<TCareerHistory> careerEntityList = careerHistoryService.getCareerHistoryDataByMemberId(memberEntity.id);

				int careerCount = 0;
				for (TCareerHistory careerHistoryEntity : careerEntityList) {
					careerCount++;
					CareerHistoryCsvDto careerCsvDto = new CareerHistoryCsvDto();
					List<String> careerJobList = new ArrayList<String>();
					List<String> careerIndustryList = new ArrayList<String>();
					Beans.copy(careerHistoryEntity, careerCsvDto).execute();

					for (TCareerHistoryAttribute careerAttrEntity : careerHistoryEntity.tCareerHistoryAttributeList) {
						// 職種
						if (MTypeConstants.JobKbn.TYPE_CD.equals(careerAttrEntity.attributeCd)) {
							careerJobList.add(String.valueOf(careerAttrEntity.attributeValue));
						}
						// 業態
						if (MTypeConstants.IndustryKbn.TYPE_CD.equals(careerAttrEntity.attributeCd)) {
							careerIndustryList.add(String.valueOf(careerAttrEntity.attributeValue));
						}
					}

					careerCsvDto.jobArray = (String[]) careerJobList.toArray(new String[0]);
					careerCsvDto.industryArray = (String[]) careerIndustryList.toArray(new String[0]);
					careerCsvDtoList.add(careerCsvDto);
				}

				memberDto.careerHistoryCsvDtoList = careerCsvDtoList;

				// 最大職歴数をセット
				if (maxCareerCount < careerCount) {
					maxCareerCount = careerCount;
				}

			} catch (WNoResultException e) {
				memberDto.careerHistoryCsvDtoList = careerCsvDtoList;
			}

			// 希望勤務地（リニューアル後からは希望都道府県を設定）
			Set<String> prefSet = new LinkedHashSet<>();
			for (VMemberHopeCity city : memberEntity.vMemberHopeCityList) {
				prefSet.add(String.valueOf(city.prefecturesCd));
			}
			memberDto.areaCdList = prefSet.toArray(new String[prefSet.size()]);

			memberDto.convertToOutPutType(valueToNameConvertLogic);
			memberDtoList.add(memberDto);
		}


		return memberDtoList;
	}

	/**
	 * 会員情報をCSVファイルへ出力する
	 * @param list 会員情報データリスト
	 * @throws IOException
	 */
	private void outputCsvFile(List<MemberCsvDto> list, MemberCsvProperty property) throws IOException {

		// ファイルをオープンする（ようなもの）
		String outputFileName = createOutputFilePath(property);

		    response.setContentType("application/octet-stream;charset=Windows-31J");
		    response.setHeader("Content-Disposition","attachment; filename=" + outputFileName);

		    PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));

		try {
			property.count = 0;
			if (list != null && !list.isEmpty()) {

				// ヘッダーを出力
				out.println(createHeader(property));

				for (MemberCsvDto memberCsvDto : list) {
					out.println(memberCsvDto.craeteRecordString());
					property.count++;
				}
			}
		} finally {
			out.close();
		}
	}

	/**
	 * ヘッダーを生成する
	 * @param property 会員CSVプロパティ
	 * @return ヘッダー文字列
	 */
	private String createHeader(MemberCsvProperty property) {

		StringBuilder sb = new StringBuilder();
		sb.append(property.header);
		for (int i = 0; i < maxCareerCount; i++) {
			sb.append(CsvUtil.DELIMITER);
			sb.append(property.careerHeader);
		}

		return sb.toString();
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 * @throws IOException
	 * @throws IOException
	 */
	private static String createOutputFilePath(MemberCsvProperty property) {

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
