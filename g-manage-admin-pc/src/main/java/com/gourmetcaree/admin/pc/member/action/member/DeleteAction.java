package com.gourmetcaree.admin.pc.member.action.member;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.member.form.member.DeleteForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MMember;

/**
 * 会員管理削除アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class DeleteAction extends MemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** 削除フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset ="resetForm", urlPattern = "index/{id}", input = TransitionConstants.Member.JSP_APH01D01)
	@MethodAccess(accessCode="MEMBER_DELETE_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_EDIT_BACK")
	public String back() {
		// 詳細画面へ遷移
		return TransitionConstants.Member.REDIRECT_MEMBER_DETAIL_INDEX + deleteForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, validate = "validate", removeActionForm = true, input = TransitionConstants.Member.MEMBER_DELETE_INDEX_AGAIN)
	@MethodAccess(accessCode="MEMBER_DELETE_SUBMIT")
	public String submit() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id, deleteForm.deleteVersion);

		deleteForm.setExistDataFlgNg();

		// idが数値かどうかチェック
		if (!StringUtils.isNumeric(deleteForm.id)) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 削除
		doDelete();

		log.debug("会員をDELETEしました。");

		return TransitionConstants.Member.REDIRECT_MEMBER_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_DELETE_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Member.JSP_APH01D03;
	}

	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_DELETE_INDEX")
	public String indexAgain() {
		return show();
	}

	/**
	 *
	 * @return
	 */
	private String show() {
		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		// 表示データ取得
		convertDispData(deleteForm);

		return TransitionConstants.Member.JSP_APH01D01;
	}



	/**
	 * 会員削除処理
	 */
	private void doDelete() {

		MMember member = new MMember();

		// 削除データ生成
		createDeleteData(member);

		// 削除
		memberService.logicalDelete(member);
	}

	/**
	 * 削除データ生成
	 * @param member 会員エンティティ
	 */
	private void createDeleteData(MMember member) {

		member.id = NumberUtils.toInt(deleteForm.id);
		member.deleteReasonKbn = NumberUtils.toInt(deleteForm.deleteReasonKbn);
		member.version = NumberUtils.toLong(deleteForm.deleteVersion);
	}

}