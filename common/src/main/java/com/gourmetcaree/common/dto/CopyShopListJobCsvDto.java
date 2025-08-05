package com.gourmetcaree.common.dto;

import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;


public class CopyShopListJobCsvDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3133652177125719123L;

	/** 店舗一覧エンティティ */
	public TShopChangeJobCondition condition;


	/** 更新フラグ */
	public boolean updateFlg;


	public int areaCd;

	public int customerid;


}
