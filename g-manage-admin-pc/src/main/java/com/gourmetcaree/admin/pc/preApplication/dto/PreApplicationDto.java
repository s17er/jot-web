package com.gourmetcaree.admin.pc.preApplication.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * @version 1.0
 */
public class PreApplicationDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2231536425519469913L;

	/** 応募ID */
	public int id;

	/** エリアコード */
	public String areaCd;

	/** 顧客名 */
	public String customerName;

	/** 氏名 */
	public String name;

	/** 性別 */
	public String sexKbn;

	/** 生年月日（リニューアル後は生年月日で管理） */
	public Date birthday;

	/** 年齢 */
	public String age;

	/** 都道府県 */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 住所 */
	public String address;

	/** 希望職種 */
	public String hopeJob;

	/** 希望職種（リニューアル後） */
	public Integer jobKbn;

	/** 雇用形態 */
	public String employPtnKbn;

	/** 原稿名 */
	public String manuscriptName;

	/** 業種区分1 */
	public int industryKbn1;

	/** 業種区分2 */
	public int industryKbn2;

	/** 業種区分3 */
	public int industryKbn3;

	/** リニューアル後の業態 */
	public List<Integer> industryKbnList;

	/** 応募日時 */
	public String applicationDateTime;

	/** 会員フラグ */
	public String memberFlg;

	/** 端末 */
	public String terminalKbn;

	/** GCWコード */
	public String gcwCd;

	/** 詳細のパス */
	public String detailPath;

	/** 応募区分 */
	public int applicationKbn;


	 /**
     * 業種のカラム1～3に入った値を配列に変換して取得します。
     * @return
     */
    public int[] getIndustryKbnList() {
		List<Integer> industryList = new ArrayList<Integer>();

		if (CollectionUtils.isNotEmpty(this.industryKbnList)) {
			industryList = this.industryKbnList;
		}

		if (industryKbn1 != 0) {
			industryList.add(industryKbn1);
		}
		if (industryKbn2 != 0) {
			industryList.add(industryKbn2);
		}
		if (industryKbn3 != 0) {
			industryList.add(industryKbn3);
		}

		return ArrayUtils.toPrimitive(industryList.toArray(new Integer[0]));
    }


}