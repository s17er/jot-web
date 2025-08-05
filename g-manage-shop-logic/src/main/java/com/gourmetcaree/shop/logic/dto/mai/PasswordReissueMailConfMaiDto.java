package com.gourmetcaree.shop.logic.dto.mai;

import java.io.Serializable;

import com.gourmetcaree.common.dto.FooterMaiDto;

public class PasswordReissueMailConfMaiDto extends FooterMaiDto implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6982731434347998734L;

	/** パスワード変更画面のパス */
	private String passwordReissuePagePath;

	/** PC/モバイル判別フラグ */
	private boolean mobileMailFlg;

	/**
	 * パスワード変更画面のパスを取得します。
	 * @return パスワード変更画面のパス
	 */
	public String getPasswordReissuePagePath() {
		return passwordReissuePagePath;
	}

	/**
	 * パスワード変更画面のパスを設定します。
	 * @param passwordReissuePagePath パスワード変更画面のパス
	 */
	public void setPasswordReissuePagePath(String passwordReissuePagePath) {
		this.passwordReissuePagePath = passwordReissuePagePath;
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
