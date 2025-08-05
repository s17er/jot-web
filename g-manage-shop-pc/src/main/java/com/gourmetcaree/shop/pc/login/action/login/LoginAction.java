package com.gourmetcaree.shop.pc.login.action.login;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.LoginFailedException;
import com.gourmetcaree.common.exception.LoginIdDuplicateException;
import com.gourmetcaree.db.common.enums.MTypeEnum.TerminalKbnEnum;
import com.gourmetcaree.db.common.property.WriteCustomerLoginHistoryProperty;
import com.gourmetcaree.db.common.service.CustomerLoginHistoryService;
import com.gourmetcaree.shop.logic.dto.CustomerAccountDto;
import com.gourmetcaree.shop.logic.logic.CustomerLogic;
import com.gourmetcaree.shop.pc.login.form.login.LoginForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * ログインを処理するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class LoginAction extends PcShopAction  {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(LoginAction.class);

	/** 顧客アカウント */
	@Resource
	protected CustomerLogic customerLogic;

	/** 顧客ログイン履歴サービス */
	@Resource
	protected CustomerLoginHistoryService customerLoginHistoryService;

	/** ログインフォーム */
	@ActionForm
	@Resource
	protected LoginForm loginForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false)
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		userDto = null;
		return TransitionConstants.Login.JSP_SPA01S01;
	}

	/**
	 * ログイン処理を行います。
	 * @return
	 */
	@Execute (validator = true, input = TransitionConstants.Login.JSP_SPA01S01)
	public String login() {

		//既存セッションがある場合は破棄
		if (userDto.userId != null) {
			session.invalidate();
			request.getSession(true);

			// リダイレクトを行わないとセッションが再生成されないため、自分自身にリダイレクト
			return "/login/login/login/?loginId=" + loginForm.loginId + "&password=" + loginForm.password + GourmetCareeConstants.REDIRECT_STR;
		}

		CustomerAccountDto dto;
		// ログイン
		try {
			// ログインIDをトリム
			loginForm.trimLoginId();

			dto = customerLogic.login(loginForm.loginId, loginForm.password);

			Beans.copy(dto, userDto).execute();
			userDto.userId = Integer.toString(dto.id);

		} catch (LoginFailedException e) {
			throw new ActionMessagesException("errors.app.loginfaild");
		} catch (LoginIdDuplicateException e) {
			log.fatal("ログインIDが2重登録されている可能性があります。loginForm.loginId：" + loginForm.loginId);
			throw new ActionMessagesException("errors.app.loginfaild");
		}

		// 最終ログイン履歴に書き込み
		WriteCustomerLoginHistoryProperty property = new WriteCustomerLoginHistoryProperty();
		property.customerId = dto.customerId;					// 顧客ID
		property.lastLoginDatetime = new Date();				// 最終ログイン日時
		property.userAgent = getRequestHeader("User-Agent");	// ユーザエージェント
		property.remoteAddress = getRemoteAddress();			// リモートアクセス
		// 顧客ログイン履歴テーブルの更新（端末区分：PC）
		customerLoginHistoryService.writeLoginHistory(property, TerminalKbnEnum.PC_VALUE);

		//ログイン情報をログに出力
		StringBuilder sb = new StringBuilder("");
		sb.append("ログイン情報[");
		sb.append("id:");
		sb.append(userDto.getUserId());
		sb.append(", RemoteAddress:");
		sb.append(getRemoteAddress());
		sb.append(", User-Agent:");
		sb.append(getRequestHeader("User-Agent"));
		sb.append("]");
		log.info(sb.toString());

		return TransitionConstants.Menu.REDIRECT_MENU;
	}

}
