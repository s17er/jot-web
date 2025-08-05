package com.gourmetcaree.admin.pc.sys.form.util;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 素材処理ユーティリティフォーム
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ImageUtilityForm extends BaseForm {

	/** シリアルUID */
	private static final long serialVersionUID = -4216323445863914957L;

	/** 素材区分 */
	public String materialKbn;

	/** WebデータID */
	public String webId;

	/** 店舗一覧ID */
	public String shopListId;

	/** ユニークキー */
	public String uniqueKey;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		materialKbn = null;
		webId = null;
		shopListId = null;
		uniqueKey = null;
	}
}
