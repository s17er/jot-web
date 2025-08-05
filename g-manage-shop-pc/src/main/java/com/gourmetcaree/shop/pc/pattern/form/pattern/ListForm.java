package com.gourmetcaree.shop.pc.pattern.form.pattern;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.shop.pc.pattern.dto.pattern.ListDto;

/**
 * 定型文一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6140092845785831577L;

	/** 定型文一覧リスト */
	public List<ListDto> list;

	/** 変更するID配列 */
	public String[] changeIdArray;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		list = null;
		changeIdArray = null;
	}

	/**
	 * マルチボックスのリセット
	 */
	public void resetMultiBox() {
		changeIdArray = null;
	}

	/**
	 * 一括処理用バリデート
	 */
	public ActionMessages validateForLump() {
		ActionMessages errors = new ActionMessages();
		if (ArrayUtils.isEmpty(changeIdArray)) {
			errors.add("errors", new ActionMessage("errors.required",
									MessageResourcesUtil.getMessage("labes.changeIdArray")));
		}
		return errors;
	}

}
