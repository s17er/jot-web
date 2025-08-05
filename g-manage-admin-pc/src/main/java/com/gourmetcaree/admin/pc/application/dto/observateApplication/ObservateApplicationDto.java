package com.gourmetcaree.admin.pc.application.dto.observateApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * GCWDTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ObservateApplicationDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4627568364807738278L;

	/** 応募ID */
	public int id;

	/** エリアコード */
	public String areaCd;

	/** 顧客名 */
	public String customerName;

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

	/** 氏名 */
	public String name;

	/** 性別 */
	public String sexKbn;

	/** 年齢 */
	public String age;

	/** 応募日時 */
	public String applicationDateTime;

	/** 会員フラグ */
	public String memberFlg;

	/** 端末 */
	public String terminalKbn;

	/** 詳細のパス */
	public String detailPath;

	/** 店舗見学区分 */
	public int observationKbn;


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