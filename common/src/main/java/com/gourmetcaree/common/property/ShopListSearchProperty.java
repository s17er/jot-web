package com.gourmetcaree.common.property;

import java.io.Serializable;

import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 *
 * @author Takehiro Nakamori
 *
 */
public class ShopListSearchProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5202565149182069749L;

	/** 検索条件 エリアコード */
	public Integer where_areaCd;

	/** 顧客ID */
	public Integer customerId;

	/** 検索条件 店舗名 */
	public String where_shopName;

	/** 検索条件 業態区分 */
	public String where_industryKbn;

	/** 検索条件 住所 */
	public String where_address;

	/** 検索条件 オープン日 */
	public String where_searchOpenDateFlg;

	/** 検索条件 フリーワード */
	public String where_keyword;

	/** 最大表示件数 */
	public int maxRow;

	/** 表示ページ */
	public int targetPage;

	/** ページナビ */
	public PageNavigateHelper pageNavi;

}
