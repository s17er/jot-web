package com.gourmetcaree.common.role;

import java.io.Serializable;

/**
 * アクセスコード用Bean
 * アクセス制限用の設定XMLを読み込む際に使用する。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AccessCode implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1117208952069021849L;

	/** アクセスコード  */
	private String accessCode;

	/**
	 * accessCodeを取得します。
	 * @return accessCode
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 *  accessCodeを設定する。
	 * @param accessCode セットする accessCode
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

}
