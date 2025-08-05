package com.gourmetcaree.admin.pc.maintenance.form.mischief;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * いたずら応募条件詳細のフォーム
 * @author Aquarius
 *
 */
@Component(instance = InstanceType.SESSION)
public class DetailForm extends MischiefApplicationForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** 編集のパス */
	public String editPath;

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		editPath = null;
	}


}