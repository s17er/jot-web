package com.gourmetcaree.admin.pc.shopMmt.form.shopMmt;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 店舗管理
 * @author kyamane
 *
 */
@Component(instance=InstanceType.SESSION)
public class IndexForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8688133410085897997L;

	public String areaCd;

	/**
	 * リセットフォーム
	 */
	public void resetForm() {
		areaCd = null;
	}
}
