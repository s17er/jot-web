package com.gourmetcaree.admin.pc.member.form.tempMember;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.admin.pc.validator.member.tempMember.EditValidator;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 仮会員編集アクションフォーム
 * @author nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends DetailBaseForm {

	/**
	 *
	 */
	private static final long serialVersionUID = 6817675850576134786L;



	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		EditValidator validator = new EditValidator(errors, this);

		validator.validate();

		// パスワードと確認パスワードが等しいかどうかチェック
		checkPassword(errors);

		// 生年月日チェック
		// 生年月日必須チェック
		checkBirthDay(errors);

		// 電話番号チェック
		if (!checkPhoneNo()) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
		}

		// 職種が1つ以上選択されているかチェック
		if (job == null || job.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckJob"));
		}

		// 業種が1つ以上選択されているかチェック
		if (industry == null || industry.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckIndustry"));
		}

		// 勤務地が1つ以上選択されているかチェック
		if (workLocation == null || workLocation.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckWorkLocation"));
		} else {
			checkWorkLocation(errors);
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
		this.areaList = new String[0];

		if (careerList != null && !careerList.isEmpty()) {
			for (CareerDto dto : careerList) {
				dto.job = new String[0];
				dto.industry = new String[0];
			}
		}

	}



	@Override
	public void resetForm() {
		super.resetForm();

		// 仮会員では使用しないが、必須チェック回避のため値を入れておく。
		memberKbn = String.valueOf(MTypeConstants.MemberKbn.NEW_MEMBER);
	}
}
