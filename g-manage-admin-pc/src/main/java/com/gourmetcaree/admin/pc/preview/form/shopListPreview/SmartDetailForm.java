package com.gourmetcaree.admin.pc.preview.form.shopListPreview;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.admin.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.preview.form.listPreview.PreviewBaseForm;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.db.common.entity.TWeb;

public class SmartDetailForm extends PreviewBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5399412298540465101L;



	/** エリアコード */
	public String areaCd;

	/** 店舗名 */
	public String shopName;

	/** 店舗情報 */
	public String shopInformation;

	/** 交通 */
	public String transit;

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

	/** スタッフ */
	public String staff;

	/** 住所1(番-号まで) */
	public String address1;

	/** 住所2(ビル名など) */
	public String address2;

	/** 定休日 */
	public String holiday;

	/** 営業時間 */
	public String businessHours;

	/** 席数 */
	public String seating;

	/** 顧客単価 */
	public String unitPrice;

	/** URL1 */
	public String url1;

	/** 求人募集フラグ */
	public Integer jobOfferFlg;

	public String imagePath;

	public List<Integer> webIndustryKbnList;

	/** 店舗一覧ID */
	public String shopListId;

	/** WEBデータID */
	public String id;

	 /** 掲載開始日時 */
    public Date postStartDatetime;

    /** 掲載終了日時 */
    public Date postEndDatetime;

	/** 原稿名 */
	public String manuscriptName;

	public String backPath;


	public String webDetailPath;

	/** バイト都道府県ID */
	public String arbeitTodouhukenId;

	/** バイト市区町村ID */
	public String arbeitAreaId;

	/** バイトサブエリア */
	public String arbeitSubAreaId;

	/** 路線一覧 */
	public List<TShopListRoute> routeList;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		id = null;
		postStartDatetime = null;
		postEndDatetime = null;
		manuscriptName = null;
		arbeitTodouhukenId = null;
		arbeitAreaId = null;
		arbeitSubAreaId = null;
		routeList = null;
	}

	public boolean isExistPhoneNo() {
		return StringUtils.isNotBlank(phoneNo1)
				&& StringUtils.isNotBlank(phoneNo2)
				&& StringUtils.isNotBlank(phoneNo3);
	}

	/**
	 * Fax番号の有無
	 * @return
	 */
	public boolean isExistFaxNo() {
		return StringUtils.isNotBlank(faxNo1)
				&& StringUtils.isNotBlank(faxNo2)
				&& StringUtils.isNotBlank(faxNo3);
	}








	/** 画像の取得方法 */
	public ImgMethodKbn imgMethodKbn;

	/** 公開側のURLのルートパスを取得します。*/
	public String frontHttpUrl;

	/** 待遇検索条件区分 */
	public List<String> treatmentKbnList = new ArrayList<String>();


	/** 雇用形態区分 */
	public List<String> employPtnList = new ArrayList<String>();

	/** その他条件区分 */
	public List<String> otherConditionKbnList = new ArrayList<String>();

	/** 画像の存在有無を保持するMap */
	public Map<String, Boolean> shopListImageCheckMap = new HashMap<String, Boolean>();

	/** 店舗一覧画像のユニークキーを保持するMap */
	public Map<String, String> shopListUniqueKeyMap = new HashMap<String, String>();

	/** WMVのファイル名 */
	public String wmMovieName;

	/** クイックタイムのファイル名 */
	public String qtMovieName;

	/** 店舗表示フラグ */
	public boolean shopListDisplayFlg;

	/** 最初の店舗一覧ID */
	public int firstShopListId;

	/** 画像のユニークキーを保持するMap */
	public String imageUniqueKey;

	/** 店舗一覧用業態1 */
	public int shopListIndustryKbn1;

	/** 店舗一覧用業態2 */
	public int shopListIndustryKbn2;

	/** 関連する店舗一覧のリスト */
	public List<RelationShopListDto> relationShopList;

	/** 業態1 */
	public Integer industryKbn1;

	/** バイトプレビューパス */
	public String arbeitPreviewPath;



	public TWeb web;

}
