package com.gourmetcaree.admin.pc.maintenance.form.terminal;

import java.io.Serializable;

/**
 * 駅グループ詳細のフォーム
 * @author yamane
 * @version 1.0
 */
public class DetailForm extends TerminalForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1347870218716647645L;

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