package com.gourmetcaree.admin.pc.interest.form.interest;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * 気になる通知データ一覧のフォーム
 * @author t_shiroumaru
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

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
	public String where_memberName;

	/** PCメールアドレス */
	public String where_loginId;

	/** モバイルメールアドレス */
	public String where_subMailAddress;

	/** 年齢(下限) */
	@IntegerHankakuType
	public String where_lowerAge;

	/** 年齢(上限) */
	@IntegerHankakuType
	public String where_upperAge;

	/** 性別区分 */
	public String where_sexKbn;

	/** 雇用形態区分 */
	public String where_empPtnKbn;

	/** 顧客名 */
	public String where_customerName;

	/** 気になる通知先名 */
	public String where_manuscriptName;

	/** 会社ID */
	public String where_companyId;

	/** 営業ID */
	public String where_salesId;

	/** 希望職種 */
	public String where_hopeJob;

	/** 気になる通知日(開始） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_interestStartDate;

	/** 気になる通知時(開始） */
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String where_interestStartHour;

	/** 気になる通知分(開始） */
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String where_interestStartMinute;

	/** webID */
	@IntegerHankakuType
	public String where_webId;

	/** 気になる通知日(終了） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_interestEndDate;

	/** 気になる通知時(終了） */
	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String where_interestEndHour;

	/** 気になる通知分(終了） */
	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String where_interestEndMinute;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 年齢の指定チェック
		checkAge(errors);

		// 気になる通知日時チェック
		checkInterestDate(errors);

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
	 * 気になる日時の指定が正しいかチェック
	 * @param errors
	 */
	private void checkInterestDate(ActionMessages errors) {

		// チェックフラグ
		boolean checkFlg = true;

		// 気になる通知日(開始)が指定されずに時間、分が指定されている場合はエラー
		if (StringUtils.isEmpty(where_interestStartDate) && StringUtils.isNotEmpty(where_interestStartHour) && StringUtils.isEmpty(where_interestStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_interestStartHour"),
					MessageResourcesUtil.getMessage("labels.where_interestStartDate"),
					MessageResourcesUtil.getMessage("labels.where_interestStartMinute")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_interestStartDate) && StringUtils.isNotEmpty(where_interestStartHour) && StringUtils.isNotEmpty(where_interestStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputTwoVsOne",
					MessageResourcesUtil.getMessage("labels.where_interestStartHour"),
					MessageResourcesUtil.getMessage("labels.where_interestStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_interestStartDate")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_interestStartDate) && StringUtils.isEmpty(where_interestStartHour) && StringUtils.isNotEmpty(where_interestStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_interestStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_interestStartDate"),
					MessageResourcesUtil.getMessage("labels.where_interestStartHour")));
			checkFlg = false;
		// 気になる通知時間(開始)が指定されずに、分が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_interestStartDate) && StringUtils.isEmpty(where_interestStartHour) && StringUtils.isNotEmpty(where_interestStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_interestStartMinute"),
					MessageResourcesUtil.getMessage("labels.where_interestStartHour")));
			checkFlg = false;
		// 気になる通知分(開始)が指定されずに、時間が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_interestStartDate) && StringUtils.isNotEmpty(where_interestStartHour) && StringUtils.isEmpty(where_interestStartMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_interestStartHour"),
					MessageResourcesUtil.getMessage("labels.where_interestStartMinute")));
			checkFlg = false;
		}

		// 気になる通知日(終了)が指定されずに時間、分が指定されている場合はエラー
		if (StringUtils.isEmpty(where_interestEndDate) && StringUtils.isNotEmpty(where_interestEndHour) && StringUtils.isEmpty(where_interestEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_interestEndHour"),
					MessageResourcesUtil.getMessage("labels.where_interestEndDate"),
					MessageResourcesUtil.getMessage("labels.where_interestEndMinute")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_interestEndDate) && StringUtils.isNotEmpty(where_interestEndHour) && StringUtils.isNotEmpty(where_interestEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputTwoVsOne",
					MessageResourcesUtil.getMessage("labels.where_interestEndHour"),
					MessageResourcesUtil.getMessage("labels.where_interestEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_interestEndDate")));
			checkFlg = false;
		} else if (StringUtils.isEmpty(where_interestEndDate) && StringUtils.isEmpty(where_interestEndHour) && StringUtils.isNotEmpty(where_interestEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.where_interestEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_interestEndDate"),
					MessageResourcesUtil.getMessage("labels.where_interestEndHour")));
			checkFlg = false;
		// 気になる通知時間(終了)が指定されずに、分が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_interestEndDate) && StringUtils.isEmpty(where_interestEndHour) && StringUtils.isNotEmpty(where_interestEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_interestEndMinute"),
					MessageResourcesUtil.getMessage("labels.where_interestEndHour")));
			checkFlg = false;
		// 気になる通知分(終了)が指定されずに、時間が指定されている場合はエラー
		} else if (StringUtils.isNotEmpty(where_interestEndDate) && StringUtils.isNotEmpty(where_interestEndHour) && StringUtils.isEmpty(where_interestEndMinute)) {
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.where_interestEndHour"),
					MessageResourcesUtil.getMessage("labels.where_interestEndMinute")));
			checkFlg = false;
		}

		// 気になる通知日時が正しく入力されている場合は、開始日と終了日を比較する
		if (checkFlg && (StringUtils.isNotEmpty(where_interestStartDate) && StringUtils.isNotEmpty(where_interestEndDate))) {
			try {
				// 気になる通知日(開始)が気になる通知日(終了)より後の日付の場合はエラー
				if (DateUtils.compareDateTime(DateUtils.formatDate(where_interestStartDate, where_interestStartHour, where_interestStartMinute),
						DateUtils.formatDate(where_interestEndDate, where_interestEndHour, where_interestEndMinute)) < 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet",
							MessageResourcesUtil.getMessage("labels.where_interestStartDateTime"),
							MessageResourcesUtil.getMessage("labels.where_interestEndDateTime")));
				}
			} catch (ParseException e) {
				errors.add("errors", new ActionMessage("errors.app.dateTimeFailed"));
			}
		}

	}


	/**
	 * リセットを行う
	 */
	public void resetForm() {
		pageNum = null;
		maxRow = null;
		where_areaCd = null;
		where_industryCd = null;
		where_prefecturesCd = null;
		where_memberName = null;
		where_loginId = null;
		where_subMailAddress = null;
		where_lowerAge = null;
		where_upperAge = null;
		where_sexKbn = null;
		where_empPtnKbn = null;
		where_customerName = null;
		where_manuscriptName = null;
		where_interestStartDate = null;
		where_interestStartHour = null;
		where_interestStartMinute = null;
		where_webId = null;
		where_interestEndDate = null;
		where_interestEndHour = null;
		where_interestEndMinute = null;
		where_companyId = null;
		where_salesId = null;
		where_hopeJob = null;
	}

}