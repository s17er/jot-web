package com.gourmetcaree.admin.pc.maintenance.action.sales;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.sales.DeleteForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MSales;

/**
 * 営業担当者削除アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class DeleteAction extends SalesBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{version}", input = TransitionConstants.Maintenance.JSP_APJ04R01)
	@MethodAccess(accessCode="SALES_DELETE_INDEX")
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
			MSales entity = new MSales();
			entity.id = Integer.parseInt(deleteForm.id);
			entity.version = Long.parseLong(deleteForm.version);

			salesService.logicalDelete(entity);

			log.debug("営業担当者をDELETEしました。");

		} catch (NumberFormatException e) {

			throw new ActionMessagesException("errors.app.noSalesData");
		}

		return TransitionConstants.Maintenance.REDIRECT_SALES_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_DELETE_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04D03;
	}


}