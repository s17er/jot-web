package com.gourmetcaree.admin.pc.shopList.form.shopList;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 店舗一覧登録アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends ShopListBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3272527957348341348L;

	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
	}

	/**
	 * 顧客ID以外のフォームをリセット
	 */
	@Override
	public void resetFormWithoutCustomerId() {
		super.resetFormWithoutCustomerId();
	}

}
