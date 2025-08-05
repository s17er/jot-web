package com.gourmetcaree.admin.pc.maintenance.action.sales;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.sales.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.SalesLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.exception.AgencyAuthLevelException;
import com.gourmetcaree.db.common.exception.AuthLevelException;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 営業担当者編集アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class EditAction extends SalesBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** 営業担当者ロジック */
	@Resource
	protected SalesLogic salesLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{id}", input = TransitionConstants.Maintenance.JSP_APJ04E01)
	@MethodAccess(accessCode="SALES_EDIT_INDEX")
	public String index() {


		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Maintenance.JSP_APJ04E01)
	@MethodAccess(accessCode="SALES_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// パラメータが数値かどうかチェック
		if (!StringUtils.isNumeric(editForm.id)) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 編集データを取得できているかチェック
		if (!editForm.existDataFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// ログインIDの重複チェック、会社の存在チェック
		try {
			salesLogic.checkInputData(editForm.id, editForm.loginId, editForm.companyId, editForm.authorityCd);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.noCompanyData");
		} catch (AgencyAuthLevelException e) {
			throw new ActionMessagesException("errors.app.agencyAuthLevel");
		} catch(AuthLevelException e) {
			throw new ActionMessagesException("errors.app.authLevel");
		}

		// 会社名、権限、サブメール受信可否を名称へ変換
		convertToName(editForm);

		// パスワードを表示用に変換
		convertPassword();

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04E02;
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_EDIT_BACK")
	public String back() {
		// 詳細画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_SALES_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}
	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_EDIT_CORRECT")
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// サブメイル受信フラグがブランクの場合は、初期値をセット
		if (StringUtils.isBlank(editForm.submailReceptionFlg)) {
			editForm.submailReceptionFlg = String.valueOf(MTypeConstants.SubmailReceptionFlg.NOT_RECEIVE);
		}

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04E01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Maintenance.JSP_APJ04E01)
	@MethodAccess(accessCode="SALES_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// ログインIDの重複チェック、会社の存在チェック
		try {
			salesLogic.checkInputData(editForm.id, editForm.loginId, editForm.companyId, editForm.authorityCd);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.noCompanyData");
		} catch (AgencyAuthLevelException e) {
			throw new ActionMessagesException("errors.app.agencyAuthLevel");
		} catch(AuthLevelException e) {
			throw new ActionMessagesException("errors.app.authLevel");
		}

		// 登録データ生成
		MSales entity = new MSales();
		convertToEntity(entity);

		// 登録
		salesService.update(entity);

		log.debug("営業担当者をUPDATEしました");

		return TransitionConstants.Maintenance.REDIRECT_SALES_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_EDIT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// 表示データをセットする
		convertShowData(editForm);

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04E01;
	}

	/**
	 * 確認画面表示用にパスワードを変換
	 */
	private void convertPassword() {

		// パスワードを'*'に変換する
		Pattern pat = Pattern.compile(GourmetCareeConstants.MASK_ALPHANUM, Pattern.CASE_INSENSITIVE);
		Matcher match = pat.matcher(editForm.password);
		editForm.dispPassword = match.replaceAll(GourmetCareeConstants.MASK_FREE_CHAR);
	}

	/**
	 * フォームからエンティティへデータをコピー
	 * @param entity
	 */
	private void convertToEntity(MSales entity) {

		// パスワードが入力されているかチェック
		if (StringUtils.isBlank(editForm.password)) {

			// フォームからエンティティへデータをコピー
			Beans.copy(editForm, entity).excludes("password", "registrationDatetime").excludesNull().execute();


		} else {

			// フォームからエンティティへデータをコピー
			Beans.copy(editForm, entity).excludes("password", "registrationDatetime").excludesNull().execute();
			entity.password = DigestUtil.createDigest(editForm.password);
		}

	}

}