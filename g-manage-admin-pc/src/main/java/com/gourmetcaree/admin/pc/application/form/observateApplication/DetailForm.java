package com.gourmetcaree.admin.pc.application.form.observateApplication;

import java.io.Serializable;

/**
 * 応募データ詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends ObservateApplicationForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4892817026368113492L;


	/**
	 * リセットを行う
	 */
	public void resetForm() {

		resetObserbateBaseForm();

	}

}