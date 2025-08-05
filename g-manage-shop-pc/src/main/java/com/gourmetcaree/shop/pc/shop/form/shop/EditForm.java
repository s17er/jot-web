package com.gourmetcaree.shop.pc.shop.form.shop;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.util.GourmetCareeUtil;

import jp.co.whizz_tech.common.sastruts.annotation.NotWhiteSpaceOnly;

/**
 * 登録情報編集フォーム
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance=InstanceType.SESSION)
public class EditForm extends ShopForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1597429328861795432L;

	/** 現在のパスワード */
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	public String nowPassword;

	/** 新しいパスワード */
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.lengthNewPassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.lengthNewPassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	public String newPassword;

	/** パスワード(確認用) */
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.lengthRePassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.lengthRePassword"), arg0 = @Arg(key = "6", resource = false), arg1 = @Arg(key = "20", resource = false))
	public String rePassword;

	/** 顧客名 */
	public String customerName;

	/** 担当者名 */
	@Required
	public String contactName;

	/** 担当者名（カナ） */
	@Required
	public String contactNameKana;


	/** ログインID */
	public String loginId;

	/** メインメールアドレス */
	@Required
	public String mainMail;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** メルマガ受信フラグ */
	@Required
	public String mailMagazineReceptionFlg;

	/** 顧客のバージョン */
	public Long customerVersion;

	/** 顧客アカウントのバージョン */
	public Long customerAccountVersion;


	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		checkPassword(errors);
		// メインメールのチェック
		checkMainMail(errors);
		// サブメールのチェック
		checkSubMail(errors);

		// 電話番号のチェック
		if (!checkPhoneNoRequired()) {
			errors.add("errors", new ActionMessage("errors.notPhoneNoInput"));
		} else if (!checkPhoneNo()){
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
		}


		return errors;
	}

	/**
	 * 電話番号をチェック
	 * 電話番号に入力があるかどうか
	 * @param phoneNo1
	 * @param phoneNo2
	 * @param phoneNo3
	 * @return
	 */
	private boolean checkPhoneNoRequired() {

		// 電話に入力があるかチェック
		if (StringUtils.isBlank(phoneNo1) && StringUtils.isBlank(phoneNo2) && StringUtils.isBlank(phoneNo3)) {
			return false;
		}

		return true;
 	}

	/**
	 * 電話番号をチェック
	 * 電話番号に入力があるかどうか
	 * 値がすべて数値かどうか
	 * 1つ入力されている場合は、他の2つも入力されているかチェック
	 * @param phoneNo1
	 * @param phoneNo2
	 * @param phoneNo3
	 * @return
	 */
	private boolean checkPhoneNo() {

		// 3つすべてに値が入力されているかチェック
		if (StringUtils.isBlank(phoneNo1) || StringUtils.isBlank(phoneNo2) || StringUtils.isBlank(phoneNo3)) {
			return false;
		}

		// 3つすべて数値かどうかチェック
		if (!StringUtils.isNumeric(phoneNo1) || !StringUtils.isNumeric(phoneNo2) || !StringUtils.isNumeric(phoneNo3)) {
			return false;
		}

		return true;
 	}

	/**
	 * パスワードのチェック
	 * @param errors
	 */
	private void checkPassword(ActionMessages errors) {

		// 全て空の場合は変更しないのでtrue
		if (isNotAllPasswordInput()) {
			return;
		}

		// 現在のパスワードがあるかないか
		if (StringUtils.isBlank(nowPassword)) {
			errors.add("errors", new ActionMessage("errors.required", "現在のパスワード"));
			return;
		}

		// 新しいパスワードと確認用パスワードがない場合
		if (StringUtils.isBlank(newPassword) && StringUtils.isBlank(rePassword)) {
			errors.add("errors", new ActionMessage("errors.required", "新しいパスワード"));
			errors.add("errors", new ActionMessage("errors.required", "確認用パスワード"));
			return;
		}

		// 新しいパスワードがない場合
		if (StringUtils.isBlank(newPassword) && StringUtils.isNotBlank(rePassword)) {
			errors.add("errors", new ActionMessage("errors.required", "新しいパスワード"));
			return;
		}

		// 確認用パスワードがない場合
		if (StringUtils.isNotBlank(newPassword) && StringUtils.isBlank(rePassword)) {
			errors.add("errors", new ActionMessage("errors.required", "確認用パスワード"));
			return;
		}

		// 新しいパスワードと確認用パスワードが一致しない場合
		if (!StringUtils.equals(newPassword.trim(), rePassword.trim())) {
			errors.add("errors", new ActionMessage("errors.passwordFailed"));
		}
	}

	/**
	 * 現在のパスワード、新しいパスワード、パスワード(確認用)が全て入力されていないかどうか
	 * @return
	 */
	public boolean isNotAllPasswordInput() {
		return StringUtils.isBlank(nowPassword) && StringUtils.isBlank(newPassword) && StringUtils.isBlank(rePassword);
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		version = null;
		contactName = null;
		contactNameKana = null;
		customerAccountVersion = null;
		customerName = null;
		customerVersion = null;
		loginId = null;
		mainMail = null;
		newPassword = null;
		nowPassword = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		rePassword = null;
	}

	/**
	 * 表示用パスワードの取得
	 * @return
	 */
	public String getDispPassword() {
		if (StringUtils.isBlank(newPassword)) {
			return "";
		}
		return GourmetCareeUtil.convertPassword(newPassword);
	}

	/**
	 * サブメールの値を保持するDTO
	 * @author whizz
	 *
	 */
	public static class SubMailDto implements Serializable {
		private static final long serialVersionUID = -535176959773981750L;
		public String subMail;
		public String submailReceptionFlg;
		public String getSubMail() {
			return subMail;
		}
		public String getSubmailReceptionFlg() {
			return submailReceptionFlg;
		}
	}
	
	/**
	 * メインメールのチェックをする
	 * @param errors
	 */
	protected void checkMainMail(ActionMessages errors) {
		if (!StringUtils.isBlank(mainMail)) {
			// アドレスチェック
			if(!Pattern.matches("[\\w._\\-+]+@([\\w_\\-]+\\.)+[\\w_\\-]+", mainMail)) {
				errors.add("errors", new ActionMessage("errors.email", MessageResourcesUtil.getMessage("labels.mainMail")));
			}
		}
	}

	/**
	 * サブメールのチェックをする
	 * @param errors
	 */
	protected void checkSubMail(ActionMessages errors) {
		int i = 1;

		List<SubMailDto> newList = new ArrayList<>();
		List<String> existMailAddress = new ArrayList<>();

		existMailAddress.add(mainMail);
		boolean sameMailAddressFlg = false;

		for (SubMailDto dto : subMailDtoList) {

			if (StringUtils.isEmpty(dto.subMail)) {
				continue;
			}

			// 空白削除
			dto.subMail = dto.subMail.replaceAll(" ", "").replaceAll("　", "");

			String no = String.valueOf(i);
			if (StringUtils.isEmpty(dto.submailReceptionFlg)) {
				errors.add("errors", new ActionMessage("errors.notSelectData",
						MessageResourcesUtil.getMessage("labels.subMailAddress") + no,
						MessageResourcesUtil.getMessage("labels.submailReceptionFlg") + no)
				);
			}

			// アドレスチェック
			if(!Pattern.matches("[\\w._\\-+]+@([\\w_\\-]+\\.)+[\\w_\\-]+", dto.subMail)) {
				errors.add("errors", new ActionMessage("errors.email", MessageResourcesUtil.getMessage("labels.subMailAddress") + no));
			}

			// 同じメールアドレスチェック
			if(existMailAddress.contains(dto.subMail)) {
				sameMailAddressFlg = true;
			}

			existMailAddress.add(dto.subMail);

			// 間を詰める
			newList.add(dto);
			i++;
		}

		if(sameMailAddressFlg) {
			errors.add("errors", new ActionMessage("errors.sameMailAddress"));
		}

		subMailDtoList = newList;
		setSubMailEntryForm();
	}

	/**
	 * サブメールDTOリストの空を詰める
	 */
	public void packSubMailDtoList()
	{
		subMailDtoList = subMailDtoList.stream().filter(s -> StringUtils.isNotEmpty(s.subMail)).collect(Collectors.toList());
	}

}

