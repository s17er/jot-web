package com.gourmetcaree.shop.pc.passwordReissue.action.passwordReissue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;

import com.gourmetcaree.common.exception.OutOfDateException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.exception.RegisteredDataException;
import com.gourmetcaree.db.common.entity.TTemporaryRegistration;
import com.gourmetcaree.db.common.service.TemporaryRegistrationService;
import com.gourmetcaree.shop.logic.logic.CustomerLogic;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.pc.passwordReissue.form.passwordReissue.PasswordReissueBaseForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;

public abstract class PasswordReissueBaseAction extends PcShopAction{

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(PasswordReissueBaseAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** 仮登録サービス */
	@Resource
	protected TemporaryRegistrationService temporaryRegistrationService;

	/** 顧客マスタサービス */
	@Resource
	protected CustomerLogic customerLogic;

	/** 確認ボタン名 */
	protected static final String CONF_BTN_NAME = "conf";

	/** 登録ボタン名 */
	protected static final String SUBMIT_BTN_NAME = "submit";

	/**
	 * 会員登録画面の認証を行う<br />
	 * 認証に失敗した場合は、「ページがみつかりません」画面へ遷移する<br />
	 * 有効期限切れの場合は「有効期限切れ」画面へ遷移する
	 * @param entity 仮登録エンティティ
	 */
	protected void certifyAccess(TTemporaryRegistration entity, String limitKey) {

		// データが存在しない場合は「ページがみつかりません」画面へ遷移
		if (!temporaryRegistrationService.isAccessRecordExistsForCustomer(entity)) {
			throw new PageNotFoundException();
		}

		try {
			// 有効期限を取得
			int limitDay = Integer.parseInt(getCommonProperty(limitKey));
			// データが存在しない場合は「有効期限切れ」画面へ遷移
			if (!temporaryRegistrationService.canAccessDate(entity, limitDay)) {
				throw new OutOfDateException();
			}

		// 有効期限の設定が無い場合はチェックしない
		} catch (NumberFormatException e) {
			log.debug("有効期限無し");
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("有効期限無し。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			}
			return;
		}
	}

	/**
	 * 仮登録をアクセス済みに更新します。
	 */
	protected void updateAccess(PasswordReissueBaseForm form) {

		TTemporaryRegistration entity = new TTemporaryRegistration();
		// IDをセット
		entity.id = Integer.parseInt(form.id);
		// バージョンをセット
		entity.version = form.version;

		try {
			// アクセス済みに更新
			temporaryRegistrationService.updateAlreadyeAccess(entity);

		// 楽観的排他制御エラーの場合はすでに変更されているため、処理不可
		} catch (SOptimisticLockException e) {
			throw new RegisteredDataException("すでに登録されているため仮登録はできません。");
		}
	}
}
