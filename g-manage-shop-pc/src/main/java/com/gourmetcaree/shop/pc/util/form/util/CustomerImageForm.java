package com.gourmetcaree.shop.pc.util.form.util;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 素材処理ユーティリティフォーム
 * @author Takahiro Kimura
 * @version 1.0
 */
public class CustomerImageForm extends BaseForm {

	/** シリアルUID */
	private static final long serialVersionUID = -4216323445863914957L;

	/** ID */
	public String id;

	/** ユニークキー */
//	public String uniqueKey;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		id = null;
	}
}