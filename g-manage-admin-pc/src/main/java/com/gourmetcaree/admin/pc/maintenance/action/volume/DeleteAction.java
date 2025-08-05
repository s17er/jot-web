package com.gourmetcaree.admin.pc.maintenance.action.volume;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.volume.DeleteForm;
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
 * 号数データの削除を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DeleteAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);
	/** フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/** 号数のロジック */
	@Resource
	protected VolumeLogic volumeLogic;

	/** 号数のサービス */
	@Resource
	protected VolumeService volumeService;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ02D02)
	@MethodAccess(accessCode="VOLUME_DELETE_INDEX")
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
	@MethodAccess(accessCode="VOLUME_DELETE_BACK")
	public String back() {

		// プロセスフラグを未確認に設定
		deleteForm.setProcessFlgNg();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_VOLUME_LIST_SHOWLIST;
	}

	/**
	 * 削除
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ02D02)
	@MethodAccess(accessCode="VOLUME_DELETE_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!deleteForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		checkArgsNull(NO_BLANK_FLG_NG, deleteForm.id, deleteForm.version);


		// 削除処理
		delete();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_VOLUME_DELETE_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="VOLUME_DELETE_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02D03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// DBから取得したデータを表示用に変換
		createDisplayValue(getData());

		// プロセスフラグを確認済みに設定
		deleteForm.setProcessFlgOk();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02D02;
	}

	/**
	 * 号数マスタからデータを取得<br />
	 * データが存在しない場合は、エラーを返す
	 * @return 号数詳細Dto
	 */
	private DetailDto getData() {

		// IDが正常かチェック
		checkId(deleteForm, deleteForm.id);

		// 号数詳細Dto
		DetailDto dto;

		try {

			// 編集データの取得
			dto = volumeLogic.getVolumeDetail(Integer.parseInt(deleteForm.id));

		// データが存在しない場合エラー
		} catch (WNoResultException e) {

			// 画面表示をしない
			deleteForm.setExistDataFlgNg();

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
		Beans.copy(entity, deleteForm).execute();

		// 締切日時のフォーマット
		deleteForm.deadlineDatetimeStr = DateUtils.getDateStr(entity.deadlineDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

		// 確定締切日時のフォーマット
		deleteForm.fixedDeadlineDatetimeStr = DateUtils.getDateStr(entity.fixedDeadlineDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

		// 掲載開始日時のフォーマット
		deleteForm.postStartDatetimeStr = DateUtils.getDateStr(entity.postStartDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

		// 掲載終了日時のフォーマット
		deleteForm.postEndDatetimeStr = DateUtils.getDateStr(entity.postEndDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

	}

	/**
	 * 号数マスタを論理削除
	 */
	private void delete() {

		// 号数マスタエンティティ
		MVolume entity = new MVolume();

		entity.id = NumberUtils.toInt(deleteForm.id);
		entity.version = NumberUtils.toLong(deleteForm.version);

		// 号数マスタから論理削除
		volumeService.logicalDelete(entity);

		log.debug("号数マスタを削除しました。" + deleteForm);
	}
}