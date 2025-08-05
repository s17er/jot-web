package com.gourmetcaree.admin.pc.preApplication.action.preApplication;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.preApplication.form.preApplication.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction extends PreApplicationBaseAction{

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** 詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{id}", input = TransitionConstants.PreApplication.JSP_APT01R01)
	@MethodAccess(accessCode="PRE_APPLICATION_DETAIL_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="PRE_APPLICATION_DETAIL_BACK")
	public String back() {
		// 確認画面へ遷移
		return TransitionConstants.PreApplication.REDIRECT_PRE_APPLICATION_SEARCH_AGAIN;
	}
	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		// 表示データをセット
		convertDispData(detailForm);

		log.debug("表示データ取得");

		// 登録画面へ遷移
		return TransitionConstants.PreApplication.JSP_APT01R01;
	}


}
