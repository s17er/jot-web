package com.gourmetcaree.shop.pc.passwordReissue.action.passwordReissue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.exception.LoginIdDuplicateException;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TTemporaryRegistration;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.shop.logic.dto.CustomerAccountDto;
import com.gourmetcaree.shop.logic.property.SendMailProperty;
import com.gourmetcaree.shop.pc.passwordReissue.form.passwordReissue.InputForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

public class InputAction extends PasswordReissueBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@ActionForm
	@Resource
	private InputForm inputForm;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, reset="resetForm", input = TransitionConstants.PasswordReissue.JSP_SPH01C01)
	public String index() {
		return show();
	}


	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {
		userDto = null;
		return TransitionConstants.PasswordReissue.JSP_SPH01C01;
	}

	/**
	 * 登録完了
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = true, reset="resetForm", input = TransitionConstants.PasswordReissue.JSP_SPH01C01)
	public String submit() {
		// パラメータが空の場合はエラー(アドレス直打ちを防ぐ)
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.loginId);

		// 入力チェック
		conf();

		// 登録処理の呼び出し
		insert();
		log.debug("パスワード変更の仮登録を行いました。");

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("パスワード変更の仮登録を行いました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// メール送信
		sendMail();


		return TransitionConstants.PasswordReissue.REDIRECT_PASSWORDREISSUE_INPUT_COMP;
	}

	/**
	 * 完了画面表示
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm=true)
	public String comp() {
		return TransitionConstants.PasswordReissue.JSP_SPH01C03;
	}

	/**
	 * データが正しいかチェックする
	 * @param errors エラーの搭載
	 */
	private void conf() {
		try {
			CustomerAccountDto dto = customerLogic.getCustomerAccountDto(inputForm.loginId);
			inputForm.mail = dto.mainMail;
			inputForm.customerId = dto.customerId;
			inputForm.areaCd = dto.areaCd;


		} catch (LoginIdDuplicateException e) {
			// 「入力された{ログインID}は、存在しない{ログインID}です。」
			throw new ActionMessagesException("errors.app.notExistData",
					MessageResourcesUtil.getMessage("labels.customerLoginId"),
			MessageResourcesUtil.getMessage("labels.customerLoginId"));
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.notExistData",
					MessageResourcesUtil.getMessage("labels.customerLoginId"),
					MessageResourcesUtil.getMessage("labels.customerLoginId"));
		}
	}

	private void insert() {
		// 仮登録マスタエンティティ
		TTemporaryRegistration entity = new TTemporaryRegistration();

		// 仮登録マスタエンティティにフォームをコピー(顧客ID、エリアコード、メールアドレス)
		Beans.copy(inputForm, entity).execute();
		entity.customerLoginId = inputForm.loginId;
		// 端末区分
		entity.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
		// 仮登録区分:顧客用パスワード変更
		entity.temporaryRegistrationKbn = MTypeConstants.TemporaryRegistrationKbn.CUSTOMER_PASSWORD_REISSUE;

		// データ登録
		temporaryRegistrationService.insertTTemporaryRegistration(entity);
		Beans.copy(entity, inputForm).execute();
	}

	/**
	 * メール送信処理
	 */
	private void sendMail() {

		// メール送信プロパティに値をセット
		SendMailProperty property = new SendMailProperty();
		property.tTemporaryRegistration = new TTemporaryRegistration();
		Beans.copy(inputForm, property.tTemporaryRegistration).execute();

		// メール送信処理
		sendMailLogic.sendPasswordReissueMailConf(property);
	}
}
