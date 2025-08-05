package com.gourmetcaree.admin.pc.application.form.arbeitApplication;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * グルメdeバイト応募管理一覧アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8844192927629618378L;


	/** 都道府県コード */
	public String prefecturesCd;

	/** 年齢(下限) */
	@IntegerHankakuType
	public String lowerAge;

	/** 年齢(上限) */
	@IntegerHankakuType
	public String upperAge;

	/** 性別区分 */
	public String sexKbn;

	/** 応募ID */
	@IntegerHankakuType
	public String applicationId;

	/** 端末区分 */
	public String terminalKbn;

	/** 顧客名 */
	public String customerName;

	/** 氏名 */
	public String name;

	/** 氏名フリガナ */
	public String nameKana;

	/** メールアドレス */
	public String mailAddress;

	/** バイト側業態 */
	public String arbeitGyotaiId;

	/** 応募名 */
	public String applicationName;

	/** エリア */
	public String areaCd;

	/** 現在の職業区分 */
	public Integer currentJobKbn;

	/** 飲食店勤務の経験区分 */
	public Integer foodExpKbn;

	/** 店舗一覧ID */
	public String shopListId;

	/** 応募日(開始） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String applicationStartDate;

	/** 応募時(開始） */
	@IntRange(min=0, max=23)
	public String applicationStartHour;

	/** 応募分(開始） */
	@IntRange(min=00, max=59)
	public String applicationStartMinute;

	/** 応募日(終了） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String applicationEndDate;

	/** 応募時(終了） */
	@IntRange(min=0, max=23)
	public String applicationEndHour;

	/** 応募分(終了） */
	@IntRange(min=00, max=59)
	public String applicationEndMinute;


	/** 最大表示件数 */
	public String maxRow;

	/** ページ番号 */
	public String pageNum;

	/**
	 * リセットフォーム
	 */
	public void resetForm() {
		super.resetBaseForm();

		/** 都道府県コード */
		prefecturesCd = null;

		/** 年齢(下限) */
		lowerAge = null;

		/** 年齢(上限) */
		upperAge = null;

		/** 性別区分 */
		sexKbn = null;

		/** 応募ID */
		applicationId = null;

		/** 端末区分 */
		terminalKbn = null;

		/** 顧客名 */
		customerName = null;

		/** 氏名 */
		name = null;

		/** 氏名フリガナ */
		nameKana = null;

		/** メールアドレス */
		mailAddress = null;

		/** 応募名 */
		applicationName = null;

		maxRow = null;

		/** バイト側業態 */
		arbeitGyotaiId = null;

		/** 店舗一覧ID */
		shopListId = null;

		/** 応募日(開始） */
		applicationStartDate = null;

		/** 応募時(開始） */
		applicationStartHour = null;

		/** 応募分(開始） */
		applicationStartMinute = null;

		/** 応募日(終了） */
		applicationEndDate = null;

		/** 応募時(終了） */
		applicationEndHour = null;

		/** 応募分(終了） */
		applicationEndMinute = null;

		/** エリア */
		areaCd = null;

		currentJobKbn = null;

		/** 飲食店勤務の経験区分 */
		foodExpKbn = null;

		pageNum = null;
	}



	/**
	 * バリデートを行います。
	 * @return エラー
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		checkAge(errors);
		checkApplicationDate(errors);
		return errors;

	}

	/**
	 * 応募日時の指定が正しいかチェック
	 * @param errors
	 */
	private void checkApplicationDate(ActionMessages errors) {

		// チェックフラグ
		boolean checkFlg = true;


		// 応募日(開始)が指定されずに時間、分が指定されている場合はエラー
		if (StringUtils.isEmpty(applicationStartDate) && StringUtils.isNotEmpty(applicationStartHour) && StringUtils.isEmpty(applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(applicationStartDate) && StringUtils.isNotEmpty(applicationStartHour) && StringUtils.isNotEmpty(applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputTwoVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartDate")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(applicationStartDate) && StringUtils.isEmpty(applicationStartHour) && StringUtils.isNotEmpty(applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour")));
			checkFlg = false;
		// 応募時間(開始)が指定されずに、分が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(applicationStartDate) && StringUtils.isEmpty(applicationStartHour) && StringUtils.isNotEmpty(applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour")));
			checkFlg = false;
		// 応募分(開始)が指定されずに、時間が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(applicationStartDate) && StringUtils.isNotEmpty(applicationStartHour) && StringUtils.isEmpty(applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute")));
			checkFlg = false;
		}

		// 応募日(終了)が指定されずに時間、分が指定されている場合はエラー
		if (StringUtils.isEmpty(applicationEndDate) && StringUtils.isNotEmpty(applicationEndHour) && StringUtils.isEmpty(applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(applicationEndDate) && StringUtils.isNotEmpty(applicationEndHour) && StringUtils.isNotEmpty(applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputTwoVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndDate")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(applicationEndDate) && StringUtils.isEmpty(applicationEndHour) && StringUtils.isNotEmpty(applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour")));
			checkFlg = false;
		// 応募時間(終了)が指定されずに、分が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(applicationEndDate) && StringUtils.isEmpty(applicationEndHour) && StringUtils.isNotEmpty(applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour")));
			checkFlg = false;
		// 応募分(終了)が指定されずに、時間が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(applicationEndDate) && StringUtils.isNotEmpty(applicationEndHour) && StringUtils.isEmpty(applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute")));
			checkFlg = false;
		}

		// 応募日時が正しく入力されている場合は、開始日と終了日を比較する
		if (checkFlg && (StringUtils.isNotEmpty(applicationStartDate) && StringUtils.isNotEmpty(applicationEndDate))) {
			try {
				// 応募日(開始)が応募日(終了)より後の日付の場合はエラー
				if (DateUtils.compareDateTime(DateUtils.formatDate(applicationStartDate, applicationStartHour, applicationStartMinute),
						DateUtils.formatDate(applicationEndDate, applicationEndHour, applicationEndMinute)) < 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet",
							MessageResourcesUtil.getMessage("labels.where_applicationStartDateTime"),
							MessageResourcesUtil.getMessage("labels.where_applicationEndDateTime")));
				}
			} catch (ParseException e) {
				errors.add("errors", new ActionMessage("errors.app.dateTimeFailed"));
			}
		}
	}


	/**
	 * 年齢の指定が正しいかチェック
	 * @param errors
	 */
	private void checkAge(ActionMessages errors) {

		if (StringUtils.isNotEmpty(lowerAge) && StringUtils.isNotEmpty(upperAge)) {
			try {
				// 上限年齢が下限年齢より小さい場合はエラー
				if (Integer.parseInt(lowerAge) > Integer.parseInt(upperAge)) {
					errors.add("errors", new ActionMessage("errors.fraudurentAge"));
				}
			} catch (NumberFormatException e1) {
				errors.add("errors", new ActionMessage("errors.ageFailed"));
			}
		}
	}

	/**
	 * 応募日時(開始)をTimestampで取得します。
	 * @return 応募日時(開始)のTimestamp
	 * @throws ParseException 変換に失敗した場合にスロー
	 */
	public Timestamp createApplicationStartDatetimeAsTimestamp() throws ParseException {
		return createTimestamp(applicationStartDate, applicationStartHour, applicationStartMinute);
	}


	/**
	 * 応募日時(終了)をTimestampで取得します。
	 * @return 応募日時(終了)のTimestamp
	 * @throws ParseException 変換に失敗した場合にスロー
	 */
	public Timestamp createApplicationEndDatetimeAsTimestamp() throws ParseException {

		Timestamp time = createTimestamp(applicationEndDate, applicationEndHour, applicationEndMinute);
		if (time == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		if (StringUtils.isBlank(applicationEndHour)) {
			cal.add(Calendar.DATE, 1);
		} else {
			cal.add(Calendar.MINUTE, 1);
		}

		return new Timestamp(cal.getTimeInMillis());
	}


	/**
	 * 日付、時間、分からTimestampを作成します。
	 * @param date 日付
	 * @param hour 時間
	 * @param minute 分
	 * @return Timestamp
	 * @throws ParseException 変換に失敗した場合にスロー
	 */
	private static Timestamp createTimestamp(String date, String hour, String minute) throws ParseException {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		StringBuilder format = new StringBuilder(GourmetCareeConstants.DATE_FORMAT_SLASH);
		StringBuilder sb = new StringBuilder(date);

		Format decimalFormat = new DecimalFormat("00");

		if (StringUtils.isNotBlank(hour)) {
			format.append("HH");
			sb.append(decimalFormat.format(NumberUtils.toInt(hour)));

			if (StringUtils.isNotBlank(minute)) {
				format.append("mm");
				sb.append(decimalFormat.format(NumberUtils.toInt(minute)));
			}
		}

		return new Timestamp(new SimpleDateFormat(format.toString()).parse(sb.toString()).getTime());
	}
}
