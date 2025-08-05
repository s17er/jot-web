package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;


/**
 * スカウトメール一覧フォームです。
 * @author Motoaki Hara
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends ScoutBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9019827184473128028L;

	public enum FromMenuKbn {
		/** スカウト・足跡 */
		SCOUT_MAIL,
		/** メールボックス */
		MAIL_BOX,;
	}

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** メモ */
	public String memo;

	/** メモ用スカウトメールログID */
	public String mailLogId;

	/** 選考フラグ */
	public String selectionFlg;

	/** 遷移元メニュー */
	public FromMenuKbn fromMenu;

	/** 一括送信ID */
	@Required(target="lumpSend, lumpSendFromMail")
	public String[] lumpSendIdArray;

	/** webID */
	public String webId;


	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		pageNum = null;
		memo = null;
		mailLogId = null;
		fromMenu = null;
		lumpSendIdArray = null;
		webId = null;
	}

	/**
	 * メモ用にリセットを行います。
	 */
	public void resetForMemo() {
		memo = null;
		mailLogId = null;
	}

	public void resetForSelection() {
		selectionFlg = null;
		mailLogId = null;
	}


	/**
	 * 一括送信用リセット
	 */
	public void resetForLumpSend() {
		lumpSendIdArray = null;
	}
}
