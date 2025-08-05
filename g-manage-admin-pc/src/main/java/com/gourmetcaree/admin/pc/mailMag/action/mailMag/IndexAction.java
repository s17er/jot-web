package com.gourmetcaree.admin.pc.mailMag.action.mailMag;

import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * メールマガジンの初期表示アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class IndexAction extends PcAdminAction {

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK01M01)
	@MethodAccess(accessCode="MAILMAG_INDEX_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {
		return TransitionConstants.MailMag.JSP_APK01M01;
	}
}
