package com.gourmetcaree.shop.logic.logic;

import java.io.Serializable;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

public class CustomerSearchConditionDto implements Serializable {

	private static final long serialVersionUID = 7069778818586603879L;

	/** 希望業種 */
	public String[] where_industryCd;

	/** 雇用形態 */
	public String[] where_empPtnKbn;

	/** 職種 */
	public String[] where_job;

	/** 取得資格 */
	public String[] where_qualification;

	/** 希望勤務地 */
	public String[] where_workLocation;

	/** 性別 */
	public String where_sexKbn = "";

	/** 下限年齢 */
	@IntegerHankakuType
	public String where_lowerAge = "";

	/** 上限年齢 */
	@IntegerHankakuType
	public String where_upperAge = "";

	/** 転勤可 */
	public String where_transferFlg = "";

	/** 深夜勤務可 */
	public String where_midnightShiftFlg = "";

	/** キーワード */
	public String where_keyword;

	/** 希望都道府県 */
	public String[] where_prefList;

	/** 住所 */
	public String[] where_addressList;

	/** 希望地域 */
	public String[] where_areaList;

	public String where_scoutedFlg;

	public String where_favoriteFlg;

	public String where_subscFlg;

	public String where_refuseFlg;
}
