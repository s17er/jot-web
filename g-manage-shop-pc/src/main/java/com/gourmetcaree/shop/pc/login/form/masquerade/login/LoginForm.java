package com.gourmetcaree.shop.pc.login.form.masquerade.login;

import java.io.Serializable;

import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;


/**
 * なりすましログインフォームです。
 * @author Keita Yamane
 * @version 1.0
 */
public class LoginForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4880149252080989634L;

	/** 顧客ID */
	@Required
	public String customerLoginId;

	/** ログインID */
	@Required
	public String loginId;

	/** パスワード */
	@Required
	public String password;

	/**
	 * ログインIDから空白(半角全角スペース)をトリム(削除)します。
	 */
	public void trimLoginId() {
		customerLoginId = GourmetCareeUtil.superTrim(customerLoginId);
		loginId = GourmetCareeUtil.superTrim(loginId);
	}
}
