package com.gourmetcaree.admin.pc.maintenance.form.volume;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 号数データ編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends VolumeForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1809555620814316514L;

	/** 号数ID */
	public String id;

	/** エリア */
	public String areaCd;

	/** エリア名 */
	public String areaName;

	/** 号数 */
	public String volume;

	/** バージョン番号 */
	public Long version;

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
		id = null;
		areaCd = null;
		areaName = null;
		volume = null;
		version = null;

	}
}