package com.gourmetcaree.admin.pc.shopList.action.shopLabel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.shopList.form.shopLabel.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.ShopListLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListLabelService;


/**
 * 系列店舗ラベルの一覧Action
 * @author kyamane
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(ListAction.class);

	/** 系列店舗ラベルの一覧Form */
	@ActionForm
	@Resource
	private ListForm listForm;

	/** 系列店舗のラベルのサービス */
	@Resource
	protected ShopListLabelService shopListLabelService;

	@Resource
	protected ShopListLogic shopListLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm" , urlPattern = "{customerId}", input = TransitionConstants.ShopList.JSP_APQ06L01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_LIST")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		listForm.setExistDataFlgNg();
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		try {
			listForm.tShopListLabelList = shopListLabelService.findByCustomerId(
					Integer.parseInt(listForm.customerId)
					);
		} catch (NumberFormatException | WNoResultException e) {
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.ShopList.JSP_APQ06L01;
		}
		listForm.setExistDataFlgOk();
		return TransitionConstants.ShopList.JSP_APQ06L01;
	}

	/**
	 * 更新用
	 * @return 一覧画面
	 */
	@Execute(validator = true, validate="validateDelete", input = TransitionConstants.ShopList.JSP_APQ06L01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_DELETE")
	public String delete() {

		List<Integer> shopListIntegerIdList = new ArrayList<>();
		listForm.shopListLabelId.stream().forEach( id -> {
			shopListIntegerIdList.add(Integer.parseInt(id));
		});

		try {
			shopListLogic.deleteShopListLabel(shopListIntegerIdList);
		} catch (NumberFormatException e) {
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.ShopList.JSP_APQ06L01;
		}

		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_LABEL_DELETE_COMP;
	}

	/**
	 * 系列店舗一覧データ削除完了画面
	 */
	@Execute(validator = false, reset = "resetMultibox", input = TransitionConstants.ShopList.JSP_APQ06D03)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_COMP")
	public String deleteComp() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		return TransitionConstants.ShopList.JSP_APQ06D03;
	}


	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_BACKINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		String backPath = createReindexPath(listForm.customerId, "/shopList", "/reindex");
		listForm.resetForm();
		return backPath;
	}

	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_BACKINDEX")
	public String backToList() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		String backPath = createReindexPath(listForm.customerId, "/shopLabel", "/list");
		listForm.resetForm();
		return backPath;
	}

	/**
	 * インデックスへのパスを作成
	 * @param customerId
	 * @return
	 */
	protected String createReindexPath(String customerId, String path1, String path2) {
		return GourmetCareeUtil.makePath(path1, path2, customerId, TransitionConstants.REDIRECT_STR);
	}


	@Execute(validator = false,  input = TransitionConstants.ShopList.JSP_APQ06L01)
	public String reShowList() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		listForm.setExistDataFlgNg();
		return show();
	}
}
