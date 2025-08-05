package com.gourmetcaree.common.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 権限レベル情報を持つクラス
 * アクセス制限用の設定XMLを読み込む際に使用する。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AuthLevel implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1117208952069021849L;

	/** 権限レベルコード  */
	private String authLevelCode;

	/** 権限レベル名称 */
	private String authLevelName;

	/** アクセスコード保持リスト */
	private List<AccessCode> accessCodes = new ArrayList<AccessCode>();

	/**
	 * アクセスコード保持リストに要素を追加する。
	 * @param accessCode
	 */
	public void addAccessCodes(AccessCode accessCode) {
		accessCodes.add(accessCode);
	}

	/**
	 * authLevelCodeを取得します。
	 * @return authLevelCode
	 */
	public String getAuthLevelCode() {
		return authLevelCode;
	}

	/**
	 *  authLevelCodeを設定する。
	 * @param authLevelCode セットする authLevelCode
	 */
	public void setAuthLevelCode(String authLevelCode) {
		this.authLevelCode = authLevelCode;
	}

	/**
	 * authLevelNameを取得します。
	 * @return authLevelName
	 */
	public String getAuthLevelName() {
		return authLevelName;
	}

	/**
	 *  authLevelNameを設定する。
	 * @param authLevelName セットする authLevelName
	 */
	public void setAuthLevelName(String authLevelName) {
		this.authLevelName = authLevelName;
	}

	/**
	 * accessCodesを取得します。
	 * @return accessCodes
	 */
	public List<AccessCode> getAccessCodes() {
		return accessCodes;
	}

	/**
	 *  accessCodesを設定する。
	 * @param accessCodes セットする accessCodes
	 */
	public void setAccessCodes(List<AccessCode> accessCodes) {
		this.accessCodes = accessCodes;
	}

}
