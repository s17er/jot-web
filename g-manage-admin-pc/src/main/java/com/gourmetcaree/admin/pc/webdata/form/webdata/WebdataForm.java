package com.gourmetcaree.admin.pc.webdata.form.webdata;

import static org.apache.commons.lang.StringUtils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.service.dto.CustomerSearchDto;
import com.gourmetcaree.admin.service.dto.ShopListDto;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.common.dto.WebRouteDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.service.WebService;

/**
 * WEBデータ入力のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class WebdataForm extends BaseForm implements Serializable  {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6818062572214079692L;

	/** 顧客検索用DTO */
	public CustomerSearchDto customerDto = new CustomerSearchDto();

	@Resource
	private WebService webService;

	/** エリアコード */
	@Required
    public String areaCd;

	/** 号数ID */
	public String volumeId;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 原稿番号 */
	public String id;

	/**ステータス */
	public String displayStatus;

	/** 過去原版（コピー元WEBID） */
	public String sourceWebId;

	/** 原稿名 */
	@Required
	public String manuscriptName;

	/** サイズ区分 */
	@Required
	public String sizeKbn;

	/** 応募フォーム区分 */
	@Required
	public String applicationFormKbn;

	/** 店舗見学区分 */
	@Required
	public String observationKbn;

	/** 特集 */
	public List<String> specialIdList = new ArrayList<String>();

	/** 所属（会社） */
	@Required(msg = @Msg(key = "errors.required"), arg0 = @Arg(key = "labels.webCompany"))
	public String companyId;

	/** 営業担当 */
	@Required
	public String salesId;

	/** 求人識別番号 */
	public String webNo;

	/** 適職診断区分 */
	public String reasonableKbn;

	/** MT記事ID */
	public String mtBlogId;

	/** トップインタビューURL */
	public String topInterviewUrl;

	/** 電話番号１ */
	public String phoneNo1;

	/** 電話番号２ */
	public String phoneNo2;

	/** 電話番号３ */
	public String phoneNo3;

	/** IP電話番号１ */
	public String ipPhoneNo1;

	/** IP電話番号２ */
	public String ipPhoneNo2;

	/** IP電話番号３ */
	public String ipPhoneNo3;

	/** 電話番号/受付時間 */
	public String phoneReceptionist;

	/** 応募メール送信先 */
	public String communicationMailKbn;

	/** メール */
	public String mail;

	/** 顧客メール */
	public String customerMail;

	/** 応募方法 */
	public String applicationMethod;

	/** 面接地住所/交通 */
	public String addressTraffic;

	/** 地図タイトル */
	public String mapTitle;

	/** 地図用住所 */
	public String mapAddress;

	/** アップロード受渡画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile imgFile;

	/** ロゴ画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile logoImg;

	/** メイン1画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile main1Img;

	/** メイン2画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile main2Img;

	/** メイン3画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile main3Img;

	/** 右画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile rightImg;

	/** 写真A画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile photoAImg;

	/** 写真B画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile photoBImg;

	/** 写真C画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile photoCImg;

	/** フリー画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile freeImg;

	/** 注目店舗画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile attentionShopImg;

	/** ここに注目画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile attentionHereImg;

	/** キャッチコピー1 */
	@Maxlength(maxlength=20)
	public String catchCopy1;

	/** キャッチコピー1 */
	@Maxlength(maxlength=20)
	public String catchCopy2;

	/** キャッチコピー1 */
	@Maxlength(maxlength=20)
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

	/** 注目店舗フラグ */
	public Integer attentionShopFlg;

	/** 注目店舗名 */
	public String attentionShopName;

	/** 注目店舗文章*/
	public String attentionShopSentence;

	/** ここに注目タイトル */
	@Maxlength(maxlength=15)
	public String attentionHereTitle;

	/** ここに注目文章 */
	public String attentionHereSentence;

	/** 動画URL */
	public String movieUrl;

	/** 動画コメント */
	@Maxlength(maxlength = 144)
	public String movieComment;

	/** メモ */
	public String memo;

	/** アクセスコード */
	public String accessCd;

	/** バージョン番号 */
	public Long version;

	/** 素材のアップロード操作用パラメータ */
	public String hiddenMaterialKbn;

	/** src属性などから呼ばれる際の素材Mapのキー */
	public String targetMaterialKey;

	/** 素材を保持するマップ */
	@Binding(bindingType = BindingType.NONE)
	public Map<String, MaterialDto> materialMap = new HashMap<String, MaterialDto>();

	/** ステータス変更区分コード */
	public String changeStatusKbn;

	/** 号数編集可否フラグ */
	public boolean isEditVolumeFlg = true;

	/**
	 * チェックステータス名
	 */
	public String checkedStatusName;

	/** カーソル位置 */
	public String cursorPosition;

	/** コピー画面かどうかのフラグ */
	public boolean copyFlg = false;

	/** 連載区分(旧) */
	public String serialPublicationKbn;

	/** 連載区分 */
	public String serialPublication;

	/** 合同説明会参加区分 */
	@Required
	public String briefingPresentKbn;

	/** 顧客ID */
	public String customerId;

	/** 掲載終了表示フラグ */
	public Integer publicationEndDisplayFlg;

	/** 検索対象フラグ */
	public Integer searchTargetFlg;

	/** 検索優先フラグ */
	public Integer searchPreferentialFlg;

	/** 職種のチェックボックスを保持する（画面制御用でDB登録しない） */
	public List<String> employJobKbnList = new ArrayList<>();

	/** 職種の選択 */
	public List<WebJobDto> webJobDtoList = new ArrayList<>();

	/** 企業の特徴 */
	public List<String> companyCharacteristicKbnList = new ArrayList<>();

	/** 系列店舗ID */
	public List<String> shopListIdList = new ArrayList<>();

	/** indeed出力店舗指定用 系列店舗ID */
	public List<String> shopListIdListForIndeed = new ArrayList<>();

	/** 求人一覧動画表示フラグ */
	public String movieListDisplayFlg;

	/** 求人詳細動画表示フラグ */
	public String movieDetailDisplayFlg;

	/** 店舗の総件数（確認画面用） */
	public Integer allShopCount = 0;

	/** 店舗情報を保持するリスト */
	public List<ShopListDto> shopListDtoList = new ArrayList<>();

	/** 緯度 */
	public String latitude;

	/** 経度 */
	public String longitude;

	/** 勤務地エリア・最寄駅 */
	public String workingPlace;

	/** 勤務地詳細 */
	public String workingPlaceDetail;

	/** 客席数・坪数 */
	public String seating;

	/** 客単価 */
	public String unitPrice;

	/** 営業時間 */
	public String businessHours;

	/** ホームページ１ */
	public String homepage1;


