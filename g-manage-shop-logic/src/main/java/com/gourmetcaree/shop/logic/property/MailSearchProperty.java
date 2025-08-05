package com.gourmetcaree.shop.logic.property;

import java.util.List;

import com.gourmetcaree.common.property.BaseProperty;

/**
 *
 * メールの検索用データを受け渡しするクラス
 *
 */
public class MailSearchProperty extends BaseProperty {

	/** 表示件数 */
	public String where_displayCount;

	/** メールステータス */
	public List<Integer> where_mailStatus;

	/** 応募先エリア */
	public List<Integer> where_areaCd;

	/** 職種 */
	public String where_jobKbn;

	/** 雇用形態 */
	public String where_employPtnKbn;

	/** ステータス */
	public String where_selectionKbn;

	/** フリーワード */
	public String where_keyword;

	/** 求人ID */
	public String where_webId;
}
