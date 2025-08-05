package com.gourmetcaree.admin.pc.login.action.login;


import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.annotation.InvalidateSession;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.WebDataEditorDto;
import com.gourmetcaree.admin.pc.webdata.form.webdata.EditForm;


/**
 * ログインを処理するアクションクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class LogoutAction extends PcAdminAction {


	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(LogoutAction.class);

	/** WEBデータフォーム */
	@Resource
	private EditForm editForm;

	/** WEBデータ編集の排他判定DTO */
	@Resource
	public WebDataEditorDto webDataEditorDto;

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
		userDto = null;
		// WEBデータ編集中だった場合は排他判定を削除する
		webDataEditorDto.remove(NumberUtils.toInt(editForm.id));
		log.debug("ログアウトしました:" + userDto);
		return TransitionConstants.Login.REDIRECT_LOGIN_LOGIN;
	}
}
