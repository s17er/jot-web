package com.gourmetcaree.shop.pc.application.form.observateApplication;

import com.gourmetcaree.common.form.BaseForm;

public class ObservateApplicationBaseForm extends BaseForm {

	/** serialVersionUID */
	private static final long serialVersionUID = 6894701463668099748L;

	/** ID */
	public String id;

	/** メモ */
	public String memo;

	/** version */
	public String version;

	/**
	 * リセット
	 */
	public void resetForm() {

		super.resetBaseForm();

		id = null;
		memo = null;
		version = null;
	}
}
