package com.gourmetcaree.shop.pc.pattern.form.pattern;

import jp.co.whizz_tech.common.sastruts.annotation.StrictRequired;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 定型文フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class PatternForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1641299806570724922L;

	/** ID */
	public String id;

	/** 顧客ID */
	public String customerId;

	/** 定型文タイトル */
	@StrictRequired
	public String sentenceTitle;

	/** 本文 */
	@StrictRequired
	public String body;

	/** バージョン番号 */
	public Long version;

	public void resetForm() {
		super.resetBaseForm();
		id = null;
		customerId = null;
		sentenceTitle = null;
		body = null;
		version = null;
	}
}
