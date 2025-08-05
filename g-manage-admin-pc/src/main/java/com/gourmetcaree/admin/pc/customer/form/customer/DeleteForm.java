package com.gourmetcaree.admin.pc.customer.form.customer;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 顧客削除のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DeleteForm extends CustomerForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7238692223282017935L;

	@Override
	public void resetForm() {
		super.resetForm();
	}

}