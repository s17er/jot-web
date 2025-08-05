package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.util.CsvUtil;

/**
 * ジャスキル会員のCSV情報を保持するクラスです。
 * @author t_shiroumaru
 *
 */
public class JuskillMemberCsvDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5841818170716212652L;

	/** ID */
	public String id;

	/** 人材紹介登録者No */
	public String juskillMemberNo;

	/** ジャスキル登録日 */
	public String juskillEntryDate;

	/** 氏名(名字のみ) */
	public String familyName;

    /** 生年月日 */
    public String birthday;

    /** 性別区分名 */
    public String sexKbnName;

    /** 路線・最寄り駅 */
    public String route;

    /** 希望業態 */
    public String hopeIndustry;

    /** 希望職種 */
    public String hopeJob;

    /** 希望年収(月収) */
    public String closestStation;

    /** 希望勤務時間・休日数 */
    public String hopeJob2;

    /** 転職時期 */
    public String transferTiming;

    /** ヒアリング担当者 */
    public String hearingRep;

	/**
	 * レコードの文字列を生成して返します。
	 * @return 文字列
	 */
	public String craeteRecordString() {

		// 変換する文字列リストを生成
		List<String> strList = new ArrayList<String>();
		strList.add(juskillMemberNo);							// 人材紹介登録者No
		strList.add(juskillEntryDate);							// 登録日
		strList.add(familyName);								// 氏名(苗字のみ)
		strList.add(birthday);									// 生年月日
		strList.add(sexKbnName);								// 性別
		strList.add(route);										// 路線
		strList.add(hopeIndustry);								// 希望業態
		strList.add(hopeJob);									// 希望職種
		strList.add(closestStation);							// 希望年収(月収)
		strList.add(hopeJob2);									// 希望勤務時間・休日数
		strList.add(transferTiming);							// 転職時期
		strList.add(hearingRep);								// ヒアリング担当者

		return CsvUtil.createDelimiterStr(strList);
	}
}
