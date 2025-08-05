package com.gourmetcaree.admin.pc.sys.dto.mai;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseSysMaiDto;

/**
 * 応募テストメールDto
 * @author Makoto Otani
 * @version 1.0
 */
public class ApplicationTestMaiDto extends BaseSysMaiDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8408212963507298036L;

	/** 顧客名 */
	private String customerName;

	/** PCパス */
	private String pcPath;

	/** モバイルパス */
	private String mobilePath;

	/** コメント */
	private String comment;

	/** 問い合わせメール(info@) */
	private String infoAddress;

	/** グルメキャリーのURL */
	private String siteURL;

	/**
	 * 顧客名を取得します。
	 * @return 顧客名
	 */
	public String getCustomerName() {
	    return customerName;
	}

	/**
	 * 顧客名を設定します。
	 * @param customerName 顧客名
	 */
	public void setCustomerName(String customerName) {
	    this.customerName = customerName;
	}

	/**
	 * PCパスを取得します。
	 * @return PCパス
	 */
	public String getPcPath() {
	    return pcPath;
	}

	/**
	 * PCパスを設定します。
	 * @param pcPath PCパス
	 */
	public void setPcPath(String pcPath) {
	    this.pcPath = pcPath;
	}

	/**
	 * モバイルパスを取得します。
	 * @return モバイルパス
	 */
	public String getMobilePath() {
	    return mobilePath;
	}

	/**
	 * モバイルパスを設定します。
	 * @param mobilePath モバイルパス
	 */
	public void setMobilePath(String mobilePath) {
	    this.mobilePath = mobilePath;
	}

	/**
	 * コメントを取得します。
	 * @return コメント
	 */
	public String getComment() {
	    return comment;
	}

	/**
	 * コメントを設定します。
	 * @param comment コメント
	 */
	public void setComment(String comment) {
	    this.comment = comment;
	}

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


}
