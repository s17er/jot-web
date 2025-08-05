package com.gourmetcaree.admin.pc.maintenance.action.volume;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.maintenance.form.volume.InputForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.service.VolumeService;

/**
 *
 *号数データ登録を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel = { ManageAuthLevel.ADMIN })
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 号数マスタのサービス */
	@Resource
	protected VolumeService volumeService;

	/** 名称取得のロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * 初期表示
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ02C01)
	@MethodAccess(accessCode = "VOLUME_INPUT_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 確認
	 *
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Maintenance.JSP_APJ02C01)
	@MethodAccess(accessCode = "VOLUME_INPUT_CONF")
	public String conf() {

		// 画面表示の値を取得
		createDisplayValue();

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02C02;
	}

	/**
	 * 訂正
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "VOLUME_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02C01;
	}

	/**
	 * 登録
	 *
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ02C01)
	@MethodAccess(accessCode = "VOLUME_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 登録処理の呼び出し
		insert();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_VOLUME_INPUT_COMP;
	}

	/**
	 * 完了
	 *
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "VOLUME_INPUT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02C03;
	}

	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {

		// 入力画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02C01;
	}

	/**
	 * 登録処理
	 */
	private void insert() {

		// 号数マスタエンティティ
		MVolume entity = new MVolume();

		// 号数マスタエンティティにフォームをコピー
		Beans.copy(inputForm, entity).execute();

		try {

			// 号数マスタに新規登録する
			volumeService.inputMVolume(entity);

		} catch (ExistDataException e) {

			// エリアが取得できない場合は、エラー「同じ{号数データ}が登録された可能性があります。メニューからやり直してください。」
			throw new ActionMessagesException("errors.ExistDataError",
					MessageResourcesUtil.getMessage("msg.app.volume"));

		}

		log.debug("号数マスタを登録しました。" + inputForm);
	}

	/**
	 * エンティティを画面表示のFormに設定する
	 */
	private void createDisplayValue() {

		// エリアマスタから名称を取得
		inputForm.areaName = valueToNameConvertLogic.convertToAreaName(new String[]{inputForm.areaCd});

		// エリアが取得できない場合はエラー
		if (StringUtil.isEmpty(inputForm.areaName)) {
			// 「データが変更された可能性があります。メニューからやり直してください。」
			throw new ActionMessagesException("errors.optimisticLockException");
		}

		// 次号数を取得して、フォームにセット
		inputForm.volume = String.valueOf(volumeService.getVolumeByAreaCd(Integer.parseInt(inputForm.areaCd)));

	}
}