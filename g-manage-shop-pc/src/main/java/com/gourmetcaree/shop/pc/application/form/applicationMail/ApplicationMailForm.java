package com.gourmetcaree.shop.pc.application.form.applicationMail;

import com.gourmetcaree.common.form.BaseForm;


/**
 * 応募メールフォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class ApplicationMailForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4661780521224838393L;

	/** メールの返信時の遷移元ページ区分（応募者の詳細から） */
	public static final String FROM_APPLICANT_DETAIL = "0";

	/** メールの返信時の遷移元ページ区分（メールの詳細から） */
	public static final String FROM_MAIL_DETAIL = "1";

	/** 応募者一覧からの一括送信 */
	public static final String FROM_APPLICATION_LIST = "2";

	public static final String APPLICATION_DETAIL_MAIL_LIST_ID = "applicationDetailMailListId";

	/** メールID */
	public String id;

	/**
	 * 応募メールのベースフォームをリセットします。
	 */
	public void resetApplicationMailBaseForm() {
		resetBaseForm();
		id = null;
	}


}
