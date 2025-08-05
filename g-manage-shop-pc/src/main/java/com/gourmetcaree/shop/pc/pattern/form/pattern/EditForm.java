package com.gourmetcaree.shop.pc.pattern.form.pattern;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 定型文編集フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends PatternForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1336245847897796750L;

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();
	}

	/**
	 * ID以外のリセットを行う
	 */
	public void resetFormWithoutId() {
		super.resetBaseForm();
		customerId = null;
		sentenceTitle = null;
		body = null;
		version = null;
	}
}
