package com.gourmetcaree.admin.pc.shopList.action.shopLabel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.shopList.form.shopLabel.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.annotation.WebdataAccessUser;
import com.gourmetcaree.admin.pc.sys.annotation.WebdataAccessUser.AccessType;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.ShopListDto;
import com.gourmetcaree.admin.service.logic.ShopListLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TShopListLabel;
import com.gourmetcaree.db.common.entity.TShopListLabelGroup;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListLabelGroupService;
import com.gourmetcaree.db.common.service.ShopListLabelService;

/**
 * 系列店舗のラベルを更新するAction
 * @author kyamane
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class EditAction extends PcAdminAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(EditAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	@Resource
	protected ShopListLabelService shopListLabelService;

	@Resource
	protected ShopListLogic shopListLogic;

	@Resource
	protected ShopListLabelGroupService shopListLabelGroupService;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_APQ06E01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_EDIT")
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		editForm.setExistDataFlgNg();

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);

		try {
			TShopListLabel entity = shopListLabelService.findById(Integer.parseInt(editForm.id));
			if (null != entity) {

				// 現在設定されている系列店舗
				editForm.labelName = entity.labelName;
				List<TShopListLabelGroup> shopListLabelGroupList = new ArrayList<>();
				shopListLabelGroupList = shopListLabelGroupService.findByShopListLabelId(Integer.parseInt(editForm.id));
				List<Integer> shopListIdList = new ArrayList<>();
				shopListLabelGroupList.stream().forEach( e -> {
					shopListIdList.add(e.shopListId);
				} );


				List<ShopListDto> dtoList = shopListLogic.getWebShopListByIds(shopListIdList);
				dtoList.stream().forEach( dto -> {
					editForm.shopListId.add(String.valueOf(dto.id));
				});

				// 該当の顧客の系列店舗
				editForm.shopListDtoList = shopListLogic.getWebShopListByCustomerId(Integer.parseInt(editForm.customerId));


			} else {
				editForm.labelName = "";
				editForm.shopListDtoList = new ArrayList<>();
			}

		} catch (WNoResultException e) {
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.ShopList.JSP_APQ06E01;
		}

		editForm.setExistDataFlgOk();

		// 一覧画面へ遷移
		return TransitionConstants.ShopList.JSP_APQ06E01;
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.ShopList.JSP_APQ06E01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_CONF")
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	public String conf() {
		editForm.setProcessFlgOk();
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);

		List<Integer> shopListIdIntList = new ArrayList<>();
		editForm.shopListId.stream().forEach( id -> {
			shopListIdIntList.add(Integer.parseInt(id));
		});

		editForm.shopListDtoList = shopListLogic.getWebShopListByIds(shopListIdIntList);

		return TransitionConstants.ShopList.JSP_APQ06E02;
	}

	/**
	 * 訂正して入力画面へ戻る。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ06E01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_CORRECT")
	public String correct() {

		editForm.setProcessFlgNg();
		show();
		return TransitionConstants.ShopList.JSP_APQ06E01;
	}

	/**
	 * 更新処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ06E01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_SUBMIT")
	public String submit() {

		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		update();
		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_LABEL_EDIT_COMP;
	}

	/**
	 * 系列店舗一覧データ更新完了画面
	 */
	@Execute(validator = false, reset = "resetMultibox", input = TransitionConstants.ShopList.JSP_APQ06E01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_COMP")
	public String updateComp() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);
		return TransitionConstants.ShopList.JSP_APQ06E03;
	}


	/**
	 * 更新用
	 * @return 一覧画面
	 */
	@Execute(validator = true, validate="validate", input = TransitionConstants.ShopList.JSP_APQ06E01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_UPDATE")
	public String update() {

		List<Integer> shopListIntegerIdList = new ArrayList<>();
		editForm.shopListId.stream().forEach( id -> {
			shopListIntegerIdList.add(Integer.parseInt(id));
		});

		try {
			shopListLogic.updateShopListLabel(Integer.parseInt(editForm.id), Integer.parseInt(editForm.customerId), editForm.labelName, shopListIntegerIdList);
		} catch (NumberFormatException e) {
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.ShopList.JSP_APQ06E01;
		} catch (WNoResultException e) {
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
			return TransitionConstants.ShopList.JSP_APQ06E01;
		}

		return show();
	}

	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_BACKINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);
		String backPath = createReindexPath(editForm.customerId, "/shopList", "/reindex");
		editForm.resetForm();
		return backPath;
	}

	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_BACKINDEX")
	public String backToList() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);
		String backPath = createReindexPath(editForm.customerId, "/shopLabel", "/list");
		editForm.resetForm();
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
}
