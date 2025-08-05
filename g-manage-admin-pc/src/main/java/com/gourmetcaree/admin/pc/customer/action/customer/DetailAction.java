package com.gourmetcaree.admin.pc.customer.action.customer;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm;
import com.gourmetcaree.admin.pc.customer.form.customer.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.service.CustomerCompanyService;

/**
 * 顧客管理詳細アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction extends CustomerBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 顧客担当会社マスタサービス */
	@Resource
	protected CustomerCompanyService customerCompanyService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{id}", input = TransitionConstants.Customer.JSP_APD01R01)
	@MethodAccess(accessCode="CUSTOMER_DETAIL_INDEX")
	public String index() {

		// WEBIDをセッションから削除
		session.removeAttribute(CustomerForm.SESSION_KEY.WEB_ID);

		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_DETAIL_BACK")
	public String back() {
		// 一覧画面へ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMER_SEARCH_AGAIN;
	}

	/**
	 * webデータからの遷移
	 * @return 詳細画面
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "indexFromWebdata/{id}/{webId}", input = TransitionConstants.Customer.JSP_APD01R01)
	@MethodAccess(accessCode="CUSTOMER_DETAIL_INDEX")
	public String indexFromWebdata() {

		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id, detailForm.webId);

		checkId(detailForm, detailForm.id);
		checkId(detailForm, detailForm.webId);

		// WEBIDをセッションに保持
		session.setAttribute(CustomerForm.SESSION_KEY.WEB_ID, detailForm.webId);

		return show();
	}


	/**
	 * 初期表示遷移用
	 * @return 詳細画面のパス
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		// 自社スタッフが編集可能かチェック
		if (ManageAuthLevel.STAFF.value().equals(userDto.authLevel) || ManageAuthLevel.SALES.value().equals(userDto.authLevel)) {
			if (!customerCompanyService.isCustomerDataExistByCustomerId(NumberUtils.toInt(detailForm.id))) {
				detailForm.editFlg = false;
			}
			// 代理店が表示可能顧客かチェック
 		} else if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
 			if (!customerCompanyService.isCustomerDataExistByCustomerIdSalesId(NumberUtils.toInt(detailForm.id), NumberUtils.toInt(userDto.userId))) {
 				detailForm.setExistDataFlgNg();
				throw new ActionMessagesException("errors.noHandlingCustomerError");
 			}
 		}

		// 表示データ取得
		convertDispData(detailForm);

		log.debug("顧客情報を取得しました。");

		// パスの生成
		detailForm.editPath = GourmetCareeUtil.makePath("/customer/edit/", "index", String.valueOf(detailForm.id));
		detailForm.deletePath = GourmetCareeUtil.makePath("/customer/delete/", "index", String.valueOf(detailForm.id), String.valueOf(detailForm.customerVersion));

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD01R01;
	}

}