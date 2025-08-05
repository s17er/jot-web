package com.gourmetcaree.db.shopList.dto.shopList;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 画像が存在するかどうかを保持するDto
 * @author Makoto Otani
 * @version 1.0
 */
public class ShopListMaterialExistsDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7635250633848173965L;

	/** 素材区分_メイン1(1) */
	public boolean isMain1ExistFlg = false;

	/** 素材区分_メイン１サムネイル(21) */
	public boolean isMain1ThumbExistFlg = false;

	/** 素材区分_ロゴ */
	public boolean isLogoExistFlg = false;

	/** 素材区分_ロゴサムネイル(21) */
	public boolean isLogoThumbExistFlg = false;
}