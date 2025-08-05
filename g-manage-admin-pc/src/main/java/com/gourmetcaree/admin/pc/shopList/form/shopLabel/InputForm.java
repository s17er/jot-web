package com.gourmetcaree.admin.pc.shopList.form.shopLabel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.admin.service.dto.ShopListDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.ActionMessageUtil;

/**
 * 系列店舗にラベルを追加する
 * @author kyamane
 *
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1994782975622286136L;

	/**
	 * 顧客ID
	 */
	public String customerId;

	/**
	 * 画面に表示する系列店舗
	 */
	public List<ShopListDto> shopListDtoList = new ArrayList<>();

	/**
	 * ラベル名
	 */
	@Required
	public String labelName;

	/**
	 * 選択した系列店舗ID
	 */
	public List<String> shopListId = new ArrayList<>();


	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetBaseForm();
		customerId = null;
		shopListDtoList = new ArrayList<>();
		shopListId = new ArrayList<>();
		labelName = null;
	}

	/**
	 * チェックボックスの初期化
	 */
	public void resetMultibox() {
		shopListId = new ArrayList<>();
	}

	/**
	 * アクション側のバリデートを行います。
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		if (null == shopListId || CollectionUtils.isEmpty(shopListId)) {
			ActionMessageUtil.addActionMessage(errors, "errors.noCheckShopList");
		}
		return errors;
	}
}
