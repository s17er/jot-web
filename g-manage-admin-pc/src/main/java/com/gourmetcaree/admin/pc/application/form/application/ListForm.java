package com.gourmetcaree.admin.pc.application.form.application;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.util.DateUtils;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * 応募データ一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends ApplicationForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -495499186245738617L;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** エリアコード */
	@Required
	public String where_areaCd;

	/** 業種コード */
	public String where_industryCd;

	/** 都道府県 */
	public String where_prefecturesCd;

	/** 氏名 */
	public String where_name;

	/** 氏名フリガナ */
	public String where_nameKana;

	/** PCメールアドレス */
	public String where_pcMail;

	/** モバイルメールアドレス */
	public String where_mobileMail;

	/** 年齢(下限) */
	@IntegerHankakuType
	public String where_lowerAge;

	/** 年齢(上限) */
	@IntegerHankakuType
	public String where_upperAge;

	/** 性別区分 */
	public String where_sexKbn;

	/** 応募ID */
	@IntegerHankakuType(arg0 = @Arg(key="応募ID", resource = false))
	public String where_applicationId;

	/** 会員登録フラグ */
	public String where_memberFlg;

	/** 雇用形態区分 */
	public String where_empPtnKbn;

	/** 端末区分 */
	public String where_terminalKbn;

	/** 顧客名 */
	public String where_customerName;

	/** GCWコード */
	public String where_gcwCd;

	/** 応募先名 */
	public String where_applicationName;

	/** 会社ID */
	public String where_companyId;

	/** 営業ID */
	public String where_salesId;

	/** 希望職種 */
	public String where_hopeJob;

	/** 応募日(開始） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_applicationStartDate;

	/** 応募時(開始） */
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String where_applicationStartHour;

	/** 応募分(開始） */
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String where_applicationStartMinute;

	/** webID */
	@IntegerHankakuType
	public String where_webId;

	/** 応募日(終了） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_applicationEndDate;

	/** 応募時(終了） */
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String where_applicationEndHour;

	/** 応募分(終了） */
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String where_applicationEndMinute;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 年齢の指定チェック
		checkAge(errors);

		// 応募日時チェック
		checkApplicationDate(errors);

		return errors;
	}

	/**
	 * 年齢の指定が正しいかチェック
	 * @param errors
	 */
	private void checkAge(ActionMessages errors) {

		if (StringUtils.isNotEmpty(where_lowerAge) && StringUtils.isNotEmpty(where_upperAge)) {
			try {
				// 上限年齢が下限年齢より小さい場合はエラー
				if (Integer.parseInt(where_lowerAge) > Integer.parseInt(where_upperAge)) {
					errors.add("errors", new ActionMessage("errors.fraudurentAge"));
				}
			} catch (NumberFormatException e1) {
				errors.add("errors", new ActionMessage("errors.ageFailed"));
			}
		}
	}

	/**
	 * 応募日時の指定が正しいかチェック
	 * @param errors
	 */
	private void checkApplicationDate(ActionMessages errors) {

		// チェックフラグ
		boolean checkFlg = true;

		// 応募日(開始)が指定されずに時間、分が指定されている場合はエラー
		if (StringUtils.isEmpty(where_applicationStartDate) && StringUtils.isNotEmpty(where_applicationStartHour) && StringUtils.isEmpty(where_applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_applicationStartDate) && StringUtils.isNotEmpty(where_applicationStartHour) && StringUtils.isNotEmpty(where_applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputTwoVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartDate")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_applicationStartDate) && StringUtils.isEmpty(where_applicationStartHour) && StringUtils.isNotEmpty(where_applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour")));
			checkFlg = false;
		// 応募時間(開始)が指定されずに、分が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_applicationStartDate) && StringUtils.isEmpty(where_applicationStartHour) && StringUtils.isNotEmpty(where_applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour")));
			checkFlg = false;
		// 応募分(開始)が指定されずに、時間が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_applicationStartDate) && StringUtils.isNotEmpty(where_applicationStartHour) && StringUtils.isEmpty(where_applicationStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationStartHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationStartMinute")));
			checkFlg = false;
		}

		// 応募日(終了)が指定されずに時間、分が指定されている場合はエラー
		if (StringUtils.isEmpty(where_applicationEndDate) && StringUtils.isNotEmpty(where_applicationEndHour) && StringUtils.isEmpty(where_applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_applicationEndDate) && StringUtils.isNotEmpty(where_applicationEndHour) && StringUtils.isNotEmpty(where_applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputTwoVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndDate")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_applicationEndDate) && StringUtils.isEmpty(where_applicationEndHour) && StringUtils.isNotEmpty(where_applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndDate"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour")));
			checkFlg = false;
		// 応募時間(終了)が指定されずに、分が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_applicationEndDate) && StringUtils.isEmpty(where_applicationEndHour) && StringUtils.isNotEmpty(where_applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour")));
			checkFlg = false;
		// 応募分(終了)が指定されずに、時間が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_applicationEndDate) && StringUtils.isNotEmpty(where_applicationEndHour) && StringUtils.isEmpty(where_applicationEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_applicationEndHour"),
					MessageResourcesUtil.getMessage("labels.where_applicationEndMinute")));
			checkFlg = false;
		}

		// 応募日時が正しく入力されている場合は、開始日と終了日を比較する
		if (checkFlg && (StringUtils.isNotEmpty(where_applicationStartDate) && StringUtils.isNotEmpty(where_applicationEndDate))) {
			try {
				// 応募日(開始)が応募日(終了)より後の日付の場合はエラー
				if (DateUtils.compareDateTime(DateUtils.formatDate(where_applicationStartDate, where_applicationStartHour, where_applicationStartMinute),
						DateUtils.formatDate(where_applicationEndDate, where_applicationEndHour, where_applicationEndMinute)) < 0) {
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
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();

		pageNum = null;
		maxRow = null;
		where_areaCd = null;
		where_industryCd = null;
		where_prefecturesCd = null;
		where_name = null;
		where_nameKana = null;
		where_pcMail = null;
		where_mobileMail = null;
		where_lowerAge = null;
		where_upperAge = null;
		where_sexKbn = null;
		where_applicationId = null;
		where_memberFlg = null;
		where_empPtnKbn = null;
		where_terminalKbn = null;
		where_customerName = null;
		where_gcwCd = null;
		where_applicationName = null;
		where_applicationStartDate = null;
		where_applicationStartHour = null;
		where_applicationStartMinute = null;
		where_webId = null;
		where_applicationEndDate = null;
		where_applicationEndHour = null;
		where_applicationEndMinute = null;
		where_companyId = null;
		where_salesId = null;
		where_hopeJob = null;
	}

}