package com.gourmetcaree.admin.pc.preApplication.form.preApplication;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.admin.pc.preApplication.dto.PreApplicationCareerHistoryDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.entity.TPreApplicationSchoolHistory;

public class PreApplicationForm extends BaseForm implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	public String id;

	/** 応募日時 */
	public String applicationDateTime;

	/** エリアコード */
	public String areaCd;

	/** 顧客名 */
	public String customerName;

	/** 原稿名 */
	public String manuscriptName;

	/** 名前 */
	public String name;

	/** 性別区分 */
	public String sexKbn;

	/** 年齢 */
	public String age;

	/** メールアドレス */
	public String pcMail;

	/** モバイルメールアドレス */
	public String mobileMail;

	/** 都道府県 */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 希望連絡時間 */
	public String connectTime;

	/** 自己PR */
	public String applicationSelfPr;

	/** 雇用形態区分 */
	public String employPtnKbn;

	/** 希望職種(リニューアル前) */
	public String jobKbn;

	/** 端末区分 */
	public String terminalKbn;

	/** 取得資格 */
	public String[] qualificationKbnList;

	/** 応募学歴 */
	public TPreApplicationSchoolHistory schoolHistory;

	/** 経歴書リスト */
	public List<PreApplicationCareerHistoryDto> careerHistoryList;

	/** 飲食業界経験年数 */
	public Integer foodExpKbn;

	/** マネジメント経験(役職) */
	public Integer expManagerKbn;

	/** マネジメント経験(経験年数) */
	public Integer expManagerYearKbn;

	/** マネジメント経験(人数) */
	public Integer expManagerPersonsKbn;

	/** 海外勤務経験 */
	public Integer foreignWorkFlg;

	/** その他資格 */
	public String qualificationOther;

	/** 希望業態 */
	public String hopeIndustry;

	/** その他希望連絡方法 */
	public String contactMethod;

	/** 第一希望 */
	public String firstDesired;

	/** 第二希望 */
	public String secondDesired;

	/** 第三希望 */
	public String thirdDesired;

	/** 希望年収 */
	public String salary;

	/** 転勤 */
	public String transfer;

	/** 深夜勤務 */
	public String midnightShift;

	/** その他 */
	public String note;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetBaseForm();
		id = null;
		applicationDateTime = null;
		areaCd = null;
		customerName = null;
		manuscriptName = null;
		name = null;
		sexKbn = null;
		age = null;
		prefecturesCd = null;
		municipality = null;
		connectTime = null;
		applicationSelfPr = null;
		employPtnKbn = null;
		jobKbn = null;
		terminalKbn = null;
		qualificationKbnList = null;
		schoolHistory = null;
		careerHistoryList = null;
		foreignWorkFlg = null;
		qualificationOther = null;
		hopeIndustry = null;
		contactMethod = null;
		firstDesired = null;
		secondDesired = null;
		thirdDesired = null;
		salary = null;
		transfer = null;
		midnightShift = null;
		note = null;
	}

}
