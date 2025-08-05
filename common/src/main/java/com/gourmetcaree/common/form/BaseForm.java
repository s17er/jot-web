package com.gourmetcaree.common.form;

import java.io.Serializable;
import java.util.Properties;

import org.seasar.framework.util.ResourceUtil;

/**
 * 共通のActionFormです。
 * ActionFormはこのクラスを継承してください。
 * @author Takahiro Ando
 * @version 1.0
 */
abstract public class BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1407773868907360775L;

	/** 画面ID */
	public String displayId = "";

	/** 画面表示の可否フラグ trueであれば表示：falseであれば非表示 */
	public boolean existDataFlg = true;

	/** プロセスフラグ */
	public boolean processFlg;

	/** 画面に保持するId */
	public String hiddenId;

	/**
	 * 画面表示可否フラグをtrueにセット
	 */
	public void setExistDataFlgOk() {
		existDataFlg = true;
	}

	/**
	 * 画面表示可否フラグをfalseにセット
	 */
	public void setExistDataFlgNg() {
		existDataFlg = false;
	}

	/**
	 * プロセスフラグをtrueにセット
	 */
	public void setProcessFlgOk() {
		processFlg = true;
	}

	/**
	 * プロセスフラグをfalseにセット
	 */
	public void setProcessFlgNg() {
		processFlg = false;
	}

	/**
	 * 共通プロパティファイルを取得します。
	 * @return gourmetcaree.propreties
	 */
	protected Properties getCommonProperty() {
		return ResourceUtil.getProperties("gourmetcaree.properties");
	}


	/**
	 * Baseフォームをリセットします。
	 */
	protected void resetBaseForm() {

		// 画面ID
		displayId = null;

		// 画面表示の可否フラグ trueであれば表示：falseであれば非表示
		existDataFlg = true;

		// プロセスフラグ
		processFlg = false;

		// 画面に保持するId
		hiddenId = null;
	}
}
