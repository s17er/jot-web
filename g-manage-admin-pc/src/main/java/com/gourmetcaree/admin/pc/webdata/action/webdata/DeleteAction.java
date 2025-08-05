package com.gourmetcaree.admin.pc.webdata.action.webdata;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.webdata.form.webdata.DeleteForm;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.WebService;
/**
*
* WEBデータ削除を行うクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DeleteAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/** WEBデータのサービス */
	@Resource
	protected WebService webService;

	/**
	 * 初期表示
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, reset="resetFormWithoutDelValue", input=TransitionConstants.Webdata.JSP_APC01R01)
	@MethodAccess(accessCode="WEBDATA_DELETE_INDEX")
	public String index() {

		// 削除
		return submit();
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="WEBDATA_DELETE_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01D03;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_DELETE_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * 削除
	 * @return 完了へリダイレクト
	 */
	private String submit() {

		// 確認画面のダイアログから遷移していない場合はエラー
		if (!deleteForm.processFlg) {
			callFraudulentProcessError(deleteForm);
		}

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		try {
			// WEBデータ詳細の取得
			WebdataProperty property = getData(deleteForm);

			// 削除処理できない場合は権限エラー
			if (!webdataLogic.checkDelete(property)) {
				callAuthLevelError(deleteForm);
			}

		// データが取得できない場合はエラー
		} catch (WNoResultException e) {
			callFraudulentProcessError(deleteForm);
		}

		// 削除処理
		delete();

		return TransitionConstants.Webdata.REDIRECT_WEBDATA_DELETE_COMP;
	}

	/**
	 * WEBデータを論理削除
	 */
	private void delete() {

		// WEBデータエンティティ
		TWeb entity = new TWeb();

		// WEBデータエンティティにフォームをコピー
		Beans.copy(deleteForm, entity).execute();

		try {
			// WEBデータから論理削除
			webService.logicalDelete(entity);

			webTagMappingService.deleteByWebDataId(entity.id);

		// 楽観的排他制御でエラーとなった場合は、画面非表示にしてエラーを投げる
		} catch (SOptimisticLockException e) {
			deleteForm.resetForm();
			deleteForm.setExistDataFlgNg();
			throw new SOptimisticLockException("WEBデータの削除時に楽観的排他制御エラーが発生しました。");
		}

		log.debug("WEBデータを削除しました。" + deleteForm);
	}
}