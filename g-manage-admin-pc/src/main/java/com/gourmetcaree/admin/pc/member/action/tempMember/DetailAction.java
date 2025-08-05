package com.gourmetcaree.admin.pc.member.action.tempMember;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.member.form.tempMember.DetailForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.tempMember.TempMemberLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.ActionMessageUtil;


/**
 * 仮会員詳細アクション
 * @author nakamori
 *
 */
@ManageLoginRequired(authLevel = {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class DetailAction extends PcAdminAction {

	@Resource
	protected TempMemberLogic tempMemberLogic;



	@ActionForm
	@Resource(name = "tempMember_detailForm")
	private DetailForm form;


	/**
	 * 初期表示
	 */
	@MethodAccess(accessCode = "TEMPMEMBER_DETAIL")
	@Execute(validator = false, urlPattern = "{id}")
	public String index() {
		try {
			tempMemberLogic.createData(form);
			form.setExistDataFlgOk();
		} catch (SNoResultException e) {
			form.setExistDataFlgNg();
			ActionMessageUtil.setActionMessageToRequest("errors.app.dataNotFound");
		}
		return TransitionConstants.Member.JSP_APH03R01;
	}


	/**
	 * 検索結果一覧へ戻る
	 */
	@MethodAccess(accessCode = "TEMPMEMBER_DETAIL")
	@Execute(validator = false)
	public String back() {
		return TransitionConstants.Member.REDIRECT_TEMP_MEMBER_SEARCH_AGAIN;
	}

	/**
	 * 仮会員の削除
	 */
	@MethodAccess(accessCode = "TEMPMEMBER_DETAIL")
	@Execute(validator = false)
	public String delete() {
		tempMemberLogic.delete(form);
		return TransitionConstants.Member.REDIRECT_TEMP_MEMBER_DELETE_COMP;
	}

	/**
	 * 仮会員の削除完了
	 */
	@MethodAccess(accessCode = "TEMPMEMBER_DETAIL")
	@Execute(validator = false)
	public String compDelete() {
		return TransitionConstants.Member.JSP_APH03D03;
	}

	/**
	 * 仮会員を承認(本登録)する
	 */
	@Execute(validator = false, urlPattern = "signIn/{id}")
	public String signIn() {
		tempMemberLogic.signIn(Integer.valueOf(form.id));
		return TransitionConstants.Member.JSP_APH03C01;
	}

}
