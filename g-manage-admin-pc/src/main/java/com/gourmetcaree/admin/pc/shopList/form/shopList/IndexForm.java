package com.gourmetcaree.admin.pc.shopList.form.shopList;

import java.io.Serializable;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 店舗一覧インデックスアクションフォーム
 * @author Takehiro Nakamori
 *
 */
public class IndexForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9010226197175456949L;

	/**
	 * 顧客ID
	 */
	public String customerId;

	/** webId */
	public String webId;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetBaseForm();
		customerId = null;
		webId = null;
	}

}
