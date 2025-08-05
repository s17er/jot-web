package com.gourmetcaree.admin.pc.shopList.action.shopLabel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.shopList.form.shopLabel.InputForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.ShopListLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TShopListLabel;
import com.gourmetcaree.db.common.entity.TShopListLabelGroup;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.service.ShopListLabelGroupService;
import com.gourmetcaree.db.common.service.ShopListLabelService;

/**
 * 系列店舗用ラベルの登録Action
 * @author kyamane
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(InputAction.class);

	/** アクションフォーム */
	@Resource
	@ActionForm
	private InputForm inputForm;

	@Resource
	private ShopListLabelService shopListLabelService;

	@Resource
	private ShopListLabelGroupService shopListLabelGroupService;

	@Resource
	private ShopListLogic shopListLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}", input = TransitionConstants.ShopList.JSP_APQ06C01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_LIST")
	public String index() {
		inputForm.setExistDataFlgNg();
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		inputForm.shopListDtoList =
				shopListLogic.getWebShopListByCustomerId(
						Integer.parseInt(inputForm.customerId)
						);

		inputForm.setExistDataFlgOk();

		return TransitionConstants.ShopList.JSP_APQ06C01;
	}

	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate="validate",  reset = "resetMultibox", input = TransitionConstants.ShopList.JSP_APQ06C01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_CONF")
	public String conf() {
		inputForm.setProcessFlgOk();
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);

		List<Integer> shopListIdIntList = new ArrayList<>();
		inputForm.shopListId.stream().forEach( id -> {
			shopListIdIntList.add(Integer.parseInt(id));
		});

		inputForm.shopListDtoList = shopListLogic.getWebShopListByIds(shopListIdIntList);

		return TransitionConstants.ShopList.JSP_APQ06C02;
	}

	/**
	 * 登録処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_SUBMIT")
	public String submit() {
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException();
		}

		insert();
		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_LABEL_INPUT_COMP;
	}

	/**
	 * 登録完了画面
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ06C01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_COMP")
	public String comp() {
		return TransitionConstants.ShopList.JSP_APQ06C03;
	}

	/**
	 * 訂正して入力画面へ戻る。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ06C01)
	@MethodAccess(accessCode="SHOPLIST_LABEL_SHOPGROUP_CORRECT")
	public String correct() {

		inputForm.setProcessFlgNg();
		show();
		return TransitionConstants.ShopList.JSP_APQ06C01;
	}

	/**
	 * 登録処理
	 */
	private void insert() {

		// ラベルの登録
		TShopListLabel tShopListLabel = new TShopListLabel();
		tShopListLabel.labelName = inputForm.labelName;
		tShopListLabel.customerId = Integer.parseInt(inputForm.customerId);

		shopListLabelService.insert(tShopListLabel);
		// ラベルに表示順の更新
		tShopListLabel.displayOrder = tShopListLabel.id;
		shopListLabelService.updateIncludesVersion(tShopListLabel);

		// ラベルに紐づく系列店舗の登録
		Integer shopListLabelId = tShopListLabel.id;
		inputForm.shopListId.stream().forEach(id -> {
			TShopListLabelGroup entity = new TShopListLabelGroup();
			entity.shopListLabelId = shopListLabelId;
			entity.shopListId = Integer.parseInt(id);
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			shopListLabelGroupService.insert(entity);
			// 表示順の更新
			entity.displayOrder = entity.id;
			shopListLabelGroupService.updateIncludesVersion(entity);
		});
	}

	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_BACKINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		String backPath = createReindexPath(inputForm.customerId, "/shopList", "/reindex");
		inputForm.resetForm();
		return backPath;
	}

	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_BACKINDEX")
	public String backToList() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		String backPath = createReindexPath(inputForm.customerId, "/shopLabel", "/list");
		inputForm.resetForm();
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
