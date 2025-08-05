package com.gourmetcaree.shop.pc.util.form.util;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 素材処理ユーティリティフォーム
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ImageUtilityForm extends BaseForm {

	/** シリアルUID */
	private static final long serialVersionUID = 3577978929596127115L;

	/** 定型求人データID */
	public String webId;

	/** 素材区分 */
	public String materialKbn;

	/** 店舗一覧ID */
	public String shopListId;

	/** ユニークキー */
	public String uniqueKey;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		webId = null;
		materialKbn = null;
		shopListId = null;
		uniqueKey = null;
	}
}
