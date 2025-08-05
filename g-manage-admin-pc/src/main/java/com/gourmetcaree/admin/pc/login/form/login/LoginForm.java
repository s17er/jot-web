package com.gourmetcaree.admin.pc.login.form.login;

import java.io.Serializable;

import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;


/**
 * ログインフォームです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class LoginForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5509525139319197272L;

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
		loginId = GourmetCareeUtil.superTrim(loginId);
	}
}
