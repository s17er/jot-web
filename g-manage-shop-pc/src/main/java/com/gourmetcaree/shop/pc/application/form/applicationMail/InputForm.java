package com.gourmetcaree.shop.pc.application.form.applicationMail;

import jp.co.whizz_tech.common.sastruts.annotation.StrictRequired;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * メール入力フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends ApplicationMailForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4298503741301406394L;

	/** 返信元のメールID */
	public String[] originalMailId;

	/** 返信するメールの応募ID */
	public String[] applicationId;

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

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetApplicationMailBaseForm();
		originalMailId = null;
		applicationId = null;
		fromPageKbn = null;
		fromName = null;
		subject = null;
		mailBody = null;
		sentenceId = null;
	}

	/**
	 * リセットを行う
	 */
	public void resetForLumpSend() {
		super.resetApplicationMailBaseForm();
		fromName = null;
		subject = null;
		mailBody = null;
		sentenceId = null;
	}

}
