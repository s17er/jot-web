package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.admin.service.dto.AdvancedRegistrationSearchResultDto;
import com.gourmetcaree.admin.service.property.AdvancedMemberCsvProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.AdvancedRegistrationMemberCsv;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryAttribute;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryLoginHistory;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntrySchoolHistory;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryAttributeService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryLoginHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntrySchoolHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * 事前登録会員用CSVロジック
 * @author Takehiro Nakamori
 *
 */
public class AdvancedMemberCsvLogic extends AbstractAdminLogic {
	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	private TypeService typeService;

	@Resource
	private AdvancedRegistrationEntryAttributeService advancedRegistrationEntryAttributeService;

	@Resource(name = "advancedRegistrationEntrySchoolHistoryService")
	private AdvancedRegistrationEntrySchoolHistoryService schoolService;

	@Resource
	private AdvancedRegistrationService advancedRegistrationService;

	@Resource
	private MemberService memberService;

	@Resource
	private AdvancedRegistrationEntryLoginHistoryService advancedRegistrationEntryLoginHistoryService;

	/**
	 * CSVを出力します。
	 * @param property CSVプロパティ
	 * @throws WNoResultException 検索結果がなかった場合にスロー
	 * @throws UnsupportedEncodingException エンコードがサポートされていない場合にスロー
	 * @throws IOException ファイル操作に失敗した場合にスロー
	 */
	public void outputCsv(AdvancedMemberCsvProperty property) throws WNoResultException, UnsupportedEncodingException, IOException {
		List<AdvancedRegistrationMemberCsv> csvList = createCsvList(property.select);

		if (CollectionUtils.isEmpty(csvList)) {
			throw new WNoResultException();
		}

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename="
				+ createOutputFilePath(property));

		PrintWriter out = null;


		S2CSVWriteCtrl<AdvancedRegistrationMemberCsv> csv_writer = null;

