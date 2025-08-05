package com.gourmetcaree.db.common.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.entity.VReleaseWeb;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * リリースウェブの検索用プロパティ
 * @author Takehiro Nakamori
 *
 */
public class ReleaseWebSearchProperty extends PagerProperty implements Serializable {


	private static final long serialVersionUID = -1660560548408560802L;

	/** オフセット */
	public int offset;

	/** ページナビ */
	public PageNavigateHelper pageNavi;

	/** 顧客ID */
	public int customerId;

	/** エリアコード */
	public int areaCd;

	/** リリースウェブリスト */
	public List<VReleaseWeb> vReleaseWebList;


}
