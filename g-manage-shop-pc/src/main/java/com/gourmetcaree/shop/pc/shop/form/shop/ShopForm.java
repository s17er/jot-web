package com.gourmetcaree.shop.pc.shop.form.shop;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.shop.pc.shop.form.shop.EditForm.SubMailDto;

/**
 * 登録情報フォーム
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class ShopForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5046869872820824083L;

	/** バージョン番号 */
	public Long version;

	/** サブメールのリスト */
	public List<SubMailDto> subMailDtoList;

	public void resetForm() {
		super.resetBaseForm();
		version = null;
		subMailDtoList = new ArrayList<>();
	}

	/**
	 * サブメールを登録用にセットする
	 * @param form
	 */
	public void setSubMailEntryForm() {
		// 3つセットする
		if (subMailDtoList.size() >= 3) {
			return;
		}
		int count = 3 - subMailDtoList.size();
		for (int i = 0; i < count; i++) {
			subMailDtoList.add(new SubMailDto());
		}
	}
}
