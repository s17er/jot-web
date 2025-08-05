package com.gourmetcaree.admin.pc.preview.form.shopListPreview;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.admin.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.admin.pc.preview.form.listPreview.PreviewBaseForm;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.entity.TShopListRoute;

public class DetailForm extends PreviewBaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -911231268885974020L;

	/** ID */
	public String id;

	/** 顧客ID */
	public String customerId;

	/** エリアコード */
	public Integer areaCd;

	/** 店舗名 */
	public String shopName;

	/** 顧客名 */
	public String customerName;

	/** 業態1 */
	public Integer industryKbn1;

	/** 業態2 */
	public Integer industryKbn2;

	/** 住所1(番-号まで) */
	public String address1;

	/** 住所2(ビル名など) */
	public String address2;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** FAX番号1 */
	public String faxNo1;

	/** FAX番号2 */
	public String faxNo2;

	/** FAX番号3 */
	public String faxNo3;

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
	public Integer jobOfferFlg;

	/** アルバイトプレビューパス */
	public String arbeitPreviewPath;

	/** 関連する店舗一覧のリスト */
	public List<RelationShopListDto> relationShopList;

	/** 店舗一覧画像の存在有無を保持するMap */
	public Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();

	/** 店舗一覧画像のユニークキーを保持するMap */
	public Map<String, String> uniqueKeyMap = new HashMap<String, String>();

	/** 公開側のトップのURL */
	public String frontHttpUrl;

	public String shopHoliday;

	public Integer shopListIndustryKbn1;

	/** バイト都道府県ID */
	public String arbeitTodouhukenId;

	/** バイト市区町村ID */
	public String arbeitAreaId;

	/** バイトサブエリア */
	public String arbeitSubAreaId;

	/** 路線一覧 */
	public List<TShopListRoute> routeList;

	/** 電話番号があるかどうか */
	public boolean isPhoneNoExist() {
		return StringUtils.isNotBlank(phoneNo1)
					&& StringUtils.isNotBlank(phoneNo2)
					&& StringUtils.isNotBlank(phoneNo3);
	}

	/** 店舗一覧画像の存在有無を保持するMap */
	public Map<String, Boolean> getShopListImageCheckMap() {
		return imageCheckMap;
	}
	/** 店舗一覧画像のユニークキーを保持するMap */
	public Map<String, String> getShopListUniqueKeyMap() {
		return uniqueKeyMap;
	}

	/**
	 * 店舗一覧ID
	 */
	public String getShopListId() {
		return id;
	}

	/**
     * 電話番号を取得します。
     * @return
     */
    public String getPhoneNo() {
    	return new StringBuilder("")
    	.append(phoneNo1)
    	.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
    	.append(phoneNo2)
    	.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
    	.append(phoneNo3)
    	.toString();
    }

	/**
	 * FAX番号があるかどうか
	 * @return
	 */
	public boolean isFaxNoExist() {
		return StringUtils.isNotBlank(faxNo1)
				&& StringUtils.isNotBlank(faxNo2)
				&& StringUtils.isNotBlank(faxNo3);
	}

	/**
     * FAX番号の取得
     * @return
     */
    public String getFaxNo() {
    	return new StringBuilder("")
    			.append(faxNo1)
    			.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
    			.append(faxNo2)
    			.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
    			.append(faxNo3)
    			.toString();
    }


	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		super.resetBaseForm();
		id = null;
		areaCd = null;
		shopName = null;
		customerId = null;
		customerName = null;
		industryKbn1 = null;
		industryKbn2 = null;
		address1 = null;
		address2 = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		faxNo1 = null;
		faxNo2 = null;
		faxNo3 = null;
		transit = null;
		shopInformation = null;
		holiday = null;
		businessHours = null;
		seating = null;
		unitPrice = null;
		staff = null;
		url1 = null;
		frontHttpUrl = null;
		arbeitTodouhukenId = null;
		arbeitAreaId = null;
		arbeitSubAreaId = null;
		routeList = null;
	}
}
