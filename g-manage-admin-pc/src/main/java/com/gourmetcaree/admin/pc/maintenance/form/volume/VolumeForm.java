package com.gourmetcaree.admin.pc.maintenance.form.volume;

import java.io.Serializable;
import java.text.ParseException;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.DateUtils;

/**
 * 号数データの入力用フォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class VolumeForm extends VolumeBaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2647669317674250240L;

	/** 締切日時(年月日) */
	@Required
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String deadlineDate;

	/** 締切日時(時) */
	@Required
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String deadlineHour;

	/** 締切日時(分) */
	@Required
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String deadlineMinute;

	/** 確定締切日時(年月日) */
	@Required
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String fixedDeadlineDate;

	/** 確定締切日時(時) */
	@Required
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String fixedDeadlineHour;

	/** 確定締切日時(分) */
	@Required
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String fixedDeadlineMinute;

	/** 掲載開始日時(年月日) */
	@Required
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String postStartDate;

	/** 掲載開始日時(時) */
	@Required
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String postStartHour;

	/** 掲載開始日時(分) */
	@Required
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String postStartMinute;

	/** 掲載終了日時(年月日) */
	@Required
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String postEndDate;

	/** 掲載終了日時(時) */
	@Required
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String postEndHour;

	/** 掲載終了日時(分) */
	@Required
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String postEndMinute;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		try {

			// 締切日時をセット
			deadlineDatetime = DateUtils.formatDate(deadlineDate, deadlineHour, deadlineMinute);
			deadlineDatetimeStr = DateUtils.getDateStr(deadlineDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			// 確定締切日時をセット
			fixedDeadlineDatetime = DateUtils.formatDate(fixedDeadlineDate, fixedDeadlineHour, fixedDeadlineMinute);
			fixedDeadlineDatetimeStr = DateUtils.getDateStr(fixedDeadlineDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			// 掲載開始日時をセット
			postStartDatetime = DateUtils.formatDate(postStartDate, postStartHour, postStartMinute);
			postStartDatetimeStr = DateUtils.getDateStr(postStartDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			// 掲載終了日時をセット
			postEndDatetime = DateUtils.formatDate(postEndDate, postEndHour, postEndMinute);
			postEndDatetimeStr = DateUtils.getDateStr(postEndDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

			// 締切日時より 確定締切日時が先の場合エラー
			if (DateUtils.compareDateTime(deadlineDatetime, fixedDeadlineDatetime) < 0) {
				//「締切日時には確定締切日時より前の日時を入力してください。」
				errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.deadlineDatetime"), MessageResourcesUtil.getMessage("labels.fixedDeadlineDatetime")));
			}
			// 確定締切日時より 掲載開始日時が先の場合エラー
			if (DateUtils.compareDateTime(fixedDeadlineDatetime, postStartDatetime) < 0) {
				// 「確定締切日時には掲載開始日時より前の日時を入力してください。」
				errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.fixedDeadlineDatetime"), MessageResourcesUtil.getMessage("labels.postStartDatetime")));
			}
			// 掲載開始日時より掲載終了日時が先の場合エラー(同じ日付もエラー）
			if (DateUtils.compareDateTime(postStartDatetime, postEndDatetime) <= 0) {
				// 「掲載開始日時には掲載終了日時より前の日時を入力してください。」
				errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.postStartDatetime"), MessageResourcesUtil.getMessage("labels.postEndDatetime")));
			}

		} catch (ParseException e) {
			// 日付の変換に失敗した場合のエラー「日時を正しく入力してください。」
			errors.add("errors", new ActionMessage("errors.app.datetimeFailed"));
		}

		return errors;
	}

	public void resetForm() {
		resetBaseVolumeForm();
		deadlineDate = null;
		deadlineHour = null;
		deadlineMinute = null;
		fixedDeadlineDate = null;
		fixedDeadlineHour = null;
		fixedDeadlineMinute = null;
		postStartDate = null;
		postStartHour = null;
		postStartMinute = null;
		postEndDate = null;
		postEndHour = null;
		postEndMinute = null;
	}
}