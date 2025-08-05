package com.gourmetcaree.shop.pc.passwordReissue.form.passwordReissue;

import java.io.Serializable;

import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 仮登録のフォーム
 * @author Takehiro Nakamori
 * @version 1.0
 */
public abstract class PasswordReissueBaseForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6968555576261972242L;

	/** 仮登録ID */
	public String id;

	/** アクセスコード */
	public String accessCd;

	/** ログインID */
	public String customerLoginId;

	@Required
	public String loginId;

	/** 顧客ID */
	public int customerId;

	/** エリアコード */
	public int areaCd;

	/** メールアドレス */
	public String mail;

	/** バージョン番号 */
	public long version;

	public Integer terminalKbn;

	/** どのボタンが押されたかを保持 */
	public String btnName;

	/**
	 * フォームのリセット
	 */
	public abstract void resetForm();

	protected void resetPasswordReissueBaseForm() {
		super.resetBaseForm();
		id = null;
		accessCd = null;
		customerLoginId = null;
		loginId = null;
		customerId = 0;
		areaCd = 0;
		mail = null;
		version = 0L;
		terminalKbn = null;
		btnName = null;
	}

}
