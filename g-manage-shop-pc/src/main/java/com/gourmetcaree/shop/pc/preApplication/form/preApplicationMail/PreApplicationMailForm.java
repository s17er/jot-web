package com.gourmetcaree.shop.pc.preApplication.form.preApplicationMail;

import com.gourmetcaree.common.form.BaseForm;


/**
 * プレ応募メールフォームです。
 */
public abstract class PreApplicationMailForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4661780521224838393L;

	/** メールの返信時の遷移元ページ区分（応募者の詳細から） */
	public static final String FROM_PRE_APPLICANT_DETAIL = "99";

	/** メールの返信時の遷移元ページ区分（メールの詳細から） */
	public static final String FROM_MAIL_DETAIL = "99";

	/** 応募者一覧からの一括送信 */
	public static final String FROM_PRE_APPLICATION_LIST = "2";

	public static final String PRE_APPLICATION_DETAIL_MAIL_LIST_ID = "preApplicationDetailMailListId";

	/** メールID */
	public String id;

	/**
	 * 応募メールのベースフォームをリセットします。
	 */
	public void resetPreApplicationMailBaseForm() {
		resetBaseForm();
		id = null;
	}


}
