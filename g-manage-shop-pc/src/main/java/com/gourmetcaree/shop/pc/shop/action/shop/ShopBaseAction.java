package com.gourmetcaree.shop.pc.shop.action.shop;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.common.exception.InternalGourmetCareeSystemErrorException;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.shop.logic.logic.CustomerLogic;
import com.gourmetcaree.shop.pc.shop.form.shop.EditForm.SubMailDto;
import com.gourmetcaree.shop.pc.shop.form.shop.ShopForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

/**
 * 登録情報のBaseアクションクラスです。
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class ShopBaseAction extends PcShopAction {

	/** 顧客マスタのロジック */
	@Resource
	protected CustomerLogic customerLogic;

	/**
	 * 顧客データを検索します。
	 * @return 顧客アカウントエンティティ
	 */
	protected MCustomerAccount getCustomerData() {

		MCustomerAccount mCustomerAccount = new MCustomerAccount();
		try {
			// 顧客データを取得して返却
			mCustomerAccount = customerLogic.getCustomerAccontData(Integer.parseInt(userDto.userId));

			// 顧客データが存在しない場合はシステムエラー
		} catch (WNoResultException e) {
			callInternalGourmetCareeSystemError();
		}
		return mCustomerAccount;
	}

	/**
	 * システムエラーを投げます。
	 */
	protected void callInternalGourmetCareeSystemError() {
		throw new InternalGourmetCareeSystemErrorException("顧客情報がありません。");
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.userInfoInstance();
	}


	/**
	 * サブメールアドレスをフォームにセット
	 * @param form
	 * @param property
	 */
	protected void convertSubMail(ShopForm form, List<MCustomerSubMail> customerSubMailList) {
		for (MCustomerSubMail subMail : customerSubMailList) {
			form.subMailDtoList.add(Beans.createAndCopy(SubMailDto.class, subMail).execute());
		}
	}
}
