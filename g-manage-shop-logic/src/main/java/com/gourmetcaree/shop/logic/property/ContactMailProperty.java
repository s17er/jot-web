package com.gourmetcaree.shop.logic.property;

import com.gourmetcaree.common.property.BaseProperty;

/**
 * 問い合わせメール受け渡しプロパティ
 * @author Aquarius
 *
 */
public class ContactMailProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8633574147467408779L;

	/** 件名 */
	public String subject;

	/** 本文 */
	public String body;

	/** 顧客名 */
	public String customerName;

	/** お名前 */
	public String contactName;

	/** メールアドレス */
	public String sender;

	/** 電話番号 */
	public String phoneNo;
}
