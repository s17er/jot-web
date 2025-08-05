package com.gourmetcaree.shop.pc.pattern.action.pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.shop.pc.pattern.form.pattern.DeleteForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 定型文削除アクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class DeleteAction extends PatternBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 定型文削除フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, reset="resetFormWithoutDelValue", input=TransitionConstants.Pattern.JSP_SPF01R01)
	public String index() {

		// 削除
		return submit();
	}

	/**
	 * 完了画面表示
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Pattern.JSP_SPF01D03;
	}

	/**
	 * 削除
	 * @return 完了へリダイレクト
	 */
	private String submit() {

		// 確認画面のダイアログから遷移していない場合はエラー
		if (!deleteForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id);

		// 削除可能なデータかチェック
		getData(deleteForm);

		// 削除処理
		delete();

		// 削除完了メソッドへリダイレクト
		return TransitionConstants.Pattern.REDIRECT_PATTERN_DELETE_COMP;
	}

	/**
	 * 定型文マスタを物理削除
	 */
	private void delete() {

		// 定型文マスタエンティティ
		MSentence entity = new MSentence();

		// 定型文マスタエンティティにフォームをコピー
		Beans.copy(deleteForm, entity).execute();

		try {
			// 定型文マスタから物理削除
			sentenceService.delete(entity);

			// 楽観的排他制御でエラーとなった場合は、画面非表示にしてエラーを投げる
		} catch (SOptimisticLockException e) {
			deleteForm.resetForm();
			deleteForm.setExistDataFlgNg();
			throw new SOptimisticLockException();
		}

		log.debug("定型文マスタを削除しました。" + deleteForm);
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("定型文マスタを削除しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			sysMasqueradeLog.debug("定型文マスタを削除しました。営業ID：" + userDto.masqueradeUserId + " " + deleteForm);
		}
	}

}
