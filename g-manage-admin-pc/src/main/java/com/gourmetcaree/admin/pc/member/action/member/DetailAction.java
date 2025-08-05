package com.gourmetcaree.admin.pc.member.action.member;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.member.form.member.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * 会員管理詳細アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class DetailAction extends MemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{id}", input = TransitionConstants.Member.JSP_APH01R01)
	@MethodAccess(accessCode="MEMBER_DETAIL_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBER_DETAIL_BACK")
	public String back() {
		// 気になる一覧から遷移している場合は気になる一覧へ戻る
		if(detailForm.interestListFlg) {
			return TransitionConstants.Interest.REDIRECT_INTEREST_SEARCH_AGAIN;
		}else {
			return TransitionConstants.Member.REDIRECT_MEMBER_SEARCH_AGAIN;
		}
	}

	/**
	 * 気になる一覧からの遷移
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "interestIndex/{id}", input = TransitionConstants.Member.JSP_APH01R01)
	@MethodAccess(accessCode="MEMBER_DETAIL_INDEX")
	public String interestIndex() {
		detailForm.interestListFlg = true;
		return show();
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

		// プレゼント希望欄表示判定
		detailForm.gourmetMagazineReceptionDisplayFlg = isEast(detailForm.areaCd);

		// パスの生成
		detailForm.editPath = GourmetCareeUtil.makePath("/member/edit/", "index", String.valueOf(detailForm.id));
		detailForm.deletePath = GourmetCareeUtil.makePath("/member/delete/", "index", String.valueOf(detailForm.id));

		// 登録画面へ遷移
		return TransitionConstants.Member.JSP_APH01R01;
	}

}