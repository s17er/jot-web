package com.gourmetcaree.admin.pc.maintenance.form.company;

import java.io.Serializable;

/**
 * 会社詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends CompanyForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1347870218716647645L;

	/** 編集のパス */
	public String editPath;

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		editPath = null;
	}
}