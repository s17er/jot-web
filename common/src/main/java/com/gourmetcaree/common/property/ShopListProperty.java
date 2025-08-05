package com.gourmetcaree.common.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.entity.TShopListRoute;

/**
 * 店舗一覧用プロパティ
 * @author Takehiro Nakamori
 *
 */
public class ShopListProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8625615543201001953L;

	/** 店舗一覧エンティティ */
	public TShopList tShopList;

	public List<TShopListMaterial> tShopListMaterialList = new ArrayList<TShopListMaterial>();

	/** 店舗一覧属性エンティティリスト */
	public List<TShopListAttribute> tShopListAttributeList;

	/** 店舗一覧路線エンティティリスト */
	@Deprecated
	public List<TShopListRoute> tShopListRouteList;

	/** リニューアル後の路線一覧エンティティリスト */
	public List<TShopListLine> tShopListLineList;

	/** 店舗表示条件エンティティリスト */
	public List<TShopChangeJobCondition> tShopChangeJobConditionList;

	/** インディードタグエンティティリスト */
	public List<MShopListTagMapping> mShopListTagMappingList;

	/** セッションの画像ディレクトリパス */
	public String shopListSessionImgdirPath;

	/** ディレクトリのID */
	public String idForDir;
}
