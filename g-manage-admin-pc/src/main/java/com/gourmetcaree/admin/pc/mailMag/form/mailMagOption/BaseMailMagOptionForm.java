package com.gourmetcaree.admin.pc.mailMag.form.mailMagOption;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * メールマガジンのヘッダを扱うベースアクションフォームです。
 * @author Takehiro Nakamori
 *
 */
public abstract class BaseMailMagOptionForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7945850945872327523L;

	/** 配信形式 */
	public Integer deliveryType;

	/** ヘッダメッセージ */
	public String optionValue;

	public String htmlBody;

	public String textBody;

	/** 配信予定日時 */
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String deliveryScheduleDatetime;

	/** エリアコード */
	public List<String> areaCd = new ArrayList<String>();


	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetBaseForm();
		deliveryType = null;
		optionValue = null;
		htmlBody = null;
		textBody = null;
		deliveryScheduleDatetime = null;
		areaCd = new ArrayList<String>();
	}

	/**
	 * バリデート
	 * @return
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		if (CollectionUtils.isEmpty(areaCd)) {
			errors.add("errors", new ActionMessage("errors.nonAreaCd"));
		}

		// ヘッダメッセージが入力されているかチェック
		if((deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML) && StringUtils.isBlank(htmlBody)) ||
				(deliveryType.equals(MTypeConstants.deliveryTypeKbn.TEXT) && StringUtils.isBlank(textBody))) {
			errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.headerMessage")));
		}

		// 配信予定日時が入力されているかチェック
		if (StringUtils.isBlank(deliveryScheduleDatetime)) {
			errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.deliveryScheduleDatetime")));
		} else {

			// 配信予定日時をチェック
			try {

				Date inputDate = DateUtils.formatDate(deliveryScheduleDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
				Calendar cal = Calendar.getInstance();
				cal.setTime(inputDate);

				// 木曜日になっているかチェック
				if (Calendar.THURSDAY != cal.get(Calendar.DAY_OF_WEEK)) {
					errors.add("errors",
						new ActionMessage("errors.app.notMutchData",
							MessageResourcesUtil.getMessage("labels.deliveryScheduleDatetime"),
							MessageResourcesUtil.getMessage("msg.weekday.thursday")));
				}

				// 未来の日付かチェック
				if (inputDate.before(new Date())) {
					errors.add("errors", new ActionMessage("errors.futureDate", MessageResourcesUtil.getMessage("labels.deliveryScheduleDatetime")));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return errors;
	}

}
