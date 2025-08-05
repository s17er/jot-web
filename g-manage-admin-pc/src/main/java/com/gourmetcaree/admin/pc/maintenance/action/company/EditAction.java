package com.gourmetcaree.admin.pc.maintenance.action.company;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.company.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.property.CompanyProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.exception.WNoResultException;
/**
*
* 会社編集を行うクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends CompanyBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ05E01)
	@MethodAccess(accessCode="COMPANY_EDIT_INDEX")
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		return show();
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.Maintenance.JSP_APJ05E01)
	@MethodAccess(accessCode="COMPANY_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05E02;
	}

	/**
	 * 戻る
	 * @return 詳細画面の初期表示
	 */
	@Execute(validator = false, reset="resetFormWithoutId")
	@MethodAccess(accessCode="COMPANY_EDIT_BACK")
	public String back() {

		// 確認画面の表示メソッドへリダイレクト
		return TransitionConstants.Maintenance.ACTION_COMPANY_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 訂正
	 * @return 編集画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="COMPANY_EDIT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05E01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ05E01)
	@MethodAccess(accessCode="COMPANY_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 編集処理
		edit();

		return TransitionConstants.Maintenance.REDIRECT_COMPANY_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="COMPANY_EDIT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		try {
			createDisplayValue(getData(editForm));

		} catch (WNoResultException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();
		}

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05E01;
	}

	/**
	 * 検索結果を画面表示にFormに移し返すロジック<br />
	 * 更新用に会社エリアマスタの値を保持
	 * @param entity MCompanyエンティティ
	 */
	private void createDisplayValue(MCompany entity) {

		// 画面表示用に値を作成
		super.createDisplayValue(entity, editForm);

		// 会社エリアマスタDelete,Insertのため、変更前のエンティティを保持
		editForm.delMCompanyAreaList = entity.mCompanyAreaList;
	}
	/**
	 * 会社マスタを更新
	 */
	private void edit() {

		// 会社マスタエンティティ
		MCompany entity = new MCompany();

		// 会社マスタエンティティにフォームをコピー
		Beans.copy(editForm, entity).dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "registrationDatetime").execute();

		// 会社管理プロパティに値をセット
		CompanyProperty property = new CompanyProperty();
		// 会社マスタ
		property.mCompany = entity;
		// エリアコード
		property.areaCd = editForm.areaCd;

		// 削除する会社エリアマスタのリストをセット
		property.mCompanyAreaList = editForm.delMCompanyAreaList;

		// 会社マスタ、会社エリアマスタを更新する
		companyLogic.updateCompany(property);

		log.debug("会社マスタを編集しました。" + editForm);
	}
}