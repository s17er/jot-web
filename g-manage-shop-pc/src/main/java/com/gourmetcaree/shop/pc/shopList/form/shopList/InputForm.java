package com.gourmetcaree.shop.pc.shopList.form.shopList;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 店舗一覧登録アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends ShopListBaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 606133644321909870L;

	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
	}

}
