package com.gourmetcaree.admin.service.property;

import java.util.Date;

import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 *
 * メルマガのデータを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class MailMagazineProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6882598843308135941L;

	/** メルマガID */
	public Integer mailMagazineId;

	/** メルマガ */
	public TMailMagazine mailMagazine;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 表示対象ページ */
	public int targetPage;

	/** 配信開始日 */
	public Date deliveryStartDate;

	/** 配信終了日 */
	public Date deliveryEndDate;

	/** メルマガタイトル */
	public String mailMagazinetitle;

	/** 配信先区分 */
	public Integer deliveryKbn;

	/** エリアコード */
	public Integer areaCd;
}
