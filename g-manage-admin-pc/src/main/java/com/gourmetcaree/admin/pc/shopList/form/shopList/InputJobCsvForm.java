package com.gourmetcaree.admin.pc.shopList.form.shopList;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

@Component(instance=InstanceType.SESSION)
public class InputJobCsvForm extends ShopListBaseForm implements Serializable  {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3203733393460224658L;



	/** チェックしたID */
	public String[] saveIdList = new String[0];

	/** インポートした数 */
	public int importNum;

	/** ターゲットID */
	public int targetId;

	/** バージョン */
	public Long version;

	/** CSVの情報を記載したMapリスト */
	public List<Map<String, Object>> csvMapList;

	/** 仮登録されたIDのリスト */
	public List<Integer> idList;

	/** エラーがあるかどうか */
	public boolean errorFlg;

	/** ターゲットページ */
	public String targetPage;

	/** 最大表示件数 */
	public String maxRow;


	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		saveIdList = new String[0];
		importNum = 0;
		version = null;
		csvMapList = null;
		idList = null;
		errorFlg = false;
		targetPage = null;
		maxRow = null;
	}

	/**
	 * チェックボックスのリセット
	 */
	public void resetCheckBox() {
		super.resetCheckBox();
		saveIdList = new String[0];
	}

	/**
	 * 顧客ID以外をリセット
	 */
	@Override
	public void resetFormWithoutCustomerId() {
		super.resetFormWithoutCustomerId();
		saveIdList = new String[0];
		importNum = 0;
		version = null;
		csvMapList = null;
		idList = null;
		errorFlg = false;
		targetId = 0;
		targetPage = null;
		maxRow = null;
	}

	/**
	 * 詳細用リセット
	 */
	public void resetDetail() {
		displayConditionDtoList = null;
	}

	/**
	 * エラーフラグのリセット
	 */
	public void resetErrorFlg() {
		errorFlg = false;
	}


	/**
	 * 一時保存IDのバリデート
	 * @return
	 */
	public ActionMessages validateSaveIdList () {
		ActionMessages errors = new ActionMessages();
		if (ArrayUtils.isEmpty(saveIdList)) {
			errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.id")));
		}
		return errors;
	}

	/**
	 * 独自チェックを行う
	 * @return
	 */
	@Override
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		checkCondition(errors);

		return errors;
	}
}
