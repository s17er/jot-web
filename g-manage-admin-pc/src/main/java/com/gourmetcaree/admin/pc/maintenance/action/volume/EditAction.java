package com.gourmetcaree.admin.pc.maintenance.action.volume;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.volume.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.VolumeLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.maintenance.dto.volume.DetailDto;
/**
 *
 * 号数データ編集を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);


	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** 号数のロジック */
	@Resource
	protected VolumeLogic volumeLogic;

	/** 号数のサービス */
	@Resource
	protected VolumeService volumeService;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ02E01)
	@MethodAccess(accessCode="VOLUME_EDIT_INDEX")
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
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Maintenance.JSP_APJ02E01)
	@MethodAccess(accessCode="VOLUME_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02E02;
	}

	/**
	 * 戻る
	 * @return 一覧画面の初期表示
	 */
	@Execute(validator = false, reset="resetForm")
	@MethodAccess(accessCode="VOLUME_EDIT_BACK")
	public String back() {

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_VOLUME_LIST_SHOWLIST;
	}

	/**
	 * 訂正
	 * @return 編集画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="VOLUME_EDIT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02E01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ02E01)
	@MethodAccess(accessCode="VOLUME_EDIT_SUBMIT")
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
		return TransitionConstants.Maintenance.REDIRECT_VOLUME_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="VOLUME_EDIT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		createDisplayValue(getData());

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02E01;
	}

	/**
	 * 号数マスタからデータを取得<br />
	 * データが存在しない場合は、エラーを返す
	 * @return 号数詳細Dto
	 */
	private DetailDto getData() {

		// IDが正常かチェック
		checkId(editForm, editForm.id);

		// 号数詳細Dto
		DetailDto dto;

		try {

			// 編集データの取得
			dto = volumeLogic.getVolumeDetail(Integer.parseInt(editForm.id));

		// データが存在しない場合エラー
		} catch (WNoResultException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// 取得したエンティティを返却
		return dto;

	}

	/**
	 * エンティティを画面表示のFormに設定する
	 * @param entity MSpecialエンティティ
	 */
	private void createDisplayValue(DetailDto entity) {

		// エンティティをフォームコピー
		Beans.copy(entity, editForm).execute();

		// 締切日時(年月日)のフォーマット
		editForm.deadlineDate = DateUtils.getDateStr(entity.deadlineDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		// 締切日時(時)のフォーマット
		editForm.deadlineHour = DateUtils.getDateStr(entity.deadlineDatetime, GourmetCareeConstants.HOUR_FORMAT);
		// 締切日時(分)のフォーマット
		editForm.deadlineMinute = DateUtils.getDateStr(entity.deadlineDatetime, GourmetCareeConstants.MINUTE_FORMAT);

		// 確定締切日時(年月日)のフォーマット
		editForm.fixedDeadlineDate = DateUtils.getDateStr(entity.fixedDeadlineDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		// 確定締切日時(時)のフォーマット
		editForm.fixedDeadlineHour = DateUtils.getDateStr(entity.fixedDeadlineDatetime, GourmetCareeConstants.HOUR_FORMAT);
		// 確定締切日時(分)のフォーマット
		editForm.fixedDeadlineMinute = DateUtils.getDateStr(entity.fixedDeadlineDatetime, GourmetCareeConstants.MINUTE_FORMAT);

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

	}

	/**
	 * 号数マスタを更新
	 */
	private void edit() {

		// 号数マスタエンティティ
		MVolume entity = new MVolume();

		// 号数マスタエンティティにフォームをコピー
		Beans.copy(editForm, entity).execute();

		// 号数マスタを更新する
		volumeService.update(entity);

		log.debug("号数マスタを編集しました。" + editForm);
	}
}