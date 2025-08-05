package com.gourmetcaree.shop.pc.scoutFoot.form.member;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;


/**
 * 求職者フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class MemberForm extends BaseForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3822604230359047692L;

//	/** スカウトメール数 */
//	public int scoutCount;

	/** 会員ID */
	public String[] checkId;


	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validateLumpSend() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// WEBデータが選択されているかチェック
		if (checkId == null || checkId.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckMember",
					MessageResourcesUtil.getMessage("labels.lumpSend"),
					MessageResourcesUtil.getMessage("labels.member")));
		} else {
			for (String id : checkId) {
				if (!StringUtils.isNumeric(id)) {
					errors.add("errors", new ActionMessage("errors.app.failGetMemberId"));
					break;
				}
			}
		}

		return errors;
	}

	public ActionMessages validateLumpAdd() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// WEBデータが選択されているかチェック
		if (checkId == null || checkId.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckMember",
					MessageResourcesUtil.getMessage("labels.lumpAdd"),
					MessageResourcesUtil.getMessage("labels.member")));
		} else {
			for (String id : checkId) {
				if (!StringUtils.isNumeric(id)) {
					errors.add("errors", new ActionMessage("errors.app.failGetMemberId"));
					break;
				}
			}
		}

		return errors;
	}

	/**
	 * 求職者IDをリセットする
	 */
	public void resetCheckId() {
		checkId = new String[0];
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		checkId = null;
	}
}
