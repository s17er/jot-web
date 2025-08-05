package com.gourmetcaree.admin.pc.shopList.action.shopList;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.shopList.form.shopList.IndexForm;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm.RefelerKbn;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * 店舗一覧インデックスアクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class IndexAction extends ShopListBaseAction {

	/** アクションフォーム */
	@Resource
	@ActionForm
	private IndexForm indexForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}", input = TransitionConstants.ShopList.JSP_APQ01A01)
	@MethodAccess(accessCode="SHOPLIST_INDEX_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, indexForm.customerId);
		session.setAttribute(ShopListBaseForm.SESSION_KEY.REFERER, RefelerKbn.CUSTOMER);
		return show();
	}

	/**
	 * webデータ詳細からの初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexWebDatail/{customerId}/{webId}", input = TransitionConstants.ShopList.JSP_APQ01A01)
	public String indexWebDatail() {
		checkArgsNull(NO_BLANK_FLG_NG, indexForm.webId, indexForm.customerId);
		session.setAttribute(ShopListBaseForm.SESSION_KEY.WEB_ID, indexForm.webId);
		session.setAttribute(ShopListBaseForm.SESSION_KEY.REFERER, RefelerKbn.WEB_DETAIL);
		return show();
	}

	/**
	 * webデータリストからの初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexWebList/{customerId}", input = TransitionConstants.ShopList.JSP_APQ01A01)
	public String indexWebList() {
		checkArgsNull(NO_BLANK_FLG_NG, indexForm.customerId);
		session.setAttribute(ShopListBaseForm.SESSION_KEY.REFERER, RefelerKbn.WEB_LIST);
		return show();
	}

	/**
	 * webデータ詳細を経由した顧客詳細からの初期表示
	 * @return 画面表示
	 * @author Makoto Otani
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "indexCustomerAndWebDatail/{customerId}/{webId}", input = TransitionConstants.ShopList.JSP_APQ01A01)
	public String indexCustomerAndWebDatail() {
		checkArgsNull(NO_BLANK_FLG_NG, indexForm.webId, indexForm.customerId);
		session.setAttribute(ShopListBaseForm.SESSION_KEY.WEB_ID, indexForm.webId);
		session.setAttribute(ShopListBaseForm.SESSION_KEY.REFERER, RefelerKbn.CUSTOMER_WEB_DETAIL);
		return show();
	}

	/**
	 * 初期表示の再表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "reindex/{customerId}", input = TransitionConstants.ShopList.JSP_APQ01A01)
	public String reindex() {

		checkId(indexForm.customerId);
		checkArgsNull(NO_BLANK_FLG_NG, indexForm.customerId);
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		return TransitionConstants.ShopList.JSP_APQ01A01;
	}


}
