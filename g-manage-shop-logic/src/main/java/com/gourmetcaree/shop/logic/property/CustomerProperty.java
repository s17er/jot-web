package com.gourmetcaree.shop.logic.property;

import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.common.property.BaseProperty;

/**
 *
 * 顧客データを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class CustomerProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5932720166053556362L;

	/** 顧客アカウントマスタエンティティ */
	public MCustomerAccount mCustomerAccount;

	/** パスワード */
	public String password;
}
