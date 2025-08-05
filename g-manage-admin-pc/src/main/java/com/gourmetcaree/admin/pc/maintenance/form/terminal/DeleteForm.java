package com.gourmetcaree.admin.pc.maintenance.form.terminal;

import java.io.Serializable;

/**
 * 駅グループ削除のフォーム
 * @author yamane
 * @version 1.0
 */
public class DeleteForm extends TerminalForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3239926078003609127L;

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
	}

	/**
	 * 削除項目以外のリセットを行う
	 */
	public void resetFormWithoutDelValue() {
		displayId = null;
	    existDataFlg = true;
	    hiddenId = null;
		prefecturesCd = null;
	}
}