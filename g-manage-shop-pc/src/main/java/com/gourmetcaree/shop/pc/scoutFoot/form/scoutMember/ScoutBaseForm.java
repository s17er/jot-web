package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember;

import com.gourmetcaree.common.form.BaseForm;


/**
 * スカウトメールフォームです。
 * @author Motoaki Hara
 * @version 1.0
 */
public abstract class ScoutBaseForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2792844014054786766L;

	/** 送受信区分 */
	public String sendKbn;

    public void resetForm() {
    	super.resetBaseForm();
    	sendKbn = null;
    }
}
