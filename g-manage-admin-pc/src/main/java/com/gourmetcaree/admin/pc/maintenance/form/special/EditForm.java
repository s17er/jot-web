package com.gourmetcaree.admin.pc.maintenance.form.special;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 特集データ編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends SpecialForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3372212347375059031L;

	/** アドレス */
	public String url;

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
		url = null;
	}
}