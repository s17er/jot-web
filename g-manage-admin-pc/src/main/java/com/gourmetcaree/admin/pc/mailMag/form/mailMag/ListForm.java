package com.gourmetcaree.admin.pc.mailMag.form.mailMag;

import java.text.ParseException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

/**
 * メルマガ一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5564128157903836370L;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** 配信開始日 */
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String deliveryStartDate;

	/** 配信終了日 */
	@DateType(datePattern="yyyy/MM/dd", msg = @Msg(key = "errors.datetype"))
	public String deliveryEndDate;

	/** メルマガタイトル */
	public String mailMagazineTitle;

	/** 配信先区分 */
	public String deliveryKbn;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		resetBaseForm();
		pageNum = null;
		maxRow = null;
		deliveryStartDate = null;
		deliveryEndDate = null;
		mailMagazineTitle = null;
		deliveryKbn = null;
	}

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 日付が入力されていればチェックする
		if (!StringUtil.isEmpty(this.deliveryStartDate) && !StringUtil.isEmpty(this.deliveryEndDate)) {
			try {
				// 開始日より終了日が先の場合エラー
				if (DateUtils.compareDateTime(this.deliveryStartDate, this.deliveryEndDate) < 0) {
					// 「{配信期間(開始日)}には{配信期間(終了日)}より前の日時を入力してください。」
					errors.add("errors", new ActionMessage("errors.app.TermSet",
							MessageResourcesUtil.getMessage("labels.deliveryStartDate"), MessageResourcesUtil.getMessage("labels.deliveryEndDate")));
				}
			} catch (ParseException e) {
				// 「日付を正しく入力してください。」
				errors.add("errors", new ActionMessage("errors.app.dateFailed"));
			}
		}

		return errors;
	}
}