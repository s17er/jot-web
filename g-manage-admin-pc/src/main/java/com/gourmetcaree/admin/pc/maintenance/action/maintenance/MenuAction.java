package com.gourmetcaree.admin.pc.maintenance.action.maintenance;

import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * マスタメンテナンス画面を表示するクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class MenuAction extends PcAdminAction {

	/**
	 * 初期表示
	 * @return マスタメンテナンスメニュー画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MAINTENANCE_INPUT_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return マスタメンテナンスメニュー画面のパス
	 */
	private String show() {
		// トップメニューへ遷移
		return TransitionConstants.Maintenance.JSP_APJ01M01;
	}
}
