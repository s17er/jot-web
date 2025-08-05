package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.DeleteForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * スカウトメール削除アクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class DeleteAction extends ScoutMailBaseAction  {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** メール削除フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String index() {
		return submit();
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ScoutFoot.JSP_SPE02R01)
	public String indexFromMail() {
		submit();
		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_DELETE_COMP_FROM_MAIL;
	}

	/**
	 * 確認画面表示
	 * @return
	 */
	public String submit() {

		deleteForm.setExistDataFlgNg();

		// 削除処理
		doDelete();

		log.debug("削除しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("スカウトメール削除アクションクラス（削除しました）。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_DELETE_COMP;
	}

	/**
	 * メール削除処理
	 */
	private void doDelete() {

//		// 削除データを取得
//		ScoutMailDetailDto dto = (ScoutMailDetailDto) session.getAttribute("scoutMailDetailDto");
//		if (dto == null) {
//			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
//		}

		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id, deleteForm.sendKbn);

		// 削除データが有るかチェック(セッション情報が変更されていないかチェック)
		if (!mailService.isCustomerMailExist(NumberUtils.toInt(deleteForm.id),
											userDto.customerId,
											NumberUtils.toInt(deleteForm.sendKbn),
											MTypeConstants.MailKbn.SCOUT)) {
			throw new FraudulentProcessException("errors.inconsistentDataError");
		}


		// 削除データを生成
		TMail entity;
		entity = new TMail();
		entity.id = NumberUtils.toInt(deleteForm.id);

		// 削除
		if (mailService.logicalDeleteIncludesVersion(entity) == 0) {
			throw new FraudulentProcessException("errors.inconsistentDataError");
		}
	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false)
	public String comp() {
		session.setAttribute(DeleteForm.FROM_MENU_SESSION_KEY, DeleteForm.FromMenuKbn.SCOUT_MAIL);
		return TransitionConstants.ScoutFoot.JSP_SPE02D04;
	}

	/**
	 * 完了画面表示
	 * @return
	 */
	@Execute(validator = false)
	public String compFromMail() {
		session.setAttribute(DeleteForm.FROM_MENU_SESSION_KEY, DeleteForm.FromMenuKbn.MAIL_BOX);
		return TransitionConstants.ScoutFoot.JSP_SPE02D04;
	}

	/**
	 * 一覧に戻るのURLを返却
	 * @return
	 */
	public String getBackListPath(){

		Object obj = session.getAttribute(DeleteForm.FROM_MENU_SESSION_KEY);
		if (obj == null) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST;
		} else if (obj == DeleteForm.FromMenuKbn.MAIL_BOX) {
			return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST_MAILBOX;
		}
		return TransitionConstants.ScoutFoot.REDIRECT_SCOUTMAIL_LIST;
	}

}
