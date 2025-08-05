package com.gourmetcaree.admin.pc.webdata.form.webdata;

import java.util.ArrayList;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.service.dto.CustomerSearchDto;
import com.gourmetcaree.common.dto.WebRouteDto;

/**
 * WEBデータ編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends WebdataForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5838619843342264959L;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	@Override
	public ActionMessages validate() {
		return super.validate();
	}

	/**
	 * フォームのリセットを行う
	 */
	@Override
	public void resetForm() {

		super.resetBaseForm();
		super.resetForm();
	}

	/**
	 * 削除項目以外のリセットを行う
	 */
	public void resetFormWithoutStatusValue() {

		// 画面ID
		displayId = null;

		// 画面表示の可否フラグ trueであれば表示：falseであれば非表示
		existDataFlg = true;

		// プロセスフラグ
		processFlg = false;

		// 画面に保持するId
		hiddenId = null;

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
//		// 誌面原稿番号
//		magazineManuscriptNo = null;
		// 過去原版（コピー元WEBID）
		sourceWebId = null;
		// 原稿名
		manuscriptName = null;
		// サイズ区分
		sizeKbn = null;
		// 応募フォームフラグ
		applicationFormKbn = null;
		// 特集
		specialIdList.clear();
		// 動画フラグ
		movieFlg = null;
		// 勤務地エリア(WEBエリアから名称変更)区分
		webAreaKbnList.clear();
		// 勤務地詳細エリア
		detailAreaKbnList.clear();
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
		// 所属（会社）
		companyId = null;
		// 営業担当
		salesId = null;
		//求人識別番号
		webNo = null;
		// 雇用形態区分
		employPtnKbnList.clear();
		// 職種検索条件区分
		jobKbnList.clear();
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
		// 適職診断区分
		reasonableKbn = null;
		// トップインタビューURL
		topInterviewUrl = null;
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
		// 勤務地エリア・最寄駅
		workingPlace = null;
		// 勤務地詳細
		workingPlaceDetail = null;
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
		// ホームページ２
		homepage2 = null;
		// ホームページ３
		homepage3 = null;
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
		// メッセージ
		message = null;
		// TOP画像
		if (topImg != null) {
			topImg.destroy();
			topImg = null;
		}
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
		// 動画(WM)
		wmMovieName = null;
		// 動画(QT)
		qtMovieName = null;
//		// キャッチコピー1
//		catchCopy1 = null;
//		// 文章1
//		sentence1 = null;
//		// キャッチコピー2
//		catchCopy2 = null;
//		// 文章2
//		sentence2 = null;
//		// キャッチコピー3
//		catchCopy3 = null;
//		// 文章3
//		sentence3 = null;
//		// 文章4
//		sentence4 = null;
		// メインキャッチコピー
		catchCopy1 = null;
		// メインテキスト
		sentence1 = null;
		// 中段テキスト
		sentence2 = null;
		// キャプションA
		captionA = null;
		// キャプションB
		captionB = null;
		// キャプションC
		captionC = null;
//		// 人事担当者より
//		comment = null;
		// 注目店舗フラグ
		attentionShopFlg = null;
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
//		// 誌面フラグ
//		magazineFlg = null;
		// 素材のアップロード操作用パラメータ
		hiddenMaterialKbn = null;
		// src属性などから呼ばれる際の素材Mapのキー
		targetMaterialKey = null;
		// 素材を保持するマップ
		materialMap.clear();
		// 号数編集可否フラグ
		isEditVolumeFlg = true;
		// カーソル位置
		cursorPosition = null;

//		Beans.copy(new DeleteForm(), this).excludes("id", "displayStatus", "changeStatusKbn", "version").execute();

	}
	/**
	 * ID以外のリセットを行う
	 */
	public void resetFormWithoutId() {

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
		//ステータス
		displayStatus = null;
//		// 誌面原稿番号
//		magazineManuscriptNo = null;
		// 過去原版（コピー元WEBID）
		sourceWebId = null;
		// 原稿名
		manuscriptName = null;
		// サイズ区分
		sizeKbn = null;
		// 応募フォームフラグ
		applicationFormKbn = null;
		// 特集
		specialIdList.clear();
		// 動画フラグ
		movieFlg = null;
		// 勤務地エリア区分
		webAreaKbnList.clear();
		// 勤務地詳細エリア
		detailAreaKbnList.clear();
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
		// 所属（会社）
		companyId = null;
		// 営業担当
		salesId = null;
		// 求人識別番号
		webNo = null;
		// 雇用形態区分
		employPtnKbnList.clear();
		// 職種検索条件区分
		jobKbnList.clear();
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
		// 適職診断区分
		reasonableKbn = null;
		// トップインタビューURL
		topInterviewUrl = null;
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
		// 勤務地エリア・最寄駅
		workingPlace = null;
		// 勤務地詳細
		workingPlaceDetail = null;
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
		// ホームページ２
		homepage2 = null;
		// ホームページ３
		homepage3 = null;
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
		// メッセージ
		message = null;
		// TOP画像
		if (topImg != null) {
			topImg.destroy();
			topImg = null;
		}
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
		// 動画(WM)
		wmMovieName = null;
		// 動画(QT)
		qtMovieName = null;
//		// キャッチコピー1
//		catchCopy1 = null;
//		// 文章1
//		sentence1 = null;
//		// キャッチコピー2
//		catchCopy2 = null;
//		// 文章2
//		sentence2 = null;
//		// キャッチコピー3
//		catchCopy3 = null;
//		// 文章3
//		sentence3 = null;
//		// 文章4
//		sentence4 = null;
		// メインキャッチコピー
		catchCopy1 = null;
		// メインテキスト
		sentence1 = null;
		// 中段テキスト
		sentence2 = null;
		// キャプションA
		captionA = null;
		// キャプションB
		captionB = null;
		// キャプションC
		captionC = null;
//		// 人事担当者より
//		comment = null;
		// 注目店舗フラグ
		attentionShopFlg = null;
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
//		// 誌面フラグ
//		magazineFlg = null;
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

//		Beans.copy(new EditForm(), this).excludes("id").execute();
	}
}
