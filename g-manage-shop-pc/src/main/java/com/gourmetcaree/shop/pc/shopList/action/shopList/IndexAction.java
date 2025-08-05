package com.gourmetcaree.shop.pc.shopList.action.shopList;

import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.shop.pc.shop.action.shop.ShopBaseAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

/**
 * 店舗一覧用アクションです。
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class IndexAction extends ShopBaseAction {

	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ01A01)
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		checkUnReadMail();
		return TransitionConstants.ShopList.JSP_SPJ01A01;
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.shopListInstance();
	}
}
