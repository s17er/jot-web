package com.gourmetcaree.admin.pc.information.action.information;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.information.form.information.MenuForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * お知らせ管理のメニューを表示するActionです。
 * @author ando
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class MenuAction extends InformationBaseAction {

	/**
	 * アクションフォーム
	 */
	@ActionForm
	@Resource
	protected MenuForm menuForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="INFORMATION_MENU_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return お知らせ管理メニューのパス
	 */
	private String show() {
		// トップメニューへ遷移
		return TransitionConstants.Information.JSP_APL01M01;
	}
}
