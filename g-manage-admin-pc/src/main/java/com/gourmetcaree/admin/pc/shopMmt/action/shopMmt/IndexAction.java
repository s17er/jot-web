package com.gourmetcaree.admin.pc.shopMmt.action.shopMmt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember.ListAction;
import com.gourmetcaree.admin.pc.shopMmt.form.shopMmt.IndexForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.PublishedShopListLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * 店舗管理Action
 * @author yamane
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class IndexAction extends PcAdminAction {

	/** ログ */
	private static final Logger log = Logger.getLogger(ListAction.class);

	@Resource
	protected PublishedShopListLogic publishedShopListLogic;

	/** アクションフォーム */
	@Resource
	@ActionForm
	private IndexForm indexForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopMmt.JSP_APQ01L01)
	@MethodAccess(accessCode="SHOP_MANAGEMENT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		return TransitionConstants.ShopMmt.JSP_APQ01L01;
	}


	/**
	 * CSVアウトプット
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopMmt.JSP_APQ01L01)
	@MethodAccess(accessCode="SHOP_MANAGEMENT_SHOPLIST_CSV")
	public String export() {
		checkArgsNull(NO_BLANK_FLG_NG, indexForm.areaCd);
		outPut();
		return null;
	}

	/**
	 * CSVのアウトプットを行う
	 */
	private void outPut() {
		try {
			publishedShopListLogic.outputCsv(Integer.parseInt(indexForm.areaCd));
		} catch (UnsupportedEncodingException e) {
			throw new ActionMessagesException("errors.app.csvDataNotFound");
		} catch (IOException e) {
			log.fatal("入出力エラーが発生しました。", e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}

	}

}
