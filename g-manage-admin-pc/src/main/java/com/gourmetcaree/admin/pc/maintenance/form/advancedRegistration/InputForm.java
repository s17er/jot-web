package com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 事前登録登録フォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends AbstractAdvancedRegistrationInputBaseForm {

	/**
	 *
	 */
	private static final long serialVersionUID = -1086585777309976870L;

	@Override
	public ActionMessages validate() {
		return super.validateDate();
	}
}
