package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 顧客アカウントの情報を保持するクラスです。
 * @author Takahiro Ando
 */
public class CustomerAccountDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7892351830606460553L;

	/** 顧客アカウントID */
	public Integer id;

	/** ログインID */
	public String loginId;

	/** 顧客ID */
	public int customerId;

	/** メインメールアドレス */
	public String mainMail;

	/** 氏名 */
	public String customerName;

	/** エリアコード */
	public int areaCd;
}
