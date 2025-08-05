package com.gourmetcaree.admin.pc.maintenance.form.tagManage;

import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.entity.MWebTag;

/**
 * タグを管理するフォーム
 * @author yamane
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7189278368763313342L;

	/** 変更する配列 */
	public String listIndex;

	/** ID */
	public String id;

	/** WEB用か系列店舗用か */
	public String mode;

	/** 画面に表示するタグリスト */
	public List<MWebTag> webTagList;

	/** 画面に表示するタグリスト */
	public List<MShopListTag> shopTagList;

	public void resetForm() {
		resetBaseForm();
		listIndex = null;
		id = null;
		mode = null;
		webTagList = null;
		shopTagList = null;
	}

	/** WEB用 */
	public static final String MODE_WEB = "1";

	/** 店舗一覧用 */
	public static final String MODE_SHOP_LIST = "2";

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		if (MODE_WEB.equals(mode)) {
			if (StringUtil.isBlank(webTagList.get(Integer.parseInt(listIndex)).webTagName)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.tagName")));
			}
		} else if (MODE_SHOP_LIST.equals(mode)) {
			if (StringUtil.isBlank(shopTagList.get(Integer.parseInt(listIndex)).shopListTagName)) {
				errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.tagName")));
			}
		}

		return errors;
	}
}