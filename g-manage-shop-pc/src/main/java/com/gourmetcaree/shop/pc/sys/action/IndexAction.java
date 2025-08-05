package com.gourmetcaree.shop.pc.sys.action;


import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;



/**
 * 関西版のインデックスアクションです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class IndexAction extends PcShopAction {

	/**
	 * インデックスメソッド
	 * @return 遷移先
	 */
	@Execute(validator = false)
	public String index() {

		return fwdLogin();
	}

	/**
	 * ログイン画面を表示するメソッドへフォワードするパスを返す
	 * @return ログイン画面を表示するメソッドへ遷移するパス
	 */
	@Execute(validator = false)
	public String fwdLogin() {

		return TransitionConstants.Login.FWD_LOGIN;
	}
}
