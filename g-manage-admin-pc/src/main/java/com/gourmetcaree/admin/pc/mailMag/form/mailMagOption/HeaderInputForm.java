package com.gourmetcaree.admin.pc.mailMag.form.mailMagOption;

import javax.persistence.Transient;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * メールマガジンのヘッダ入力用アクションフォームです。
 * @author TakehiroNakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class HeaderInputForm extends BaseMailMagOptionForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6271495528167476030L;

	/** アップロード受渡 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile imageFile;

	/**
	 * バリデート
	 */
	public ActionMessages validate() {
		return super.validate();
	}

	@Override
	public void resetForm() {
		// TODO 自動生成されたメソッド・スタブ
		super.resetForm();
		imageFile = null;
	}

}
