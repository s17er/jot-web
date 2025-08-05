package com.gourmetcaree.admin.pc.shopList.form.shopList;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 店舗一覧編集画面アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends ShopListBaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4401036136266675984L;

	/** バージョン */
	public Long version;

	/**
	 * リセットフォーム
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		version = null;
		setProcessFlgNg();
	}

	@Override
	public void resetFormWithoutCustomerId() {
		super.resetFormWithoutCustomerId();
		version = null;
	}

	/**
	 * 確認画面用リセットフォーム
	 */
	public void resetFormForConf() {
		super.resetFormWithoutCustomerId();
	}

	/**
	 * 独自チェックを行う
	 */
	@Override
	public ActionMessages validate() {
		return super.validate();
	}
}
