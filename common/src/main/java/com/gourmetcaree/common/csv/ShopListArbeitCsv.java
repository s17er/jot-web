package com.gourmetcaree.common.csv;

import java.util.List;

import org.seasar.s2csv.csv.annotation.entity.CSVEntity;

/**
 * バイト項目を含むCSVエンティティ
 * @author Takehiro Nakamori
 *
 */
@CSVEntity(header = true)
public class ShopListArbeitCsv extends AbstractShopListCsv {

	public List<StationGroupCsv> stationGroupList;

}
