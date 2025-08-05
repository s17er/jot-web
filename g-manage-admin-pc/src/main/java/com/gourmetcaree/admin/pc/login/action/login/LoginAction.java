package com.gourmetcaree.admin.pc.login.action.login;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.login.form.login.LoginForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.SalesDto;
import com.gourmetcaree.admin.service.logic.SalesLogic;
import com.gourmetcaree.common.exception.LoginFailedException;
import com.gourmetcaree.common.exception.LoginIdDuplicateException;


/**
 * ログインを処理するアクションクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class LoginAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(LoginAction.class);

	/** ログインフォーム */
	@ActionForm
	@Resource
	protected LoginForm loginForm;

	/** 営業担当者に関するロジック */
	@Resource
	SalesLogic salesLogic;

	/**
	 * 初期表示
	 * @return ログイン画面
	 */
	@Execute(validator = false)
	public String index() {
		return show();
	}

	/**
	 * ログイン処理を行う
	 * @return
	 */
	@Execute (validator = true, input = TransitionConstants.Login.JSP_APA01S01)
	public String login() {

		//既存セッションがある場合は破棄
		if (userDto.userId != null) {

			// セッションのリセット
			session.invalidate();
			request.getSession(true);

			// リダイレクトを行わないとセッションが再生成されないため、自分自身にリダイレクト
			return "/login/login/login/?loginId=" + loginForm.loginId + "&password=" + loginForm.password + "&redirect=true";
		}

		try {
			// ログインIDをトリム
			loginForm.trimLoginId();

			SalesDto dto = salesLogic.login(loginForm.loginId, loginForm.password);

			//ログインユーザDtoに保持
			userDto.authLevel = Integer.toString(dto.authorityCd);
			userDto.company = dto.companyName;
			userDto.name = dto.salesName;
			userDto.userId = Integer.toString(dto.id);
			userDto.companyId = dto.companyId;
			userDto.loginId = loginForm.loginId;

			//ログイン情報をログに出力
			StringBuilder sb = new StringBuilder("");
			sb.append("ログイン情報[");
			sb.append("id:");
			sb.append(userDto.userId);
			sb.append(", RemoteAddress:");
			sb.append(getRemoteAddress());
			sb.append(", User-Agent:");
			sb.append(getRequestHeader("User-Agent"));
			sb.append("]");

			log.info(sb.toString());

		} catch (LoginFailedException e) {
			throw new ActionMessagesException("errors.app.loginfaild");
		} catch (LoginIdDuplicateException e) {
			log.fatal("ログインIDが2重登録されている可能性があります。loginForm.loginId：" + loginForm.loginId);
			throw new ActionMessagesException("errors.app.loginfaild");
		}

		return TransitionConstants.Top.REDIRECT_TOP_MENU;
	}

	/**
	 * 初期表示遷移用
	 * @return ログイン画面のパス
	 */
	private String show() {
		// ログイン画面へ遷移
		return TransitionConstants.Login.JSP_APA01S01;
	}

}
