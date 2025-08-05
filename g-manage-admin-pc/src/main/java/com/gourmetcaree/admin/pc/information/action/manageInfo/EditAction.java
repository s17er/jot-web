package com.gourmetcaree.admin.pc.information.action.manageInfo;


import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.information.action.information.InformationBaseAction;
import com.gourmetcaree.admin.pc.information.form.manageInfo.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.TInformation;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * お知らせ管理編集のアクションクラス
 * @author Takahiro Ando
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends InformationBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{managementScreenKbn}/{areaCd}", input = TransitionConstants.Information.JSP_APL02L01)
	@MethodAccess(accessCode="INFORMATION_EDIT_INDEX")
	public String index() {

		checkId(editForm, editForm.managementScreenKbn);
		checkId(editForm, editForm.areaCd);

		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="INFORMATION_EDIT_CONF")
	public String conf() {

		checkArgsNull(NO_BLANK_FLG_NG, editForm.managementScreenKbn, editForm.areaCd);

		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Information.JSP_APL02E02;
	}

	/**
	 * 戻る
	 * 一覧画面へのリダイレクト
	 * @return
	 */
	@Execute(validator = false, removeActionForm=true)
	@MethodAccess(accessCode="INFORMATION_EDIT_BACK")
	public String back() {
		// 確認画面へ遷移
		return "/manageInfo/list/index/" + editForm.managementScreenKbn + GourmetCareeConstants.REDIRECT_STR;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="INFORMATION_EDIT_CORRECT")
	public String correct() {

		checkArgsNull(NO_BLANK_FLG_NG, editForm.areaCd);
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Information.JSP_APL02E01;
	}

	/**
	 * 登録<br>
	 * 運営側管理のお知らせの場合エリアコードはデフォルトで関西がセットされているので考慮しない。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Information.JSP_APL02L01)
	@MethodAccess(accessCode="INFORMATION_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		updateInformation();

		return TransitionConstants.Information.REDIRECT_ADMININFO_EDIT_COMP;
	}

	/**
	 * 運営者側管理画面のお知らせをUPDATEします。
	 */
	private void updateInformation() {
		TInformation entity = new TInformation();
		Beans.copy(editForm, entity).execute();

		informationService.update(entity);

		log.debug("お知らせテーブルをUPDATEしました");
	}

	/**
	 * 完了<br>
	 * 他のcompメソッドと異なり、管理画面区分のみ残すようにリセットした後に
	 * 完了画面を表示する。
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="INFORMATION_EDIT_COMP")
	public String comp() {

		//管理画面区分以外をリセット
		Beans.copy(new EditForm(), editForm).excludes("managementScreenKbn").execute();

		// 完了画面へ遷移
		return TransitionConstants.Information.JSP_APL02E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {
		TInformation entity;
		try {
			entity = informationService.getInformationEntity(
															NumberUtils.toInt(editForm.managementScreenKbn),
															NumberUtils.toInt(editForm.areaCd));

		} catch (WNoResultException e) {
			entity = new TInformation();
			entity.managementScreenKbn = NumberUtils.toInt(editForm.managementScreenKbn);
			entity.areaCd = NumberUtils.toInt(editForm.areaCd);
			informationService.insert(entity);
		}

		Beans.copy(entity, editForm).execute();

		// 登録画面へ遷移
		return TransitionConstants.Information.JSP_APL02E01;
	}

}