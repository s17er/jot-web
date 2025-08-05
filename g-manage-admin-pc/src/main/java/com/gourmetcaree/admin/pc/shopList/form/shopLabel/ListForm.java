package com.gourmetcaree.admin.pc.shopList.form.shopLabel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.db.common.entity.TShopListLabel;

/**
 * 系列店舗ラベルの一覧Form
 * @author kyamane
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -32574252366499756L;

	/** 顧客ID */
	public String customerId;

	/**
	 * 顧客に紐づくラベル一覧
	 */
	public List<TShopListLabel> tShopListLabelList = new ArrayList<>();

	/** 選択したラベルID（削除する） */
	public List<String> shopListLabelId = new ArrayList<>();



	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetFormWithoutCustomerId();
		customerId = null;
		tShopListLabelList = new ArrayList<>();
		shopListLabelId = new ArrayList<>();
	}

	/**
	 * 顧客IDを除いたフォームのリセット
	 */
	public void resetFormWithoutCustomerId() {
		resetBaseForm();
	}


	public void resetMultibox() {
		shopListLabelId = new ArrayList<>();
	}


	/**
	 * アクション側のバリデート（削除用）を行います。
	 */
	public ActionMessages validateDelete() {
		ActionMessages errors = new ActionMessages();
		if (null == shopListLabelId || CollectionUtils.isEmpty(shopListLabelId)) {
			ActionMessageUtil.addActionMessage(errors, "errors.noCheckShopListLabel");
		}
		return errors;
	}

}
