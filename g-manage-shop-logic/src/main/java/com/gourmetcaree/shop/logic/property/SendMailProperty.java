package com.gourmetcaree.shop.logic.property;

import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.TTemporaryRegistration;

import com.gourmetcaree.db.common.entity.TApplicationTest;
import com.gourmetcaree.common.property.BaseProperty;

/**
 *
 * メール送信データを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class SendMailProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5562092269119276661L;

	/** 応募テストエンティティ */
	public TApplicationTest tApplicationTest;

	/** 顧客マスタエンティティ */
	public MCustomer mCustomer;

	/** 顧客名 */
	public String customerName;

	/** 仮登録エンティティ */
	public TTemporaryRegistration tTemporaryRegistration;
}
