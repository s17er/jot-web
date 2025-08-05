package com.gourmetcaree.admin.pc.maintenance.action.special;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.special.InputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.SpecialLogic;
import com.gourmetcaree.admin.service.property.SpecialProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSpecial;
/**
 * 特集データ登録を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class InputAction extends SpecialBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 特集マスタのロジック */
	@Resource
	protected SpecialLogic specialLogic;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ03C01)
	@MethodAccess(accessCode="SPECIAL_INPUT_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.Maintenance.JSP_APJ03C01)
	@MethodAccess(accessCode="SPECIAL_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03C02;
	}

	/**
	 * 訂正
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SPECIAL_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03C01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ03C01)
	@MethodAccess(accessCode="SPECIAL_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 登録処理の呼び出し
		insert();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_SPECIAL_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="SPECIAL_INPUT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03C03;
	}

	/**
	 * 初期表示遷移用
	 * @return 入力画面のパス
	 */
	private String show() {

		// 入力画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03C01;
	}

	/**
	 * 登録処理
	 */
	private void insert() {

		// 特集プロパティに値をセット
		SpecialProperty property = new SpecialProperty();
		property.mSpecial = new MSpecial();
		// 特集情報をコピー
		Beans.copy(inputForm, property.mSpecial).execute();
//		Beans.copy(inputForm.areaCd, property.areaCd).execute();

		// エリアが選択されていればプロパティにセット
		if (inputForm.areaCd != null && !inputForm.areaCd.isEmpty()) {
			property.areaCd = GourmetCareeUtil.toIntegerList(inputForm.areaCd);
		}

		// 特集情報をマスタに新規登録する
		specialLogic.insertSpecial(property);

		log.debug("特集マスタを登録しました。" + inputForm);
	}

}