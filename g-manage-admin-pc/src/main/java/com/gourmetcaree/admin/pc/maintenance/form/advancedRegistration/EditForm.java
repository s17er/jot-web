package com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 事前登録マスタ変更フォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends AbstractAdvancedRegistrationInputBaseForm {

	/**
	 *
	 */
	private static final long serialVersionUID = -3295592516414173493L;




	@Override
	public ActionMessages validate() {
		return super.validateDate();
	}

}
