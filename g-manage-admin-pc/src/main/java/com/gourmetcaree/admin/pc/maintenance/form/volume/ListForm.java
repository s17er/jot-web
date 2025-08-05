package com.gourmetcaree.admin.pc.maintenance.form.volume;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.constants.MAreaConstants;

/**
 * 号数データ一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3508413875017924450L;

	/** エリアコード */
	public String areaCd;

	/** 最大表示件数 */
	public String maxRowValue;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		maxRowValue = null;
		// 首都圏エリアを初期値にセット
		this.areaCd = String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA);
	}

	/**
	 * エリア、表示件数以外のリセットを行う
	 */
	public void resetFormShowList() {
		super.resetBaseForm();
	}
}