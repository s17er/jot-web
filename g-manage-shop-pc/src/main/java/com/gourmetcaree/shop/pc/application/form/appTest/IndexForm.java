package com.gourmetcaree.shop.pc.application.form.appTest;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 応募テスト確認のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class IndexForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1017479970783968704L;

	/** 原稿番号 */
	public String id;

	/** アクセスコード */
	public String accessCd;

	/** バージョン番号 */
	public Long version;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		id = null;
		accessCd = null;
		version = null;
	}
}
