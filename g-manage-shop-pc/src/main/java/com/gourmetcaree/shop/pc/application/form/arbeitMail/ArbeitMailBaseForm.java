package com.gourmetcaree.shop.pc.application.form.arbeitMail;

import com.gourmetcaree.common.form.BaseForm;

/**
 * アルバイトメール用ベースフォーム
 * @author Takehiro Nakamori
 *
 */
public abstract class ArbeitMailBaseForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8815171657212594457L;

	/** メールの返信時の遷移元ページ区分（応募者の詳細から） */
	public static final String FROM_APPLICANT_DETAIL = "0";

	/** メールの返信時の遷移元ページ区分（メールの詳細から） */
	public static final String FROM_MAIL_DETAIL = "1";

	/** メールID */
	public String id;


	@Override
	protected void resetBaseForm() {
		super.resetBaseForm();
		id = null;
	}

	/**
	 * フォームのリセット
	 */
	public abstract void resetForm();

}
