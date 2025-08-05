package com.gourmetcaree.admin.pc.maintenance.action.terminal;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.company.DetailForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
/**
*
* 駅グループ詳細を表示するクラス
* @author yamane
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DetailAction extends PcAdminAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ05R01)
	@MethodAccess(accessCode="COMPANY_DETAIL_INDEX")
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		return show();
	}

	/**
	 * 戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="COMPANY_DETAIL_BACK")
	public String back() {

		// 一覧画面の検索メソッドへ遷移
		return TransitionConstants.Maintenance.REDIRECT_COMPANY_LIST_SEARCH;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {


		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05R01;
	}

}