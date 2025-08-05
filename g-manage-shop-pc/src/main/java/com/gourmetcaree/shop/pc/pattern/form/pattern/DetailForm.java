package com.gourmetcaree.shop.pc.pattern.form.pattern;


/**
 * 定型文詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class DetailForm extends PatternForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4265039245979744291L;

	/** 編集のパス */
	public String editPath;

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();
		editPath = null;

	}

}
