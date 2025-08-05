package com.gourmetcaree.shop.pc.login.action.login;


import org.apache.log4j.Logger;
import org.seasar.framework.aop.annotation.InvalidateSession;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * ログアウトを処理するアクションクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class LogoutAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(LogoutAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false)
	public String index() {
		return logout();
	}

	/**
	 * ログアウト処理
	 * @return
	 */
	@Execute (validator = false)
	@InvalidateSession
	public String logout() {

		boolean masqueradeFlg = userDto.isMasqueradeFlg();

		userDto = null;
		log.debug("ログアウトしました:" + userDto);

		if (masqueradeFlg) {
			sysMasqueradeLog.debug("なりすましログインが、ログアウトしました");
			return TransitionConstants.MasqueradeLogin.REDIRECT_LOGIN;
		}

		return TransitionConstants.Login.REDIRECT_LOGIN;
	}
}
