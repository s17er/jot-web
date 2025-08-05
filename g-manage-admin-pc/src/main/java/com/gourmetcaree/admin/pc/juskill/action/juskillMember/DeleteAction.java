package com.gourmetcaree.admin.pc.juskill.action.juskillMember;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.service.CustomerAccountService;
import com.gourmetcaree.db.common.service.JuskillMemberService;


/**
 * ジャスキル会員削除アクションクラス
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class DeleteAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	public String id;

	public String version;

	/** ジャスキル会員マスタサービス */
	@Resource
	protected JuskillMemberService juskillMemberService;

	/** 顧客アカウントサービス */
	@Resource
	protected CustomerAccountService customerAccountService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{version}", input = TransitionConstants.Juskill.JSP_APQ01R01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_DELETE_INDEX")
	public String index() {
		return submit();
	}

	/**
	 * 登録
	 * @return
	 */
	private String submit() {

		checkArgsNull(NO_BLANK_FLG_NG, id);
		checkArgsNull(NO_BLANK_FLG_NG, version);

		try {
			MJuskillMember entity = new MJuskillMember();
			entity.id = Integer.parseInt(id);
			entity.version = Long.parseLong(version);

			juskillMemberService.logicalDelete(entity);

			log.info("ジャスキルをDELETEしました。");

		} catch (NumberFormatException e) {

			throw new ActionMessagesException("errors.fraudulentProcessError");
		}

		return TransitionConstants.Juskill.REDIRECT_JUSKILL_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="JUSKILL_MEMBER_DELETE_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01D03;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="JUSKILL_MEMBER_DELETE_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Juskill.REDIRECT_JUSKILL_SEARCH_AGAIN;
	}

}