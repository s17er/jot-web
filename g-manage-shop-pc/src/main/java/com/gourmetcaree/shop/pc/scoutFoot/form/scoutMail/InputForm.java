package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail;

import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * スカウトメール入力フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends ScoutMailForm {

	/** FROMページのセッションキー */
	public static final String FROM_PAGE_SESSION_KEY
		= "com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputForm.FROM_PAGE_SESSION_KEY";

	/** FROMメニューのセッションキー */
	public static final String FROM_MENU_SESSION_KEY
		= "com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputForm.FROM_MENU_SESSION_KEY";

	/** FROMボタンのセッションキー */
	public static final String FROM_BUTTON_SESSION_KEY
		= "com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.InputForm.FROM_BUTTON_SESSION_KEY";

	/**
	 * FROMページの区分
	 * @author Takehiro nakamori
	 *
	 */
	public static enum FromPageKbn {
		MEMBER,
		KEEP_BOX;
	}

	/**
	 * FROMメニューの区分
	 *
	 */
	public static enum FromMenuKbn {
		MAIL_BOX,
		SCOUT_MAIL;
	}

	/**
	 * FROMボタンの区分
	 *
	 */
	public static enum FromButtonKbn {
		RETURN_MAIL,
		RETURN_LUMP;
	}

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7361974143711041863L;

	/** 会員IDリスト */
	public List<Integer> memberIdList;

	/** 返信メールIDリスト */
	public List<Integer> mailIdList;

	/** 定型文番号 */
	public String sentenceId;

	/** Ajax用プロパティ */
	public String limitValue;

	/** URL追加ボタン表示フラグ */
	public boolean urlBtnDispFlg;

	public Integer scoutMailLogId;

	/** スカウトメールログIDリスト */
	public List<Integer> scoutMailLogIdList;

	public boolean returnMailBlockDispFlg;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		memberIdList = null;
		sentenceId = null;
		limitValue = null;
		urlBtnDispFlg = false;
		scoutMailLogId = null;
		returnMailBlockDispFlg = false;
		scoutMailLogIdList = null;
	}
}
