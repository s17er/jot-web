package com.gourmetcaree.shop.pc.shop.action.shop;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.shop.pc.shop.form.shop.IndexForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * 登録情報詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class IndexAction extends ShopBaseAction {

	/** 登録情報詳細フォーム */
	@ActionForm
	@Resource
	protected IndexForm indexForm;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Shop.JSP_SPG01R01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 詳細画面
	 */
	private String show() {

		// 顧客データを取得
		MCustomerAccount mCustomerAccount = getCustomerData();

		// データのコピー
		Beans.copy(mCustomerAccount.mCustomer, indexForm).execute();
		// ログインIDをセット
		indexForm.loginId = mCustomerAccount.loginId;
		// 顧客アカウントマスタのバージョンをセット
		indexForm.version = mCustomerAccount.version;

		// サブメールの設定
		convertSubMail(indexForm, mCustomerAccount.mCustomer.mCustomerSubMailList);
		indexForm.setSubMailEntryForm();
		checkUnReadMail();

		// 詳細画面
		return TransitionConstants.Shop.JSP_SPG01R01;
	}

}
