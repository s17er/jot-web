package com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

/**
 * 事前登録会員一覧アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1614045507113767572L;

	/** 事前登録ユーザID */
	@IntegerType
	public String id;


	/**
	 * ふりがな
	 */
	public String furigana;

	/**
	 * 名前
	 */
	public String name;

	/**
	 * エリアコード
	 */
	public String areaCd;

	/**
	 * 都道府県コード
	 */
	public String prefecturesCd;

	/**
	 * 登録状況配列
	 */
	public String[] statusKbnArray;

	/**
	 * 事前登録メルマガフラグ
	 */
	public String advancedMailMagazineReceptionFlg;

	/**
	 * 性別区分
	 */
	public String sexKbn;

	/**
	 * メールアドレス
	 */
	public String mailAddress;

	/**
	 * 端末区分
	 */
	public String terminalKbn;

	/** 事前登録ID配列 */
	public String[] advancedRegistrationIdArray;

	/**
	 * 年齢(下限)
	 */
	@IntegerType
	public String minAge;

	/**
	 * 年齢(上限)
	 */
	@IntegerType
	public String maxAge;

	/** ページ番号 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** 来場ステータス */
	public String attendedStatus;

	/** 登録日時のYMD(開始) */
	@DateType(datePattern=GourmetCareeConstants.DATE_FORMAT_SLASH)
	public String registrationStartDate;

	/** 登録時日時の時間(開始) */
	@IntRange(min=0, max=23)
	public String registrationStartHour;

	/** 登録時日時の秒(開始) */
	@IntRange(min=0, max=59)
	public String registrationStartMinute;

	/** 登録日時のYMD(終了) */
	@DateType(datePattern=GourmetCareeConstants.DATE_FORMAT_SLASH)
	public String registrationEndDate;

	/** 登録時日時の時間(終了) */
	@IntRange(min=0, max=23)
	public String registrationEndHour;

	/** 登録時日時の秒(終了) */
	@IntRange(min=0, max=59)
	public String registrationEndMinute;


	public String toggleId;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		super.resetBaseForm();
		id = null;
		name = null;
		furigana = null;
		areaCd = null;
		prefecturesCd = null;
		advancedMailMagazineReceptionFlg = null;
		sexKbn = null;
		mailAddress = null;
		terminalKbn = null;
		minAge = null;
		maxAge = null;
		pageNum = null;
		maxRow = null;


		registrationStartDate = null;
		registrationStartHour = null;
		registrationStartMinute = null;
		registrationEndDate = null;
		registrationEndHour = null;
		registrationEndMinute = null;
		toggleId = null;
		resetCheckBox();
	}

	/**
	 * チェックボックスのリセット
	 */
	public void resetCheckBox() {
		existDataFlg = false;
		statusKbnArray = null;
		advancedRegistrationIdArray = null;
	}


	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		if (StringUtils.isNotEmpty(minAge) && StringUtils.isNotEmpty(maxAge)) {
			try {
				// 上限年齢が下限年齢より小さい場合はエラー
				if (Integer.parseInt(minAge) > Integer.parseInt(maxAge)) {
					errors.add("errors", new ActionMessage("errors.fraudurentAge"));
				}
			} catch (NumberFormatException e1) {
				errors.add("errors", new ActionMessage("errors.ageFailed"));
			}
		}

		validateRegistrationDate(errors);

		return errors;
	}

	public void validateRegistrationDate(ActionMessages errors) {

		if (StringUtils.isNotBlank(registrationStartMinute)) {
			if (StringUtils.isBlank(registrationStartDate)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.registrationStartDate")));
			}
			if (StringUtils.isBlank(registrationStartHour)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.registrationStartHour")));
			}
		} else if (StringUtils.isNotBlank(registrationStartHour)){
			if (StringUtils.isBlank(registrationStartDate)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.registrationStartDate")));
			}
		}


		if (StringUtils.isNotBlank(registrationEndMinute)) {
			if (StringUtils.isBlank(registrationEndDate)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.registrationEndDate")));
			}
			if (StringUtils.isBlank(registrationEndHour)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.registrationEndHour")));
			}
		} else if (StringUtils.isNotBlank(registrationEndHour)){
			if (StringUtils.isBlank(registrationEndDate)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.registrationEndDate")));
			}
		}


		Timestamp start = createRegistrationStartTimestamp();
		Timestamp end = createRegistrationEndTimestamp();

		if (start != null && end != null && start.compareTo(end) > 0) {
			errors.add("errors", new ActionMessage(
					"errors.app.TermSet",
					MessageResourcesUtil.getMessage("msg.registrationStartDatetime"),
					MessageResourcesUtil.getMessage("msg.registrationEndDatetime")));
		}
	}

	/**
	 * 登録日時(開始)のTimestampを作成します。
	 * @return 登録日時(開始)のTimestamp
	 */
	public Timestamp createRegistrationStartTimestamp() {
		try {
			return createTimestamp(registrationStartDate, registrationStartHour, registrationStartMinute);
		} catch (ParseException e) {
			throw new ActionMessagesException("errors.app.targetDateFailed", "msg.registrationStartDatetime");
		}
	}

	/**
	 * 登録日時(終了)のTimestampを作成します。
	 * @return 登録日時(終了)のTimestamp
	 */
	public Timestamp createRegistrationEndTimestamp() {
		try {
			return createTimestamp(registrationEndDate, registrationEndHour, registrationEndMinute);
		} catch (ParseException e) {
			throw new ActionMessagesException("errors.app.targetDateFailed", "msg.registrationEndDatetime");
		}
	}



	/**
	 * 最大表示件数を作成します。
	 * @param defaultRowNum デフォルトの最大表示件数
	 * @return 最大表示件数
	 */
	public int createMaxRow(int defaultRowNum) {
		return NumberUtils.toInt(maxRow, defaultRowNum);
	}

	/**
	 * Timestampを作成します。
	 * @param date 日付
	 * @param hour 時間
	 * @param minute 分
	 * @return Timestamp
	 * @throws ParseException 日付変換に失敗した際にスロー
	 */
	private static Timestamp createTimestamp(String date, String hour, String minute) throws ParseException {
		if (StringUtils.isBlank(date)) {
			return null;
		}

		StringBuilder pattern = new StringBuilder(GourmetCareeConstants.DATE_FORMAT_SLASH);
		StringBuilder dateStr = new StringBuilder(date);

		if (StringUtils.isBlank(hour) || !NumberUtils.isDigits(hour)) {
			return new Timestamp(DateUtils.formatDate(dateStr.toString(), pattern.toString()).getTime());
		}

		pattern.append("HH");
		dateStr.append(new DecimalFormat("00").format(Long.parseLong(hour)));

		if (StringUtils.isBlank(minute) || !NumberUtils.isDigits(minute)) {
			return new Timestamp(DateUtils.formatDate(dateStr.toString(), pattern.toString()).getTime());
		}

		pattern.append("mm");
		dateStr.append(new DecimalFormat("00").format(Long.parseLong(minute)));
		return new Timestamp(DateUtils.formatDate(dateStr.toString(), pattern.toString()).getTime());
	}


}
