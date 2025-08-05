package com.gourmetcaree.shop.pc.scoutFoot.form.member;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;


/**
 * キープボックスフォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class KeepBoxForm extends MemberForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6716660522498610044L;

	/** 会員ID */
	public String id;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 削除会員ID */
	public String[] deleteId;

	/** 最大表示件数 */
	public String maxRow;


	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validateLumpDelete() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 求職者が選択されているかチェック
		if (deleteId == null || deleteId.length == 0) {
			errors.add("errors", new ActionMessage("errors.noCheckMember",
					MessageResourcesUtil.getMessage("labels.deleteFromKeepBox"),
					MessageResourcesUtil.getMessage("labels.member")));
		} else {
			for (String id : deleteId) {
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
	public void resetId() {
		checkId = new String[0];
		deleteId = new String[0];
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		id = null;
		pageNum = null;
		deleteId = null;
		maxRow = "20";
	}
}
