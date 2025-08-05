package com.gourmetcaree.common.dto;

import java.util.List;

import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListAttribute;


/**
 * 店舗一覧CSVをエンティティにコピーするためのDTOです。
 * @author Takehiro Nakamori
 *
 */
public class CopyShopListCsvDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3133652177125719123L;

	/** 店舗一覧エンティティ */
	public TShopList tShopList;

	/** 店舗一覧属性エンティティリスト */
	public List<TShopListAttribute> shopListAttrList;


	/** 更新フラグ */
	public boolean updateFlg;


	public int areaCd;

	public int customerid;


}
