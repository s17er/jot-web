package com.gourmetcaree.shop.pc.application.form.observateApplication;


/**
 * 応募者毎のメール一覧
 * @author Yamane
 *
 */
public class DetailMailListForm extends ObservateApplicationBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8784032168689151548L;

	/** ページ番号 */
	public String pageNum;

	/** 一覧に戻る */
	public String backButtonPath;

	/**
	 * 一覧に戻るURL
	 * @return
	 */
	public String getBackButtonPath() {

		return "/observateApplication/list/";
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();

		pageNum = null;
	}
}
