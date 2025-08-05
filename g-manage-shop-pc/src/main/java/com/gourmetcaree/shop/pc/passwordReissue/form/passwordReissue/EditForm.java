package com.gourmetcaree.shop.pc.passwordReissue.form.passwordReissue;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

@Component(instance=InstanceType.SESSION)
public class EditForm extends PasswordReissueBaseForm implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8606923186051554395L;


	/** 新しいパスワード */
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.lengthPassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.lengthPassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	public String password;

	/** パスワード(確認用) */
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.lengthRePassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.lengthRePassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	public String rePassword;

	@Override
	public void resetForm() {
		resetPasswordReissueBaseForm();
		password = null;
		rePassword = null;
	}

	/**
	 *  変更前ログインメールアドレス確認画面のチェック
	 * @return ActionMessages
	 */
	public ActionMessages validateLoginMail() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		//ブラウザで「戻る」を押したときの対応
		if(StringUtil.isEmpty(customerLoginId)){
			//「不正な操作の可能性があります。お手数ですが、最初からやり直してください。」
			errors.add("errors", new ActionMessage("errors.ProcessError"));

			//表示可否を否にする
			setExistDataFlgNg();

			return errors;
		}

		//入力必須チェック
		if(StringUtil.isEmpty(loginId)){
			//「{ログインメールアドレス}を入力して下さい。」
			errors.add("errors", new ActionMessage("errors.required",
										MessageResourcesUtil.getMessage("labels.loginId")
										));
			return errors;
		}

		// 入力されたアドレスとログインメールアドレスが一致するかチェック
		if (!loginId.equals(customerLoginId)) {
			// 「{メールアドレス}と{ログインID(登録メールアドレス)}は一致していません。」
			errors.add("errors", new ActionMessage("errors.incorrect",
										MessageResourcesUtil.getMessage("labels.loginId")
									));
		}
		return errors;
	}

	/**
	 *  新しいログインメールアドレス確認画面のチェック
	 * @return ActionMessages
	 */
	public ActionMessages validatePassword() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// メールアドレスの入力必須チェック
		if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
			// 「{パスワード}を入力して下さい。」
			errors.add("errors", new ActionMessage("errors.required",
										MessageResourcesUtil.getMessage("labels.password")
									));
		} else {
			// 入力された新しいパスワードと、パスワード(確認用)が一致するかチェック
			if (!password.equals(rePassword)) {
				// 「{パスワード}と{パスワード(確認用)}は一致していません。」
				errors.add("errors", new ActionMessage("error.notSameData",
											MessageResourcesUtil.getMessage("labels.password"),
											MessageResourcesUtil.getMessage("labels.rePassword")
										));
			}
		}

		return errors;
	}

}
