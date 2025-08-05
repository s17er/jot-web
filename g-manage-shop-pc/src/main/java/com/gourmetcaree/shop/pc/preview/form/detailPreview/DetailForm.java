package com.gourmetcaree.shop.pc.preview.form.detailPreview;

import static org.apache.commons.lang.BooleanUtils.*;
import static org.apache.commons.lang.StringUtils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.util.ResourceUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ApplicationFormKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.MovieFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.shop.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.preview.form.listPreview.PreviewBaseForm;

/**
 * プレビュー詳細のフォーム
 * @author Takahiro Ando
 * @version 1.0
 */
public class DetailForm extends PreviewBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3606564643489607837L;

	/** 原稿番号(パラメータ渡しのためString) */
	public String id;

	/** 表示用エリアコード */
	public String siteAreaCd;

	/** アクセスコード */
	public String accessCd;

	/** サイズ区分 */
	public int sizeKbn;

	/** 募集職種 */
	public String recruitmentJob;

	/** 募集業種 */
	public String recruitmentIndustry;

	/** 号数ID */
	public int volumeId;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 原稿名 */
	public String manuscriptName;

	/** 応募フォームフラグ */
	public int applicationFormKbn;

	/** 動画フラグ */
	public int movieFlg;

	/** 所属（会社） */
	public int companyId;

	/** 雇用形態区分 */
	public List<String> employPtnList = new ArrayList<String>();

	/** 職種検索条件区分 */
	public List<String> jobKbnList = new ArrayList<String>();

	/** 業種区分1 */
	public int industryKbn1;

	/** 業種区分2 */
	public int industryKbn2;

	/** 業種区分3 */
	public int industryKbn3;

	/** 待遇検索条件区分 */
	public List<String> treatmentKbnList = new ArrayList<String>();

	/** その他条件区分 */
	public List<String> otherConditionKbnList = new ArrayList<String>();

	/** 店舗数区分 */
	public String shopsKbn;

	/** 仕事内容 */
	public String workContents;

	/** 給与 */
	public String salary;

	/** 求める人物像 */
	public String personHunting;

	/** 勤務時間 */
	public String workingHours;

	/** 勤務地エリア・最寄駅 */
	public String workingPlace;

	/** 勤務地詳細 */
	public String workingPlaceDetail;

	/** 待遇 */
	public String treatment;

	/** 休日休暇 */
	public String holiday;

	/** 客席数・坪数 */
	public String seating;

	/** 客単価 */
	public String unitPrice;

	/** 営業時間 */
	public String businessHours;

	/** オープン日 */
	public String openingDay;

	/** 会社（店舗）情報 */
	public String shopInformation;

	/** ホームページ１ */
	public String homepage1;

	/** ホームページコメント1 */
	public String homepageComment1;

	/** ホームページ２ */
	public String homepage2;

	/** ホームページコメント2 */
	public String homepageComment2;

	/** ホームページ３ */
	public String homepage3;

	/** ホームページコメント3 */
	public String homepageComment3;

	/** 電話番号/受付時間 */
	public String phoneReceptionist;

	/** 応募方法 */
	public String applicationMethod;

	/** 面接地住所/交通 */
	public String addressTraffic;

	/** 地図タイトル */
	public String mapTitle;

	/** 地図用住所 */
	public String mapAddress;

	/** メッセージ */
	public String message;

	/** キャッチコピー1 */
	public String catchCopy1;

	/** 文章1 */
	public String sentence1;

	/** キャッチコピー2 */
	public String catchCopy2;

	/** 文章2 */
	public String sentence2;

	/** キャッチコピー3 */
	public String catchCopy3;

	/** 文章3 */
	public String sentence3;

	/** 文章4 */
	public String sentence4;

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

	/** 人事担当者より */
	public String comment;

	/** トップインタビューURL */
	public String topInterviewUrl;

	/** 合同説明会参加区分 */
	public Integer briefingPresentKbn;

	/** SEO用文言（h1タグ用） */
	public String seoH1;

	/** SEO用文言（title用） */
	public String seoTitle;

	/** SEO用文言（keywords用） */
	public String seoKeywords;

	/** SEO用文言（description用） */
	public String seoDescription;

	/** 画像の存在有無を保持するMap */
	public Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();

	/** エリアコード(デフォルトは首都圏) */
	public String areaCd = Integer.toString(MAreaConstants.AreaCd.SHUTOKEN_AREA);

	/** 公開側のURLのルートパスを取得します。*/
	public String frontHttpUrl;

	/** WMVのファイル名 */
	public String wmMovieName;

	/** クイックタイムのファイル名 */
	public String qtMovieName;

	/** プレビューの種類を保持するEnum */
	public PreviewMethodKbn previewMethodKbn;

	/** 画像のユニークキーを保持するMap */
	public Map<String, String> imageUniqueKeyMap = new HashMap<String, String>();

	/** 基本カラー(デフォルトは首都圏) */
	public String SITE_COLOR = "#ff9900";

	/** 基本文字カラー(デフォルトは首都圏) */
	public String SITE_TEXT_COLOR = "#000000";

	/** 基本絵文字(デフォルトは首都圏) */
	public String SITE_EMOJI = "/i/emoji/images/";

	/** 基本リンクカラー(デフォルトは首都圏) */
	public String SITE_TEXT_LINK_COLOR = "#FF6600";

	/** 地図のズーム値 */
	public String zoom;





	/** 店舗一覧名 */
	public String shopName;

	/** 店舗見学区分 */
	public int observationKbn;

	/** 店舗表示フラグ */
	public boolean shopListDisplayFlg;

	/** 最初の店舗一覧ID */
	public int firstShopListId;

	/** 店舗一覧用業態1 */
	public int shopListIndustryKbn1;

	/** 店舗一覧用業態2 */
	public int shopListIndustryKbn2;

	/** 関連する店舗一覧のリスト */
	public List<RelationShopListDto> relationShopList;

	/** 店舗一覧のID */
	public String shopListId;

	/** 店舗一覧画像の存在有無を保持するMap */
	public Map<String, Boolean> shopListImageCheckMap = new HashMap<String, Boolean>();

	/** 店舗一覧画像のユニークキーを保持するMap */
	public Map<String, String> shopListUniqueKeyMap = new HashMap<String, String>();

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

	/** スタッフ */
	public String staff;

	/** URL1 */
	public String url1;

	/** 求人募集フラグ */
	public Integer jobOfferFlg;

	/** 住所1(番-号まで) */
	public String address1;

	/** 住所2(ビル名など) */
	public String address2;

	public List<String> employPtnKbnList;

	/** バイトプレビューパス */
	public String arbeitPreviewPath;

	/** 動画URL */
	public String movieUrl;

	/** 動画コメント */
	public String movieComment;

	public String shopHoliday;

	/** バイト都道府県ID */
	public String arbeitTodouhukenId;

	/** バイト市区町村ID */
	public String arbeitAreaId;

	/** バイトサブエリア */
	public String arbeitSubAreaId;

	/** 路線一覧 */
	public List<TShopListRoute> routeList;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		id = null;
		siteAreaCd = "1";
		accessCd = null;
		sizeKbn = 0;
		recruitmentJob = null;
		recruitmentIndustry = null;
		volumeId = 0;
		postStartDatetime = null;
		postEndDatetime = null;
		manuscriptName = null;
		applicationFormKbn = 0;
		movieFlg = 0;
		companyId = 0;
		employPtnList = new ArrayList<String>();
		jobKbnList = new ArrayList<String>();
		industryKbn1 = 0;
		industryKbn2 = 0;
		industryKbn3 = 0;
		treatmentKbnList = new ArrayList<String>();
		otherConditionKbnList = new ArrayList<String>();
		shopsKbn = null;
		workContents = null;
		salary = null;
		personHunting = null;
		workingHours = null;
		workingPlace = null;
		workingPlaceDetail = null;
		treatment = null;
		holiday = null;
		seating = null;
		unitPrice = null;
		businessHours = null;
		openingDay = null;
		shopInformation = null;
		homepage1 = null;
		homepageComment1 = null;
		homepage2 = null;
		homepageComment2 = null;
		homepage3 = null;
		homepageComment3 = null;
		phoneReceptionist = null;
		applicationMethod = null;
		addressTraffic = null;
		mapTitle = null;
		mapAddress = null;
		message = null;
		catchCopy1 = null;
		sentence1 = null;
		catchCopy2 = null;
		sentence2 = null;
		catchCopy3 = null;
		sentence3 = null;
		sentence4 = null;
		captionA = null;
		captionB = null;
		captionC = null;
		attentionHereTitle = null;
		attentionHereSentence = null;
		comment = null;
		topInterviewUrl = null;
		seoH1 = null;
		seoTitle = null;
		seoKeywords = null;
		seoDescription = null;
		imageCheckMap = new HashMap<String, Boolean>();
		areaCd = Integer.toString(MAreaConstants.AreaCd.SHUTOKEN_AREA);
		frontHttpUrl = null;
		wmMovieName = null;
		qtMovieName = null;
		previewMethodKbn = null;
		imageUniqueKeyMap = new HashMap<String, String>();
		SITE_COLOR = "#ff9900";
		SITE_TEXT_COLOR = "#000000";
		shopName = null;
		observationKbn = 0;
		shopListDisplayFlg = false;
		firstShopListId = 0;
		shopListIndustryKbn1 = 0;
		shopListIndustryKbn2 = 0;
		relationShopList = null;
		shopListId = null;
		shopListImageCheckMap = new HashMap<String, Boolean>();
		shopListUniqueKeyMap = new HashMap<String, String>();
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		faxNo1 = null;
		faxNo2 = null;
		faxNo3 = null;
		transit = null;
		staff = null;
		url1 = null;
		jobOfferFlg = null;
		address1 = null;
		address2 = null;
		employPtnKbnList = null;
		// google map の拡大初期値をセット
		zoom = StringUtils.defaultString(ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gourmetcaree.googleMap.zoom"), "15");

		SITE_EMOJI = "/i/emoji/images/";
		SITE_TEXT_LINK_COLOR = "#FF6600";

		arbeitPreviewPath = null;

		arbeitTodouhukenId = null;
		arbeitAreaId = null;
		arbeitSubAreaId = null;
		routeList = null;
	}

    /**
     * 業種のカラム1～3に入った値を配列に変換して取得します。
     * @return
     */
    public int[] getIndustryKbnList() {
		List<Integer> industryList = new ArrayList<Integer>();

		if (industryKbn1 != 0) {
			industryList.add(industryKbn1);
		}
		if (industryKbn2 != 0) {
			industryList.add(industryKbn2);
		}
		if (industryKbn3 != 0) {
			industryList.add(industryKbn3);
		}

		return ArrayUtils.toPrimitive(industryList.toArray(new Integer[0]));
    }

    /**
     * サイズ区分がABCであればtrueを返します。
     * @return
     */
    public boolean isSizeKbnABC () {
    	return sizeKbn == SizeKbn.A
    			|| sizeKbn == SizeKbn.B
    			|| sizeKbn == SizeKbn.C ? true : false;
    }

    /**
     * サイズ区分がDEであればtrueを返します。
     */
    public boolean isSizeKbnDE() {
    	return sizeKbn == SizeKbn.D
    			|| sizeKbn == SizeKbn.E ? true : false;
    }

    /**
     * 募集要項に表示される情報が1件でも存在するかを取得します。
     * @return true:存在する、false:存在しない
     */
    public boolean isRecruitInformationExist() {

    	if (isNotBlank(recruitmentJob)) {
    		return true;
    	} else if (isNotBlank(workContents)) {
    		return true;
    	} else if (isNotBlank(salary)) {
    		return true;
    	} else if (isNotBlank(personHunting)) {
    		return true;
    	} else if (isNotBlank(workingHours)) {
    		return true;
    	} else if (isNotBlank(workingPlace)) {
    		return true;
    	} else if (isNotBlank(workingPlaceDetail)) {
    		return true;
    	} else if (isNotBlank(treatment)) {
    		return true;
    	} else if (isNotBlank(holiday)) {
    		return true;
    	}

    	return false;
    }

    /**
     * ホームページに表示される情報が1件でも存在するかを取得します。
     * @return true:存在する、false:存在しない
     */
    public boolean isAnyHomepageAddressExist() {
    	if (isNotBlank(homepage1)) {
    		return true;
    	} else if (isNotBlank(homepage2)) {
    		return true;
    	} else if (isNotBlank(homepage3)) {
    		return true;
    	}

    	return false;
    }

    /**
     * お店情報に表示される情報が1件でも存在するかを取得します。
     * @return true:存在する、false:存在しない
     */
    public boolean isShopDataExist() {
    	if (isNotBlank(seating)) {
    		return true;
    	} else if (isNotBlank(unitPrice)) {
    		return true;
    	} else if (isNotBlank(businessHours)) {
    		return true;
    	} else if (isNotBlank(openingDay)) {
    		return true;
    	} else if (isNotBlank(shopInformation)) {
    		return true;
    	} else if (isAnyHomepageAddressExist()) {
    		return true;
    	}

    	return false;
    }

    /**
     * 応募に表示される情報が1件でも存在するかを取得します。
     * @return true:存在する、false:存在しない
     */
    public boolean isApplicationDataExist() {

    	if (isNotBlank(phoneReceptionist)) {
    		return true;
    	} else if (isNotBlank(applicationMethod)) {
    		return true;
    	} else if (isNotBlank(addressTraffic)) {
    		return true;
    	}

    	return false;
    }

    /**
     * 応募できるかどうかを取得します。
     * @return
     */
	public boolean isApplicationOkFlg() {
    	if (applicationFormKbn == ApplicationFormKbn.EXIST) {
    		return true;
    	}

    	return false;
    }


    /**
     * ここに注目ブロックを表示するかどうかを取得します。
     */
    public boolean isBlockAttentionHere() {
    	if (sizeKbn != SizeKbn.TEXT_WEB && sizeKbn != SizeKbn.A) {
    		return true;
    	}

    	return false;
    }

    /**
     * ここに注目ブロックに情報があるかどうかを取得します。
     */
    public boolean isBlockAttentionHereExist() {
    	if(isNotBlank(attentionHereSentence) && isTrue(imageCheckMap.get(MaterialKbn.ATTENTION_HERE))) {
    		return true;
    	}

    	return false;
    }

    /**
     * 中段ブロック(右に縦長の写真があるブロック)を表示できるかどうかを取得します。
     */
    public boolean isBlockRight() {
    	return sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E;
    }

    /**
     * 右写真エリアに情報が存在するかを取得します。
     */
    public boolean isBlockRightExist() {
    	if (sizeKbn == SizeKbn.C) {
    		return isNotBlank(sentence2) ? true : false;
    	}
    	if (sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {
    		return isNotBlank(sentence2) || isTrue(imageCheckMap.get(MaterialKbn.RIGHT)) ? true : false;
    	}

    	return false;
    }

    /**
     * キャプションブロックを表示するかどうかを取得します。
     */
    public boolean isBlockCaption() {
    	if (sizeKbn == SizeKbn.C
    			|| sizeKbn == SizeKbn.D
    			|| sizeKbn == SizeKbn.E) {
    		return true;
    	}

    	return false;
    }

    /**
     * キャプションブロックにデータがあるかを取得します
     */
    public boolean isBlockCaptionExist() {
    	if (isTrue(imageCheckMap.get(MaterialKbn.PHOTO_A))
    			|| isTrue(imageCheckMap.get(MaterialKbn.PHOTO_B))
    			|| isTrue(imageCheckMap.get(MaterialKbn.PHOTO_C))) {
    		return true;
    	}

    	return false;
    }

    /**
     * タブ[メッセージ]を表示するかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isTabMessageDisplayFlg() {
//    	if (sizeKbn == SizeKbn.F) {
//    		return true;
//    	}

    	return false;
    }

    /**
     * タブ[動画]を表示するかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isTabMovieDisplayFlg() {
    	if (movieFlg == MovieFlg.EXIST) {
    		if (isNotBlank(wmMovieName)) {
    			return true;
    		} else if (isNotBlank(qtMovieName)) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * データのサイズにより[ロゴ画像ブロック]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockLogo() {
    	return (sizeKbn != SizeKbn.TEXT_WEB) ? true : false;
    }

    /**
     * データのサイズからトップイメージ部分を表示できるかどうかを取得します。
     */
    public boolean isBlockMainImage() {
    	if (sizeKbn == SizeKbn.TEXT_WEB) {
    		return false;
    	}
    	return true;
    }

    /**
     * メインイメージブロックに情報があるかどうかを取得します。
     */
    public boolean isBlockMainImageExist() {
    	if (isSizeKbnABC()) {
    		return isTrue(imageCheckMap.get(MaterialKbn.MAIN_1)) ? true : false;
    	}

    	if (isSizeKbnDE()) {
    		return isTrue(imageCheckMap.get(MaterialKbn.MAIN_1))
    				|| isTrue(imageCheckMap.get(MaterialKbn.MAIN_2))
    				|| isTrue(imageCheckMap.get(MaterialKbn.MAIN_3)) ? true : false;
    	}

    	return false;
    }



    /**
     * データのサイズにより[文章1ブロック]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockSentence1() {
    	return (sizeKbn != SizeKbn.TEXT_WEB) ? true : false;
    }



    /**
     * 文章1のブロックにデータがあるかどうかを取得
     * @return
     */
    public boolean isBlockSentence1NotEmpty() {

    	if (imageCheckMap == null) {
    		return false;
    	} else if (
    		isNotBlank(catchCopy1)
    		|| isNotBlank(sentence1)) {

			return true;
    	} else {
    		return false;
    	}
    }

    /**
     * データのサイズにより[文章2ブロック]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockSentence2() {
    	return (sizeKbn != SizeKbn.A
    			&& sizeKbn != SizeKbn.B) ? true : false;
    }

    /**
     * 文章2のブロックにデータがあるかどうかを取得
     * @return
     */
    public boolean isBlockSentence2NotEmpty() {

    	if (imageCheckMap == null) {
    		return false;
    	} else if (isTrue(imageCheckMap.get(MaterialKbn.MAIN_2))
    		|| isNotBlank(catchCopy2)
    		|| isNotBlank(sentence2)) {

			return true;
    	} else {
    		return false;
    	}
    }

    /**
     * データのサイズにより[文章3ブロック]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockSentence3() {
    	return sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E;
    }

    /**
     * 文章3のブロックにデータがあるかどうかを取得
     * @return
     */
    public boolean isBlockSentence3NotEmpty() {

    	if (imageCheckMap == null) {
    		return false;
    	} else if (
    		isNotBlank(sentence3)) {

			return true;
    	} else {
    		return false;
    	}
    }

    /**
     * データのサイズにより[文章4ブロック]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockSentence4() {
    	return (sizeKbn == SizeKbn.E
//    			|| sizeKbn == SizeKbn.F
    			) ? true : false;
    }

    /**
     * データのサイズにより[写真A~Cの]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockPhoto() {
    	return (sizeKbn == SizeKbn.E
    				|| sizeKbn == SizeKbn.D
    			) ? true : false;
    }

	/**
	 * 画像のズーム値を変更します。
	 * @param value
	 */
	public void zoomMap(int value) {

		int zoomValue = 0;

		try {
			zoomValue = Integer.parseInt(zoom);
			if (StringUtils.isBlank(zoom)) {
				zoomValue = NumberUtils.toInt(ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gourmetcaree.googleMap.zoom"), 15);
			}

		// 数値で無い場合は初期値をセット
		} catch (NumberFormatException e) {
			zoomValue = NumberUtils.toInt(ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gourmetcaree.googleMap.zoom"), 15);
		}

		zoom = String.valueOf(zoomValue + value);
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
	 * 詳細ページ2にデータがあるかを取得
	 * @return
	 */
	public boolean isDetail2DataExist() {
		if (sizeKbn == SizeKbn.TEXT_WEB) {
			return false;
		}

		if (isSizeKbnDE()) {
			if (isCatchCopyExist()
					|| isBlockMainImageExist()
					|| isNotBlank(sentence1)
					|| isNotBlank(sentence3)
					|| isBlockAttentionHereExist()) {
				return true;
			}
		}

		if (sizeKbn == SizeKbn.C) {
			if (isCatchCopyExist()
					|| isBlockMainImageExist()
					|| isNotBlank(sentence1)
					|| isNotBlank(sentence2)
					|| isBlockAttentionHereExist()) {
				return true;
			}
		}

		if(sizeKbn == SizeKbn.B) {
			if (isCatchCopyExist()
					|| isBlockMainImageExist()
					|| isNotBlank(sentence1)
					|| isBlockAttentionHereExist()) {
				return true;
			}
		}

		if(sizeKbn == SizeKbn.A) {
			if (isCatchCopyExist()
					|| isBlockMainImageExist()
					|| isNotBlank(sentence1)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 詳細ページ3にデータがあるかを取得
	 */
	public boolean isDetail3DataExist() {
		if (isSizeKbnDE() && isBlockRightExist()) {
			return true;
		}
		return false;
	}

    /**
     * ホームページ1の表示
     * @return
     */
    public String getDisplayHomepage1() {
    	if (isNotBlank(homepageComment1)) {
    		return homepageComment1;
    	}
    	return homepage1;
    }

    /**
     * ホームページ2の表示
     * @return
     */
    public String getDisplayHomepage2() {
    	if (isNotBlank(homepageComment2)) {
    		return homepageComment2;
    	}
    	return homepage2;
    }

    /**
     * ホームページ3の表示
     * @return
     */
    public String getDisplayHomepage3() {
    	if (isNotBlank(homepageComment3)) {
    		return homepageComment3;
    	}
    	return homepage3;
    }

    /**
     * 緯度経度かどうか
     * @return
     */
    public boolean isLatLngAddress() {
    	if (StringUtils.isBlank(mapAddress)) {
    		return false;
    	}

    	String[] latLng = mapAddress.split(GourmetCareeConstants.KANMA_STR);
    	if (latLng.length != 2) {
    		return false;
    	}

    	for (String condition : latLng) {
    		if (!NumberUtils.isNumber(GourmetCareeUtil.superTrim(condition))) {
    			return false;
    		}
    	}

    	return true;
    }

    /**
     * 地図用住所を緯度経度に変換して取得する。(全角スペース回避のため)
     * @return
     */
    public String getConvertMapAddressToLatLng() {
    	if (StringUtils.isBlank(mapAddress)) {
    		return null;
    	}
    	String[] latLng = mapAddress.split(GourmetCareeConstants.KANMA_STR);
    	if (latLng.length != 2) {
    		return null;
    	}
    	return GourmetCareeUtil.superTrim(latLng[0]) + GourmetCareeConstants.KANMA_STR + GourmetCareeUtil.superTrim(latLng[1]);
    }

    /**
     * 応募ボタンブロックを表示するかどうか
     * @return
     */
    public boolean isApplicationButtonBlock() {
    	if (MTypeConstants.ApplicationFormKbn.NON == applicationFormKbn
    			&& MTypeConstants.ObservationKbn.ADMIN_NONE == observationKbn) {
    		return false;
    	}

    	return true;
    }







    /**
     * 電話番号があるかどうか
     * @return
     */
    public boolean isPhoneNoExist() {
    	if (StringUtils.isBlank(phoneNo1)
    			&& StringUtils.isBlank(phoneNo2)
    			&& StringUtils.isBlank(phoneNo3)) {
    		return false;
    	}
    	return true;
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
	 * 募集要項があるかないか
	 * @return ある場合にtrue
	 */
	public boolean isRecruitmentDataExist(){
		if (StringUtils.isNotBlank(recruitmentJob)
			|| StringUtils.isNotBlank(salary)
			|| StringUtils.isNotBlank(personHunting)
			|| StringUtils.isNotBlank(workingHours)
			|| StringUtils.isNotBlank(workingPlace)
			|| StringUtils.isNotBlank(workingPlaceDetail)
			|| StringUtils.isNotBlank(treatment)
			|| StringUtils.isNotBlank(holiday)) {

			return true;
		}

		return false;
	}


	/**
	 * お店・会社DATAがあるかどうか(スマホ用)
	 * @return ある場合にtrue
	 */
	public boolean isShopDataExistForSmart() {
		if (StringUtils.isNotBlank(seating)
			|| StringUtils.isNotBlank(unitPrice)
			|| StringUtils.isNotBlank(businessHours)
			|| StringUtils.isNotBlank(openingDay)
			|| StringUtils.isNotBlank(shopInformation)
			|| StringUtils.isNotBlank(homepage1)
			|| StringUtils.isNotBlank(homepage2)
			|| StringUtils.isNotBlank(homepage3)
			){

			return true;
		}
		return false;
	}


    /**
     * 応募に表示される情報が1件でも存在するかを取得します。
     * @return true:存在する、false:存在しない
     */
    public boolean isApplicationDataExistForSmart() {

    	if (StringUtils.isNotBlank(phoneReceptionist)
    		|| StringUtils.isNotBlank(applicationMethod)
    		|| StringUtils.isNotBlank(addressTraffic)
    		|| StringUtils.isNotBlank(mapAddress)) {

    		return true;
    	}

    	return false;
    }


    /**
	 * 店のデータがあるかないか
	 * @return ある場合true
	 */
	public boolean isShopMessageExist() {
		boolean displayFlg;

		if (StringUtils.isNotBlank(movieUrl)) {
			return true;
		}

		if (isTrue(imageCheckMap.get(MaterialKbn.LOGO))
			|| isTrue(imageCheckMap.get(MaterialKbn.MAIN_1))
			|| isCatchCopyExist()
			|| StringUtils.isNotBlank(sentence1)
			) {
			displayFlg = true;
		} else {
			displayFlg = false;
		}

		if (SizeKbn.A == sizeKbn || SizeKbn.TEXT_WEB == sizeKbn) {
			return displayFlg;
		}


		displayFlg = displayFlg || isBlockAttentionHereExist() ? true: false;

		if (SizeKbn.B == sizeKbn) {
			return displayFlg;
		}

		displayFlg = displayFlg || isCaptionExist() ? true : false;

		if (SizeKbn.C == sizeKbn) {
			return displayFlg || isNotBlank(sentence2) ? true : false;
		}

		displayFlg = displayFlg || isBlockRightExistForSmart() ? true : false;

		if (isSizeKbnDE()) {
			return displayFlg;
		}

		return false;
	}

    /**
     * キャプションがあるかどうかを取得(スマホ用)
     * @return
     */
    public boolean isCaptionExist () {
    	if (isTrue(imageCheckMap.get(MaterialKbn.PHOTO_A))
    			|| isTrue(imageCheckMap.get(MaterialKbn.PHOTO_B))
    			|| isTrue(imageCheckMap.get(MaterialKbn.PHOTO_C))) {

    		return true;
    	}

    	return false;
    }


    /**
     * 右写真エリアに情報が存在するかを取得します。
     */
    public boolean isBlockRightExistForSmart() {

    	if (sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {
    		return isNotBlank(sentence2) || isTrue(imageCheckMap.get(MaterialKbn.RIGHT)) ? true : false;
    	}
    	if (sizeKbn == SizeKbn.C) {
    		return isNotBlank(sentence2) ? true : false;
    	}

    	return false;
    }

    public boolean isPluralMainImage() {
    	int imageNum = 0;
    	if (isTrue(imageCheckMap.get(MaterialKbn.MAIN_1))) {
    		imageNum++;
    	}

    	if (isTrue(imageCheckMap.get(MaterialKbn.MAIN_2))) {
    		imageNum++;
    	}

    	if (isTrue(imageCheckMap.get(MaterialKbn.MAIN_3))) {
    		imageNum++;
    	}

    	return imageNum > 1;
    }


    /**
     * スマホ用マップタイトルの取得
     */
    public String getMapTitleForSmart() {
    	StringBuilder sb = new StringBuilder("");
    	if (StringUtils.isNotBlank(mapTitle)){
    		sb.append(mapTitle);
    		sb.append("／");
    	}
    	sb.append("地図を見る");
    	return sb.toString();
    }


    /**
     * 右写真エリアを表示するかどうかを取得します
     * @return
     */
    public boolean isBlockRightForSmart() {
    	return sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E;
    }


}
