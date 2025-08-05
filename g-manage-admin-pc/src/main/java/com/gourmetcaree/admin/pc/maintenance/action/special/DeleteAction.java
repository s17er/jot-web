package com.gourmetcaree.admin.pc.maintenance.action.special;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.special.DeleteForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MSpecialDisplay;
/**
 * 特集データの削除を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DeleteAction extends SpecialBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ03D02)
	@MethodAccess(accessCode="SPECIAL_DELETE_INDEX")
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, deleteForm.id);

		return show();
	}

	/**
	 * 戻る
	 * @return 一覧画面の初期表示
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SPECIAL_DELETE_BACK")
	public String back() {

		// プロセスフラグを未確認に設定
		deleteForm.setProcessFlgNg();

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_SPECIAL_LIST_INDEX;
	}

	/**
	 * 削除
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ03D02)
	@MethodAccess(accessCode="SPECIAL_DELETE_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!deleteForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		checkArgsNull(NO_BLANK_FLG_NG,
						deleteForm.id,
						deleteForm.version
						);

		// 削除処理
		delete();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_SPECIAL_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="SPECIAL_DELETE_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03D03;
	}

	/**
	 * 初期表示遷移用
	 * @return 確認画面のパス
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		createDisplayValue(getData(deleteForm));

		// プロセスフラグを確認済みに設定
		deleteForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03D02;
	}

	/**
	 * エンティティを画面表示のFormに設定する
	 * @param entity MSpecialエンティティ
	 */
	private void createDisplayValue(MSpecial entity) {

		// エンティティをフォームコピー（フォーマットが必要なカラムは下でセット）
		Beans.copy(entity, deleteForm).execute();

		// 掲載開始日時のフォーマット
		deleteForm.postStartDatetimeStr = DateUtils.getDateStr(entity.postStartDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
		// 掲載終了日時のフォーマット
		deleteForm.postEndDatetimeStr = DateUtils.getDateStr(entity.postEndDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

		// アドレスの作成
		deleteForm.url =  GourmetCareeUtil.makePath(getSpecialUrl(), String.valueOf(entity.id));

		// エリアをセット
		deleteForm.areaCd = new ArrayList<String>();
		for (MSpecialDisplay mSpecialDisplay : entity.mSpecialDisplayList) {
			deleteForm.areaCd.add(String.valueOf(mSpecialDisplay.areaCd));
		}
	}

	/**
	 * 特集マスタを論理削除
	 */
	private void delete() {

		// 特集マスタエンティティ
		MSpecial entity = new MSpecial();

		// 特集マスタエンティティにフォームをコピー
		Beans.copy(deleteForm, entity)
			.dateConverter(GourmetCareeConstants.DATE_TIME_FORMAT, "postStartDatetime")
			.dateConverter(GourmetCareeConstants.DATE_TIME_FORMAT, "postEndDatetime")
			.execute();

		// 特集マスタから論理削除
		specialService.logicalDelete(entity);

		log.debug("特集マスタを削除しました。" + deleteForm);
	}
}