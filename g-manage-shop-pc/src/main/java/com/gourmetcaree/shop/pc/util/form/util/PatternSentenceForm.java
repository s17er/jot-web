package com.gourmetcaree.shop.pc.util.form.util;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 定型文のユーティリティアクション用フォームです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class PatternSentenceForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 821446105775826173L;

	/** 定型文ID */
	public String limitValue;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		limitValue = null;
	}
}
