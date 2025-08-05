package com.gourmetcaree.admin.pc.member.form.member;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 会員削除のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DeleteForm extends MemberForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3043572510804542599L;

	/** 削除理由 */
	public String deleteReasonKbn;

	/** 削除するバージョン */
	public String deleteVersion;


	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 削除理由入力チェック
		if (StringUtils.isEmpty(deleteReasonKbn)) {
			errors.add("errors", new ActionMessage("errors.required", "削除理由"));
		} else {
			if (!StringUtils.isNumeric(deleteReasonKbn)) {
				errors.add("errors", new ActionMessage("errors.resounKbnFailed"));
			}
		}

		return errors;

	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();
		deleteReasonKbn = null;
		deleteVersion = null;
	}

}