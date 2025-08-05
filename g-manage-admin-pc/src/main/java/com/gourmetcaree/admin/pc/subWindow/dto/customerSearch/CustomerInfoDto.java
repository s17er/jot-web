package com.gourmetcaree.admin.pc.subWindow.dto.customerSearch;

import java.io.Serializable;
import java.util.List;

/**
 * 顧客情報DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class CustomerInfoDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1479993728537836549L;

	/** ID */
	public int id;

	/** 顧客名 */
	public String customerName;

	/** 担当者名 */
	public String contactName;

	/** エリアコード */
	public String areaCd;

	/** エリア名 */
	public String areaName;

	/** 電話番号 */
	public String phoneNo;

	/** メインメールアドレス */
	public String mainMail;

	/** サブメールアドレス */
	public List<String> subMailList;

	/** 担当会社・営業担当者名 */
	public String companySalesName;


}