package com.gourmetcaree.common.dto;

import static org.apache.commons.lang.BooleanUtils.isTrue;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.MovieFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;


/**
 * プレビュー表示に使用するDtoです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class PreviewDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4543523587875786314L;

	private static final String ARI = "有";
	private static final String NASHI = "無";

	/** 素材(動画)の状態が不正な場合のコンテントタイプ */
	public static int WRONG_TYPE;

	/** エリアコード */
	public int areaCd;

	/** 顧客ID */
	public String customerId;

	/** 原稿番号(パラメータ渡しのためString) */
	public int id;

	/** サイズ区分 */
	public int sizeKbn;

	/** 募集職種 */
	public String recruitmentJob;

	/** 号数ID */
	public String volumeId;

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

	/** トップインタビューURL */
	public String topInterviewUrl;

	/** 合同説明会参加区分 */
	public Integer briefingPresentKbn;

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

	/** 座席数 */
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

	/** 画像の存在有無を保持するMap */
	public Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();

    /** 素材：トップ画像存在フラグ（存在していればtrue） */
    public boolean materialSearchListExistFlg = false;

    /** 素材：メイン画像2の存在フラグ（存在していればtrue） */
	public boolean materialCaptionAExistFlg = false;

	/** 素材：メイン画像3の存在フラグ（存在していればtrue） */
	public boolean materialCaptionBExistFlg = false;

	/** WMVのファイル名 */
	public String wmMovieName;

	/** クイックタイムのファイル名 */
	public String qtMovieName;

	/** 店舗見学区分 */
	public int observationKbn;

	/** 店舗一覧表示区分 */
	public String shopListDisplayKbn;

	/** 募集業態 */
	public String recruitmentIndustry;





	/** 特集 */
	public List<String> specialIdList = new ArrayList<String>();

	/** 勤務地エリア(WEBエリアから名称変更)区分 */
	public List<String> webAreaKbnList = new ArrayList<String>();

	/** 海外エリア区分 */
	public List<String> foreignAreaKbnList = new ArrayList<String>();

	/** 勤務地詳細エリア区分 */
	public List<String> detailAreaKbnList = new ArrayList<String>();

	/** WEBデータ路線図リスト */
	public  List<WebRouteDto> webRouteList = new ArrayList<WebRouteDto>();

	/** 応募メール送信先 */
	public String mail;

	/** 顧客のサブメールアドレス */
	public String customerSubMailAddress;

	/** 注目店舗フラグ */
	public Integer attentionShopFlg;

	/** 注目店舗文章 */
	public String attentionShopSentence;

	/** 入力/編集からのフラグ */
	public boolean fromInputFlg;

	/** 鉄道会社 */
	public String railroadId;

	/** 路線 */
	public String routeId;

	/** 駅 */
	public String stationId;

	/** 応募メール送信先 */
	public String communicationMailKbn;

	/** IP電話リスト */
	public List<String> ipPhoneList;

	/** 電話番号リスト(電話番号/受付時間 から正規表現で電話番号を抽出したもの) */
	public List<String> telList;

	/** IP電話に変換する前の顧客電話リスト */
	public List<String> customerTelList;

	/** 動画URL */
	public String movieUrl;

	/** 動画コメント */
	public String movieComment;


	/**
	 * 一覧に表示される表示項目が１つでも存在しているかを取得します。
	 * @return true:1つでも存在する、false:存在しない
	 */
	public boolean isAnyListInformation() {
		if (isNotBlank(recruitmentJob)) {
			return true;
		} else if (isNotBlank(salary)) {
			return true;
		} else if (isNotBlank(personHunting)) {
			return true;
		} else if (isNotBlank(workingPlace)) {
			return true;
		}

		return false;
	}

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		Beans.copy(new PreviewDto(), this).execute();
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
     * 一覧に表示されるサブ画像1,2 どちらかが存在しているかを取得。
     */
    public boolean isCaptionMaterialExist() {
    	return materialCaptionAExistFlg || materialCaptionBExistFlg;
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
     * 「ホームページ」ブロックに表示される情報が1件でも存在するかを取得します。
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
     * 「お店情報」ブロックに表示される情報が1件でも存在するかを取得します。
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
    	} else if (isNotBlank(message)) {
    		return true;
    	}

    	return false;
    }

    /**
     * 「応募」ブロックに表示される情報が1件でも存在するかを取得します。
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
     * タブ[メッセージ]を表示するかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isTabMessageDisplayFlg() {
    	if (sizeKbn == SizeKbn.TEXT_WEB) {
    		return true;
    	}

    	return false;
    }

    /**
     * タブ[動画]を表示するかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isTabMovieDisplayFlg() {
    	if (movieFlg == MovieFlg.EXIST) {
    		if (imageCheckMap.get(MaterialKbn.MOVIE_WM) || imageCheckMap.get(MaterialKbn.MOVIE_QT)) {
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
     * 中段ブロック(右に縦長の写真があるブロック)を表示できるかどうかを取得します。
     */
    public boolean isBlockRight() {
    	return sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E;
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
     * 検索リストの画像が存在するかを取得します。
     */
    public boolean isSearchImageExist() {
    	if (sizeKbn == SizeKbn.A || sizeKbn == SizeKbn.B || sizeKbn == SizeKbn.C) {
    		return isTrue(imageCheckMap.get(MTypeConstants.MaterialKbn.MAIN_1)) ? true: false;
    	}

    	if (sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {
    		return isTrue(imageCheckMap.get(MTypeConstants.MaterialKbn.FREE)) ? true : false;
    	}

    	return false;
    }


    /**
     * 文章1のブロックにデータがあるかどうかを取得
     * @return
     */
    public boolean isBlockSentence1NotEmpty() {
		if (StringUtils.isNotBlank(catchCopy1)
    		|| StringUtils.isNotBlank(sentence1)) {

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
		if (/*imageCheckMap.get(MaterialKb)
    		||*/ StringUtils.isNotBlank(catchCopy2)
    		|| StringUtils.isNotBlank(sentence2)) {

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
    	return sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E;
    }


    /**
     * キャプションブロックにデータがあるかどうかを判断
     */
    public boolean isBlockCaption() {

    	if( (sizeKbn == SizeKbn.C
    			|| sizeKbn == SizeKbn.D
    			|| sizeKbn == SizeKbn.E)
    			&& (isTrue(imageCheckMap.get(MaterialKbn.PHOTO_A))
    				|| isTrue(imageCheckMap.get(MaterialKbn.PHOTO_B))
    				|| isTrue(imageCheckMap.get(MaterialKbn.PHOTO_C)))) {
    			return true;
    	}

    	return false;
    }



    /**
     * データのサイズにより[写真A~Cの]を表示できるかどうかを取得します。
     * @return true:表示可、false:表示不可
     */
    public boolean isBlockPhoto() {
    	return (sizeKbn == SizeKbn.D
    			|| sizeKbn == SizeKbn.E) ? true : false;
    }

	/**
	 * ファイル出力用のキーを取得します。
	 * idがゼロでなければwebId、それ以外の場合は文字列「INPUT」を取得します。
	 * ゼロとなるのは登録、COPY画面から来た場合。
	 * @return
	 */
	public String getIdForDirName() {
		return (id != 0) ? Integer.toString(id) : GourmetCareeConstants.IMG_FILEKEY_INPUT;
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
	 * 勤務地エリア(WEBエリアから名称変更)区分があるかどうか
	 * @return
	 */
	public boolean isWebAreaKbnExist() {
		return CollectionUtils.isNotEmpty(webAreaKbnList);
	}

	/**
	 * 掲載期間が存在するかどうか
	 * @return
	 */
	public boolean isPostDateExist() {
		return postStartDatetime != null && postEndDatetime != null;
	}


	/**
	 * その他条件の存在名を取得します。
	 * @param otherConditionKbn その他条件
	 * @return その他条件が含まれていれば「有」なければ「無」
	 */
	private String getOtherConditionExistName(int otherConditionKbn) {
		if (CollectionUtils.isNotEmpty(otherConditionKbnList)) {
			if (otherConditionKbnList.contains(
					String.valueOf(otherConditionKbn))) {
				return ARI;
			}
		}

		return NASHI;
	}

	/**
	 * その他条件の存在名のマップを取得します。
	 * @return
	 */
	public Map<Integer, String> getOtherConditionExistNameMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(MTypeConstants.OtherConditionKbn.OPENING_STAFF,
				getOtherConditionExistName(
						MTypeConstants.OtherConditionKbn.OPENING_STAFF));

		map.put(MTypeConstants.OtherConditionKbn.AMATEUR,
				getOtherConditionExistName(
						MTypeConstants.OtherConditionKbn.AMATEUR));

		map.put(MTypeConstants.OtherConditionKbn.NEW_GRADUATES,
				getOtherConditionExistName(
						MTypeConstants.OtherConditionKbn.NEW_GRADUATES));

		return map;
	}
}
