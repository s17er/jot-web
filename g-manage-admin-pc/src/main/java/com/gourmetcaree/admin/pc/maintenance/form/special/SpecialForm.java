package com.gourmetcaree.admin.pc.maintenance.form.special;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

/**
 * 特集データの入力用フォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class SpecialForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1895359219102485977L;

	/** 特集ID */
	public String id;

	/** 特集名 */
	@Required
	@Maxlength(maxlength=35)
	public String specialName;

	/** 表示名 */
	@Required
	public String displayName;

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

	/** エリアコード */
	@Required
	public List<String> areaCd = new ArrayList<String>();

	/** バージョン番号 */
	public String version;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 掲載開始日時 */
	public String postStartDatetimeStr;

	/** 掲載終了日時 */
	public String postEndDatetimeStr;

	/** 説明 */
	public String explanation;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		try {
			// 掲載開始日時をセット
			postStartDatetime = DateUtils.formatDate(postStartDate, postStartHour, postStartMinute);
			postStartDatetimeStr = DateUtils.getDateStr(postStartDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

			// 掲載終了日時をセット
			postEndDatetime = DateUtils.formatDate(postEndDate, postEndHour, postEndMinute);
			postEndDatetimeStr = DateUtils.getDateStr(postEndDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

			// 開始日時より終了日時が先の場合エラー（同じ日付もエラー）
			if (DateUtils.compareDateTime(postStartDatetime, postEndDatetime) <= 0) {
				errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.postStartDatetime"), MessageResourcesUtil.getMessage("labels.postEndDatetime")));
			}

		} catch (ParseException e) {
			errors.add("errors", new ActionMessage("errors.app.dateFailed"));
		}

		return errors;
	}

	/**
	 * multiboxのリセットを行う
	 */
	public void resetMultibox() {
		// エリアコード
		this.areaCd = new ArrayList<String>();
	}



	public void resetForm() {
		id = null;
		specialName = null;
		displayName = null;
		postStartDate = null;
		postStartHour = null;
		postStartMinute = null;
		postEndDate = null;
		postEndHour = null;
		postEndMinute = null;
		areaCd = new ArrayList<String>();
		version = null;
		postStartDatetime = null;
		postEndDatetime = null;
		postStartDatetimeStr = null;
		postEndDatetimeStr = null;
		explanation = null;
	}
}