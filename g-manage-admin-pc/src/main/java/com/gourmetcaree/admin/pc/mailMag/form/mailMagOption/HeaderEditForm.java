package com.gourmetcaree.admin.pc.mailMag.form.mailMagOption;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * メルマガヘッダメッセージ編集用アクションフォーム
 * @author TakehiroNakamori
 *
 */
@Component(instance=InstanceType.SESSION)
public class HeaderEditForm extends BaseMailMagOptionForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 462894652615502515L;

	/** メルマガオプションのID */
	public String id;

	/** バージョン */
	public int version;

	/**
	 * バリデート
	 */
	@Override
	public ActionMessages validate() {
		return super.validate();
	}

	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		id = null;
		version = 0;
	}

}
