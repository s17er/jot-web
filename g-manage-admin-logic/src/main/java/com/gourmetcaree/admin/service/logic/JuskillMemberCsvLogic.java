package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.admin.service.dto.JuskillMemberCsvDto;
import com.gourmetcaree.admin.service.property.JuskillMemberCsvProperty;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.VJuskillMemberList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.JuskillMemberListService;

/**
 * ジャスキル会員CSVロジッククラス
 * @author t_shiroumaru
 *
 */
public class JuskillMemberCsvLogic  extends AbstractAdminLogic {

	/** ジャスキル会員サービス */
	@Resource
	protected JuskillMemberListService juskillMemberListService;

	/**
	 * 会員情報CSVを出力する
	 * @param property 会員情報CSVプロパティ
	 * @throws ParseException
	 * @throws WNoResultException
	 * @throws IOException
	 */
	public void outPutCsv(JuskillMemberCsvProperty property) throws WNoResultException, ParseException, IOException {

		// 会員情報を取得
		List<JuskillMemberCsvDto> memberCsvDtoList = convertCsvData(property);

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
	private List<JuskillMemberCsvDto> convertCsvData(JuskillMemberCsvProperty property) throws WNoResultException, ParseException {

		// 会員情報を取得
		List<VJuskillMemberList> memberList = juskillMemberListService.getJuskillMemberDataForCsv(property.map, property.sortKey);

		List<JuskillMemberCsvDto> memberDtoList = new ArrayList<JuskillMemberCsvDto>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// ジャスキル会員情報をDTOへセット
		for (VJuskillMemberList memberEntity : memberList) {
			JuskillMemberCsvDto memberDto = new JuskillMemberCsvDto();

			// 人材紹介登録者No
			memberDto.juskillMemberNo =  Objects.nonNull(memberEntity.juskillMemberNo) ? memberEntity.juskillMemberNo.toString() : "";

			// 登録日
			memberDto.juskillEntryDate = sdf.format(memberEntity.juskillEntryDate);

			// 氏名(苗字のみ)
			memberDto.familyName = StringUtils.isNotBlank(memberEntity.familyName) ? memberEntity.familyName : "" ;

			// 生年月日
			memberDto.birthday = memberEntity.birthday != null ? sdf.format(memberEntity.birthday) : "" ;

			// 性別
			if(memberEntity.sexKbn.equals(MTypeConstants.Sex.MALE)) {
				memberDto.sexKbnName = "男性";
			} else if(memberEntity.sexKbn.equals(MTypeConstants.Sex.FEMALE)) {
				memberDto.sexKbnName = "女性";
			} else {
				memberDto.sexKbnName = "回答なし";
			}

			// 路線・最寄駅
			memberDto.route = StringUtils.isNotBlank(memberEntity.route) ? memberEntity.route : "";

			// 希望業態
			memberDto.hopeIndustry  = StringUtils.isNotBlank(memberEntity.hopeIndustry) ? memberEntity.hopeIndustry : "";

			// 希望職種
			memberDto.hopeJob = StringUtils.isNotBlank(memberEntity.hopeJob) ? memberEntity.hopeJob : "" ;

			// 希望年収(月収)
			memberDto.closestStation = StringUtils.isNotBlank(memberEntity.closestStation) ? memberEntity.closestStation : "" ;

			// 希望勤務時間・休日数
			memberDto.hopeJob2 = StringUtils.isNotBlank(memberEntity.hopeJob2) ? memberEntity.hopeJob2 : "" ;

			// 転職時期
			memberDto.transferTiming = StringUtils.isNotBlank(memberEntity.transferTiming) ? memberEntity.transferTiming : "" ;

			// ヒアリング担当者
			memberDto.hearingRep = StringUtils.isNotBlank(memberEntity.hearingRep) ? memberEntity.hearingRep : "" ;

			memberDtoList.add(memberDto);
		}

		return memberDtoList;
	}

	/**
	 * 会員情報をCSVファイルへ出力する
	 * @param list 会員情報データリスト
	 * @throws IOException
	 */
	private void outputCsvFile(List<JuskillMemberCsvDto> list, JuskillMemberCsvProperty property) throws IOException {

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

				for (JuskillMemberCsvDto memberCsvDto : list) {
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
	private String createHeader(JuskillMemberCsvProperty property) {

		StringBuilder sb = new StringBuilder();
		sb.append(property.header);

		return sb.toString();
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 * @throws IOException
	 * @throws IOException
	 */
	private static String createOutputFilePath(JuskillMemberCsvProperty property) {

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
