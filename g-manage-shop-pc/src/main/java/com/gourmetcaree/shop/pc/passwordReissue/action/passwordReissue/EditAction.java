package com.gourmetcaree.shop.pc.passwordReissue.action.passwordReissue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.exception.AlreadyChangeDateException;
import com.gourmetcaree.common.util.CookieUtil;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.entity.TTemporaryRegistration;
import com.gourmetcaree.db.common.service.CustomerAccountService;
import com.gourmetcaree.shop.logic.property.SendMailProperty;
import com.gourmetcaree.shop.pc.passwordReissue.form.passwordReissue.EditForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

public class EditAction extends PasswordReissueBaseAction{

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@Resource
	@ActionForm
	private EditForm editForm;

	/** 顧客アカウントサービス */
	@Resource
	private CustomerAccountService customerAccountService;

	/**
	 * 初期表示
	 * @return 変更前ログインメールアドレス確認画面
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}/{accessCd}", input = com.gourmetcaree.shop.pc.sys.constants.TransitionConstants.ErrorTransition.JSP_PAGE_NOT_FOUND_ERROR)
	public String index() {
		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id, editForm.accessCd);

		// IDが不正かどうかチェック
		checkId(editForm, editForm.id);

		// セッション情報をリセット
		userDto = null;

		return show();
	}

	/**
	 * 確認
	 * @return sslIndexアドレス
	 */
	@Execute(validator = false)
	public String conf() {

		// 押されたボタン名を保持
		editForm.btnName = CONF_BTN_NAME;

		// 登録画面表示メソッドへ遷移
		return TransitionConstants.PasswordReissue.REDIRECT_PASSWORDREISSUE_EDIT_SSLINDEX;
	}

	/**
	 * 確認用メソッド
	 * @return メールアドレス入力画面
	 */
	@Execute(validator = true, validate = "validateLoginMail", input = TransitionConstants.PasswordReissue.JSP_SPI01C01_1)
	public String checkConf() {

		// パラメータが空の場合はエラー(アドレス直打ちから遷移を防ぐ)
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.mail);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// メールアドレス入力画面へ遷移
		return TransitionConstants.PasswordReissue.JSP_SPI01C01_2;
	}

	/**
	 * 登録
	 * @return sslIndexアドレス
	 */
	@Execute(validator = false)
	public String submit() {

		// 押されたボタン名を保持
		editForm.btnName = SUBMIT_BTN_NAME;

		// 登録画面表示メソッドへ遷移
		return TransitionConstants.PasswordReissue.REDIRECT_PASSWORDREISSUE_EDIT_SSLINDEX;
	}

	/**
	 * 登録完了
	 * @return 完了へリダイレクト
	 */
	@Execute(validator=true, validate = "validatePassword", input=TransitionConstants.PasswordReissue.JSP_SPI01C01_2)
	public String executeSubmit() {

		// パラメータが空の場合はエラー(アドレス直打ちを防ぐ)
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.mail);

		// 変更画面で入力したログインIDがすでに変更されている場合はエラー
		if (!customerAccountService.isCustomerExists(editForm.customerId, editForm.customerLoginId)) {
			// データがすでに登録されている場合はエラー
			throw new AlreadyChangeDateException("入力したログインIDがすでに変更されています。");
		}

		// 変更処理の呼び出し
		update();
		log.debug("パスワード変更を行いました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("パスワード変更を行いました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// メール送信
		sendMail();
		log.debug("パスワード変更完了メールを送信しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("パスワード変更完了メールを送信しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// 仮登録の更新処理
		updateAccess(editForm);
		log.debug("仮登録をアクセス済みに更新しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("仮登録をアクセス済みに更新しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// 自動ログイン設定があればクリア
		CookieUtil.deleteLoginInfo(request, response);

		// 完了メソッドへリダイレクト
		return TransitionConstants.PasswordReissue.REDIRECT_PASSWORDREISSUE_EDIT_COMP;
	}

	/**
	 * 完了画面表示
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.PasswordReissue.JSP_SPI01C03;
	}

	/**
	 * 初期表示遷移用
	 * @return ログインID確認画面のパス
	 */
	private String show() {
		// プロパティにセット
		TTemporaryRegistration entity = new TTemporaryRegistration();
		Beans.copy(editForm, entity).execute();

		// ログインIDの認証を行う
		certifyAccess(entity, "gc.passwordReissue.limitDay");
		log.debug("アクセスコードの認証に成功しました。");

		// 変更画面で入力したログインIDがすでに変更されている場合はエラー
		if (!customerAccountService.isCustomerExists(entity.customerId, entity.customerLoginId)) {
			throw new AlreadyChangeDateException("入力したログインIDがすでに変更されています。");
		}

		Beans.copy(entity, editForm).execute();

		editForm.setExistDataFlgOk();

		return TransitionConstants.PasswordReissue.REDIRECT_PASSWORDREISSUE_EDIT_SSLINDEX;
	}

	/**
	 * 初期表示(https対策用)
	 * @return ログインメールアドレス入力画面
	 */
	@Execute(validator = false, input = TransitionConstants.ErrorTransition.JSP_PAGE_NOT_FOUND_ERROR)
	public String sslIndex() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id, editForm.accessCd);

		// 確認の場合
		if (CONF_BTN_NAME.equals(editForm.btnName)) {
			editForm.btnName = "";
			// チェックメソッドへ遷移
			return "/passwordReissue/edit/checkConf";

		// 登録の場合
		} else if (SUBMIT_BTN_NAME.equals(editForm.btnName)) {
			editForm.btnName = "";
			// 登録メソッドへ遷移
			return "/passwordReissue/edit/executeSubmit";
		}

		editForm.btnName = "";

		// ログインメールアドレス入力画面へ遷移
		return TransitionConstants.PasswordReissue.JSP_SPI01C01_1;
	}

	/**
	 * パスワードの変更
	 */
	private void update() {

		MCustomerAccount entity = customerAccountService.getMCustomerAccountByCustomerId(editForm.customerId);

		// ログインIDをセット
		entity.password = editForm.password;

		// 更新
		customerAccountService.updatePassword(entity);
	}

	/**
	 * メール送信処理
	 */
	private void sendMail() {

		// メール送信プロパティに値をセット
		SendMailProperty property = new SendMailProperty();
		TTemporaryRegistration entity = new TTemporaryRegistration();
		Beans.copy(editForm, entity).execute();

		property.tTemporaryRegistration = entity;
		// メール送信処理
		sendMailLogic.sendPasswordReissueMailComp(property);
	}

}
