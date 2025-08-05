package com.gourmetcaree.shop.pc.shop.action.shop;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.property.SendMailProperty;
import com.gourmetcaree.shop.pc.shop.form.shop.EditForm;
import com.gourmetcaree.shop.pc.shop.form.shop.EditForm.SubMailDto;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * 登録情報の編集をするアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class EditAction extends ShopBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 登録情報編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	@Resource
	private CustomerSubMailService customerSubMailService;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, reset="resetForm", input=TransitionConstants.Shop.JSP_SPG01E01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 編集画面
	 */
	private String show() {
		MCustomerAccount mCustomerAccount = getCustomerData();
		Beans.copy(mCustomerAccount.mCustomer, editForm).execute();

		// ログインIDをセット
		editForm.loginId = mCustomerAccount.loginId;
		// 顧客アカウントマスタのバージョンをセット
		editForm.customerAccountVersion = mCustomerAccount.version;
		// 顧客マスタのバージョンをセット
		editForm.customerVersion = mCustomerAccount.mCustomer.version;

		// サブメールをセット
		convertSubMail(editForm, mCustomerAccount.mCustomer.mCustomerSubMailList);
		editForm.setSubMailEntryForm();
		checkUnReadMail();

		return TransitionConstants.Shop.JSP_SPG01E01;
	}

	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Shop.JSP_SPG01E01)
	public String conf() {
		editForm.setProcessFlgOk();
		if (!editForm.isNotAllPasswordInput()) {
			checkPassword();
		}
		checkUnReadMail();
		return TransitionConstants.Shop.JSP_SPG01E02;
	}

	/**
	 * 戻る
	 * @return 詳細画面の初期表示
	 */
	@Execute(validator = false, input = TransitionConstants.Shop.JSP_SPG01R01)
	public String back() {

		// 確認画面の表示メソッドへリダイレクト
		return TransitionConstants.Shop.REDIRECT_SHOP_INDEX_INDEX;
	}

	/**
	 * 入力画面へ戻る
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Shop.JSP_SPG01E01)
	public String collect() {
		editForm.setProcessFlgNg();
		checkUnReadMail();
		return TransitionConstants.Shop.JSP_SPG01E01;
	}

	/**
	 * パスワード変更
	 * @return
	 */
	@Execute(validator = true, input=TransitionConstants.Shop.JSP_SPG01E01)
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (editForm.processFlg == false) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 更新
		edit();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Shop.REDIRECT_SHOP_EDIT_COMP;
	}

	/**
	 * 完了画面表示
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {
		checkUnReadMail();
		// 完了画面へ遷移
		return TransitionConstants.Shop.JSP_SPG01E03;
	}

	/**
	 * パスワード更新
	 */
	private void edit() {

		try {
			MCustomerAccount entity = new MCustomerAccount();
			entity.mCustomer = new MCustomer();
			Beans.copy(editForm, entity.mCustomer).execute();
			entity.id = Integer.parseInt(userDto.userId);
			entity.mCustomer.id = userDto.customerId;
			entity.version = editForm.customerAccountVersion;
			entity.mCustomer.version = editForm.customerVersion;

			editForm.packSubMailDtoList();
			// サブメールの1つ目を親テーブルに設定しておく
			if (CollectionUtils.isNotEmpty(editForm.subMailDtoList)) {
				SubMailDto subMailDto = editForm.subMailDtoList.get(0);
				if (StringUtils.isNotEmpty(subMailDto.subMail)) {
					entity.mCustomer.subMail = subMailDto.subMail;
					entity.mCustomer.submailReceptionFlg = NumberUtils.toInt(subMailDto.submailReceptionFlg, 0);
				}
			} else {
				entity.mCustomer.subMail = "";
				entity.mCustomer.submailReceptionFlg = 0;
			}

			// パスワードのチェック
			if (!editForm.isNotAllPasswordInput()) {
				checkPassword();
			}

//			customerLogic.updateCustomerPassword(property);
			customerLogic.updateCustomerInfo(entity, editForm.newPassword);

			// サブメールの登録
			updateSubMail(entity.mCustomer.id);

			log.debug("パスワードを更新しました。" + editForm);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("パスワードを更新しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.debug("パスワードを更新しました。営業ID：" + userDto.masqueradeUserId + " " + editForm);
			}

			// 顧客マスタからデータを取得して、メールプロパティにセット
			SendMailProperty mailProperty = new SendMailProperty();
			mailProperty.mCustomer = customerService.findById(userDto.customerId);

			// メール送信
			sendMailLogic.sendChangePassword(mailProperty);
			log.debug("完了メールを送信しました。" + editForm);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.debug(String.format("完了メールを送信しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.debug("完了メールを送信しました。営業ID：" + userDto.masqueradeUserId + " " + editForm);
			}

		//データが取得できない場合はシステムエラー
		} catch (SNoResultException e) {
			callInternalGourmetCareeSystemError();
		}
	}

	/**
	 * サブメールエンティティに変換
	 * @return サブメールエンティティのリスト
	 */
	protected void updateSubMail(int customerId) {
		List<MCustomerSubMail> list = new ArrayList<>();
		for (SubMailDto dto : editForm.subMailDtoList) {
			list.add(Beans.createAndCopy(MCustomerSubMail.class, dto).execute());
		}
		customerSubMailService.deleteInsert(customerId, list);
	}


	/**
	 * 現在のパスワードのチェック。
	 * パスワードが違えば「{現在のパスワード}が正しくありません。」のエラーメッセージを表示
	 */
	private void checkPassword() {
		try {
			if (!customerLogic.isSamePassword(NumberUtils.toInt(userDto.userId), editForm.nowPassword)) {
				editForm.setProcessFlgNg();
				throw new ActionMessagesException("error.falseData", MessageResourcesUtil.getMessage("labels.nowPassword"));
			}
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("パスワードの取得に失敗しました。");
		}
	}

}
