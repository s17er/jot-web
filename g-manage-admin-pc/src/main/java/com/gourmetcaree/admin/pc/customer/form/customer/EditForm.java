package com.gourmetcaree.admin.pc.customer.form.customer;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;

import jp.co.whizz_tech.common.sastruts.annotation.NotWhiteSpaceOnly;

/**
 * 顧客編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends CustomerForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -378049682741486816L;

	/** パスワード */
	@NotWhiteSpaceOnly
	@Mask(mask=MASK_SINGLE_ALPHANUM, msg = @Msg(key = "errors.singleAlphanum"))
	@Maxlength(maxlength = 20, msg = @Msg(key = "errors.passMaxLimit"), arg0 = @Arg(key = "6", resource = false))
	@Minlength(minlength = 6, msg = @Msg(key = "errors.passMinLimit"), arg0 = @Arg(key = "20", resource = false))
	public String password;

	/** パスワード再入力 */
	@NotWhiteSpaceOnly
	public String rePassword;

	/** 追加スカウトメール数 */
	public String scoutAddCount;

	/** 削除スカウトメール数 */
	public String scoutRemoveCount;

	/** スカウトメール使用期限 */
	public String scoutUseEndDatetime;

	/** スカウトメール使用開始日 */
	public String scoutUseStartDatetime;


	/**
	 * 0と等しいかどうか
	 */
	public static boolean eqZero(String str) {
		return "0".equals(str);
	}


	/**
	 *  入力チェック
	 */
	public ActionMessages validate() {

		ActionMessages errors = new ActionMessages();

		// 電話番号必須チェック
		if ((!checkPhoneNoRequired())) {
			errors.add("errors", new ActionMessage("errors.notPhoneNoInput"));
		}

		// 電話番号が正しく入力されているかチェック
		if (!checkPhoneNo()) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
		}

//		// サブアドレス受信フラグチェック
//		if (StringUtils.isBlank(subMail) && String.valueOf(MSales.SubMailReceptionFlgKbn.RECEIVE).equals(submailReceptionFlg)) {
//			errors.add("errors", new ActionMessage("errors.subMailReceptionFlg"));
//		}

		// 担当会社、営業担当者チェック
		assigneCompanySalesCheck(errors);
		
		// メインメールのチェック
		checkMainMail(errors);

		// サブメールのチェック
		checkSubMail(errors);

		// ホームページチェック
		checkURL(errors);

		// パスワードと確認パスワードが等しいかどうかチェック
		checkPassword(errors);

		checkScoutCount(errors);

		checkKatakanaInput(errors);

		return errors;

	}

	/**
	 * パスワード入力チェック
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
	 * 電話番号をチェック
	 * 電話番号に入力があるかどうか
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
	 * スカウトメール追加数が正しいかチェック
	 */
	private void checkScoutCount(ActionMessages errors) {

		//  手動とボタン両方が入力されていたらエラー
		if (StringUtils.isNotBlank(scoutUseStartDatetime) && StringUtils.isNotBlank(scoutUseEndDatetime)) {
			errors.add("errors", new ActionMessage("errors.selectedPluralScout"));
			return;
		}

		//  購入分が0でなく、数値でもない場合はエラー
		if (!eqZero(scoutAddCount)
				&& !NumberUtils.isDigits(scoutAddCount)) {
			errors.add("errors", new ActionMessage("errors.notNumScoutCount"));
			return;
		}

		//  削除分が0でなく、数値でもない場合はエラー
		if (!eqZero(scoutRemoveCount)
				&& !NumberUtils.isDigits(scoutRemoveCount)) {
			errors.add("errors", new ActionMessage("errors.notNumScoutCount"));
			return;
		}

		// 購入と削除の両方が入力されていたらエラー
		if(!eqZero(scoutAddCount) && !eqZero(scoutRemoveCount)) {
			errors.add("errors", new ActionMessage("errors.selectedAddAndRemove"));
			return;
		}

		// 利用開始日が空の場合はエラー
		if (StringUtils.isBlank(scoutUseStartDatetime) && StringUtils.isNotBlank(scoutAddCount) && !eqZero(scoutAddCount) ) {
			errors.add("errors", new ActionMessage("errors.app.scoutUseStartDatetimeFailed"));
			return;
		}

		try {
			if(StringUtils.isNotBlank(scoutUseStartDatetime)) {
				DateUtils.formatDate(scoutUseStartDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
			} else if(StringUtils.isNotBlank(scoutUseEndDatetime)){
				Date scoutUseEndDate = DateUtils.formatDate(scoutUseEndDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
				Calendar cal = Calendar.getInstance();
				cal.setTime(scoutUseEndDate);
				int compare = DateUtils.compareDateTime(DateUtils.getJustDate(), scoutUseEndDate);

				if (compare < 1) {
					errors.add("errors", new ActionMessage("errors.futureDate",
							MessageResourcesUtil.getMessage("labels.scoutUseEndDate")));
				}
				if (Calendar.THURSDAY != cal.get(Calendar.DAY_OF_WEEK)) {
					errors.add("errors",
							new ActionMessage("errors.app.notMutchData",
								MessageResourcesUtil.getMessage("labels.scoutUseEndDate"),
								MessageResourcesUtil.getMessage("msg.weekday.thursday")));
				}
			}

		} catch (ParseException e) {
			//  日付変換失敗エラー
			errors.add("errors", new ActionMessage("errors.app.dateFailed"));
		}
			return;
	}


	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		password = null;
		rePassword = null;
		scoutAddCount = null;
		scoutRemoveCount = null;
		scoutUseEndDatetime = null;
		scoutUseStartDatetime = null;
	}

}