// リニューアルで削除された項目

	/** 動画フラグ(リニューアル削除) */
	public String movieFlg;

	/** 勤務地エリア(WEBエリアから名称変更)区分(リニューアル削除) */
	public List<String> webAreaKbnList = new ArrayList<String>();

	/** 海外エリア区分(リニューアル削除) */
	public List<String> foreignAreaKbnList = new ArrayList<String>();

	/** 勤務地詳細エリア区分(リニューアル削除) */
	public List<String> detailAreaKbnList = new ArrayList<String>();

	/** 複数勤務地店舗フラグ(リニューアル削除) */
	public String multiWorkingPlaceFlg;

	/** 主要駅区分(リニューアル削除) */
	public List<String> mainStationKbnList = new ArrayList<String>();

	/** 鉄道会社(リニューアル削除) */
	public String railroadId;

	/** 路線(リニューアル削除) */
	public String routeId;

	/** 駅(リニューアル削除) */
	public String stationId;

	/** 削除する駅(リニューアル削除) */
	public String deleteStationId;

	/** WEBデータ路線図リスト(リニューアル削除) */
	public  List<WebRouteDto> webRouteList = new ArrayList<WebRouteDto>();

	/** 雇用形態区分(リニューアル削除) */
	public List<String> employPtnKbnList = new ArrayList<String>();

	/** 職種検索条件区分(リニューアル仕様変更削除) */
	public List<String> jobKbnList = new ArrayList<String>();

	/** 業種区分1(リニューアル削除) */
	public String industryKbn1;

	/** 業種区分2(リニューアル削除) */
	public String industryKbn2;

	/** 業種区分3(リニューアル削除) */
	public String industryKbn3;

	/** 待遇検索条件区分(リニューアル仕様変更削除) */
	public List<String> treatmentKbnList = new ArrayList<String>();

	/** その他条件区分(リニューアル削除) */
	public List<String> otherConditionKbnList = new ArrayList<String>();

	/** 店舗数区分(リニューアル削除) */
	public String shopsKbn;

	/** 募集職種(リニューアル仕様変更削除) */
	public String recruitmentJob;

	/** 仕事内容(リニューアル仕様変更削除) */
	public String workContents;

	/** 給与(リニューアル仕様変更削除) */
	public String salary;

	/** 求める人物像(リニューアル仕様変更削除) */
	@Maxlength(maxlength=200, msg=@Msg(key="errors.overLimitStr"))
	public String personHunting;

	/** 勤務時間(リニューアル仕様変更削除) */
	public String workingHours;

	/** 待遇(リニューアル仕様変更削除) */
	public String treatment;

	/** 休日休暇(リニューアル仕様変更削除) */
	public String holiday;

	/** オープン日(リニューアル削除) */
	public String openingDay;

	/** 会社（店舗）情報(リニューアル顧客に移動で削除) */
	public String shopInformation;

	/** ホームページ２(リニューアル顧客に移動で削除) */
	public String homepage2;

	/** ホームページ３(リニューアル顧客に移動で削除) */
	public String homepage3;

	/** ホームページコメント1(リニューアル顧客に移動で削除) */
	public String homepageComment1;

	/** ホームページコメント2(リニューアル顧客に移動で削除) */
	public String homepageComment2;

	/** ホームページコメント3(リニューアル顧客に移動で削除) */
	public String homepageComment3;

	/** メッセージ(リニューアル削除) */
	public String message;

	/** 検索リスト画像(リニューアル削除) */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile searchListImg;

	/** TOP画像(リニューアル削除) */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile topImg;

	/** 動画(WM)(リニューアル削除) */
	public String wmMovieName;

	/** 動画(QT)(リニューアル削除) */
	public String qtMovieName;

	/** 店舗一覧表示区分(リニューアル削除) */
	public String shopListDisplayKbn;

	/** ピックアップ開始日 */
	public String attentionShopStartDate;

	/** ピックアップ終了日 */
	public String attentionShopEndDate;

	/** 検索優先開始日 */
	public String searchPreferentialStartDate;

	/** 検索優先終了日 */
	public String searchPreferentialEndDate;

	/** よく使うタグ */
	public List<TagListDto> tagListDto = new ArrayList<>();

	/** タグ */
	public String[] tags;

	public String tagList;


	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();

		// 顧客検索用DTO
		customerDto = new CustomerSearchDto();
		// エリアコード
		areaCd = null;
		// 号数ID
		volumeId = null;
		// 掲載開始日時
		postStartDatetime = null;
		// 掲載終了日時
		postEndDatetime = null;
		// 原稿番号
		id = null;
		//ステータス
		displayStatus = null;
		// 過去原版（コピー元WEBID）
		sourceWebId = null;
		// 原稿名
		manuscriptName = null;
		// サイズ区分
		sizeKbn = null;
		// 応募フォームフラグ
		applicationFormKbn = null;
		// 店舗見学区分
		observationKbn = String.valueOf(MTypeConstants.ObservationKbn.ADMIN_QUESTION);
		// 特集
		specialIdList.clear();
		// 所属（会社）
		companyId = null;
		// 営業担当
		salesId = null;
		// 求人識別番号
		webNo = null;

		// 適職診断区分
		reasonableKbn = null;
		// トップインタビューURL
		topInterviewUrl = null;
		mtBlogId = null;
		// 電話番号
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		// IP電話番号
		ipPhoneNo1 = null;
		ipPhoneNo2 = null;
		ipPhoneNo3 = null;

		// 電話番号/受付時間
		phoneReceptionist = null;
		// メール選択
		communicationMailKbn = null;
		// メール
		mail = null;
		// 顧客メール
		customerMail = null;
		// 応募方法
		applicationMethod = null;
		// 面接地住所/交通
		addressTraffic = null;
		// 地図タイトル
		mapTitle = null;
		// 地図用住所
		mapAddress = null;
		// ロゴ画像
		if (logoImg != null) {
			logoImg.destroy();
			logoImg = null;
		}
		// メイン1画像
		if (main1Img != null) {
			main1Img.destroy();
			main1Img = null;
		}
		// メイン2画像
		if (main2Img != null) {
			main2Img.destroy();
			main2Img = null;
		}
		// メイン3画像
		if (main3Img != null) {
			main3Img.destroy();
			main3Img = null;
		}
		// 写真A画像
		if (photoAImg != null) {
			photoAImg.destroy();
			photoAImg = null;
		}
		// 写真B画像
		if (photoBImg != null) {
			photoBImg.destroy();
			photoBImg = null;
		}
		// 写真C画像
		if (photoCImg != null) {
			photoCImg.destroy();
			photoCImg = null;
		}
		// フリー画像
		if (freeImg != null) {
			freeImg.destroy();
			freeImg = null;
		}
		// 注目店舗画像
		if (attentionShopImg != null) {
			attentionShopImg.destroy();
			attentionShopImg = null;
		}


		// キャッチコピー1
		catchCopy1 = null;
		// 文章1
		sentence1 = null;
		// 文章2
		sentence2 = null;
		// 文章3
		sentence3 = null;
		// キャプションA
		captionA = null;
		// キャプションB
		captionB = null;
		// キャプションC
		captionC = null;
		// 注目店舗フラグ
		attentionShopFlg = null;
		//注目店舗名
		attentionShopName = null;
		// 注目店舗文章
		attentionShopSentence = null;
		// ここに注目タイトル
		attentionHereTitle = null;
		// ここに注目文章
		attentionHereSentence = null;
		// メモ
		memo = null;
		// アクセスコード
		accessCd = null;
		// バージョン番号
		version = null;
		// 素材のアップロード操作用パラメータ
		hiddenMaterialKbn = null;
		// src属性などから呼ばれる際の素材Mapのキー
		targetMaterialKey = null;
		// 素材を保持するマップ
		materialMap.clear();
		// ステータス変更区分コード
		changeStatusKbn = null;
		// 号数編集可否フラグ
		isEditVolumeFlg = true;
		// カーソル位置
		cursorPosition = null;
		// 勤務地エリア・最寄駅
		workingPlace = null;
		// 勤務地詳細
		workingPlaceDetail = null;

		serialPublicationKbn = null;

		serialPublication = null;

		copyFlg = false;

		briefingPresentKbn = null;

		customerId = null;

		movieUrl = null;
		movieComment = null;
		checkedStatusName = null;

		publicationEndDisplayFlg = null;

		searchTargetFlg = null;

		searchPreferentialFlg = null;

		webJobDtoList.clear();

		companyCharacteristicKbnList.clear();

		shopListIdList.clear();

		shopListIdListForIndeed.clear();

		employJobKbnList.clear();

		movieListDisplayFlg = null;

		movieDetailDisplayFlg = null;

		allShopCount = 0;

		shopListDtoList.clear();

		latitude = null;

		longitude = null;


