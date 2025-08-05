package com.gourmetcaree.admin.pc.shopList.form.shopLabel;

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
 * 系列店舗にラベルを変更する
 * @author kyamane
 *
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7189278368763313342L;

	/** ID */
	public String id;

	/** 顧客ID */
	public String customerId;

	/** ラベル名 */
	@Required
	public String labelName;

	/** 系列店舗リスト（表示用） */
	public List<ShopListDto> shopListDtoList = new ArrayList<>();

	/** 選択した系列店舗ID */
	public List<String> shopListId = new ArrayList<>();

	public void resetForm() {
		resetBaseForm();
		id = null;
		customerId = null;
		labelName = null;
		shopListId = new ArrayList<>();
		shopListDtoList = new ArrayList<>();
	}

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