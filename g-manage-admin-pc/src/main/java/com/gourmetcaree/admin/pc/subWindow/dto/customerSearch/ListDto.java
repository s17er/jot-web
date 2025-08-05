package com.gourmetcaree.admin.pc.subWindow.dto.customerSearch;

import java.io.Serializable;

/**
 * 顧客データ検索一覧で使用するDto
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class ListDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 9029265263519545019L;

	/** 顧客ID */
	public Integer customerId;

	/** 顧客名 */
	public String customerName;

	/** 担当者名 */
	public String contactName;

	/** エリア */
	public String area;

	/** 電話番号 */
	public String phoneNo;

	/** メールアドレス */
	public String mailAddress;

	/** サブメールアドレス */
	public String subMail;

	/** 担当会社名 */
	public String companyName;

	/** 営業担当者名 */
	public String salesName;
}
