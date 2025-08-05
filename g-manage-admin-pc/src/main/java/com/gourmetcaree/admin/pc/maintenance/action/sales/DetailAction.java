package com.gourmetcaree.admin.pc.maintenance.action.sales;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.sales.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * 営業担当者詳細アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class DetailAction extends SalesBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** 詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ04R01)
	@MethodAccess(accessCode="SALES_DETAIL_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_DETAIL_BACK")
	public String back() {
		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_SALES_SEARCH_AGAIN;
	}

	/**
	 * 初期表示遷移用
	 * @return 詳細画面のパス
	 */
	private String show() {

		// 表示データ取得
		convertShowData(detailForm);

		log.debug("営業担当者情報取得");

		// パスの生成
		detailForm.editPath = GourmetCareeUtil.makePath("/sales/edit/", "index", String.valueOf(detailForm.id));
		detailForm.deletePath = GourmetCareeUtil.makePath("/sales/delete/", "index", String.valueOf(detailForm.id), String.valueOf(detailForm.version));

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04R01;
	}

}