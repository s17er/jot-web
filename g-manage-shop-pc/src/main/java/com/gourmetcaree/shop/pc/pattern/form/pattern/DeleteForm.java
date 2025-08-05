package com.gourmetcaree.shop.pc.pattern.form.pattern;


/**
 * 定型文削除フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class DeleteForm extends PatternForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6216980481878891334L;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();

	}

	/**
	 * 削除項目以外のリセットを行う
	 */
	public void resetFormWithoutDelValue() {
		displayId = null;
	    existDataFlg = true;
	    hiddenId = null;
		customerId = null;
		sentenceTitle = null;
		body = null;
	}
}
