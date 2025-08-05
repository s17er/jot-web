package com.gourmetcaree.db.common.property;

import java.util.Date;

import com.gourmetcaree.common.property.BaseProperty;

/**
 * 顧客ログイン情報の書き込みに使用するプロパティ
 * @author Takahiro Ando
 */
public class WriteCustomerLoginHistoryProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -673298503571704126L;

	/** 顧客ID */
	public int customerId;

	/** 基準日 */
	public Date lastLoginDatetime;

	/** ユーザエージェント */
	public String userAgent;

	/** リモートアドレス */
	public String remoteAddress;
}
