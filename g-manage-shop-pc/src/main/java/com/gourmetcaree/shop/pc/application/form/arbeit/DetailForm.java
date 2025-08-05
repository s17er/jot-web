package com.gourmetcaree.shop.pc.application.form.arbeit;

import java.util.Date;

import com.gourmetcaree.shop.pc.application.form.application.ApplicationForm;

/**
 * 応募者詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class DetailForm extends ApplicationForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7970172543339323045L;

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

	/** 電話番号 */
	public String phoneNo;

	/** メールアドレス */
	public String mailAddress;
	
	/** 現在の職業 */
	public String currentJobKbn;
	
	/** 勤務可能時期区分 */
	public Integer possibleEntryTermKbn;
	
	/** 飲食店勤務の経験区分 */
	public Integer foodExpKbn;

	/** 希望連絡時間 */
	public String connectionTime;

	/** 顧客ID */
	public Integer customerId;

	/** 応募先 */
	public String applicationName;

	/** 希望職種 */
	public String applicationJob;

	/** 応募自己PR */
	public String applicationSelfPr;


	/** メモ */
	public String memo;

	/** 応募の初回時に送信されたメールのID */
	public int firstMailId = 0;

	/**
	 * 初回メールが存在しているかどうかを取得する。
	 * @return true:存在している, false:削除されている
	 */
	public boolean isFirstMailExist() {
		 return (firstMailId != 0) ? true : false;
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
		age = null;
		zipCd = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo = null;
		mailAddress = null;
		connectionTime = null;
		customerId = null;
		applicationName = null;
		applicationJob = null;
		applicationSelfPr = null;
		memo = null;
		firstMailId = 0;
	}
}
