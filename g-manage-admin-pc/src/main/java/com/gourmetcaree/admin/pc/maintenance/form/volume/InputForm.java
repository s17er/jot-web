package com.gourmetcaree.admin.pc.maintenance.form.volume;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

/**
 * 号数データ登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends VolumeForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1809555620814316514L;

	/** エリア */
	@Required
	public String areaCd;

	/** エリア名 */
	public String areaName;

	/** 号数 */
	public String volume;

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
		areaCd = null;
		areaName = null;
		volume = null;
	}



}