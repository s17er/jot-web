package com.gourmetcaree.admin.service.property.tempWebdata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import net.arnx.jsonic.JSON;

public class TempWebdataProperty implements Serializable {

	private static final long serialVersionUID = -3234586057287349940L;

	/** WebId */
	public String id;

	/** アクセスコード */
	public String accessCd;

	/** 顧客検索用DTO */
	public String customerId;

	/** 系列店舗ID */
	public List<String> shopListIdList;

	/** エリアコード */
	public String areaCd;

	/** 号数ID */
	public String volumeId;

	/** 連載区分 */
	public String serialPublicationKbn;

	/** 原稿名 */
	public String manuscriptName;

	/** 特集 */
	public List<String> specialIdList;

	/** 適職診断区分 */
	public String reasonableKbn;

	/** MT記事ID */
	public String mtBlogId;

	/** トップインタビューURL */
	public String topInterviewUrl;

	/** 合同説明会参加区分 */
	public String briefingPresentKbn;

	/** 掲載終了表示フラグ */
	public Integer publicationEndDisplayFlg;

	/** 所属（会社） */
	public String companyId;

	/** 営業担当 */
	public String salesId;

	/** 勤務エリア・最寄り駅 */
	public String workingPlace;

	/** 勤務地詳細 */
	public String workingPlaceDetail;

	/** 客席数 */
	public String seating;

	/** 客単価 */
	public String unitPrice;

	/** 営業時間 */
	public String businessHours;

	/** ホームページ */
	public String homepage1;

	/** 企業の特徴 */
	public List<String> companyCharacteristicKbnList;

	/** タグリスト */
	public String tagList;

	/** 職種のチェックボックスを保持する（画面制御用でDB登録しない） */
	public List<String> employJobKbnList;

	/** 職種の選択 */
	public List<TempWebJobPropterty> webJobDtoList;

	/** 電話番号/受付時間 */
	public String phoneReceptionist;

	/** 応募フォーム区分 */
	public String applicationFormKbn;

	/** 店舗見学区分 */
	public String observationKbn;

	/** 応募メール送信先 */
	public String communicationMailKbn;

	/** メール */
	public String mail;

	/** 応募方法 */
	public String applicationMethod;

	/** 面接地住所/交通 */
	public String addressTraffic;

	/** 地図用住所 */
	public String mapAddress;

	/** 緯度 */
	public String latitude;

	/** 経度 */
	public String longitude;

	/** サイズ区分 */
	public String sizeKbn;

	/** 動画URL */
	public String movieUrl;

	/** 求人一覧動画表示フラグ */
	public String movieListDisplayFlg;

	/** 求人詳細動画表示フラグ */
	public String movieDetailDisplayFlg;

	/** キャッチコピー1 */
	public String catchCopy1;

	/** キャッチコピー1 */
	public String catchCopy2;

	/** キャッチコピー1 */
	public String catchCopy3;

	/** 文章1 */
	public String sentence1;

	/** 文章2 */
	public String sentence2;

	/** 文章3 */
	public String sentence3;

	/** キャプションA */
	public String captionA;

	/** キャプションB */
	public String captionB;

	/** キャプションC */
	public String captionC;

	/** ここに注目タイトル */
	public String attentionHereTitle;

	/** ここに注目文章 */
	public String attentionHereSentence;

	/** 注目店舗フラグ */
	public Integer attentionShopFlg;

	/** 注目店舗文章 */
	public String attentionShopSentence;

	public Integer searchPreferentialFlg;

	/** メモ */
	public String memo;

	/** 素材情報を保持するリスト */
	public List<TempMaterialProperty> materialList;

	/** セッションID */
	public String sessionId;

	/** 受信元番号 */
	public String phoneNo1;
	public String phoneNo2;
	public String phoneNo3;

	/** IP電話 */
	public String ipPhoneNo1;
	public String ipPhoneNo2;
	public String ipPhoneNo3;

