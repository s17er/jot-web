package com.gourmetcaree.admin.pc.application.form.observateApplication;

import java.io.Serializable;
import java.util.Date;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 応募データフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class ObservateApplicationForm extends BaseForm implements Serializable {

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

	/** 生年月日 */
	public Date birthday;

	/** 年齢 */
	public String age;

	/** 住所 */
	public String address;

	/** 電話番号 */
	public String phoneNo;

	/** PCメールアドレス */
	public String pcMail;

	/** 携帯メールアドレス */
	public String mobileMail;

	/** 応募先名 */
	public String applicationName;

	/** 会員登録フラグ */
	public String memberFlg;

	/** 端末区分 */
	public String terminalKbn;

	/** 入社可能時期 */
	public String possibleEntryTermKbn;

	/** 店舗見学区分 */
	public String observationKbn;

	/** 内容 */
	public String contents;

	/** いたずら応募 */
	public String mischiefFlg;

	public void resetObserbateBaseForm() {
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
		address = null;
		phoneNo = null;
		pcMail = null;
		mobileMail = null;
		applicationName = null;
		memberFlg = null;
		terminalKbn = null;
		possibleEntryTermKbn = null;
		observationKbn = null;
		mischiefFlg = null;
		contents = null;

	}


}