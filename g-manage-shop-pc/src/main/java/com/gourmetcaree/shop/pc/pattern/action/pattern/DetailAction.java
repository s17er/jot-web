package com.gourmetcaree.shop.pc.pattern.action.pattern;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.shop.pc.pattern.form.pattern.DetailForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 定型文詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class DetailAction extends PatternBaseAction {

	/** 定型文詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Pattern.JSP_SPF01R01)
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
	public String back() {

		// 一覧画面の検索メソッドへ遷移
		return TransitionConstants.Pattern.ACTION_PATTERN_LIST_INDEX;
	}

	/**
	 * 初期表示遷移用
	 * @return 詳細画面
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		createDisplayValue(getData(detailForm));

		// 詳細画面表示
		return TransitionConstants.Pattern.JSP_SPF01R01;
	}

	/**
	 * 検索結果を画面表示にFormに移し返すロジック
	 * @param entity MSentenceエンティティ
	 */
	private void createDisplayValue(MSentence entity) {

		// データコピー
		Beans.copy(entity, detailForm).execute();

		// 編集画面のパスをセット
		detailForm.editPath = GourmetCareeUtil.makePath(TransitionConstants.Pattern.ACTION_PATTERN_EDIT_INDEX, String.valueOf(entity.id));
	}
}
