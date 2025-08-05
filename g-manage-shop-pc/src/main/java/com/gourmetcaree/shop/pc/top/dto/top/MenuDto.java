package com.gourmetcaree.shop.pc.top.dto.top;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * メニューのDTO
 * @author Aquarius
 *
 */
public class MenuDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** 原稿名 */
	public String manuscriptName;

	/** プレビューURL */
	public String previewUrl;

	/** エリアコード */
	public int areaCd;

	/** 原稿番号 */
	public String id;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** サイズ区分 */
	public String sizeKbn;

	/** PV数 */
	public int allAccessCount;

	/** 応募件数 */
	public int applicationCount;

	/** プレ応募件数 */
	public int preApplicationCount;

	/** 電話応募件数 */
	public int ipPhoneHistoryCount;
}