		try {
			out = new PrintWriter(
					new OutputStreamWriter(
							response.getOutputStream(),
							property.encode));

			csv_writer =
					s2CSVCtrlFactory.getWriteController(AdvancedRegistrationMemberCsv.class, out);

			for (AdvancedRegistrationMemberCsv csv : csvList) {
				csv_writer.write(csv);
			}
		} finally {
			if (csv_writer != null) {
				csv_writer.close();
				csv_writer = null;
			}

			if (out != null) {
				out.close();
				out = null;
			}

			csvList = null;
		}

	}

	/**
	 * CSVリストを作成します。
	 * @param select 検索用セレクト
	 * @return CSVリスト
	 */
	private List<AdvancedRegistrationMemberCsv> createCsvList(SqlSelect<AdvancedRegistrationSearchResultDto> select) {

		final SimpleDateFormat YMD_SDF = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
		final SimpleDateFormat YMDHMS_SDF = new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT);
		List<AdvancedRegistrationSearchResultDto> entityList = select.getResultList();
		List<AdvancedRegistrationMemberCsv> csvList = new ArrayList<AdvancedRegistrationMemberCsv>();

		final Map<Integer, String> sexMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.Sex.TYPE_CD);
		final Map<Integer, String> industryMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.IndustryKbn.TYPE_CD);
		final Map<Integer, String> jobMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.JobKbn.TYPE_CD);
		final Map<Integer, String> qualificationMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.QualificationKbn.TYPE_CD);
		// 事前登録メルマガ受信マップ
		final Map<Integer, String> advancedMailMagFlgMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.AdvancedMailMagReceptionFlg.TYPE_CD);
		// 転職希望時期マップ
		final Map<Integer, String> hopeCareerChangeTermMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.HopeCareerChangeTerm.TYPE_CD);
		final Map<Integer, String> schoolGradKbnMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.GraduationKbn.TYPE_CD);

		final Map<Integer, String> pcMailStopFlgMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.PcMailStopFlg.TYPE_CD);
		final Map<Integer, String> mobileMailStopFlgMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.MobileMailStopFlg.TYPE_CD);

		// 事前登録区分のマップ
		final Map<Integer, String> advancedKbnMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.AdvancedRegistrationStatusKbn.TYPE_CD);
		// 会員区分のマップ
		final Map<Integer, String> memberKbnMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.MemberKbn.TYPE_CD);

		final Map<Integer, String> currentSituationKbnMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.CurrentSituationKbn.TYPE_CD);

		final Map<Integer, String> attendedStatusKbnMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.AdvancedRegistrationAttendedStatus.TYPE_CD);




		for (AdvancedRegistrationSearchResultDto entity : entityList) {
			AdvancedRegistrationMemberCsv csv =
					Beans.createAndCopy(AdvancedRegistrationMemberCsv.class, entity).execute();



			csv.birthdayStr = YMD_SDF.format(entity.birthday);
			csv.phoneNo = createPhoneNo(entity);
			csv.addressStr = createAddress(entity);
			csv.advancedRegistrationShortName = valueToNameConvertLogic.convertToAdvancedRegistrationShortName(entity.advancedRegistrationId);


			csv.sex = sexMap.get(entity.sexKbn);


			List<TAdvancedRegistrationEntryAttribute> attributeList = advancedRegistrationEntryAttributeService.findByAdvancedRegistrationEntryId(entity.id);
			List<Integer> industryList = new ArrayList<Integer>();
			List<Integer> jobList = new ArrayList<Integer>();
			List<Integer> qualificationList = new ArrayList<Integer>();
			// 転職希望時期の区分リスト
			List<Integer> hopeCareerChangeTermList = new ArrayList<Integer>();

			Integer currentSituationKbn = null;
			for (TAdvancedRegistrationEntryAttribute attr : attributeList) {
				final String typecd = attr.attributeCd;
				final Integer value = attr.attributeValue;
				if (MTypeConstants.IndustryKbn.TYPE_CD.equals(typecd)) {
					industryList.add(value);
					continue;
				}

				if (MTypeConstants.JobKbn.TYPE_CD.equals(typecd)) {
					jobList.add(value);
					continue;
				}
				if (MTypeConstants.QualificationKbn.TYPE_CD.equals(typecd)) {
					qualificationList.add(value);
					continue;
				}

				if (MTypeConstants.HopeCareerChangeTerm.TYPE_CD.equals(typecd)) {
					hopeCareerChangeTermList.add(value);
					continue;
				}

				if (MTypeConstants.CurrentSituationKbn.TYPE_CD.equals(typecd)) {
					if (currentSituationKbn == null) {
						currentSituationKbn = value;
						continue;
					}
				}

			}
			csv.advancedMailMagazineReceptionFlgStr = advancedMailMagFlgMap.get(entity.advancedMailMagazineReceptionFlg);

			csv.hopeIndustries = GourmetCareeUtil.joinMapValues(industryMap, industryList);
			csv.hopeJobs = GourmetCareeUtil.joinMapValues(jobMap, jobList);
			csv.qualificationStr = GourmetCareeUtil.joinMapValues(qualificationMap, qualificationList)
					+ (StringUtils.isNotEmpty(entity.qualificationOther) ? ":" + entity.qualificationOther : "");
			csv.hopeCareerChangeTermStr = GourmetCareeUtil.joinMapValues(hopeCareerChangeTermMap, hopeCareerChangeTermList)
					+ (StringUtils.isNotEmpty(entity.hopeCareerChangeTermOther) ? ":" + entity.hopeCareerChangeTermOther : "");
			csv.pcMailReceiveFlg = pcMailStopFlgMap.get(entity.pcMailStopFlg);
			csv.mobileMailReceiveFlg = mobileMailStopFlgMap.get(entity.mobileMailStopFlg);
			csv.currentStatus = currentSituationKbnMap.get(currentSituationKbn);

			if (entity.registrationDatetime != null) {
				csv.registrationDatetimeStr = YMDHMS_SDF.format(entity.registrationDatetime);
			}


			createSchoolData(entity.id, csv, schoolGradKbnMap);



			Integer advancedMemberKbn = null;
			Integer memberKbn = null;
			if (entity.advancedRegistrationUserId == null) {
				memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
				advancedMemberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
			} else {
				try {
					MMember member = memberService.findById(entity.advancedRegistrationUserId);
					memberKbn = member.memberKbn;
					advancedMemberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_NEW_MEMBER;
				} catch (SNoResultException e) {
					memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
					advancedMemberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
				}
			}

			Integer advancedKbn = GourmetCareeUtil.convertMemberKbnToAdvancedStatusKbn(advancedMemberKbn);
			csv.advancedRegistrationKbnStr = advancedKbnMap.get(advancedKbn);
			csv.memberKbn = memberKbnMap.get(memberKbn);
			try {
				MAdvancedRegistration registration = advancedRegistrationService.findById(entity.advancedRegistrationId);
				csv.advancedRegistrationShortName = registration.advancedRegistrationShortName;
			} catch (SNoResultException e) {
				logicLog.warn(String.format("事前登録データが削除されています。ID:[%d]", entity.advancedRegistrationId));
			}

			createHopeTermStr(csv, entity, hopeCareerChangeTermMap, hopeCareerChangeTermList);


			TAdvancedRegistrationEntryLoginHistory history
			= advancedRegistrationEntryLoginHistoryService.findByAdvancedRegistrationEntryId(entity.id);

			if (history != null) {
				csv.lastLoginDatetime = YMDHMS_SDF.format(history.lastLoginDatetime);
			}

			csv.attendedStatus = attendedStatusKbnMap.get(entity.attendedStatus);


			csvList.add(csv);

		}




		if (CollectionUtils.isEmpty(csvList)) {
			return new ArrayList<AdvancedRegistrationMemberCsv>();
		}

		Collections.sort(csvList, new Comparator<AdvancedRegistrationMemberCsv>() {

			@Override
			public int compare(AdvancedRegistrationMemberCsv o1, AdvancedRegistrationMemberCsv o2) {
				return o1.memberNameKana.compareTo(o2.memberNameKana);
			}
		});

		return csvList;
	}


	/**
	 * 転職希望時期文字列作成
	 * @return
	 */
	protected void createHopeTermStr(AdvancedRegistrationMemberCsv csv, AdvancedRegistrationSearchResultDto entity, Map<Integer, String> hopeCareerMap, List<Integer> kbnList) {
		if (StringUtils.isNotBlank(entity.hopeCareerChangeYear)
				&& StringUtils.isNotBlank(entity.hopeCareerChangeMonth)) {
			csv.hopeCareerChangeTermStr = String.format("%s年 %s月", entity.hopeCareerChangeYear, entity.hopeCareerChangeMonth);
			return;
		}

		if (CollectionUtils.isNotEmpty(kbnList)) {
			csv.hopeCareerChangeTermStr = GourmetCareeUtil.joinMapValues(hopeCareerMap, kbnList);
			return;
		}

		csv.hopeCareerChangeTermStr = "";
	}


	/**
	 * 最終学歴データを作成
	 * @param id
	 * @param csv
	 * @param schoolGradKbnMap
	 */
	private void createSchoolData(Integer id, AdvancedRegistrationMemberCsv csv, final Map<Integer, String> schoolGradKbnMap) {
		try {
			TAdvancedRegistrationEntrySchoolHistory school = schoolService.findByAdvancedRegistrationEntryId(id);
			csv.shoolName = school.schoolName;
			csv.department = school.department;
			csv.graduationKbnStr = schoolGradKbnMap.get(school.graduationKbn);
		} catch (SNoResultException e) {
		}
	}

	/**
	 * 住所を作成します。
	 * @param entity 会員エンティティ
	 * @return 住所
	 */
	private String createAddress(AdvancedRegistrationSearchResultDto entity) {
		StringBuilder sb = new StringBuilder(0);

		if (StringUtils.isNotBlank(entity.zipCd)) {
			sb.append("(〒")
				.append(entity.zipCd)
				.append(")")
				.append(" ");
		}
		if (entity.prefecturesCd != null) {
			sb.append(valueToNameConvertLogic.convertToPrefecturesName(entity.prefecturesCd));
		}

		if (StringUtils.isNotBlank(entity.municipality)) {
			sb.append(entity.municipality);
		}

		if (StringUtils.isNotBlank(entity.address)) {
			sb.append(entity.address);
		}

		return sb.toString();
	}


	/**
	 * 電話番号を作成します
	 * @param entity 会員エンティティ
	 * @return 電話番号
	 */
	private static String createPhoneNo(AdvancedRegistrationSearchResultDto entity) {
		if (StringUtils.isNotBlank(entity.phoneNo1)
				&& StringUtils.isNotBlank(entity.phoneNo2)
				&& StringUtils.isNotBlank(entity.phoneNo3)) {
			StringBuilder sb = new StringBuilder(0);
			sb.append(entity.phoneNo1)
				.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
				.append(entity.phoneNo2)
				.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
				.append(entity.phoneNo3);

			return sb.toString();
		}

		return "";
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(AdvancedMemberCsvProperty property) {

		String filename = null;
		String dateStr = null;
		filename = property.fileName;

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
