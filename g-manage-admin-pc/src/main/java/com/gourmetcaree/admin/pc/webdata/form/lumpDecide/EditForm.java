package com.gourmetcaree.admin.pc.webdata.form.lumpDecide;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * WEBデータ一括確定のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class EditForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5302562224305203019L;


	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();

	}

}
