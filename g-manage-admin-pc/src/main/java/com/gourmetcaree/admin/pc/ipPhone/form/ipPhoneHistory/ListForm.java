package com.gourmetcaree.admin.pc.ipPhone.form.ipPhoneHistory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntRange;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.SearchProperty;

/**
 * IP電話応募履歴の一覧フォーム
 * @author nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	private static final long serialVersionUID = -21372581009839861L;

	/** 顧客ID */
	@IntegerHankakuType
	public String customerId;

	/** 原稿名 */
	public String manuscriptName;

	/** 顧客名 */
	public String customerName;

	/** 応募者電話番号 */
	public String memberTel;

	/** 会社ID */
	public String where_companyId;

	/** 営業ID */
	public String where_salesId;

	/** IP電話番号(顧客電話) */
	public String ipPhoneTel;

	/** 検索開始日時 */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String ipPhoneStartDate;

	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String ipPhoneStartHour;

	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String ipPhoneStartMinute;

	/** 検索終了日時 */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String ipPhoneEndDate;

	@IntegerHankakuType
	@IntRange(min=0, max=23)
	public String ipPhoneEndHour;

	@IntegerHankakuType
	@IntRange(min=00, max=59)
	public String ipPhoneEndMinute;

	@IntegerHankakuType
	public String webId;

	/** 表示件数 */
	public String maxRow;

	/** ページ番号 */
	public String pageNum;

	/**
	 * バリデートを行います。
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		validateDate(errors);
		return errors;
	}

	/**
	 * 日付のバリデートを行います。
	 */
	private void validateDate(ActionMessages errors) {

		// 開始チェック
		boolean checkFlg = true;
		// 分が入っていて、日時が入っていない場合
		if (StringUtils.isNotEmpty(ipPhoneStartMinute)
				&& (StringUtils.isEmpty(ipPhoneStartDate) || StringUtils.isEmpty(ipPhoneStartHour))) {
			//{応募日時(開始分)}入力時は、{応募日時(開始日)}、{応募日時(開始時)}を入力してください。
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.ipPhoneStartMinute"),
					MessageResourcesUtil.getMessage("labels.ipPhoneStartDate"),
					MessageResourcesUtil.getMessage("labels.ipPhoneStartHour")));
			checkFlg = false;

		// 時が入っていて、日が入っていない場合
		}else if (StringUtils.isNotEmpty(ipPhoneStartHour) && StringUtils.isEmpty(ipPhoneStartDate)) {
			// {応募日時(開始分)}入力時は、{応募日時(開始日)}を入力してください。
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.ipPhoneStartHour"),
					MessageResourcesUtil.getMessage("labels.ipPhoneStartDate")));
			checkFlg = false;
		}

		// 終了チェック
		// 分が入っていて、日時が入っていない場合
		if (StringUtils.isNotEmpty(ipPhoneEndMinute)
				&& (StringUtils.isEmpty(ipPhoneEndDate) || StringUtils.isEmpty(ipPhoneEndHour))) {
			//{応募日時(終了分)}入力時は、{応募日時(終了日)}、{応募日時(終了時)}を入力してください。
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsTwo",
					MessageResourcesUtil.getMessage("labels.ipPhoneEndMinute"),
					MessageResourcesUtil.getMessage("labels.ipPhoneEndDate"),
					MessageResourcesUtil.getMessage("labels.ipPhoneEndHour")));
			checkFlg = false;

		// 時が入っていて、日が入っていない場合
		} else if (StringUtils.isNotEmpty(ipPhoneEndHour) && StringUtils.isEmpty(ipPhoneEndDate)) {
			// {応募日時(開始分)}入力時は、{応募日時(開始日)}を入力してください。
			errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
					MessageResourcesUtil.getMessage("labels.ipPhoneEndHour"),
					MessageResourcesUtil.getMessage("labels.ipPhoneEndDate")));
			checkFlg = false;
		}

		if (!checkFlg) {
			return;
		}

		// 応募日時が正しく入力されている場合は、開始日と終了日を比較する
		if (checkFlg && (StringUtils.isNotEmpty(ipPhoneStartDate) && StringUtils.isNotEmpty(ipPhoneEndDate))) {
			try {
				// 応募日(開始)が応募日(終了)より後の日付の場合はエラー
				if (DateUtils.compareDateTime(DateUtils.formatDate(ipPhoneStartDate, ipPhoneStartHour, ipPhoneStartMinute),
						DateUtils.formatDate(ipPhoneEndDate, ipPhoneEndHour, ipPhoneEndMinute)) < 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet",
							MessageResourcesUtil.getMessage("labels.ipPhoneStartDateTime"),
							MessageResourcesUtil.getMessage("labels.ipPhoneEndDateTime")));
				}
			} catch (ParseException e) {
				errors.add("errors", new ActionMessage("errors.app.datetimeFailed"));
			}
		}
	}

	/**
	 * フォームのリセット
	 */
	public void resetForm() {

		super.resetBaseForm();

		/* 顧客ID */
		customerId = null;

		/* 原稿名 */
		manuscriptName = null;

		/* 顧客名 */
		customerName = null;

		/* 応募者電話番号 */
		memberTel = null;

		/* IP電話番号(顧客電話) */
		ipPhoneTel = null;

		/* 検索開始日時 */
		ipPhoneStartDate = null;

		ipPhoneStartHour = null;

		ipPhoneStartMinute = null;

		/* 検索終了日時 */
		ipPhoneEndDate = null;

		ipPhoneEndHour = null;

		ipPhoneEndMinute = null;

		/* 表示件数 */
		maxRow = null;

		/* ページ番号 */
		pageNum = null;

		webId = null;

		where_companyId = null;
		where_salesId = null;
	}

	/**
	 * ページ番号を初期化
	 */
	public void resetPageNum() {
		pageNum = null;
	}

	/**
	 * 最大表示件数のint値を作成
	 */
	private int createMaxRow() {
		return NumberUtils.toInt(maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	/**
	 * ページ番号のint値を作成
	 */
	private int createPageNum() {
		return NumberUtils.toInt(pageNum, GourmetCareeConstants.DEFAULT_PAGE);
	}

	/**
	 * 検索プロパティを作成
	 */
	public SearchProperty createSearchProperty() {
		SearchProperty property = Beans.createAndCopy(SearchProperty.class, this)
				.excludes("searchStartDatetime", "searchEndDatetime")
				.execute();

		createDate(property);
		property.maxRow = createMaxRow();
		property.targetPage = createPageNum();
		return property;
	}

	/**
	 * 日付項目を生成
	 */
	private void createDate(SearchProperty property) {

		if (StringUtils.isNotBlank(ipPhoneStartDate)) {
			try {
				Date date = DateUtils.parseDate(ipPhoneStartDate, ipPhoneStartHour, ipPhoneStartMinute);
				property.searchStartDatetime = new Timestamp(date.getTime());
			} catch (ParseException e) {
				throw new ActionMessagesException("errors.app.targetDateFailed",
						MessageResourcesUtil.getMessage("labels.ipPhoneStartDateTime"));
			}
		}

		if (StringUtils.isNotBlank(ipPhoneEndDate)) {
			try {
				// 分指定がなければ59分をセット
				String whereEndMinute = StringUtils.isEmpty(ipPhoneEndMinute) ? "59" : ipPhoneEndMinute;
				// 時指定がなければ23時をセット
				String whereEndHour = StringUtils.isEmpty(ipPhoneEndHour) ? "23" : ipPhoneEndHour;
				// 59秒をセットして日時分秒を生成
				Date date = DateUtils.parseDate(ipPhoneEndDate, whereEndHour, whereEndMinute, "59");
				property.searchEndDatetime = new Timestamp(date.getTime());
			} catch (ParseException e) {
				throw new ActionMessagesException("errors.app.targetDateFailed",
						MessageResourcesUtil.getMessage("labels.ipPhoneEndDateTime"));
			}
		}
	}


}
