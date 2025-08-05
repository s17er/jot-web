package com.gourmetcaree.admin.pc.maintenance.form.special;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 特集データ削除のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DeleteForm extends SpecialForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -663621282932729422L;


	/** アドレス */
	public String url;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		url = null;
	}
}