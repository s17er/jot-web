package com.gourmetcaree.admin.pc.application.action.observateApplication;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.application.form.observateApplication.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * 応募管理詳細アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction extends ObservateApplicationBaseAction {

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
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{id}", input = TransitionConstants.Application.JSP_API02R01)
	@MethodAccess(accessCode="APPLICATION_DETAIL_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="APPLICATION_DETAIL_BACK")
	public String back() {
		// 確認画面へ遷移
		return TransitionConstants.Application.REDIRECT_OBSERVATE_APPLICATION_SEARCH_AGAIN;
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
		return TransitionConstants.Application.JSP_API02R01;
	}

}