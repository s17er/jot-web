package com.gourmetcaree.admin.pc.member.form.member;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * 会員編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends MemberForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2862762640663949569L;

	/** ジャスキル登録フラグ */
	public String entryJuskillFlg;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// パスワードと確認パスワードが等しいかどうかチェック
		checkPassword(errors);

		// 電話番号チェック
		if (!checkPhoneNo()) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
		}

		// 学歴入力チェック
		if (!checkSchoolHistory()) {
			errors.add("errors", new ActionMessage("errors.schoolHistoryFailed"));
		}

		// 職歴入力チェック
		if (!checkCareerHistory()) {
			errors.add("errors", new ActionMessage("errors.careerHistoryFailed"));
		}

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
	 * 生年月日必須入力チェック
	 * @return 生年月日が入力されていない場合、falseを返す
	 */
	private void checkBirthDay(ActionMessages errors) {

		if (StringUtils.isEmpty(birthYear) && StringUtils.isEmpty(birthMonth) && StringUtils.isEmpty(birthDay)) {
			errors.add("errors", new ActionMessage("errors.required", "生年月日"));
		} else {
			if (StringUtils.isEmpty(birthYear) || StringUtils.isEmpty(birthMonth) || StringUtils.isEmpty(birthDay)) {
				errors.add("errors", new ActionMessage("errors.birthDayFailed"));
			} else if (!StringUtils.isNumeric(birthYear) || !StringUtils.isNumeric(birthMonth) || !StringUtils.isNumeric(birthDay)) {
				errors.add("errors", new ActionMessage("errors.birthDayFailed"));
			} else {
				if (!GourmetCareeUtil.checkDate(birthYear, birthMonth, birthDay)) {
					errors.add("errors", new ActionMessage("errors.birthDayFailed"));
				}
			}
		}
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
		if (StringUtils.isNotBlank(phoneNo1) && StringUtils.isNotBlank(phoneNo2) && StringUtils.isNotBlank(phoneNo3)) {
			// 3つすべて数値かどうかチェック
			if (!StringUtils.isNumeric(phoneNo1) || !StringUtils.isNumeric(phoneNo2) || !StringUtils.isNumeric(phoneNo3)) {
				return false;
			}
		} else {
			if (StringUtils.isNotBlank(phoneNo1) || StringUtils.isNotBlank(phoneNo2) || StringUtils.isNotBlank(phoneNo3)) {
				return false;
			}
		}

		return true;
 	}

	/**
	 * 希望勤務地のチェック<br />
	 * 「こだわらない」が選択されている場合、他の値がチェックされていないかチェック
	 * @param errors エラーを格納するActionMessages
	 */
	private void checkWorkLocation(ActionMessages errors) {

		// 希望勤務地に「こだわらない」が登録されている場合は、他の勤務地は設定できない
		if (workLocation != null && workLocation.length > 1) {

//			// 「こだわらない」の値を取得
//			String NotDecideValue = String.valueOf(MTypeConstants.WebAreaKbn.getNotDecideValue(areaCd));

			// 「こだわらない」を選択している場合
//			if (ArrayUtil.contains(workLocation, NotDecideValue)) {
//
//				// 「{希望勤務地}に{こだわらない}を選択した場合は、{他の値}を選択できません。」
//				errors.add("errors", new ActionMessage("errors.notSelectOtherData",
//											MessageResourcesUtil.getMessage("labels.workLocation"),
//											MessageResourcesUtil.getMessage("msg.webAreaKbn.notDecide"),
//											MessageResourcesUtil.getMessage("msg.otherValue")
//										));
//			}
		}

	}
	/**
	 * 学歴入力チェック
	 * @return 学校名を入力せずに他の項目を入力している場合、falseを返す
	 */
	private boolean checkSchoolHistory() {

		if (StringUtils.isEmpty(schoolName)) {
			if (StringUtils.isNotEmpty(department) || StringUtils.isNotEmpty(graduationKbn)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 職歴入力チェック
	 * @return 会社名を入力せずに他の項目を入力している場合、falseを返す
	 */
	private boolean checkCareerHistory() {

		for (CareerDto dto : careerList) {
			if (StringUtils.isEmpty(dto.companyName)) {
				if (StringUtils.isNotEmpty(dto.workTerm) || (dto.job != null && dto.job.length > 0)
						|| StringUtils.isNotEmpty(dto.businessContent) || StringUtils.isNotEmpty(dto.seat) || StringUtils.isNotEmpty(dto.monthSales)) {
					return false;
				}
			}
		}
		return true;
	}


	/**
	 * multiboxのリセットを行う
	 */
	public void resetMultibox() {
		this.qualification = new String[0];
		this.job = new String[0];
		this.industry = new String[0];
		this.workLocation = new String[0];
		this.hopeCityCdList = new ArrayList<String>();
		this.entryJuskillFlg = null;

		if (careerList != null && !careerList.isEmpty()) {
			for (CareerDto dto : careerList) {
				dto.job = new String[0];
				dto.industry = new String[0];
			}
		}

	}


	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		entryJuskillFlg = null;
	}


}