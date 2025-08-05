package com.gourmetcaree.db.common.property;

import java.util.Date;

import com.gourmetcaree.common.property.BaseProperty;

/**
 * ログイン情報の書き込みに使用するプロパティ
 * @author Takahiro Ando
 */
public class WriteLoginHistoryProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4492630707320682367L;

	/** 会員ID */
	public int memberId;

	/** 基準日 */
	public Date lastLoginDatetime;

	/** ユーザエージェント */
	public String userAgent;

	/** リモートアドレス */
	public String remoteAddress;
}
