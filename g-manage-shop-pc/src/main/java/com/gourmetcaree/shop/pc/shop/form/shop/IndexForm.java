package com.gourmetcaree.shop.pc.shop.form.shop;

import java.io.Serializable;

/**
 * 登録情報詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class IndexForm extends ShopForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7928881451765138563L;

	/** 顧客名 */
	public String customerName;

	/** 担当者名 */
	public String contactName;

	/** 担当者名(カナ) */
	public String contactNameKana;

	/** ログインID */
	public String loginId;

	/** メインメールアドレス */
	public String mainMail;

	/** サブメールアドレス */
	public String subMail;

	/** サブメール受信フラグ */
	public String submailReceptionFlg;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** メルマガ受信フラグ */
	public String mailMagazineReceptionFlg;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		customerName = null;
		contactName = null;
		contactNameKana = null;
		loginId = null;
		mainMail = null;
		subMail = null;
		submailReceptionFlg = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		mailMagazineReceptionFlg = null;
	}
}
