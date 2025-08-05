package com.gourmetcaree.admin.pc.maintenance.form.special;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 特集データ登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends SpecialForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7210545142712763419L;

	/**
	 * 独自の入力チェックを行う。
	 * スーパークラスのメソッド実行後にこのメソッドを実行する。
	 */
	@Override
	public ActionMessages validate() {

		ActionMessages errors = super.validate();
		return errors;
	}

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		super.resetForm();
	}
}