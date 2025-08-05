package com.gourmetcaree.admin.pc.maintenance.action.tagManage;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.tagManage.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.TagListLogic;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.entity.MWebTag;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListTagService;
import com.gourmetcaree.db.common.service.WebTagService;

/**
 * タグを編集するクラス
 * @author yamane
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends PcAdminAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(EditAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	@Resource
	protected WebTagService webTagService;

	@Resource
	protected ShopListTagService shopListTagService;

	@Resource
	protected TagListLogic tagListLogic;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "{mode}", input = TransitionConstants.Maintenance.JSP_APJ09E01)
	@MethodAccess(accessCode="TAG_EDIT_INDEX")
	public String index() {

		if (EditForm.MODE_WEB.equals(editForm.mode) || EditForm.MODE_SHOP_LIST.equals(editForm.mode)) {
		} else {
			throw new ActionMessagesException("errors.fraudulentProcessError");
		}

		return show();
	}

	/**
	 * 更新用
	 * @return 一覧画面
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ09E01)
	@MethodAccess(accessCode="TAG_EDIT_EDIT")
	public String update() {

		if  (EditForm.MODE_WEB.equals(editForm.mode)) {

			editForm.webTagList.stream().forEach(entity -> {
				MWebTag result = tagListLogic.updateTagNameByUnique(entity);
				if (null != result) {
					throw new ActionMessagesException("errors.alreadyUsed");
				}
				log.debug("WEB用のタグを更新しました。" + entity);
			});

		} else {

			editForm.shopTagList.stream().forEach(entity -> {

				MShopListTag result = tagListLogic.updateTagNameByUnique(entity);
				if (null != result) {
					throw new ActionMessagesException("errors.alreadyUsed");
				}
				log.debug("ShopList用のタグを更新しました。" + entity);
			});
		}

		ActionMessageUtil.setActionMessageToRequest("msg.update.succcess", "msg.update");

		return show();
	}

	/**
	 * 削除用
	 * @return 一覧画面
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ09E01)
	@MethodAccess(accessCode="TAG_EDIT_DELETE")
	public String delete() {

		if (EditForm.MODE_WEB.equals(editForm.mode)) {

			MWebTag entity = new MWebTag();
			entity.id = Integer.parseInt(editForm.id);

			webTagService.logicalDeleteIncludesVersion(entity);

		} else {

			MShopListTag entity = new MShopListTag();
			entity.id = Integer.parseInt(editForm.id);

			shopListTagService.logicalDeleteIncludesVersion(entity);

		}

		ActionMessageUtil.setActionMessageToRequest("msg.update.succcess", "msg.delete");

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		editForm.setExistDataFlgNg();

		// modeがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, editForm.mode);

		try {
			if (EditForm.MODE_WEB.equals(editForm.mode)) {
				editForm.webTagList = webTagService.getWebTagListFindByAll();
			} else {
				editForm.shopTagList = shopListTagService.getShopListTagFindByAll();
			}

		} catch (WNoResultException e) {
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.Maintenance.JSP_APJ09E01;
		}

		editForm.setExistDataFlgOk();

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ09E01;
	}
}
