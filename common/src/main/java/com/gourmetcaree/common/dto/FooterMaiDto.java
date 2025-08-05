package com.gourmetcaree.common.dto;

/**
 * メール送信時のフッターにセットする値を保持するDto
 * @author Makoto Otani
 * @version 1.0
 */
public class FooterMaiDto extends BaseSysMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 539050713970105113L;

	/** 問い合わせメール(info@) */
	private String infoAddress;

	/** グルメキャリーのURL */
	private String siteURL;

	/** 運営者 */
	private String management;

	/**
	 * 問い合わせメール(info@)を取得します。
	 * @return 問い合わせメール(info@)
	 */
	public String getInfoAddress() {
	    return infoAddress;
	}

	/**
	 * 問い合わせメール(info@)を設定します。
	 * @param infoAddress 問い合わせメール(info@)
	 */
	public void setInfoAddress(String infoAddress) {
	    this.infoAddress = infoAddress;
	}

	/**
	 * グルメキャリーのURLを取得します。
	 * @return グルメキャリーのURL
	 */
	public String getSiteURL() {
	    return siteURL;
	}

	/**
	 * グルメキャリーのURLを設定します。
	 * @param siteURL グルメキャリーのURL
	 */
	public void setSiteURL(String siteURL) {
	    this.siteURL = siteURL;
	}

	/**
	 * 運営者を取得します
	 * @return 運営者
	 */
	public String getManagement() {
		return management;
	}

	/**
	 * 運営者を設定します。
	 */
	public void setManagement(String management) {
		this.management = management;
	}
}
