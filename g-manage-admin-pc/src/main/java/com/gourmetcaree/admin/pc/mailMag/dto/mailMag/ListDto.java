package com.gourmetcaree.admin.pc.mailMag.dto.mailMag;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * メルマガDTOクラス
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2212218798802747398L;

	/** ID */
	public Integer id;

	/** 端末区分 */
	public Integer terminalKbn;

	/** PC版メルマガタイトル */
	public String pcMailMagazineTitle;

	/** PC版本文 */
	public String pcBody;

	/** モバイル版メルマガタイトル */
	public String mbMailMagazineTitle;

	/** モバイル版本文 */
	public String mbBody;

	/** 配信日時 */
	public Date deliveryDatetime;

	/** 配信フラグ */
	public Integer deliveryFlg;

	/** 配信先区分 */
	public Integer deliveryKbn;

}