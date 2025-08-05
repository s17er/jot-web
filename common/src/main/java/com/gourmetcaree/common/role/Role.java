package com.gourmetcaree.common.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ロール情報をもつクラス
 * アクセス制限用の設定XMLを読み込む際に使用する。
 * @author Takahiro Ando
 * @version 1.0
 */
public class Role implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1117208952069021849L;

	/** バージョン */
	private String version;

	/** 説明 */
	private String description;

	/** 権限レベルオブジェクト保持リスト */
	private List<AuthLevel> authLevels = new ArrayList<AuthLevel>();


	/**
	 * 権限レベルオブジェクト保持リストに要素を追加する。
	 * @param authLevel
	 */
	public void addAuthLevels(AuthLevel authLevel) {
		authLevels.add(authLevel);
	}


	/**
	 * versionを取得します。
	 * @return version
	 */
	public String getVersion() {
		return version;
	}


	/**
	 *  versionを設定する。
	 * @param version セットする version
	 */
	public void setVersion(String version) {
		this.version = version;
	}


	/**
	 * descriptionを取得します。
	 * @return description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 *  descriptionを設定する。
	 * @param description セットする description
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * authLevelsを取得します。
	 * @return authLevels
	 */
	public List<AuthLevel> getAuthLevels() {
		return authLevels;
	}


	/**
	 *  authLevelsを設定する。
	 * @param authLevels セットする authLevels
	 */
	public void setAuthLevels(List<AuthLevel> authLevels) {
		this.authLevels = authLevels;
	}

}
