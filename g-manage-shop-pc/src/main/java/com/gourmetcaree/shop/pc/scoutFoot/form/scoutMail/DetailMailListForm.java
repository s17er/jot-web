package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * スカウトメール一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class DetailMailListForm extends ScoutMailForm {

	public enum MailTypeKbn {
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

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		pageNum = null;
		mailType = null;
	}
}
