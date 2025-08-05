package com.gourmetcaree.shop.pc.pattern.action.pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.shop.pc.pattern.form.pattern.EditForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;

/**
 * 定型文の編集をするアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class EditAction extends PatternBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 定型文編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Pattern.JSP_SPF01E01)
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		return show();
	}

//	2022年4月6日　管理画面リニューアルで確認画面未使用になったためコメントアウト
//	/**
//	 * 確認画面表示
//	 * @return 確認画面
//	 */
//	@Execute(validator = true, input = TransitionConstants.Pattern.JSP_SPF01E01)
//	public String conf() {
//
//		// パラメータが空の場合はエラー
//		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
//
//		// プロセスフラグを確認済みに設定
//		editForm.setProcessFlgOk();
//
//		// 確認画面へ遷移
//		return TransitionConstants.Pattern.JSP_SPF01E02;
//	}

//	2022年4月6日　管理画面リニューアルで確認画面未使用になったためコメントアウト
//	/**
//	 * 戻る
//	 * @return 詳細画面の初期表示
//	 */
//	@Execute(validator = false, reset="resetFormWithoutId")
//	public String back() {
//
//		// 確認画面の表示メソッドへリダイレクト
//		return TransitionConstants.Pattern.ACTION_PATTERN_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
//	}

//	2022年4月6日　管理画面リニューアルで確認画面未使用になったためコメントアウト
//	/**
//	 * 訂正
//	 * @return 編集画面
//	 */
//	@Execute(validator = false)
//	public String correct() {
//
//		// プロセスフラグを未確認に設定
//		editForm.setProcessFlgNg();
//
//		// 登録画面へ遷移
//		return TransitionConstants.Pattern.JSP_SPF01E01;
//	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = true, input = TransitionConstants.Pattern.JSP_SPF01E01)
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

		// 編集完了メソッドへリダイレクト
		return TransitionConstants.Pattern.REDIRECT_PATTERN_EDIT_COMP;
	}

	/**
	 * 完了画面表示
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Pattern.JSP_SPF01E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		createDisplayValue(getData(editForm));

		// 画面を表示
		editForm.setExistDataFlgOk();

		// 登録画面へ遷移
		return TransitionConstants.Pattern.JSP_SPF01E01;
	}

	/**
	 * 検索結果を画面表示にFormに移し返すロジック<br />
	 * 更新用に定型文エリアマスタの値を保持
	 * @param entity MSentenceエンティティ
	 */
	private void createDisplayValue(MSentence entity) {

		// データのコピー
		Beans.copy(entity, editForm).execute();
	}

	/**
	 * 定型文マスタを更新
	 */
	private void edit() {

		// 定型文マスタエンティティ
		MSentence entity = new MSentence();

		// 定型文マスタエンティティにフォームをコピー
		Beans.copy(editForm, entity).converter(new ZenkakuKanaConverter()).execute();

		// 定型文マスタを更新する
		sentenceService.update(entity);

		log.debug("定型文マスタを編集しました。" + editForm);

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("定型文マスタを編集しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			sysMasqueradeLog.debug("定型文マスタを編集しました。営業ID：" + userDto.masqueradeUserId + " " + editForm);
		}
	}
}
