package com.gourmetcaree.admin.service.property;

import java.io.Serializable;

/**
 *
 * IP電話応募のCSVのデータを受け渡しするクラス
 *
 */
public class IpPhoneHistoryCsvProperty extends BaseProperty implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3220294305573919216L;

	/** 出力パス */
	public String pass;

	/** ファイル名 */
	public String fileName;

	/** エンコード */
	public String encode;

	/** 出力カウント */
	public int count;
}
