package com.gourmetcaree.shop.pc.price.action.price;

import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 料金表表示アクション。
 * @author Daiki Uchida
 *
 */
@ManageLoginRequired
public class IndexAction extends PcShopAction {

	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Price.JSP_SPK01A01)
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		checkUnReadMail();
		return TransitionConstants.Price.JSP_SPK01A01;
	}
}
