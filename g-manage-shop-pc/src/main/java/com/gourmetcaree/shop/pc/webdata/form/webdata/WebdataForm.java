package com.gourmetcaree.shop.pc.webdata.form.webdata;

import java.io.Serializable;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 求人原稿一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class WebdataForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3892473934559711034L;

	public void resetForm() {
		super.resetBaseForm();
	}

}
