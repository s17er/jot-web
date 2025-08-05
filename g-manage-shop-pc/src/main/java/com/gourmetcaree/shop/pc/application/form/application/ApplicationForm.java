package com.gourmetcaree.shop.pc.application.form.application;

import com.gourmetcaree.common.form.BaseForm;


/**
 * 応募者フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class ApplicationForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2962758813010054430L;

	/** 応募者ID */
	public String id;

	/** 応募対象区分 */
	public String checkKbn;

	/** バージョン */
	public String version;

	/** ステータス区分 */
	public String selectionKbn;



}