//リニューアルで削除された項目
		// 動画フラグ
		movieFlg = null;
		// 勤務地エリア区分
		webAreaKbnList.clear();
		// 海外エリア区分
		foreignAreaKbnList.clear();
		// 複数勤務地店舗フラグ
		multiWorkingPlaceFlg = null;
		// 主要駅区分
		mainStationKbnList.clear();
		// 鉄道会社
		railroadId = null;
		// 路線
		routeId = null;
		// 駅
		stationId = null;
		// 削除する駅
		deleteStationId = null;
		// WEBデータ路線図リスト
		webRouteList = new ArrayList<WebRouteDto>();
		// 雇用形態区分
		employPtnKbnList.clear();
		// 職種検索条件区分
		jobKbnList.clear();
		// 勤務地詳細エリア
		detailAreaKbnList.clear();
		// 業種区分1
		industryKbn1 = null;
		// 業種区分2
		industryKbn2 = null;
		// 業種区分3
		industryKbn3 = null;
		// 待遇検索条件区分
		treatmentKbnList.clear();
		// その他条件区分
		otherConditionKbnList.clear();
		// 店舗数区分
		shopsKbn = null;
		// 募集職種
		recruitmentJob = null;
		// 仕事内容
		workContents = null;
		// 給与
		salary = null;
		// 求める人物像
		personHunting = null;
		// 勤務時間
		workingHours = null;
		// 待遇
		treatment = null;
		// 休日休暇
		holiday = null;
		// 客席数・坪数
		seating = null;
		// 客単価
		unitPrice = null;
		// 営業時間
		businessHours = null;
		// オープン日
		openingDay = null;
		// 会社（店舗）情報
		shopInformation = null;
		// ホームページ１
		homepage1 = null;
		// ホームページコメント1
		homepageComment1 = null;
		// ホームページ２
		homepage2 = null;
		// ホームページコメント2
		homepageComment2 = null;
		// ホームページ３
		homepage3 = null;
		// ホームページコメント3
		homepageComment3 = null;

		// メッセージ
		message = null;
		// TOP画像
		if (topImg != null) {
			topImg.destroy();
			topImg = null;
		}
		// 動画(WM)
		wmMovieName = null;
		// 動画(QT)
		qtMovieName = null;
		// 店舗一覧表示区分
		shopListDisplayKbn = String.valueOf(MTypeConstants.ShopListDisplayKbn.ARI);

		attentionShopStartDate = null;
		attentionShopEndDate = null;
		searchPreferentialStartDate = null;
		searchPreferentialEndDate = null;

		// よく使うタグ
		tagListDto = new ArrayList<>();
		// タグ
		tags = null;
		tagList = null;

	}

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		sortWebJobDto();

		// 応募フォーム「有」の場合、顧客が選択されていなければエラー
		if (this.applicationFormKbn.equals(String.valueOf(MTypeConstants.ApplicationFormKbn.EXIST))
				&& StringUtil.isEmpty(this.customerDto.id)) {

			// 「{応募フォーム}選択時は、{顧客}を選択してください。」
			errors.add("errors", new ActionMessage("errors.notChooseData",
					MessageResourcesUtil.getMessage("labels.applicationFormFlg"),
					MessageResourcesUtil.getMessage("msg.app.customer")));
		}

		if ((GourmetCareeUtil.eqInt(MTypeConstants.ObservationKbn.ADMIN_QUESTION, observationKbn)
				|| GourmetCareeUtil.eqInt(MTypeConstants.ObservationKbn.ADMIN_QUESTION_OBSERVATION, observationKbn))
				&& StringUtils.isEmpty(customerDto.id)) {
			errors.add("errors", new ActionMessage("errors.notChooseData",
					MessageResourcesUtil.getMessage("labels.observationKbn"),
					MessageResourcesUtil.getMessage("msg.app.customer")));
		}


		// 応募メール送信先の入力チェック
		if (StringUtil.isEmpty(communicationMailKbn)) {
			errors.add("errors", new ActionMessage("errors.required",
					MessageResourcesUtil.getMessage("labels.communicationMailKbn")));

		} else {
			// 応募メール送信先：応募フォーム有で、入力メール選択時に、値が入力されていなければエラー
			if((this.applicationFormKbn.equals(String.valueOf(MTypeConstants.ApplicationFormKbn.EXIST)))
						&& this.communicationMailKbn.equals(String.valueOf(MTypeConstants.CommunicationMailKbn.INPUT_MAIL)) &&
						StringUtil.isEmpty(this.mail)) {

				// 「{入力メール}選択時は、{メール}を入力してください。」
				errors.add("errors", new ActionMessage("errors.notInputData",
						MessageResourcesUtil.getMessage("labels.inputMail"),
						MessageResourcesUtil.getMessage("labels.mail")));
			}
			// 応募メール送信先：顧客メール選択時に、顧客が選択されていなければエラー
			if (this.communicationMailKbn.equals(String.valueOf(MTypeConstants.CommunicationMailKbn.CUSTOMER_MAIL)) &&
					StringUtil.isEmpty(this.customerDto.id)) {

				// 「{顧客登録メール}選択時は、{顧客}を選択してください。」
				errors.add("errors", new ActionMessage("errors.notChooseData",
						MessageResourcesUtil.getMessage("labels.customerMail"),
						MessageResourcesUtil.getMessage("msg.app.customer")));
			}

			// 応募メール送信先：入力メール選択時に値が入力されていて、メールの形でなければエラー
			if (communicationMailKbn.equals(String.valueOf(MTypeConstants.CommunicationMailKbn.INPUT_MAIL))
					&& StringUtils.isNotBlank(mail)) {
				if(!Pattern.compile("[\\w\\d_-]+@[\\w\\d_-]+\\.[\\w\\d._-]+").matcher(mail).find()) {
					errors.add("errors", new ActionMessage("errors.email", MessageResourcesUtil.getMessage("labels.mail")));
				}
			}
		}

		// 注目店舗フラグ：対象の場合は、注目店舗画像と注目店舗文章とピックアップ期限が登録されていなければエラー
		if (GourmetCareeUtil.eqInt(MTypeConstants.AttentionShopFlg.TARGET, this.attentionShopFlg)) {

			if (StringUtil.isBlank(this.attentionShopSentence) || StringUtil.isBlank(this.attentionShopName)){

				// 「{注目店舗}選択時は、{注目店舗画像}、{注目店舗文章}、{ピックアップ期限}を入力してください。」
				errors.add("errors", new ActionMessage("errors.noRelatedChooseOneVsTwo",
						MessageResourcesUtil.getMessage("labels.attentionShopFlg"),
						MessageResourcesUtil.getMessage("labels.attentionShopName"),
						MessageResourcesUtil.getMessage("labels.attentionShopSentence")));
			}

			if(StringUtil.isBlank(this.attentionShopStartDate) || StringUtil.isBlank(this.attentionShopEndDate)) {
				errors.add("errors", new ActionMessage("errors.noRelatedChooseOneVsTwo",
						MessageResourcesUtil.getMessage("labels.attentionShopFlg"),
						MessageResourcesUtil.getMessage("labels.attentionShopStartDate"),
						MessageResourcesUtil.getMessage("labels.attentionShopEndDate")));
			} else {
				//ピックアップ終了日がピックアップ開始日よりも前の場合エラー
				if (attentionShopEndDate.compareTo(attentionShopStartDate) <= 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.attentionShopStartDate"), MessageResourcesUtil.getMessage("labels.attentionShopEndDate")));
				}
			}
		}

		if (GourmetCareeUtil.eqInt(MTypeConstants.SearchPreferentialFlg.TARGET, this.searchPreferentialFlg)) {

			if(StringUtil.isBlank(this.searchPreferentialStartDate) || StringUtil.isBlank(this.searchPreferentialEndDate)) {
				errors.add("errors", new ActionMessage("errors.noRelatedChooseOneVsTwo",
						MessageResourcesUtil.getMessage("labels.searchPreferentialFlg"),
						MessageResourcesUtil.getMessage("labels.searchPreferentialStartDate"),
						MessageResourcesUtil.getMessage("labels.searchPreferentialEndDate")));
			} else {
				if (searchPreferentialEndDate.compareTo(searchPreferentialStartDate) <= 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.searchPreferentialStartDate"), MessageResourcesUtil.getMessage("labels.searchPreferentialEndDate")));
				}
			}
		}


		checkMovieUrl(errors);

		// 求人識別番号のチェック
		if (!StringUtil.isBlank(webNo) && !StringUtil.isBlank(this.customerDto.id)) {
			// 形式が不正な場合はエラー
			if (!webNo.matches("^[0-9]+$")) {
				errors.add("errors", new ActionMessage("errors.notExistsWebNo"));
			} else {
				// 対象企業内に求人識別番号が存在しなければエラー
				if (webService.checkByCustomerIdAndWebNo(
					Integer.parseInt(this.customerDto.id), 
					Integer.parseInt(webNo)
					).isEmpty()) {
					errors.add("errors", new ActionMessage("errors.notExistsWebNo"));
				}
	
				// 同じ求人識別番号を持つ求人と掲載期間が重複していればエラー
				if (!StringUtil.isBlank(volumeId) && !webService.duplicationPostPeriodInWebNo(
					Integer.parseInt(volumeId), 
					Integer.parseInt(this.customerDto.id), 
					Integer.parseInt(webNo), 
					StringUtil.isBlank(id) ? 0 : Integer.parseInt(id)
					).isEmpty()) {
					errors.add("errors", new ActionMessage("errors.webNoPeriodDuplication"));
				}
			}
		}

		return errors;
	}

	/**
	 * 職種を並び変える
	 */
	public void sortWebJobDto() {
		webJobDtoList = webJobDtoList.stream()
				.filter(o -> o.displayOrder != null)
				.sorted(Comparator.comparing(i -> Integer.parseInt(i.getDisplayOrder())))
				.collect(Collectors.toList());
	}

	/**
	 * サイズの入力チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validateSizeKbn() {
		// エラー情報
		ActionMessages errors = new ActionMessages();

		if (StringUtils.isBlank(sizeKbn)) {
			// 「{サイズ}を入力してください。」
			errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.sizeKbn")));
		}

		return errors;
	}

	/**
	 * multiboxのリセットを行う
	 */
	public void resetMultibox() {
		// 特集
		this.specialIdList = new ArrayList<String>();
		this.shopListIdList = new ArrayList<>();
		this.shopListIdListForIndeed = new ArrayList<>();
		this.employJobKbnList = new ArrayList<>();
		this.companyCharacteristicKbnList = new ArrayList<>();
		// DTOもリセット
		this.webJobDtoList = new ArrayList<>();
	}

	/**
	 * sizeKbnのリセットを行う
	 */
	public void resetSizeKbn() {
		sizeKbn = null;
	}

