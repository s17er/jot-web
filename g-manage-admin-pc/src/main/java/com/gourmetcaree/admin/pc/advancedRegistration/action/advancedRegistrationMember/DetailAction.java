package com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * 事前登録会員詳細アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES} )
public class DetailAction extends AdvancedRegisterdMemberBaseAction {


	/** アクションフォーム */
	@ActionForm
	@Resource(name = "advancedRegistrationMember_detailForm")
	private DetailForm form;


	/**
	 * 初期表示
	 * @return 詳細画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", input = TransitionConstants.AdvancedRegistration.JSP_APH01R01)
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, form.id);
		return show();
	}

	/**
	 * 初期表示用メソッド
	 * @return 詳細画面
	 */
	private String show() {

		logic.createDetailData(form);

		return TransitionConstants.AdvancedRegistration.JSP_APH01R01;
	}

	/**
	 * 事前登録会員データ一覧へ戻る
	 */
	@Execute(validator = false, removeActionForm = true)
	public String back() {
		return TransitionConstants.AdvancedRegistration.REDIRECT_ADVANCED_MEMBER_SEARCH_AGAIN;
	}







}
