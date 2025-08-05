package com.gourmetcaree.admin.pc.sys.action;

import static com.gourmetcaree.admin.pc.sys.constants.TransitionConstants.FWD_LOGIN;

import org.seasar.struts.annotation.Execute;

/**
 * 全体のインデックスアクションです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class IndexAction extends PcAdminAction {

	/**
	 * インデックスメソッド
	 * @return 遷移先
	 */
	@Execute(validator = false)
	public String index() {
		return FWD_LOGIN;
	}
}
