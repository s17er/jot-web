package com.gourmetcaree.admin.pc.preview.dto.detailPreview;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 関連する店舗一覧DTO
 * @author Takehiro Nakamori
 *
 */
public class RelationShopListDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5291011204838081264L;

	/** 店舗名 */
	public String shopName;

	/** 店舗一覧ID */
	public Integer id;

	/** 住所1 */
	public String address1;

	/** 住所2 */
	public String address2;

	/** 交通 */
	public String transit;

	/** 詳細パス */
	public String detailPath;

	/** 画像パス */
	public String imagePath;

	public String shopListImagePath;

}
