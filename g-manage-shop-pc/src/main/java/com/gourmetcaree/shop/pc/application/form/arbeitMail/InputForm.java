package com.gourmetcaree.shop.pc.application.form.arbeitMail;

import jp.co.whizz_tech.common.sastruts.annotation.StrictRequired;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.UUID;

/**
 * アルバイトメール返信入力フォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends ArbeitMailBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1440341728260315010L;

	/** 返信元のメールID */
	public String[] originalMailId;

	/** 返信するメールの応募ID */
	public String[] arbeitApplicationId;

	/** 遷移元のページ */
	public String fromPageKbn;

	/** 送信者名 */
	public String[] fromName;

	/** 件名 */
	@StrictRequired
	public String subject;

	/** 本文 */
	@StrictRequired
	public String mailBody;

	/** 定型文番号 */
	public String sentenceId;

	/** ランダムキー */
	public String randomKey;

	/** hiddenに使用するランダムキー */
	public String hiddenRandomKey;

	/** 一括送信フラグ */
	public boolean lumpSendFlg;

	@Override
	public void resetForm() {
		super.resetBaseForm();
		originalMailId = null;
		arbeitApplicationId = null;
		fromPageKbn = null;
		fromName = null;
		subject = null;
		mailBody = null;
		sentenceId = null;
		// ランダムキー
		randomKey = null;
		// hiddenに使用するランダムキー
		hiddenRandomKey = null;
		lumpSendFlg = false;
	}

	/**
	 * ランダムキーを作成
	 */
	public void createRandomKey() {
		String random = UUID.create();
		randomKey = random;
		hiddenRandomKey = random;
	}

	/**
	 * リセットを行う
	 */
	public void resetForLumpSend() {
		super.resetBaseForm();
		fromName = null;
		subject = null;
		mailBody = null;
		sentenceId = null;
		lumpSendFlg = false;
	}
}
