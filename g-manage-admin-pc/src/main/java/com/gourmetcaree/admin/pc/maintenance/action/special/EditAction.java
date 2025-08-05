package com.gourmetcaree.admin.pc.maintenance.action.special;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.special.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.SpecialLogic;
import com.gourmetcaree.admin.service.property.SpecialProperty;
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
 * 特集データ編集を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends SpecialBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** 特集マスタのロジック */
	@Resource
	protected SpecialLogic specialLogic;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ03E01)
	@MethodAccess(accessCode="SPECIAL_EDIT_INDEX")
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
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.Maintenance.JSP_APJ03E01)
	@MethodAccess(accessCode="SPECIAL_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03E02;
	}

	/**
	 * 戻る
	 * @return 一覧画面の初期表示
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SPECIAL_EDIT_BACK")
	public String back() {

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_SPECIAL_LIST_SEARCH;
	}

	/**
	 * 訂正
	 * @return 編集画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SPECIAL_EDIT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03E01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ03E01)
	@MethodAccess(accessCode="SPECIAL_EDIT_SUBMIT")
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

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_SPECIAL_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="SPECIAL_EDIT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 入力画面のパス
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		createDisplayValue(getData(editForm));

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03E01;
	}

	/**
	 * エンティティを画面表示のFormに設定する
	 * @param entity MSpecialエンティティ
	 */
	private void createDisplayValue(MSpecial entity) {

		// エンティティをフォームコピー(別名なのでDate以外の変換は下で行う)
		Beans.copy(entity, editForm).execute();

		// 掲載開始日時(年月日)のフォーマット
		editForm.postStartDate = DateUtils.getDateStr(entity.postStartDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		// 掲載開始日時(時)のフォーマット
		editForm.postStartHour = DateUtils.getDateStr(entity.postStartDatetime, GourmetCareeConstants.HOUR_FORMAT);
		// 掲載開始日時(分)のフォーマット
		editForm.postStartMinute = DateUtils.getDateStr(entity.postStartDatetime, GourmetCareeConstants.MINUTE_FORMAT);
		// 掲載終了日時(年月日)のフォーマット
		editForm.postEndDate = DateUtils.getDateStr(entity.postEndDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		// 掲載終了日時(時)のフォーマット
		editForm.postEndHour = DateUtils.getDateStr(entity.postEndDatetime, GourmetCareeConstants.HOUR_FORMAT);
		// 掲載終了日時(分)のフォーマット
		editForm.postEndMinute  = DateUtils.getDateStr(entity.postEndDatetime, GourmetCareeConstants.MINUTE_FORMAT);

		// アドレスの作成
		editForm.url =  GourmetCareeUtil.makePath(getSpecialUrl(), String.valueOf(entity.id));

		// エリアをセット
		editForm.areaCd = new ArrayList<String>();
		for (MSpecialDisplay mSpecialDisplay : entity.mSpecialDisplayList) {
			editForm.areaCd.add(String.valueOf(mSpecialDisplay.areaCd));
		}

		entity = null;
	}

	/**
	 * 特集マスタを更新
	 */
	private void edit() {

		// 特集プロパティに値をセット
		SpecialProperty property = new SpecialProperty();
		property.mSpecial = new MSpecial();
		// 特集情報をコピー
		Beans.copy(editForm, property.mSpecial).execute();

		// エリアが選択されていればプロパティにセット
		if (editForm.areaCd != null && !editForm.areaCd.isEmpty()) {
			property.areaCd = GourmetCareeUtil.toIntegerList(editForm.areaCd);
		}

		// 特集情報をマスタ更新する
		specialLogic.updateSpecial(property);

		log.debug("特集マスタを編集しました。" + editForm);
	}

}