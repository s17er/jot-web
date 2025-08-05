package com.gourmetcaree.admin.pc.report.form.report;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.constants.MAreaConstants;

/**
 * レポート一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2628585972313912636L;

	/** エリアコード */
	public String areaCd;

	/** 最大表示件数 */
	public String maxRow;



	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		maxRow = null;

		//初期表示のエリアをセット
		areaCd = Integer.toString(MAreaConstants.AreaCd.SHUTOKEN_AREA);
	}
}