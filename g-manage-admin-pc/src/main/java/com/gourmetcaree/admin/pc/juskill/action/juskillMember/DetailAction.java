package com.gourmetcaree.admin.pc.juskill.action.juskillMember;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.juskill.form.juskillMember.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;


/**
 * ジャスキル会員詳細アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class DetailAction extends JuskillMemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", input = TransitionConstants.Juskill.JSP_APQ01R01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_DETAIL_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="JUSKILL_MEMBER_DETAIL_BACK")
	public String back() {
		// 確認画面へ遷移
		return TransitionConstants.Juskill.REDIRECT_JUSKILL_SEARCH_AGAIN;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		// 表示データ取得
		convertDispData(detailForm);
		log.debug("表示データ取得");

		// 登録画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01R01;
	}
}