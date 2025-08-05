package com.gourmetcaree.admin.pc.maintenance.action.company;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.company.InputForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.CompanyLogic;
import com.gourmetcaree.admin.service.property.CompanyProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.entity.MCompany;

/**
 *
 * 会社登録を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 名称取得のロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 会社マスタのロジック */
	@Resource
	protected CompanyLogic companyLogic;

	/**
	 * 初期表示
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ05C01)
	@MethodAccess(accessCode = "COMPANY_INPUT_INDEX")
	public String index() {

		return show();

	}

	/**
	 * 確認
	 *
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.Maintenance.JSP_APJ05C01)
	@MethodAccess(accessCode = "COMPANY_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05C02;
	}

	/**
	 * 訂正
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "COMPANY_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05C01;
	}

	/**
	 * 登録
	 *
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ05C01)
	@MethodAccess(accessCode = "COMPANY_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 登録処理の呼び出し
		insert();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_COMPANY_INPUT_COMP;
	}

	/**
	 * 完了
	 *
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "COMPANY_INPUT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05C03;
	}

	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {
		if(inputForm.areaCd != null && inputForm.areaCd.isEmpty()) {
			inputForm.areaCd.add(String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}
		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05C01;
	}

	/**
	 * 登録処理
	 */
	private void insert() {

		// 会社マスタエンティティ
		MCompany entity = new MCompany();

		// 会社マスタエンティティにフォームをコピー
		Beans.copy(inputForm, entity).dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "registrationDatetime").execute();

		// 会社管理プロパティに値をセット
		CompanyProperty property = new CompanyProperty();
		// 会社マスタ
		property.mCompany = entity;
		// エリアコード
		property.areaCd = inputForm.areaCd;

		// 会社データを登録
		companyLogic.insertCompay(property);

		log.debug("会社マスタを登録しました。" + inputForm);
	}

}