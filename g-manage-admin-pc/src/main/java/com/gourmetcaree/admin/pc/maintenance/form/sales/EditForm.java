package com.gourmetcaree.admin.pc.maintenance.form.sales;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;

import jp.co.whizz_tech.common.sastruts.annotation.NotWhiteSpaceOnly;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;

import com.gourmetcaree.db.common.entity.MSales;


/**
 * 営業担当者編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends SalesForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8138786124919805386L;

	/** パスワード */
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.passMaxLimit"), arg0 = @Arg(key = "6", resource = false))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.passMinLimit"), arg0 = @Arg(key = "20", resource = false))
	public String password;

	/** パスワード再入力 */
	@NotWhiteSpaceOnly
	public String rePassword;


	/**
	 *  入力チェック
	 * @return
	 */
	public ActionMessages validate() {

		ActionMessages errors = new ActionMessages();

		// 携帯電話チェック
		if (!checkMobileNo(mobileNo1, mobileNo2, mobileNo3)) {
			errors.add("errors", new ActionMessage("errors.mobileNoFailed"));
		}

		// サブアドレス受信フラグチェック
		if (StringUtils.isBlank(subMail) && String.valueOf(MSales.SubMailReceptionFlgKbn.RECEIVE).equals(submailReceptionFlg)) {
			errors.add("errors", new ActionMessage("errors.subMailReceptionFlg"));
		}

		// パスワードと確認パスワードが等しいかどうかチェック
		checkPassword(errors);

		return errors;

	}

	/**
	 * パスワード入力チェック
	 * @param errors
	 */
	private void checkPassword(ActionMessages errors) {

		if (StringUtils.isNotEmpty(password) && StringUtils.isEmpty(rePassword)) {
			errors.add("errors", new ActionMessage("errors.required", "確認用パスワード"));
		} else if (StringUtils.isEmpty(password) && StringUtils.isNotEmpty(rePassword)) {
			errors.add("errors", new ActionMessage("errors.required", "パスワード"));
		} else if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(rePassword)) {
			if (!password.trim().equals(rePassword.trim())) {
				errors.add("errors", new ActionMessage("errors.passwordFailed"));
			}
		}
	}

	/**
	 * 携帯電話番号をチェック
	 * 値がすべて数値かどうか
	 * 1つ入力されている場合は、他の2つも入力されているかチェック
	 * @param mobileNo1
	 * @param mobileNo2
	 * @param mobileNo3
	 * @return
	 */
	private boolean checkMobileNo(String mobileNo1, String mobileNo2, String mobileNo3) {

		// 携帯電話に入力があるかチェック
		if (StringUtils.isBlank(mobileNo1) && StringUtils.isBlank(mobileNo2) && StringUtils.isBlank(mobileNo3)) {
			return true;
		}

		// 3つすべてに値が入力されているかチェック
		if (StringUtils.isBlank(mobileNo1) || StringUtils.isBlank(mobileNo2) || StringUtils.isBlank(mobileNo3)) {
			return false;
		}

		// 3つすべて数値かどうかチェック
		if (!StringUtils.isNumeric(mobileNo1) || !StringUtils.isNumeric(mobileNo2) || !StringUtils.isNumeric(mobileNo3)) {
			return false;
		}

		return true;
 	}


	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		password  = null;
		rePassword = null;

	}


}