package com.gourmetcaree.admin.pc.maintenance.form.sales;

import java.io.Serializable;

/**
 * 営業担当者詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends SalesForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7607187680822271961L;

	/** 編集パス */
	public String editPath;

	/** 削除パス */
	public String deletePath;


	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();
		editPath = null;
		deletePath = null;

	}
}