	public String toJson() {

		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotBlank(id)) map.put("id", id);
		if (StringUtils.isNotBlank(accessCd)) map.put("access_cd", accessCd);
		if (StringUtils.isNotBlank(customerId)) map.put("customer_id", customerId);
		if (CollectionUtils.isNotEmpty(shopListIdList)) map.put("shop_list_id_list", shopListIdList);
		if (StringUtils.isNotBlank(areaCd)) map.put("area_cd", areaCd);
		if (StringUtils.isNotBlank(volumeId)) map.put("volume_id", volumeId);
		if (StringUtils.isNotBlank(serialPublicationKbn)) map.put("serial_publication_kbn", serialPublicationKbn);
		if (StringUtils.isNotBlank(manuscriptName)) map.put("manuscript_name", manuscriptName);
		if (CollectionUtils.isNotEmpty(specialIdList)) map.put("special_id_list", specialIdList);
		if (StringUtils.isNotBlank(reasonableKbn)) map.put("reasonable_kbn", reasonableKbn);
		if (StringUtils.isNotBlank(mtBlogId)) map.put("mt_blog_id", mtBlogId);
		if (StringUtils.isNotBlank(topInterviewUrl)) map.put("top_interview_url", topInterviewUrl);
		if (StringUtils.isNotBlank(briefingPresentKbn)) map.put("briefing_present_kbn", briefingPresentKbn);
		if (publicationEndDisplayFlg != null) map.put("publication_end_display_flg", publicationEndDisplayFlg);
		if (StringUtils.isNotBlank(companyId)) map.put("company_id", companyId);
		if (StringUtils.isNotBlank(salesId)) map.put("sales_id", salesId);
		if (CollectionUtils.isNotEmpty(companyCharacteristicKbnList)) map.put("company_characteristic_kbn_list", companyCharacteristicKbnList);
		if (StringUtils.isNotBlank(tagList)) map.put("tag_list", tagList);
		if (CollectionUtils.isNotEmpty(employJobKbnList)) map.put("employ_job_kbn_list", employJobKbnList);
		if (CollectionUtils.isNotEmpty(webJobDtoList)) map.put("web_job", webJobDtoList);
		if (StringUtils.isNotBlank(phoneReceptionist)) map.put("phone_receptionist", phoneReceptionist);
		if (StringUtils.isNotBlank(applicationFormKbn)) map.put("application_form_kbn", applicationFormKbn);
		if (StringUtils.isNotBlank(observationKbn)) map.put("observation_kbn", observationKbn);
		if (StringUtils.isNotBlank(communicationMailKbn)) map.put("communication_mail_kbn", communicationMailKbn);
		if (StringUtils.isNotBlank(applicationMethod)) map.put("application_method", applicationMethod);
		if (StringUtils.isNotBlank(addressTraffic)) map.put("address_traffic", addressTraffic);
		if (StringUtils.isNotBlank(mapAddress)) map.put("map_address", mapAddress);
		if (StringUtils.isNotBlank(latitude)) map.put("latitude", latitude);
		if (StringUtils.isNotBlank(longitude)) map.put("longitude", longitude);
		if (StringUtils.isNotBlank(sizeKbn)) map.put("size_kbn", sizeKbn);
		if (StringUtils.isNotBlank(movieUrl)) map.put("movie_url", movieUrl);
		if (StringUtils.isNotBlank(movieListDisplayFlg)) map.put("movie_list_display_flg", movieListDisplayFlg);
		if (StringUtils.isNotBlank(movieDetailDisplayFlg)) map.put("movie_detail_display_flg", movieDetailDisplayFlg);
		if (StringUtils.isNotBlank(catchCopy1)) map.put("catch_copy1", catchCopy1);
		if (StringUtils.isNotBlank(catchCopy2)) map.put("catch_copy2", catchCopy2);
		if (StringUtils.isNotBlank(catchCopy3)) map.put("catch_copy3", catchCopy3);
		if (StringUtils.isNotBlank(sentence1)) map.put("sentence1", sentence1);
		if (StringUtils.isNotBlank(sentence2)) map.put("sentence2", sentence2);
		if (StringUtils.isNotBlank(sentence3)) map.put("sentence3", sentence3);
		if (StringUtils.isNotBlank(captionA)) map.put("captiona", captionA);
		if (StringUtils.isNotBlank(captionB)) map.put("captionb", captionB);
		if (StringUtils.isNotBlank(captionC)) map.put("captionc", captionC);
		if (StringUtils.isNotBlank(attentionHereTitle)) map.put("attention_here_title", attentionHereTitle);
		if (StringUtils.isNotBlank(attentionHereSentence)) map.put("attention_here_sentence", attentionHereSentence);
		if (attentionShopFlg != null) map.put("attention_shop_flg", attentionShopFlg);
		if (searchPreferentialFlg != null) map.put("search_preferential_flg", searchPreferentialFlg);
		if (StringUtils.isNotBlank(attentionShopSentence)) map.put("attention_shop_sentence", attentionShopSentence);
		if (StringUtils.isNotBlank(memo)) map.put("memo", memo);
		if (StringUtils.isNotBlank(areaCd)) map.put("area_cd", areaCd);
		if (CollectionUtils.isNotEmpty(materialList)) map.put("material", materialList);
		if (StringUtils.isNotBlank(sessionId)) map.put("session_id", sessionId);
		if (StringUtils.isNotBlank(phoneNo1)) map.put("phone_no1", phoneNo1);
		if (StringUtils.isNotBlank(phoneNo2)) map.put("phone_no2", phoneNo2);
		if (StringUtils.isNotBlank(phoneNo3)) map.put("phone_no3", phoneNo3);
		if (StringUtils.isNotBlank(ipPhoneNo1)) map.put("ip_phone_no1", ipPhoneNo1);
		if (StringUtils.isNotBlank(ipPhoneNo2)) map.put("ip_phone_no2", ipPhoneNo2);
		if (StringUtils.isNotBlank(ipPhoneNo3)) map.put("ip_phone_no3", ipPhoneNo3);
		if (StringUtils.isNotBlank(workingPlace)) map.put("working_place", workingPlace);
		if (StringUtils.isNotBlank(workingPlaceDetail)) map.put("working_place_detail", workingPlaceDetail);
		if (StringUtils.isNotBlank(mail)) map.put("mail", mail);
		if (StringUtils.isNotBlank(seating)) map.put("seating", seating);
		if (StringUtils.isNotBlank(unitPrice)) map.put("unit_price", unitPrice);
		if (StringUtils.isNotBlank(businessHours)) map.put("business_hours", businessHours);
		if (StringUtils.isNotBlank(homepage1)) map.put("homepage1", homepage1);

		return JSON.encode(map);
	}

}
