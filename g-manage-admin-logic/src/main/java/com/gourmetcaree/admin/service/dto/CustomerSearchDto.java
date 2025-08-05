package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 顧客検索サブウィンドウの結果を保持するDto
 * @author Makoto Otani
 *
 */
public class CustomerSearchDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2401756910821757906L;

	/** ID */
	public String id;

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

	/** メールアドレス */
	public String mainMail;

	/** サブアドレス */
	public List<String> subMailList;

	/** 担当会社・営業担当者名 */
	public String companySalesName;

}
