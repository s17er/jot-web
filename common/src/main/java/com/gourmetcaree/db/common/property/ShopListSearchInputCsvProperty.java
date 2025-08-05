package com.gourmetcaree.db.common.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ShopListArbeitCsv;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * CSVからインポートした店舗一覧を検索するために用いるプロパティ
 * @author Takehiro Nakamori
 *
 */
public class ShopListSearchInputCsvProperty extends PagerProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2971797437151717739L;

	/** ページナビ */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

	/** IDリスト */
	public List<Integer> idList;

	/** オフセット */
	public String offset;

	/** リミット */
	public String limit;

	/** 店舗リスト */
	public List<TShopList> shopList;

}
