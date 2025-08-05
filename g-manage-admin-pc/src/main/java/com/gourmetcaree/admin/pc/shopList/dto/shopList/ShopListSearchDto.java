package com.gourmetcaree.admin.pc.shopList.dto.shopList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.shopList.dto.shopList.ShopListMaterialExistsDto;

/**
 * 店舗一覧検索結果DTO
 * @author Takehiro Nakamori
 *
 */
public class ShopListSearchDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3221291784023174762L;

	/** 詳細パス */
	public String detailPath;

	/** ID */
	public Integer id;

	/** 店舗名 */
	public String areaCd;

	/** 店舗名 */
	public String shopName;

	/** 顧客ID */
	public Integer customerId;

	/** 業態名 */
	public List<Integer> industryNameList = new ArrayList<>();

	/** 住所1(番-号まで) */
	public String address1;

	/** 住所2(ビル名など) */
	public String address2;

	/** 緯度 */
	public Double latitude;

	/** 経度 */
	public Double longitude;

	/** 電話番号 */
	public String phoneNo;

	/** FAX番号 */
	public String faxNo;

	/** 交通 */
	public String transit;

	/** 店舗情報 */
	public String shopInformation;

	/** 定休日 */
	public String holiday;

	/** 営業時間 */
	public String businessHours;

	/** 席数 */
	public String seating;

	/** 顧客単価 */
	public String unitPrice;

	/** スタッフ */
	public String staff;

	/** URL1 */
	public String url1;

	/** 求人募集フラグ */
	public String jobOfferFlgName;

	/** ディスプレイオーダー */
	public Integer dispOrder;

	/** 表示フラグ */
	public Integer dispFlg;

	/** グルメdeバイトプレビューパス */
	public String arbeitPreviewPath;

	/** 素材有り無しのDTO */
	public ShopListMaterialExistsDto materialExistsDto;

}
