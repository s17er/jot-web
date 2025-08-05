package com.gourmetcaree.admin.pc.maintenance.action.sales;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.sales.InputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.SalesLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.exception.AgencyAuthLevelException;
import com.gourmetcaree.db.common.exception.AuthLevelException;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 営業担当者登録アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class InputAction extends SalesBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 営業担当者ロジック */
	@Resource
	protected SalesLogic salesLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ04C01)
	@MethodAccess(accessCode="SALES_INPUT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Maintenance.JSP_APJ04C01)
	@MethodAccess(accessCode="SALES_INPUT_CONF")
	public String conf() {

		// ログインIDの重複チェック、会社の存在チェック
		try {
			salesLogic.checkInputData(inputForm.id, inputForm.loginId, inputForm.companyId, inputForm.authorityCd);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.noCompanyData");
		} catch (AgencyAuthLevelException e) {
			throw new ActionMessagesException("errors.app.agencyAuthLevel");
		} catch(AuthLevelException e) {
			throw new ActionMessagesException("errors.app.authLevel");
		}

		// 会社名、権限、サブメール受信可否を名称へ変換
		convertToName(inputForm);

		// パスワードを表示用に変換
		convertPassword();

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04C02;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// サブメイル受信フラグがブランクの場合は、初期値をセット
		if (StringUtils.isBlank(inputForm.submailReceptionFlg)) {
			inputForm.submailReceptionFlg = String.valueOf(MTypeConstants.SubmailReceptionFlg.NOT_RECEIVE);
		}

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04C01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Maintenance.JSP_APJ04C01)
	@MethodAccess(accessCode="SALES_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// ログインIDの重複チェック、会社の存在チェック
		try {
			salesLogic.checkInputData(inputForm.id, inputForm.loginId, inputForm.companyId, inputForm.authorityCd);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.noCompanyData");
		} catch (AgencyAuthLevelException e) {
			throw new ActionMessagesException("errors.app.agencyAuthLevel");
		} catch(AuthLevelException e) {
			throw new ActionMessagesException("errors.app.authLevel");
		}

		// 登録データ生成
		MSales entity = new MSales();
		convertToEntity(entity);
		entity.registrationDatetime = new Timestamp(new Date().getTime());

		// 登録
		salesService.insert(entity);

		log.debug("営業担当者をINSERTしました");

		return TransitionConstants.Maintenance.REDIRECT_SALES_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="SALES_INPUT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04C03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// サブメール受信フラグのみチェックする
		inputForm.submailReceptionFlg = "0";

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04C01;
	}

	/**
	 * 確認画面表示用にパスワードを変換
	 */
	private void convertPassword() {

		// パスワードを'*'に変換する
		Pattern pat = Pattern.compile(GourmetCareeConstants.MASK_ALPHANUM, Pattern.CASE_INSENSITIVE);
		Matcher match = pat.matcher(inputForm.password);
		inputForm.dispPassword = match.replaceAll(GourmetCareeConstants.MASK_FREE_CHAR);
	}

	/**
	 * フォームからエンティティへデータをコピー
	 * @param entity
	 */
	private void convertToEntity(MSales entity) {

		// フォームからエンティティへデータをコピー
		Beans.copy(inputForm, entity).dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "registrationDatetime").excludes("password").excludesNull().execute();
		entity.password = DigestUtil.createDigest(inputForm.password);
		entity.displayOrder = GourmetCareeConstants.DEFAULT_DISP_NO;

	}

}