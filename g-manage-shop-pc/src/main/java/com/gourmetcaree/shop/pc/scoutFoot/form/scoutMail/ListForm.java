package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * スカウトメール一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends ScoutMailForm {

	public enum MailTypeKbn {
		/** スカウト・足跡 */
		SCOUT_MAIL,
		/** メールボックス */
		MAIL_BOX,;
	}

	public enum FromMenuKbn {
		/** スカウト・足跡 */
		SCOUT_MAIL,
		/** メールボックス */
		MAIL_BOX,;
	}

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1361962746543263520L;


	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** メールタイプ */
	public MailTypeKbn mailType;

	/** 遷移元メニュー */
	public FromMenuKbn fromMenu;

	public String where_displayCount;

	public String[] where_mailStatus;

	public String[] where_areaCd;

	public String where_selectionKbn;

	public String where_keyword;

	/** ステータス区分 */
	public String selectionKbn;

	public Integer scoutMailLogId;

	/** 変更するID配列 */
	public String[] changeIdArray;

	public String errorMessage;

	public String memo;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		pageNum = null;
		mailType = null;
		fromMenu = null;
		where_displayCount = null;
		where_mailStatus = null;
		where_areaCd = null;
		where_selectionKbn = null;
		where_keyword = null;
		selectionKbn = null;
		scoutMailLogId = null;
		changeIdArray = null;
		errorMessage = null;
		memo = null;
	}

	/**
	 * メール選択のチェックのみリセット
	 */
	public void resetChangeIdArray() {
		changeIdArray = null;
	}

	/**
	 * メールボックスで一覧画面を見ているかどうか
	 */
	public boolean isMailboxFlg() {
		return mailType == MailTypeKbn.MAIL_BOX;
	}
}
