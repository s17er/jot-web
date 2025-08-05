package com.gourmetcaree.shop.logic.dto.mai;

import java.io.Serializable;

import com.gourmetcaree.common.dto.FooterMaiDto;

/**
 * パスワード変更変更完了メール送信Dto
 * @author Makoto Otani
 * @version 1.0
 */
public class PasswordReissueMailCompMaiDto extends FooterMaiDto implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7645379910640495820L;

	/** ログインURL */
	private String loginUrl;

	/** PC/モバイル判別フラグ */
	private boolean mobileMailFlg;

	/**
	 * ログインURLを取得します。
	 * @return ログインURL
	 */
	public String getLoginUrl() {
	    return loginUrl;
	}

	/**
	 * ログインURLを設定します。
	 * @param loginUrl ログインURL
	 */
	public void setLoginUrl(String loginUrl) {
	    this.loginUrl = loginUrl;
	}

	/**
	 * PC/モバイル判別フラグを取得します。
	 * @return PC/モバイル判別フラグ
	 */
	public boolean getMobileMailFlg() {
		return mobileMailFlg;
	}

	/**
	 * PC/モバイル判別フラグを設定します。
	 * @param mobileMailFlg PC/モバイル判別フラグ
	 */
	public void setMobileMailFlg(boolean mobileMailFlg) {
		this.mobileMailFlg = mobileMailFlg;
	}
}