//
//	/**
//	 * 鉄道会社、路線、駅をチェック
//	 * @param errors
//	 */
//	private void checkRouteData(ActionMessages errors) {
//
//		// 鉄道会社、路線、駅がすべて選択されているかチェック
//		if ((!StringUtil.isEmpty(railroadId) || !StringUtil.isEmpty(routeId) || !StringUtil.isEmpty(stationId))) {
//
//			// 鉄道会社・路線・駅が全て入力されているかチェック
//			if (StringUtil.isEmpty(this.railroadId) || StringUtil.isEmpty(this.routeId) || StringUtil.isEmpty(this.stationId)) {
//				//「{路線・最寄駅}は全て入力してください。」
//				errors.add("errors", new ActionMessage("errors.notAllData",
//						MessageResourcesUtil.getMessage("labels.routeStation")));
//				// カーソルをリセット
//				cursorPosition = "";
//			}
//
//			// 同一の駅が入力されていないかチェック
//			for (WebRouteDto dto : this.webRouteList) {
//				if (this.stationId.equals(String.valueOf(dto.stationId))) {
//					//「{路線・最寄駅}は他のデータと重複しています。」
//					errors.add("errors", new ActionMessage("errors.unique",
//							MessageResourcesUtil.getMessage("labels.routeStation")));
//					// カーソルをリセット
//					cursorPosition = "";
//					break;
//				}
//			}
//
//
//
//			// 選択件数を超えていればエラー
//			if (this.webRouteList.size() >= GourmetCareeConstants.DEFAULT_ROUTE_LIMIT) {
//				//「{路線・最寄駅}は{「件数」}件以内で入力してください。」
//				errors.add("errors", new ActionMessage("errors.overLimit",
//						MessageResourcesUtil.getMessage("labels.routeStation"),
//						String.valueOf(GourmetCareeConstants.DEFAULT_ROUTE_LIMIT) + "件"
//						));
//				// カーソルをリセット
//				cursorPosition = "";
//			}
//		}
//	}


	/**
	 * 素材保持用Mapの使用前処理
	 */
	public void initMaterialMap() {

		if (this.materialMap == null) {
			this.materialMap = new HashMap<String, MaterialDto>();
		}
	}

	/**
	 * 受渡用画像を削除
	 */
	public void deleteImgFile() {
		if (imgFile != null) {
			imgFile.destroy();
			imgFile = null;
		}
	}

	/**
	 * アップロードされたフォームファイルを取得する。
	 * @param materialKbn 素材区分
	 * @return Formfileオブジェクト
	 */
	public FormFile createFormFile(String materialKbn) {

		//Mapの初期処理を行う
		initMaterialMap();

		// 選択された素材によって、セットするフォームを返す
		if (MTypeConstants.MaterialKbn.LOGO.equals(materialKbn)) {
			this.logoImg = this.imgFile;
			return this.logoImg;

		} else if (MTypeConstants.MaterialKbn.MAIN_1.equals(materialKbn)) {
			this.main1Img = this.imgFile;
			return this.main1Img;

		} else if (MTypeConstants.MaterialKbn.MAIN_2.equals(materialKbn)) {
			this.main2Img = this.imgFile;
			return this.main2Img;

		} else if (MTypeConstants.MaterialKbn.MAIN_3.equals(materialKbn)) {
			this.main3Img = this.imgFile;
			return this.main3Img;

		} else if (MTypeConstants.MaterialKbn.RIGHT.equals(materialKbn)) {
			this.rightImg = this.imgFile;
			return this.rightImg;

		} else if (MTypeConstants.MaterialKbn.PHOTO_A.equals(materialKbn)) {
			this.photoAImg = this.imgFile;
			return this.photoAImg;

		} else if (MTypeConstants.MaterialKbn.PHOTO_B.equals(materialKbn)) {
			this.photoBImg = this.imgFile;
			return this.photoBImg;

		} else if (MTypeConstants.MaterialKbn.PHOTO_C.equals(materialKbn)) {
			this.photoCImg = this.imgFile;
			return this.photoCImg;

		}else if (MTypeConstants.MaterialKbn.FREE.equals(materialKbn)) {
			this.freeImg = this.imgFile;
			return this.freeImg;

		}else if (MTypeConstants.MaterialKbn.ATTENTION_SHOP.equals(materialKbn)) {
			this.attentionShopImg = this.imgFile;
			return this.attentionShopImg;

		} else if (MTypeConstants.MaterialKbn.ATTENTION_HERE.equals(materialKbn)) {
			this.attentionHereImg = this.imgFile;
			return this.attentionHereImg;

		} else {
			return null;
		}
	}

	/**
	 * アップロードされた素材の削除処理
	 * @param materialKbn 素材区分
	 */
	public void deleteMaterial(String materialKbn) {

		// Mapの初期処理を行う
		initMaterialMap();

		// 選択された素材のMapを消す
		materialMap.remove(materialKbn);
		// 選択されたサムネイル素材のMapを消す
		materialMap.remove(MTypeConstants.MaterialKbn.getThumbValue(materialKbn));

		// FormFileを削除する
		deleteFormFile(materialKbn);
	}

	/**
	 * 素材区分を元に、登録されているFormFileを削除する
	 * @param materialKbn 素材区分
	 */
	public void deleteFormFile(String materialKbn) {

		// 選択された素材を空にする

		if (MTypeConstants.MaterialKbn.LOGO.equals(materialKbn)) {
			if (logoImg != null) {
				logoImg.destroy();
				this.logoImg = null;
			}

		} else if (MTypeConstants.MaterialKbn.MAIN_1.equals(materialKbn)) {
			if (main1Img != null) {
				main1Img.destroy();
				this.main1Img = null;
			}

		} else if (MTypeConstants.MaterialKbn.MAIN_2.equals(materialKbn)) {
			if (main2Img != null) {
				main2Img.destroy();
				this.main2Img = null;
			}

		} else if (MTypeConstants.MaterialKbn.MAIN_3.equals(materialKbn)) {
			if (main3Img != null) {
				main3Img.destroy();
				this.main3Img = null;
			}

		} else if (MTypeConstants.MaterialKbn.RIGHT.equals(materialKbn)) {
			if (rightImg != null) {
				rightImg.destroy();
				this.rightImg = null;
			}

		} else if (MTypeConstants.MaterialKbn.PHOTO_A.equals(materialKbn)) {
			if (photoAImg != null) {
				photoAImg.destroy();
				this.photoAImg = null;
			}

		} else if (MTypeConstants.MaterialKbn.PHOTO_B.equals(materialKbn)) {
			if (photoBImg != null) {
				photoBImg.destroy();
				this.photoBImg = null;
			}

		} else if (MTypeConstants.MaterialKbn.PHOTO_C.equals(materialKbn)) {
			if (photoCImg != null) {
				photoCImg.destroy();
				this.photoCImg = null;
			}

		}else if (MTypeConstants.MaterialKbn.FREE.equals(materialKbn)) {
			if (freeImg != null) {
				freeImg.destroy();
				this.freeImg = null;
			}

		}else if (MTypeConstants.MaterialKbn.ATTENTION_SHOP.equals(materialKbn)) {
			if (attentionShopImg != null) {
				attentionShopImg.destroy();
				this.attentionShopImg = null;
			}
		} else if (MTypeConstants.MaterialKbn.ATTENTION_HERE.equals(materialKbn)) {
			if (attentionHereImg != null) {
				attentionHereImg.destroy();
				this.attentionHereImg = null;
			}
		}
	}

	/**
	 * ファイル出力用のキーを取得します。
	 * コピー画面以外でwebIdが存在すればwebId、それ以外の場合は文字列「INPUT」を取得します。
	 * @return
	 */
	public String getIdForDirName() {
		return (StringUtil.isNotBlank(id) && !copyFlg) ? id : GourmetCareeConstants.IMG_FILEKEY_INPUT;
	}


	/**
	 * テキストエリアを対象に末尾の不要なスペースや改行をトリムします。
	 */
	public void convertTextAreaData() {
		topInterviewUrl = GourmetCareeUtil.trimSuffixLineBreak(topInterviewUrl);
		phoneReceptionist = GourmetCareeUtil.trimSuffixLineBreak(phoneReceptionist);
		applicationMethod = GourmetCareeUtil.trimSuffixLineBreak(applicationMethod);
		addressTraffic = GourmetCareeUtil.trimSuffixLineBreak(addressTraffic);
		catchCopy2 = GourmetCareeUtil.trimSuffixLineBreak(catchCopy2);
		catchCopy3 = GourmetCareeUtil.trimSuffixLineBreak(catchCopy3);
		catchCopy1 = GourmetCareeUtil.trimSuffixLineBreak(catchCopy1);
		sentence1 = GourmetCareeUtil.trimSuffixLineBreak(sentence1);
		sentence2 = GourmetCareeUtil.trimSuffixLineBreak(sentence2);
		captionA = GourmetCareeUtil.trimSuffixLineBreak(captionA);
		captionB = GourmetCareeUtil.trimSuffixLineBreak(captionB);
		captionC = GourmetCareeUtil.trimSuffixLineBreak(captionC);
		attentionHereSentence = GourmetCareeUtil.trimSuffixLineBreak(attentionHereSentence);
		memo = GourmetCareeUtil.trimSuffixLineBreak(memo);

		if (StringUtils.isNotBlank(movieUrl) && isBeforeConvertYoutubeUrl(movieUrl)) {
			convertYoutubeUrl();
		}
	}

	/**
	 * キャッチコピーがあるかどうかを判断
	 */
	public boolean isCatchCopyExist() {
		if (isNotBlank(catchCopy1) || isNotBlank(catchCopy2) || isNotBlank(catchCopy3)) {
			return true;
		}

		return false;
	}

	/**
	 * キャッチコピーを取得
	 */
	public String getCatchCopy() {
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder("");
		if(isNotBlank(catchCopy1)) {
			sb.append(catchCopy1);
			sb.append(lineSeparator);
		}

		if(isNotBlank(catchCopy2)) {
			sb.append(catchCopy2);
			sb.append(lineSeparator);
		}

		if(isNotBlank(catchCopy3)) {
			sb.append(catchCopy3);
		}

		return sb.toString();
	}

	/**
	 * 登録顧客が存在するかどうか
	 */
	public boolean isCustomerDtoExist() {
		if (customerDto == null) {
			return false;
		}

		return StringUtils.isNotBlank(customerDto.id);
	}
