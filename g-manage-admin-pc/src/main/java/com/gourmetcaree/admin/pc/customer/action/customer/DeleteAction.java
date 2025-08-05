package com.gourmetcaree.admin.pc.customer.action.customer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.customer.form.customer.DeleteForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.service.CustomerAccountService;
import com.gourmetcaree.db.common.service.CustomerService;


/**
 * 顧客削除アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DeleteAction extends CustomerBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/** 顧客マスタサービス */
	@Resource
	protected CustomerService customerService;

	/** 顧客アカウントサービス */
	@Resource
	protected CustomerAccountService customerAccountService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{customerVersion}", input = TransitionConstants.Customer.JSP_APD01R01)
	@MethodAccess(accessCode="CUSTOMER_DELETE_INDEX")
	public String index() {
		return submit();
	}

	/**
	 * 登録
	 * @return
	 */
	private String submit() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		deleteForm.setExistDataFlgNg();

		try {
			MCustomer entity = new MCustomer();
			entity.id = Integer.parseInt(deleteForm.id);
			entity.version = deleteForm.customerVersion;

			customerService.logicalDelete(entity);
			customerAccountService.logicalDeleteByCustomerId(entity.id);

			log.debug("顧客をDELETEしました。");

		} catch (NumberFormatException e) {

			throw new ActionMessagesException("errors.app.noCustomerData");
		}


		return TransitionConstants.Customer.REDIRECT_CUSTOMER_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_DELETE_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Customer.JSP_APD01D03;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_DELETE_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMER_LIST_SEARCHAGAIN;
	}

}