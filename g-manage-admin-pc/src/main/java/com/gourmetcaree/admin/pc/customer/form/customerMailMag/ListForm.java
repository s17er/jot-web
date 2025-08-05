package com.gourmetcaree.admin.pc.customer.form.customerMailMag;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 顧客メルマガ一覧確認のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4977515062291250125L;

	/** 顧客IDリスト */
	public List<String> customerIdList = new ArrayList<String>();

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		resetBaseForm();
		customerIdList = new ArrayList<String>();
	}
}