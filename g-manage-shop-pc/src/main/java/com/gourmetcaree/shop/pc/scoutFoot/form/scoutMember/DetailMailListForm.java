package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;



/**
 * 応募者毎のメール一覧
 * @author Yamane
 *
 */
@Component(instance = InstanceType.SESSION)
public class DetailMailListForm extends ScoutBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1901308175916204102L;

	public enum FromMenuKbn {
		/** スカウト・足跡 */
		SCOUT_MAIL,
		/** メールボックス */
		MAIL_BOX,;
	}

	/** メモ */
	public String memo;

	/** メモ用スカウトメールログID */
	public String mailLogId;

	/** 選考フラグ */
	public String selectionFlg;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 遷移元メニュー */
	public FromMenuKbn fromMenu;

	/**
	 * ヘッダを隠すフラグ
	 * 応募者ごとの一覧と、日付での一覧からの遷移があるが、
	 * 応募者ごとからの遷移はfalse、
	 * 日付での遷移はtrueとなる。
	 */
	public boolean hideHeaderFlg;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		memo = null;
		mailLogId = null;
		selectionFlg = null;
		pageNum = null;
		fromMenu = null;
		hideHeaderFlg = false;
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
}
