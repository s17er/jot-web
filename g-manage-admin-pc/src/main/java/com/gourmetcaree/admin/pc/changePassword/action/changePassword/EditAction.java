package com.gourmetcaree.admin.pc.changePassword.action.changePassword;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.changePassword.form.changePassword.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.service.SalesService;

/**
 * パスワード編集アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class EditAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** 営業担当者マスタサービス */
	@Resource
	protected SalesService salesService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", input = TransitionConstants.ChangePassword.JSP_APM01E01)
	@MethodAccess(accessCode="CHANGEPASSWORD_EDIT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate",input = TransitionConstants.ChangePassword.JSP_APM01E01)
	@MethodAccess(accessCode="CHANGEPASSWORD_EDIT_CONF")
	public String conf() {

		// パスワードを確認画面表示用に変換
		editForm.dispPassword = GourmetCareeUtil.convertPassword(editForm.password);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.ChangePassword.JSP_APM01E02;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CHANGEPASSWORD_EDIT_CORRECT")
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.ChangePassword.JSP_APM01E01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.ChangePassword.JSP_APM01E01)
	@MethodAccess(accessCode="CHANGEPASSWORD_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 登録処理
		doUpdate();

		log.debug("パスワードを編集しました。");

		return TransitionConstants.ChangePassword.REDIRECT_CHANGEPASSWORD_EDIT_COMP;
	}

	/**
	 * 更新処理
	 */
	private void doUpdate() {

		// 更新データ生成
		MSales updateEntity = new MSales();
		createUpdataData(updateEntity);

		// 更新
		salesService.update(updateEntity);

	}

	/**
	 * 更新データを生成
	 */
	private void createUpdataData(MSales updateEntity) {

		updateEntity.id = NumberUtils.toInt(editForm.id);
		updateEntity.password = DigestUtil.createDigest(editForm.password);
		updateEntity.version = editForm.version;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CHANGEPASSWORD_EDIT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.ChangePassword.JSP_APM01E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// 表示データをセット
		convertDispData();

		// 登録画面へ遷移
		return TransitionConstants.ChangePassword.JSP_APM01E01;
	}

	/**
	 * 表示データをフォームにセット
	 */
	private void convertDispData() {

		try {
			MSales entity = salesService.findById(NumberUtils.toInt(userDto.userId));

			Beans.copy(entity, editForm).excludes("password").execute();
			if (StringUtils.isNotEmpty(entity.mobileNo1)) {
				editForm.mobileNo = entity.mobileNo1 + "-" + entity.mobileNo2 + "-" + entity.mobileNo3;
			}

		} catch (SNoResultException e) {
			editForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

}