package com.gourmetcaree.shop.pc.contact.form.contact;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;

import jp.co.whizz_tech.common.sastruts.annotation.EmailType;

@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm implements Serializable {

	/** 企業名 */
	@Required
	public String customerName;

	/** お名前  */
	@Required
	public String contactName;

	/** 電話番号1 */
	@Required
	public String phoneNo1;

	/** 電話番号2 */
	@Required
	public String phoneNo2;

	/** 電話番号3 */
	@Required
	public String phoneNo3;

	/** メールアドレス */
	@Required
	@EmailType
	public String sender;

	/** 内容 */
	@Required
	public String contents;

	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		checkPhoneNo(errors);
		return errors;
	}

	protected void checkPhoneNo(ActionMessages errors) {
		// 全て空の場合はなにもしない。
		if (StringUtils.isBlank(phoneNo1)
				&& StringUtils.isBlank(phoneNo2)
				&& StringUtils.isBlank(phoneNo3)) {
			return;
		}

		// 3つすべてに値が入力されているかチェック
		if (StringUtils.isBlank(phoneNo1) || StringUtils.isBlank(phoneNo2) || StringUtils.isBlank(phoneNo3)) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
			return;
		}

		phoneNo1 = GourmetCareeUtil.removeAllSpace(phoneNo1);
		phoneNo2 = GourmetCareeUtil.removeAllSpace(phoneNo2);
		phoneNo3 = GourmetCareeUtil.removeAllSpace(phoneNo3);

		// 3つすべて数値かどうかチェック
		if (!StringUtils.isNumeric(phoneNo1) || !StringUtils.isNumeric(phoneNo2) || !StringUtils.isNumeric(phoneNo3)) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
			return;
		}
	}

	public void resetForm() {
		super.resetBaseForm();
		customerName = null;
		contactName = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		sender = null;
		contents = null;
	}

}
