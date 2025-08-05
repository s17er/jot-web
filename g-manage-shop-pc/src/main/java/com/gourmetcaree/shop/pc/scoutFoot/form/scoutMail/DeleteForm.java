package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail;

import java.io.Serializable;


/**
 * スカウトメール削除フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class DeleteForm extends ScoutMailForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8698781596428563278L;

	/** FROMメニューのセッションキー */
	public static final String FROM_MENU_SESSION_KEY
		= "com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.DeleteForm.FROM_MENU_SESSION_KEY";

	/** 遷移元メニュー */
	public enum FromMenuKbn {
		/** スカウト・足跡 */
		SCOUT_MAIL,
		/** メールボックス */
		MAIL_BOX,;
	}
}
