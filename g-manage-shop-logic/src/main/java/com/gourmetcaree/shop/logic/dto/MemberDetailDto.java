package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

/**
 * 会員DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class MemberDetailDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5226642801652181637L;

	/** 会員ID */
	public String id;

	/** 最終ログイン日時 */
	public String lastLoginDatetime;

	/** 会員名 */
	public String memberName;

	/** 会員名(カナ) */
	public String memberNameKana;

	/** エリアコード */
	public String areaCd;

	/** 年齢 */
	public String age;

	/** 性別区分 */
	public String sexKbn;

	/** 都道府県 */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 資格 */
	public String[] qualification;

	/** 取得資格その他 */
	public String qualificationOther;

	/** 飲食業界経験年数区分 */
	public String foodExpKbn;

	/** 海外勤務経験区分 */
	public String foreignWorkFlg;

	/** スカウト自己PR */
	public String scoutSelfPr;

	/** スカウトメール受信フラグ */
	public String scoutMailReceptionFlg;

	/** 希望職種区分 */
	public String[] job;

	/** 希望業種区分 */
	public String[] industry;

	/** 希望勤務地区分 */
	public String[] workLocation;

	/** 希望雇用形態区分 */
	public List<String> employPtnKbnList;

	/** 転勤フラグ */
	public String transferFlg;

	/** 深夜勤務フラグ */
	public String midnightShiftFlg;

	/** 希望年収区分 */
	public String salaryKbn;

	/** 学校名 */
	public String schoolName;

	/** 学部・学科 */
	public String department;

	/** 状況 */
	public String graduationKbn;

	/** 職歴リスト */
	public List<CareerDto> careerList;

	/** 一覧へ戻るパス */
	public String backListPath;

	/** キープBOXに追加パス */
	public String keepPath;

	/** 足あとをつけるパス */
	public String footPrintPath;

	/** 気になる済フラグ */
	public boolean footPrintFlg;

	/** スカウト可否フラグ */
	public boolean scoutMailFlg;

	/** キープ済みフラグ */
	public boolean keepFlg;

	/** スカウトメール数 */
	public int scoutCount;

	/** スカウト使用フラグ */
	public boolean scoutUseFlg;

	/** 会員エリアリスト */
	public String[] areaList;

	/** スカウト受取区分 */
	public Integer scoutReceiveKbn;

	/** 希望勤務地（prefectures_cd,市区町村名） */
	public Map<Integer, List<String>> hopeCityMap;

	/** 旧希望勤務地コードリスト */
	public List<Integer> oldHopeCityCdList;

	/** 経験役職区分 */
	public Integer expManagerKbn;

	/** 経験年数区分 */
	public Integer expManagerYearKbn;

	/** 経験役職人数区分 */
	public Integer expManagerPersonsKbn;


	public boolean isWorkLocationExist() {
		return !ArrayUtils.isEmpty(workLocation);
	}

}