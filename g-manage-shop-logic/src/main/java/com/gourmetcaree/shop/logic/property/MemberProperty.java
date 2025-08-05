package com.gourmetcaree.shop.logic.property;

import java.util.List;
import java.util.Map;

import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 *
 * 会員のデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class MemberProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7240181984189220087L;


	/** 会員エンティティリスト */
	public List<MMember> memberEntityList;

	/** 会員ID */
	public int memberId;

	/** 顧客ID */
	public int customerId;

	/** エリアコード */
	public int areaCd;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** ソートキー */
	public String sortKey;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

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
	public String where_sexKbn;

	/** 下限年齢 */
	public String where_lowerAge;

	/** 上限年齢 */
	public String where_upperAge;

	/** 転勤可 */
	public String where_transferFlg;

	/** 深夜勤務可 */
	public String where_midnightShiftFlg;

	/** 希望地域 */
	public String[] where_areaList;

	/** キーワード */
	public String where_keyword;

	/** 希望勤務地(都道府県) */
	public String[] where_prefList;

	/** 現住所 */
	public String[] where_addressList;

	public String where_scoutedFlg;

	public String where_favoriteFlg;

	public String where_subscFlg;

	public String where_refuseFlg;

	/** 検索条件Map */
	public Map<String, String> map;

	/** 除外会員 */
	public List<Integer> excludeMemberList;

	/** ページ区分 */
	public String pageKbn = "";

	public int targetPage;

}
