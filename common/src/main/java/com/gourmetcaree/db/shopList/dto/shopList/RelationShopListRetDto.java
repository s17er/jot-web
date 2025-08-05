package com.gourmetcaree.db.shopList.dto.shopList;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 関連する店舗一覧DTO
 * @author Takehiro Nakamori
 *
 */
public class RelationShopListRetDto extends BaseDto implements Serializable {

	/** シリアルバージョン */
	private static final long serialVersionUID = 3143073718168009828L;

	/** 店舗一覧ID */
	public Integer id;

	/** 店舗名 */
	public String shopName;

	public Integer industryKbn1;

	public String shopInformation;

	/** 住所1 */
	public String address1;

	/** 住所2 */
	public String address2;

	/** 交通 */
	public String transit;

	/** バイト側職種 */
	public String arbeitJob;

	/** バイト側時給(下限) */
	public String arbeitHourSalary;

	/** バイト側時給(上限) */
	public String arbeitHourMaxSalary;

	/** 求人募集フラグ */
	public Integer jobOfferFlg;

	/** アクセスキー */
	public String accessKey;

	/** 都道府県ID */
	public String arbeitTodouhukenId;

	/** アルバイトプレビューパス */
	public String arbeitPreviewPath;

	public String mobileImageUrl;

	public boolean existImageFlg;

	public String recruitmentJob;

	public String salary;

}
