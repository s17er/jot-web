package com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;


/**
 * 事前登録入力系ベースフォーム
 * @author Takehiro Nakamori
 *
 */
public abstract class AbstractAdvancedRegistrationInputBaseForm extends BaseForm {

	/**
	 *
	 */
	private static final long serialVersionUID = -7194788171893245668L;

	/** id */
	public String id;

	/** バージョン */
	public Integer version;

	/** エリア */
	@Required
	public Integer areaCd;

	/** 事前登録名 */
	@Required
	public String advancedRegistrationName;

	/** 事前登録省略名 */
	@Required
	public String advancedRegistrationShortName;

	/** 開始年月日 */
	@Required
	public String termStartDate;

	/** 開始時 */
	@Required
	public String termStartHour;

	/** 開始分 */
	@Required
	public String termStartMinute;

	/** 終了年月日 */
	@Required
	public String termEndDate;

	/** 終了時 */
	@Required
	public String termEndHour;

	/** 終了分 */
	@Required
	public String termEndMinute;


	/**
	 * バリデート
	 * @return
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		return errors;
	}


	/**
	 * 日付バリデート
	 * @param errors エラー
	 */
	protected ActionMessages validateDate() {
		ActionMessages errors = new ActionMessages();

		Date startDate = null;
		Date endDate = null;
		if (!GourmetCareeUtil.existBlankStr(termStartDate, termStartHour, termStartMinute)) {
			try {
				startDate = createTermStartDatetime();
			} catch (ParseException e) {
				errors.add("errors", new ActionMessage("errors.app.targetDateFailed",
						MessageResourcesUtil.getMessage("labels.termStartDatetime")));
			}
		}

		if (!GourmetCareeUtil.existBlankStr(termEndDate, termEndHour, termEndMinute)) {
			try {
				endDate = createTermEndDatetime();
			} catch (ParseException e) {
				errors.add("errors", new ActionMessage("errors.app.targetDateFailed",
						MessageResourcesUtil.getMessage("labels.termEndDatetime")));
			}
		}

		if (startDate != null && endDate != null) {
			if (startDate.compareTo(endDate) >= 0) {
				errors.add("errors", new ActionMessage("errors.app.TermSet",
						MessageResourcesUtil.getMessage("labels.termStartDatetime"),
						MessageResourcesUtil.getMessage("labels.termEndDatetime")
						));
			}
		}

		return errors;
	}


	/**
	 * 開始日時を取得
	 * @return 開始日時
	 * @throws ParseException 変換失敗時にスロー
	 */
	public Timestamp createTermStartDatetime() throws ParseException {
		String datetime = String.format("%s %s:%s", termStartDate, termStartHour, termStartMinute);
		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT);
		sdf.setLenient(false);

		return new Timestamp(sdf.parse(datetime).getTime());
	}


	public String getTermStartDatetimeStr() {
		try {
			return new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT).format(createTermStartDatetime());
		} catch (ParseException e) {
			return "";
		}
	}

	/**
	 * 終了日時を取得
	 * @return 終了日時
	 * @throws ParseException 変換失敗時にスロー
	 */
	public Timestamp createTermEndDatetime() throws ParseException {
		String datetime = String.format("%s %s:%s", termEndDate, termEndHour, termEndMinute);
		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT);
		sdf.setLenient(false);

		return new Timestamp(sdf.parse(datetime).getTime());
	}

	public String getTermEndDatetimeStr() {
		try {
			return new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT).format(createTermEndDatetime());
		} catch (ParseException e) {
			return "";
		}
	}


	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		super.resetBaseForm();
		id = null;

		version = null;

		areaCd = null;

		advancedRegistrationName = null;

		advancedRegistrationShortName = null;

		termStartDate = null;

		termStartHour = null;

		termStartMinute = null;

		termEndDate = null;

		termEndHour = null;

		termEndMinute = null;
	}


}
