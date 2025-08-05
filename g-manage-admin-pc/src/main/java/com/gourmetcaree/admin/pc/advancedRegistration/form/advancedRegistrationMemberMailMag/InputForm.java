package com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMemberMailMag;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 事前登録会員用メルマガ入力アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7680119265983376474L;

	/** PCタイトル */
	@Required
	public String pcMailMagazineTitle;

	/** PC本文 */
	@Required
	public String pcBody;



	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		pcMailMagazineTitle = null;
		pcBody = null;
	}
}
