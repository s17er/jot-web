package com.gourmetcaree.shop.pc.shopList.form.shopList;

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
	private static final long serialVersionUID = -3865060274393616284L;

	/** バージョン */
	public Long version;

	/**
	 * リセットフォーム
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		id = null;
		version = null;
		setProcessFlgNg();
	}

	public void resetForConf() {
		super.resetForm();
		setProcessFlgNg();
	}

	/**
	 * 独自チェックを行う
	 */
	@Override
	public ActionMessages validate() {
		return super.validate();
	}
}
