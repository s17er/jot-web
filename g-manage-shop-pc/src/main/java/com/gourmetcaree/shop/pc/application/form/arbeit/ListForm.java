package com.gourmetcaree.shop.pc.application.form.arbeit;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.shop.pc.application.form.application.ApplicationForm;

/**
 * アルバイト者一覧アクションフォーム
 * @author Yamane
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends ApplicationForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8208094755043471884L;

	/** ページ番号 */
	public String pageNum;

	/** 送受信区分 */
	public int sendKbn;

	/** 一括送信IDリスト */
	public String lumpSendIds;
	/** メモ */
	public String memo;

	/** キーワード */
	public String keyword;

	public void resetForm() {
		super.resetBaseForm();
		pageNum = null;
		sendKbn = 0;
		memo = null;
		lumpSendIds = null;
		keyword = null;
	}

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validateLumpSend() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// FIXME 会員ではないため修正
		if (StringUtils.isBlank(lumpSendIds)) {
			errors.add("errors", new ActionMessage("errors.noCheckMember",
					MessageResourcesUtil.getMessage("labels.lumpSend"),
					MessageResourcesUtil.getMessage("labels.member")));
		}

		return errors;
	}
}