//
//	/**
//	 * 求める人物像・資格に表示するその他条件区分リスト
//	 * @return 求める人物像・資格に表示するその他条件区分リスト
//	 */
//	public List<String> getPersonHuntingOtherConditionKbnList() {
//		if (CollectionUtils.isEmpty(otherConditionKbnList)) {
//			return new ArrayList<String>();
//		}
//
//		List<String> list = new ArrayList<String>();
//		for (String kbn : otherConditionKbnList) {
//			if (MTypeConstants.OtherBackConditionKbn.
//					PERSON_HUNTING_NO_DISPLAY_LIST
//					.contains(NumberUtils.toInt(kbn))) {
//
//				continue;
//			}
//			list.add(kbn);
//		}
//
//		return list;
//	}



	/**
	 * IP電話番号を追加します。
	 */
	public void addIpPhoneNumber(TWebIpPhone ipPhone) {
		if (GourmetCareeUtil.eqInt(1, ipPhone.edaNo)) {
			phoneNo1 = ipPhone.customerTel;
			ipPhoneNo1 = ipPhone.ipTel;
		} else if (GourmetCareeUtil.eqInt(2, ipPhone.edaNo)) {
			phoneNo2 = ipPhone.customerTel;
			ipPhoneNo2 = ipPhone.ipTel;
		} else if (GourmetCareeUtil.eqInt(3, ipPhone.edaNo)) {
			phoneNo3 = ipPhone.customerTel;
			ipPhoneNo3 = ipPhone.ipTel;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(GourmetCareeConstants.CRLF);
		sb.append("webId(id)=")
			.append(id)
			.append(GourmetCareeConstants.CRLF);
		sb.append("customerDto=")
		.append(ToStringBuilder.reflectionToString(customerDto))
		.append(GourmetCareeConstants.CRLF);
		sb.append("areaCd=")
		.append(areaCd)
		.append(GourmetCareeConstants.CRLF);
		sb.append("volumeId=")
			.append(volumeId)
			.append(GourmetCareeConstants.CRLF);
		sb.append("postStartDatetime=")
			.append(postStartDatetime)
			.append(GourmetCareeConstants.CRLF);
		sb.append("postEndDatetime=")
			.append(postEndDatetime)
			.append(GourmetCareeConstants.CRLF);
		sb.append("displayStatus=")
			.append(displayStatus)
			.append(GourmetCareeConstants.CRLF);
		sb.append("sourceWebId=")
			.append(sourceWebId)
			.append(GourmetCareeConstants.CRLF);
		sb.append("manuscriptName=")
			.append(manuscriptName)
			.append(GourmetCareeConstants.CRLF);
		sb.append("sizeKbn=")
			.append(sizeKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("applicationFormKbn=")
			.append(applicationFormKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("observationKbn=")
			.append(observationKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("specialIdList=")
			.append(specialIdList)
			.append(GourmetCareeConstants.CRLF);
		sb.append("companyId=")
			.append(companyId)
			.append(GourmetCareeConstants.CRLF);
		sb.append("salesId=")
			.append(salesId)
			.append(GourmetCareeConstants.CRLF);
		sb.append("reasonableKbn=")
			.append(reasonableKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("mtBlogId=")
			.append(mtBlogId)
			.append(GourmetCareeConstants.CRLF);
		sb.append("topInterviewUrl=")
			.append(topInterviewUrl)
			.append(GourmetCareeConstants.CRLF);
		sb.append("phoneReceptionist=")
			.append(phoneReceptionist)
			.append(GourmetCareeConstants.CRLF);
		sb.append("communicationMailKbn=")
			.append(communicationMailKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("mail=")
			.append(mail)
			.append(GourmetCareeConstants.CRLF);
		sb.append("customerMail=")
			.append(customerMail)
			.append(GourmetCareeConstants.CRLF);
		sb.append("applicationMethod=")
			.append(applicationMethod)
			.append(GourmetCareeConstants.CRLF);
		sb.append("addressTraffic=")
			.append(addressTraffic)
			.append(GourmetCareeConstants.CRLF);
		sb.append("mapTitle=")
			.append(mapTitle)
			.append(GourmetCareeConstants.CRLF);
		sb.append("mapAddress=")
			.append(mapAddress)
			.append(GourmetCareeConstants.CRLF);
		sb.append("catchCopy1=")
			.append(catchCopy1)
			.append(GourmetCareeConstants.CRLF);
		sb.append("catchCopy2=")
			.append(catchCopy2)
			.append(GourmetCareeConstants.CRLF);
		sb.append("catchCopy3=")
			.append(catchCopy3)
			.append(GourmetCareeConstants.CRLF);
		sb.append("sentence1=")
			.append(sentence1)
			.append(GourmetCareeConstants.CRLF);
		sb.append("sentence2=")
			.append(sentence2)
			.append(GourmetCareeConstants.CRLF);
		sb.append("sentence3=")
			.append(sentence3)
			.append(GourmetCareeConstants.CRLF);
		sb.append("captionA=")
			.append(captionA)
			.append(GourmetCareeConstants.CRLF);
		sb.append("captionB=")
			.append(captionB)
			.append(GourmetCareeConstants.CRLF);
		sb.append("captionC=")
			.append(captionC)
			.append(GourmetCareeConstants.CRLF);
		sb.append("attentionShopFlg=")
			.append(attentionShopFlg)
			.append(GourmetCareeConstants.CRLF);
		sb.append("attentionShopSentence=")
			.append(attentionShopSentence)
			.append(GourmetCareeConstants.CRLF);
		sb.append("attentionHereTitle=")
			.append(attentionHereTitle)
			.append(GourmetCareeConstants.CRLF);
		sb.append("attentionHereSentence=")
			.append(attentionHereSentence)
			.append(GourmetCareeConstants.CRLF);
		sb.append("memo=")
			.append(memo)
			.append(GourmetCareeConstants.CRLF);
		sb.append("accessCd=")
			.append(accessCd)
			.append(GourmetCareeConstants.CRLF);
		sb.append("version=")
			.append(version)
			.append(GourmetCareeConstants.CRLF);
		sb.append("hiddenMaterialKbn=")
			.append(hiddenMaterialKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("targetMaterialKey=")
			.append(targetMaterialKey)
			.append(GourmetCareeConstants.CRLF);
		sb.append("changeStatusKbn=")
			.append(changeStatusKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("isEditVolumeFlg=")
			.append(isEditVolumeFlg)
			.append(GourmetCareeConstants.CRLF);
		sb.append("cursorPosition=")
			.append(cursorPosition)
			.append(GourmetCareeConstants.CRLF);
		sb.append("copyFlg=")
			.append(copyFlg)
			.append(GourmetCareeConstants.CRLF);
		sb.append("serialPublicationKbn=")
			.append(serialPublicationKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("serialPublication=")
			.append(serialPublication)
			.append(GourmetCareeConstants.CRLF);
		sb.append("briefingPresentKbn=")
			.append(briefingPresentKbn)
			.append(GourmetCareeConstants.CRLF);
		sb.append("phoneNo1=")
		.append(phoneNo1)
		.append(GourmetCareeConstants.CRLF);
		sb.append("ipPhoneNo1=")
		.append(ipPhoneNo1)
		.append(GourmetCareeConstants.CRLF);
		sb.append("phoneNo2=")
		.append(phoneNo2)
		.append(GourmetCareeConstants.CRLF);
		sb.append("ipPhoneNo2=")
		.append(ipPhoneNo2)
		.append(GourmetCareeConstants.CRLF);
		sb.append("phoneNo3=")
		.append(phoneNo3)
		.append(GourmetCareeConstants.CRLF);
		sb.append("ipPhoneNo3=")
		.append(ipPhoneNo3)
		.append(GourmetCareeConstants.CRLF);
		return sb.toString();
	}


	private void checkMovieUrl(ActionMessages errors) {
		if (StringUtils.isBlank(movieUrl)) {
			return;
		}

		if (!isYoutubeUrl(movieUrl)) {
			errors.add("errors", new ActionMessage("errors.youtubeEmbedUrl",
						MessageResourcesUtil.getMessage("labels.movieUrl")));
		}

	}


	protected boolean isYoutubeUrl(String url) {
		if (StringUtils.isBlank(url)) {
			return false;
		}
		if (isBeforeConvertYoutubeUrl(url)) {
			return true;
		}

		return isAfterConvertYoutubeUrl(url);
	}

	protected boolean isBeforeConvertYoutubeUrl(String url) {
		if (!url.startsWith("https://www.youtube.com/watch?")) {
			return false;
		}
		Map<String, String> paramMap = GourmetCareeUtil.getUrlParams(url);

		return paramMap.containsKey("v");
	}

	protected boolean isAfterConvertYoutubeUrl(String url) {
		return url.startsWith("https://www.youtube.com/embed/");
	}

	public void convertYoutubeUrl() {
		if (StringUtils.isBlank(movieUrl)) {
			return;
		}

		if (isBeforeConvertYoutubeUrl(movieUrl)) {
			Map<String, String> params = GourmetCareeUtil.getUrlParams(movieUrl);
			movieUrl = "https://www.youtube.com/embed/".concat(StringUtils.defaultString(params.get("v")));
		}
	}

	/**
	 * 職種のDTO
	 * @author whizz
	 *
	 */
	public static class WebJobDto implements Serializable {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = 435933628733011346L;

		/** 職種 */
		public String jobKbn;
		/** 掲載フラグ */
		public String publicationFlg;
		/** 雇用形態 */
		public String employPtnKbn;
		/** 仕事内容 */
		public String workContents;
		/** 給与体系区分 */
		public String saleryStructureKbn;
		/** 給与金額（下限） */
		public String lowerSalaryPrice;
		/** 給与金額（上限） */
		public String upperSalaryPrice;
		/** 給与 */
		public String salary;
		/** その他条件区分 */
		public List<String> otherConditionKbnList = new ArrayList<>();
		/** 求める人物像 */
		public String personHunting;
		/** 勤務時間 */
		public String workingHours;
		/** 待遇選択 */
		public List<String> treatmentKbnList = new ArrayList<>();
		/** 待遇 */
		public String treatment;
		/** 休日・休暇 */
		public String holiday;
		/** 表示順 */
		public String displayOrder;
		/** 登録済みかどうか */
		public String editFlg;
		/** 加入保険 */
		public String insurance;
		/** 契約期間 */
		public String contractPeriod;
		/** 給与詳細 */
		public String salaryDetail;
		/** 想定初年度年収給与下限 */
		public String annualLowerSalaryPrice;
		/** 想定初年度年収給与上限 */
		public String annualUpperSalaryPrice;
		/** 想定初年度年収フリー欄 */
		public String annualSalary;
		/** 想定初年度年収給与詳細 */
		public String annualSalaryDetail;
		/** 想定初年度月収給与下限 */
		public String monthlyLowerSalaryPrice;
		/** 想定初年度月収給与上限 */
		public String monthlyUpperSalaryPrice;
		/** 想定初年度月収フリー欄 */
		public String monthlySalary;
		/** 想定初年度月収給与詳細 */
		public String monthlySalaryDetail;
		/** 店舗ID */
		public List<String> shopIdList = new ArrayList<>();

		public String getJobKbn() {
			return jobKbn;
		}
		public String getPublicationFlg() {
			return publicationFlg;
		}
		public String getEmployPtnKbn() {
			return employPtnKbn;
		}
		public String getWorkContents() {
			return workContents;
		}
		public String getSaleryStructureKbn() {
			return saleryStructureKbn;
		}
		public String getLowerSalaryPrice() {
			return lowerSalaryPrice;
		}
		public String getUpperSalaryPrice() {
			return upperSalaryPrice;
		}
		public String getSalary() {
			return salary;
		}
		public List<String> getOtherConditionKbnList() {
			return otherConditionKbnList;
		}
		public String getPersonHunting() {
			return personHunting;
		}
		public String getWorkingHours() {
			return workingHours;
		}
		public List<String> getTreatmentKbnList() {
			return treatmentKbnList;
		}
		public String getTreatment() {
			return treatment;
		}
		public String getHoliday() {
			return holiday;
		}
		public String getInsurance() {
			return insurance;
		}
		public String getContractPeriod() {
			return contractPeriod;
		}
		public String getDisplayOrder() {
			return displayOrder;
		}
		public String getEditFlg() {
			return editFlg;
		}
		public List<String> getShopIdList() {
			return shopIdList;
		}
		public String getSalaryDetail() {
			return salaryDetail;
		}
		public String getAnnualLowerSalaryPrice() {
			return annualLowerSalaryPrice;
		}
		public String getAnnualUpperSalaryPrice() {
			return annualUpperSalaryPrice;
		}
		public String getAnnualSalary() {
			return annualSalary;
		}
		public String getAnnualSalaryDetail() {
			return annualSalaryDetail;
		}
		public String getMonthlyLowerSalaryPrice() {
			return monthlyLowerSalaryPrice;
		}
		public String getMonthlyUpperSalaryPrice() {
			return monthlyUpperSalaryPrice;
		}
		public String getMonthlySalary() {
			return monthlySalary;
		}
		public String getMonthlySalaryDetail() {
			return monthlySalaryDetail;
		}
	}

}
