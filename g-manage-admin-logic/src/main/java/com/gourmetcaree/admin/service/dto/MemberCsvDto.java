package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.CsvUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 会員のCSV情報を保持するクラスです。
 * @author Takahiro Kimura
 */
public class MemberCsvDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5841818170716212652L;

	/** ID */
	public String id;

	/** エリアコード */
	public String[] areaCdList;

    /** エリアコード名 */
    public String areaCdName;

	/** 登録日時 */
	public String insertDateTime;

	/** 最終ログイン日時 */
	public String lastLoginDateTime;

    /** 会員名 */
    public String memberName;

    /** 会員名（カナ） */
    public String memberNameKana;

    /** ログインメールアドレス */
    public String loginId;

    /** サブメールアドレス */
    public String subMailAddress;

    /** 性別区分 */
    public String sexKbn;

    /** 性別区分名 */
    public String sexKbnName;

    /** 生年月日 */
    public String birthday;

    /** 電話番号1 */
    public String phoneNo1;

    /** 電話番号2 */
    public String phoneNo2;

    /** 電話番号3 */
    public String phoneNo3;

    /** 電話番号 */
    public String phoneNo;

    /** 郵便番号 */
    public String zipCd;

    /** 都道府県コード */
    public String prefecturesCd;

    /** 都道府県コード名 */
    public String prefecturesCdName;

    /** 市区町村 */
    public String municipality;

    /** 住所2 */
    public String address;

    /** 取得資格 */
    public String qualification;

    /** 取得資格コード配列 */
    public String[] qualificationArray;

    /** 取得資格その他 */
    public String qualificationOther;

    /** 飲食業界経験区分 */
    public String foodExpKbn;

    /** 飲食業界経験区分名 */
    public String foodExpKbnName;

    /** 海外勤務フラグ */
    public String foreignWorkFlg;

    /** 海外勤務フラグ名 */
    public String foreignWorkFlgName;

    /** スカウト自己PR */
    public String scoutSelfPr;

    /** 応募自己PR */
    public String applicationSelfPr;

    /** 新着求人情報受信フラグ */
    public String jobInfoReceptionFlg;

    /** 新着求人情報受信フラグ名 */
    public String jobInfoReceptionFlgName;

    /** メルマガ受信フラグ */
    public String mailMagazineReceptionFlg;

    /** メルマガ受信フラグ名 */
    public String mailMagazineReceptionFlgName;

    /** スカウトメール受信フラグ */
    public String scoutMailReceptionFlg;

    /** スカウトメール受信フラグ名 */
    public String scoutMailReceptionFlgName;

    /** 希望職種 */
    public String job;

    /** 希望職種コード配列 */
    public String[] jobArray;

    /** 希望業種 */
    public String industry;

    /** 希望業種コード配列 */
    public String[] industryArray;

    /** 希望勤務地 */
    public String workLocation;

    /** 希望勤務地コード配列 */
    public String[] workLocationArray;

    /** 希望勤務地コード配列(海外) */
    public String[] foreignWorkLocationArray;

    /** 雇用形態区分 */
    public String[] employPtnKbnArray;

    /** 雇用形態区分名 */
    public String employPtnKbnName;

    /** 転勤フラグ */
    public String transferFlg;

    /** 転勤フラグ名 */
    public String transferFlgName;

    /** 深夜勤務フラグ */
    public String midnightShiftFlg;

    /** 深夜勤務フラグ名 */
    public String midnightShiftFlgName;

    /** 希望年収区分 */
    public String salaryKbn;

    /** 希望年収区分名 */
    public String salaryKbnName;

    /** メインメール送信停止フラグ */
    public String mainMailStopFlg;

    /** メインメール送信停止フラグ名 */
    public String mainMailStopFlgName;

    /** サブメール送信停止フラグ */
    public String subMailStopFlg;

    /** サブメール送信停止フラグ名 */
    public String subMailStopFlgName;

    /** 端末区分 */
    public String terminalKbn;

    /** 端末区分名 */
    public String terminalKbnName;

    /** 会員区分 */
    public String memberKbn;

    /** 会員区分名 */
    public String memberKbnName;

    /** 旧会員ID */
    public String oldMemberId;

    /** ジャスキル登録フラグ */
    public String juskillFlg;

    /** ジャスキル登録フラグ名 */
    public String juskillFlgName;

    /** ジャスキル連絡フラグ */
    public String juskillContactFlg;

    /** ジャスキル連絡フラグ名 */
    public String juskillContactFlgName;

    /** 最終更新日時 */
    public String lastUpdateDatetime;

    /** 学校名 */
    public String schoolName;

    /** 学部・学科 */
    public String department;

    /** 状況区分 */
    public String graduationKbn;

    /** 状況区分名 */
    public String graduationKbnName;

    /** 職歴 */
    public List<CareerHistoryCsvDto> careerHistoryCsvDtoList;


    /**
     * 出力形式へ変換
     * @param valueToNameConvertLogic
     */
    public void convertToOutPutType(ValueToNameConvertLogic valueToNameConvertLogic) {

    	// エリアを変換
    	if (!ArrayUtils.isEmpty(areaCdList)) {
    		this.areaCdName = valueToNameConvertLogic.convertToPrefecturesName(areaCdList);
    	} else {
    		this.areaCdName = "";
    	}

    	// 性別を変換
    	this.sexKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, new String[] {sexKbn});

    	// 電話番号を変換
    	if (StringUtils.isNotEmpty(phoneNo1)) {
    		this.phoneNo = phoneNo1 + "-" + phoneNo2 + "-" + phoneNo3;
    	}

    	// 都道府県を変換
    	this.prefecturesCdName = valueToNameConvertLogic.convertToPrefecturesName(new String[]{prefecturesCd});

    	// 資格を変換
    	if (qualificationArray != null && qualificationArray.length > 0) {
    		this.qualification = valueToNameConvertLogic.convertToTypeName(MTypeConstants.QualificationKbn.TYPE_CD, qualificationArray);
    		if (StringUtils.isNotEmpty(qualificationOther)) {
    			this.qualification += " " + qualificationOther;
    		}
    	} else {
    		this.qualification = "";
    	}

    	// 飲食業界経験を変換
    	if (StringUtils.isNotEmpty(foodExpKbn)) {
    		this.foodExpKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.FoodExpKbn.TYPE_CD, new String[]{foodExpKbn});
    	} else {
    		this.foodExpKbnName = "";
    	}

    	// 海外勤務経験を変換
    	if (StringUtils.isNotEmpty(foreignWorkFlg)) {
    		this.foreignWorkFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ForeignWorkFlg.TYPE_CD, new String[]{foreignWorkFlg});
    	} else {
    		this.foreignWorkFlgName = "";
    	}

    	// 新着求人受信を変換
    	this.jobInfoReceptionFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobInfoReceptionFlg.TYPE_CD, new String[]{jobInfoReceptionFlg});

    	// メルマガ受信を変換
    	this.mailMagazineReceptionFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MailMagazineReceptionFlg.TYPE_CD, new String[]{mailMagazineReceptionFlg});

    	// スカウトメール受信を変換
    	this.scoutMailReceptionFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ScoutMailReceptionFlg.TYPE_CD, new String[]{scoutMailReceptionFlg});

    	// 希望職種を変換
    	if (jobArray != null && jobArray.length > 0) {
    		this.job = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, jobArray);
    	} else {
    		this.job = "";
    	}

    	// 希望業種を変換
    	if (industryArray != null && industryArray.length > 0) {
    		this.industry = valueToNameConvertLogic.convertToTypeName(MTypeConstants.IndustryKbn.TYPE_CD, industryArray);
    	} else {
    		this.industry = "";
    	}

    	if (!ArrayUtils.isEmpty(workLocationArray)) {
    		StringBuilder sb = new StringBuilder("");
   			if (!ArrayUtils.isEmpty(workLocationArray)) {
   				// 希望勤務地を変換
   				sb.append(valueToNameConvertLogic.convertToTypeNameForAllArea(workLocationArray));
   			}
    		this.workLocation = sb.toString();
    	}

    	// 雇用形態を変換
    	if (!ArrayUtils.isEmpty(employPtnKbnArray)) {
    		this.employPtnKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, employPtnKbnArray);
    	} else {
    		this.employPtnKbnName = "";
    	}

    	// 転勤フラグを変換
    	if (StringUtils.isNotEmpty(transferFlg)) {
    		this.transferFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TransferFlg.TYPE_CD, new String[]{transferFlg});
    	} else {
    		this.transferFlgName = "";
    	}

    	// 深夜勤務フラグを変換
    	if (StringUtils.isNotEmpty(midnightShiftFlg)) {
    		this.midnightShiftFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MidnightShiftFlg.TYPE_CD, new String[]{midnightShiftFlg});
    	} else {
    		this.midnightShiftFlgName = "";
    	}

    	// 希望年収を変換
    	if (StringUtils.isNotEmpty(salaryKbn)) {
    		this.salaryKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalaryKbn.TYPE_CD, new String[]{salaryKbn});
    	} else {
    		this.salaryKbnName = "";
    	}

    	// PCメール配信を変換
    	this.mainMailStopFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PcMailStopFlg.TYPE_CD, new String[]{mainMailStopFlg});

    	// モバイルメール配信を変換
    	this.subMailStopFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MobileMailStopFlg.TYPE_CD, new String[]{subMailStopFlg});

    	// 端末区分を変換
    	this.terminalKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TerminalKbn.TYPE_CD, new String[]{terminalKbn});

    	// 会員区分を変換
    	this.memberKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MemberKbn.TYPE_CD, new String[]{memberKbn});

    	// ジャスキル登録フラグを変換
    	this.juskillFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JuskillFlg.TYPE_CD, new String[]{juskillFlg});

    	// ジャスキル連絡フラグを変換
    	this.juskillContactFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JuskillContactFlg.TYPE_CD, new String[]{juskillContactFlg});

    	// 状況区分を変換
    	if (StringUtils.isNotEmpty(graduationKbn)) {
    		this.graduationKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.GraduationKbn.TYPE_CD, new String[]{graduationKbn});
    	} else {
    		this.graduationKbnName = "";
    	}

    	// 職歴を変換
    	List<CareerHistoryCsvDto> dtoList = new ArrayList<CareerHistoryCsvDto>();
    	for (CareerHistoryCsvDto dto : careerHistoryCsvDtoList) {

    		// 職種
    		if (dto.jobArray != null && dto.jobArray.length > 0) {
    			dto.job = valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, dto.jobArray);
    		} else {
    			dto.job = "";
    		}

    		// 業態
    		if (dto.industryArray != null && dto.industryArray.length > 0) {
    			dto.industry = valueToNameConvertLogic.convertToTypeName(MTypeConstants.IndustryKbn.TYPE_CD, dto.industryArray);
    		} else {
    			dto.industry = "";
    		}

    		dtoList.add(dto);
    	}

    	this.careerHistoryCsvDtoList = dtoList;
    }

	/**
	 * レコードの文字列を生成して返します。
	 * @return 文字列
	 */
	public String craeteRecordString() {

		// 変換する文字列リストを生成
		List<String> strList = new ArrayList<String>();
		strList.add(id);											// 会員ID
		strList.add(areaCdName);									// エリア
		strList.add(insertDateTime);								// 登録日
		strList.add(lastLoginDateTime);								// 最終ログイン日
		strList.add(memberName);									// 会員名
		strList.add(memberNameKana);								// 会員名(カナ)
		strList.add(loginId);										// ログインメールアドレス
		strList.add(subMailAddress);								// サブメールアドレス
		strList.add(sexKbnName);									// 性別
		strList.add(birthday);										// 生年月日
		strList.add(phoneNo);										// 電話番号
		strList.add(zipCd);											// 郵便番号
		strList.add(prefecturesCdName);								// 都道府県
		strList.add(municipality);									// 市区町村
		strList.add(address);										// 住所2
		strList.add(qualification);									// 資格
		strList.add(foodExpKbnName);								// 飲食業界経験年数
		strList.add(foreignWorkFlgName);							// 海外勤務経験
		strList.add(scoutSelfPr);									// スカウト用自己PR
		strList.add(applicationSelfPr);								// 応募用自己PR
		strList.add(jobInfoReceptionFlgName);						// 新着求人情報受信
		strList.add(mailMagazineReceptionFlgName);					// メルマガ受信
		strList.add(scoutMailReceptionFlgName);						// スカウトメール受信
		strList.add(job);											// 希望職種
		strList.add(industry);										// 希望業種
		strList.add(workLocation);									// 希望勤務地
		strList.add(employPtnKbnName);								// 希望雇用形態
		strList.add(transferFlgName);								// 転勤
		strList.add(midnightShiftFlgName);							// 深夜勤務
		strList.add(salaryKbnName);									// 希望年収
		strList.add(mainMailStopFlgName);							// PCメール配信
		strList.add(subMailStopFlgName);							// モバイルメール配信
		strList.add(terminalKbnName);								// 登録端末
		strList.add(memberKbnName);									// 会員区分
		strList.add(oldMemberId);									// 旧会員ID
		strList.add(juskillFlgName);								// ジャスキル登録
		strList.add(juskillContactFlgName);							// ジャスキル連絡（転職窓口相談）
		strList.add(lastUpdateDatetime);							// 最終更新日時
		strList.add(schoolName);									// 学校名
		strList.add(department);									// 学部・学科
		strList.add(graduationKbnName);								// 状況

		for (CareerHistoryCsvDto dto : careerHistoryCsvDtoList) {
			strList.add(dto.companyName);								// 会社名
			strList.add(dto.workTerm);									// 勤務期間
			strList.add(dto.job);										// 職種
			strList.add(dto.industry);									// 業態
			strList.add(dto.businessContent);							// 業務内容
			strList.add(dto.monthSales);								// 月売上
			strList.add(dto.seat);										// 客席数・坪数

		}

		return CsvUtil.createDelimiterStr(strList);
	}


}
