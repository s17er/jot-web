package com.gourmetcaree.shop.pc.application.form.application;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.gourmetcaree.db.common.entity.TApplicationSchoolHistory;
import com.gourmetcaree.db.common.entity.TSchoolHistory;
import com.gourmetcaree.shop.pc.application.dto.application.CareerHistoryDto;

/**
 * 応募者詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class DetailForm extends ApplicationForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7726065777340883277L;

	/** 応募日時 */
	public Date applicationDatetime;

	/** メールID */
	public Integer mailId;

	/** 氏名 */
	public String name;

	/** 氏名（カナ） */
	public String nameKana;

	/** 性別区分 */
	public Integer sexKbn;

	/** 生年月日 */
	public Date birthday;

	/** 年齢 */
	public Integer age;

	/** 郵便番号 */
	public String zipCd;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** 住所（番地以下の住所情報） */
	public String address;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** PCメールアドレス */
	public String pcMail;

	/** 携帯メールアドレス */
	public String mobileMail;

	/** 希望連絡時間 */
	public String connectTime;

	/** 雇用形態区分 */
	public Integer employPtnKbn;

	/** 顧客ID */
	public Integer customerId;

	/** 応募先 */
	public String applicationName;

	/** 希望職種（リニューアル後のプルダウン） */
	public Integer jobKbn;

	/** 希望職種（リニューアルで廃止） */
	public String hopeJob;

	/** 応募自己PR */
	public String applicationSelfPr;

	/** 経歴書表示フラグ */
	public Integer jobHistoryDisplayFlg;

	/** Web履歴書 */
	public TSchoolHistory schoolHistory;

	/** 経歴書リスト */
	public List<CareerHistoryDto> careerHistoryList;

	/** Web履歴書(非会員) */
	public TApplicationSchoolHistory applicationSchoolHistory;

	/** 経歴書リスト */
	public List<CareerHistoryDto> applicationCareerHistoryList;

	/** メモ */
	public String memo;

	/** 応募の初回時に送信されたメールのID */
	public int firstMailId = 0;

	/** 経験役職区分 */
	public Integer expManagerKbn;

	/** 経験年数区分 */
	public Integer expManagerYearKbn;

	/** 経験役職人数区分 */
	public Integer expManagerPersonsKbn;

	/** 飲食業界経験年数 */
	public Integer foodExpKbn;

	/** 取得資格 */
	public String[] qualificationKbnList;

	/** 海外勤務経験 */
	public Integer foreignWorkFlg;

	/** 希望業態 */
	public String hopeIndustry;

	/** 第一希望 */
	public String firstDesired;

	/** 第二希望 */
	public String secondDesired;

	/** 第三希望 */
	public String thirdDesired;

	/**その他希望連絡方法*/
	public String contactMethod;

	/** その他 */
	public String note;

	/**
	 * 初回メールが存在しているかどうかを取得する。
	 * @return true:存在している, false:削除されている
	 */
	public boolean isFirstMailExist() {
		 return (firstMailId != 0) ? true : false;
	}

	/**
	 * Web履歴書があるかどうか
	 */
	public boolean isSchoolHistoryExist() {
		return schoolHistory != null;
	}

	/**
	 * Web履歴書があるかどうか(非会員)
	 */
	public boolean isApplicationSchoolHistoryExist() {
		return applicationSchoolHistory != null;
	}

	/**
	 * 経歴書リストがあるかどうか
	 * @return
	 */
	public boolean isCareerHistoryListExist() {
		return CollectionUtils.isNotEmpty(careerHistoryList);
	}

	/**
	 * 経歴書リストがあるかどうか(非会員)
	 * @return
	 */
	public boolean isApplicationCareerHistoryListExist() {
		return CollectionUtils.isNotEmpty(applicationCareerHistoryList);
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		applicationDatetime = null;
		mailId = null;
		name = null;
		nameKana = null;
		sexKbn = null;
		birthday = null;
		age = null;
		zipCd = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		pcMail = null;
		mobileMail = null;
		connectTime = null;
		employPtnKbn = null;
		customerId = null;
		applicationName = null;
		jobKbn = null;
		hopeJob = null;
		applicationSelfPr = null;
		jobHistoryDisplayFlg = null;
		schoolHistory = null;
		careerHistoryList = null;
		applicationSchoolHistory = null;
		applicationCareerHistoryList = null;
		memo = null;
		firstMailId = 0;
		expManagerKbn = null;
		expManagerYearKbn = null;
		expManagerPersonsKbn =null;
		foodExpKbn = null;
		qualificationKbnList = null;
		foreignWorkFlg = null;
		hopeIndustry = null;
		firstDesired = null;
		secondDesired = null;
		thirdDesired = null;
		contactMethod = null;
		note = null;
	}
}
