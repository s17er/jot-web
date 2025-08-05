package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMember;

import org.seasar.struts.annotation.Execute;

/**
 * スカウト会員詳細を表示するアクションクラスです。
 * @author Motoaki Hara
 * @version 1.0
 */
public class DetailAction  extends ScoutBaseAction {

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false)
	public String index() {

		return "";
	}
}
