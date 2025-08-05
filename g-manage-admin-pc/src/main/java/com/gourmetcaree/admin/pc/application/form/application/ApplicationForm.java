package com.gourmetcaree.admin.pc.application.form.application;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.gourmetcaree.admin.pc.application.dto.application.CareerHistoryDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.entity.TApplicationSchoolHistory;

/**
 * 応募データフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class ApplicationForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1263852372654653730L;


	/** ID */
	public String id;

	/** 応募日時 */
	public String applicationDateTime;

	/** エリアコード */
	public String areaCd;

	/** 顧客名 */
	public String customerName;

	/** 氏名(漢字) */
	public String name;

	/** 氏名(カナ) */
	public String nameKana;

	/** 性別区分 */
	public String sexKbn;

	/** 年齢 */
	public String age;

	/** 生年月日 */
	public Date birthday;

	/** 郵便番号 */
	public String zipCd;

	/** 都道府県 */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 住所 */
	public String address;

	/** 電話番号 */
	public String phoneNo;

	/** PCメールアドレス */
	public String pcMail;

	/** 携帯メールアドレス */
	public String mobileMail;

	/** 希望連絡時間 */
	public String connectTime;

	/** 自己PR */
	public String applicationSelfPr;

	/** 応募先名 */
	public String applicationName;

	/** 雇用形態区分 */
	public String employPtnKbn;

	/** 希望職種(リニューアル前) */
	public String jobKbn;

	/** 希望職種(リニューアル後) */
	public String hopeJob;

	/** 会員登録フラグ */
	public String memberFlg;

	/** 端末区分 */
	public String terminalKbn;

	/** 現在の状況 */
	public String currentEmployedSituationKbn;

	/** 入社可能時期 */
	public String possibleEntryTermKbn;

	/** 取得資格 */
	public String[] qualificationKbnList;

	/** GCWコード */
	public String gcwCd;

	/** いたずら応募 */
	public String mischiefFlg;

	/** 応募学歴 */
	public TApplicationSchoolHistory schoolHistory;

	/** 経歴書リスト */
	public List<CareerHistoryDto> careerHistoryList;

	/**応募区分*/
	public String applicationKbn;

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

	/** その他 */
	public String note;

	/** 店舗名 */
	public String shopName;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetBaseForm();
		id = null;
		applicationDateTime = null;
		areaCd = null;
		customerName = null;
		name = null;
		nameKana = null;
		sexKbn = null;
		birthday = null;
		age = null;
		zipCd = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo = null;
		pcMail = null;
		mobileMail = null;
		connectTime = null;
		applicationSelfPr = null;
		applicationName = null;
		employPtnKbn = null;
		jobKbn = null;
		hopeJob = null;
		memberFlg = null;
		terminalKbn = null;
		possibleEntryTermKbn = null;
		qualificationKbnList = null;
		gcwCd = null;
		mischiefFlg = null;
		schoolHistory = null;
		careerHistoryList = null;
		applicationKbn = null;
		foreignWorkFlg = null;
		qualificationOther = null;
		hopeIndustry = null;
		contactMethod = null;
		firstDesired = null;
		secondDesired = null;
		thirdDesired = null;
		note = null;
		shopName = null;
	}
}