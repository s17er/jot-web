package com.gourmetcaree.admin.pc.maintenance.action.company;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.company.DetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.exception.WNoResultException;
/**
*
* 会社詳細を表示するクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DetailAction extends CompanyBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ05R01)
	@MethodAccess(accessCode="COMPANY_DETAIL_INDEX")
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		return show();
	}

	/**
	 * 戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="COMPANY_DETAIL_BACK")
	public String back() {

		// 一覧画面の検索メソッドへ遷移
		return TransitionConstants.Maintenance.REDIRECT_COMPANY_LIST_SEARCH;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		try {
			// DBから取得したデータを表示用に変換
			createDisplayValue(getData(detailForm));

		} catch (WNoResultException e) {

			// 画面表示をしない
			detailForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();
		}

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05R01;
	}

	/**
	 * 検索結果を画面表示にFormに移し返すロジック
	 * @param entity MCompanyエンティティ
	 */
	private void createDisplayValue(MCompany entity) {

		// 値をセット
		super.createDisplayValue(entity, detailForm);

		// 編集画面のパスをセット
		detailForm.editPath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_COMPANY_EDIT_INDEX, String.valueOf(entity.id));
	}
}