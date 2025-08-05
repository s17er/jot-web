package com.gourmetcaree.admin.pc.maintenance.form.mischief;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * いたずら応募条件の登録フォーム
 * @author Aquarius
 *
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends MischiefApplicationForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 条件未指定
		if(StringUtils.isEmpty(name) && StringUtils.isEmpty(nameKana)
				&& StringUtils.isEmpty(prefecturesCd) && StringUtils.isEmpty(municipality) && StringUtils.isEmpty(address)
				&& StringUtils.isEmpty(phoneNo1) && StringUtils.isEmpty(phoneNo2) && StringUtils.isEmpty(phoneNo3)
				&& StringUtils.isEmpty(mailAddress) && StringUtils.isEmpty(memberFlg)
				&& StringUtils.isEmpty(terminalKbn) && StringUtils.isEmpty(applicationSelfPr)) {
			errors.add("error", new ActionMessage("errors.noSelect"));
		}

		return errors;
	}

	public void resetFormWithoutId() {
		name = null;
		nameKana = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		mailAddress = null;
		memberFlg = null;
		terminalKbn = null;
		applicationSelfPr = null;
	}

}