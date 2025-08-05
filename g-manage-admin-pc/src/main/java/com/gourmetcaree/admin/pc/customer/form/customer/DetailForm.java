package com.gourmetcaree.admin.pc.customer.form.customer;

import java.io.Serializable;

/**
 * 顧客詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends CustomerForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2879631048326555294L;

	/** 編集パス */
	public String editPath;

	/** 削除パス */
	public String deletePath;

	/** 編集可否フラグ */
	public boolean editFlg = true;

	/** webデータID */
	public String webId;

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		editPath = null;
		deletePath = null;
		editFlg = true;
		webId = null;
	}

}