package com.gourmetcaree.admin.pc.changePassword.form.changePassword;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;

import jp.co.whizz_tech.common.sastruts.annotation.NotWhiteSpaceOnly;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;

/**
 * パスワード編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5861344116495742159L;

	/** 営業担当者ID */
	public String id;

	/** 会社ID */
	public String companyId;

	/** 氏名 */
	public String salesName;

	/** 氏名(カナ) */
	public String salesNameKana;

	/** 携帯電話1 */
	public String mobileNo1;

	/** 携帯電話2 */
	public String mobileNo2;

	/** 携帯電話3 */
	public String mobileNo3;

	/** 携帯電話番号 */
	public String mobileNo;

	/** 権限コード */
	public String authorityCd;

	/** メインアドレス */
	public String mainMail;

	/** サブアドレス */
	public String subMail;

	/** サブアドレス受信フラグ */
	public String submailReceptionFlg;

	/** 所属部署 */
	public String department;

	/** ログインID */
	public String loginId;

	/** パスワード */
	@Required
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.passMaxLimit"), arg0 = @Arg(key = "6", resource = false))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.passMinLimit"), arg0 = @Arg(key = "20", resource = false))
	public String password;

	/** パスワード再入力 */
	@Required
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.rePassMaxLimit"), arg0 = @Arg(key = "6", resource = false))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.rePassMinLimit"), arg0 = @Arg(key = "20", resource = false))
	public String rePassword;

	/** パスワード表示用 */
	public String dispPassword;

	/** 備考 */
	public String note;

	/** バージョン番号 */
	public Long version;


	/**
	 *  入力チェック
	 * @return
	 */
	public ActionMessages validate() {

		ActionMessages errors = new ActionMessages();

		// パスワードと確認パスワードが等しいかどうかチェック
		checkPassword(errors);

		return errors;

	}

	/**
	 * パスワード入力チェック
	 * @param errors
	 */
	private void checkPassword(ActionMessages errors) {

		if (!password.trim().equals(rePassword.trim())) {
			errors.add("errors", new ActionMessage("errors.passwordFailed"));
		}
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		resetBaseForm();
		id = null;
		companyId = null;
		salesName = null;
		salesNameKana = null;
		mobileNo1 = null;
		mobileNo2 = null;
		mobileNo3 = null;
		mobileNo = null;
		authorityCd = null;
		mainMail = null;
		subMail = null;
		submailReceptionFlg = null;
		department = null;
		loginId = null;
		password = null;
		rePassword = null;
		dispPassword = null;
		note = null;
		version = null;

	}
}