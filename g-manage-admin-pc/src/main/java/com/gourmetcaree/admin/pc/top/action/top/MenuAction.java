package com.gourmetcaree.admin.pc.top.action.top;

import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
/**
 *
 * TOP画面を表示するクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class MenuAction extends PcAdminAction {

	/**
	 * 初期表示
	 * @return TOP画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="TOP_MENU_INDEX")
	public String index() {

		return show();
	}


	/**
	 * 初期表示遷移用
	 * @return TOP画面のパス
	 */
	private String show() {
		// トップメニューへ遷移
		return TransitionConstants.Top.JSP_APB01M01;
	}
}
