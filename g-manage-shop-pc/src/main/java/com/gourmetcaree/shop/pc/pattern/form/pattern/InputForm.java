package com.gourmetcaree.shop.pc.pattern.form.pattern;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 定型文登録フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends PatternForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7153754025339818164L;

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();

	}
}
