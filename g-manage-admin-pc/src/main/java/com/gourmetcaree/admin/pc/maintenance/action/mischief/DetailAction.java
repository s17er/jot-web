package com.gourmetcaree.admin.pc.maintenance.action.mischief;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.mischief.DetailForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MischiefApplicationConditionLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;

/**
 * いたずら応募条件詳細クラス
 * @author Aquarius
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DetailAction extends PcAdminAction {

	/** 一覧フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** いたずら応募条件ロジック */
	@Resource
	protected MischiefApplicationConditionLogic mischiefApplicationConditionLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ10R01)
	@MethodAccess(accessCode = "MISCHIEF_DETAIL_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MISCHIEF_DETAIL_BACK")
	public String back() {

		// 一覧画面の検索メソッドへ遷移
		return TransitionConstants.Maintenance.REDIRECT_MISCHIEF_LIST_SEARCH;
	}

	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {

		createDisplayValue();

		detailForm.setExistDataFlgOk();

		// 詳細画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10R01;
	}

	/**
	 * 画面表示用の値を作成する
	 */
	private void createDisplayValue() {

		try {
			MMischiefApplicationCondition entity = mischiefApplicationConditionLogic.findById(Integer.valueOf(detailForm.id));
			Beans.copy(entity, detailForm).execute();
			detailForm.editPath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_MISCHIEF_EDIT_INDEX, String.valueOf(entity.id));
		} catch (SNoResultException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}


}