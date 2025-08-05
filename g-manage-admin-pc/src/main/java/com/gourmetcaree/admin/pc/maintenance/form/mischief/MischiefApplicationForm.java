package com.gourmetcaree.admin.pc.maintenance.form.mischief;

import java.io.Serializable;

import com.gourmetcaree.common.form.BaseForm;

import jp.co.whizz_tech.common.sastruts.annotation.EmailType;
import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * いたずら応募フォーム
 * @author Aquarius
 *
 */
public abstract class MischiefApplicationForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	public String id;

	/** 氏名 */
	public String name;

	/** 氏名カナ */
	public String nameKana;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 市区町村 */
	public String municipality;

	/** その他住所 */
	public String address;

	/** 電話番号1 */
	@IntegerHankakuType
	public String phoneNo1;

	/** 電話番号2 */
	@IntegerHankakuType
	public String phoneNo2;

	/** 電話番号3 */
	@IntegerHankakuType
	public String phoneNo3;

	/** メールアドレス */
	@EmailType
	public String mailAddress;

	/** 会員フラグ */
	public String memberFlg;

	/** 端末区分 */
	public String terminalKbn;

	/** 自己PR */
	public String applicationSelfPr;

	/** バージョン番号 */
	public Long version;

	public void resetForm() {
		id = null;
		name = null;
		nameKana = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		mailAddress = null;
		memberFlg = null;
		terminalKbn = null;
		applicationSelfPr = null;
		version = null;
	}
}