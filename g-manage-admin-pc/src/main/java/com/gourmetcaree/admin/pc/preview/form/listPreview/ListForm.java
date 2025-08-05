package com.gourmetcaree.admin.pc.preview.form.listPreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 求人情報一覧のフォーム
 * @author takahiro Ando
 * @version 1.0
 */
@Component(instance=InstanceType.REQUEST)
public class ListForm extends PreviewBaseForm {

	/**
	 * 検索時にアクセスされたメソッドを保持する列挙型
	 * @author ando
	 */
	public enum ImgMethodKbn {
		/** 画像取得方法（セッション） */
		IMG_FROM_SESSION,
		/** 画像取得方法（DB） */
		IMG_FROM_DB,
	}



	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3464480389469189545L;

	/** 職種(単項目) */
	public String jobKbn;

	/** 業種 */
	public String industryKbn;

	/** 勤務地(単項目) */
	public String webArea;

	/** 鉄道 */
	public String railroadId;

	/** 路線 */
	public String routeId;

	/** 駅（FROM）*/
	public String stationIdFrom;

	/** 駅（FROMの表示順）*/
	public int stationIdFromDisplayOrder;

	/** 駅（TO）*/
	public String stationIdTo;

	/** 駅（TOの表示順）*/
	public int stationIdToDisplayOrder;

	/** 主要駅 */
	public String mainStation;

	/** 待遇(単項目) */
	public String treatment;

	/** 雇用形態 */
	public String employPtn;

	/** 店舗数 */
	public String shopsKbn;

	/** その他 */
	public String[] otherConditionKbnList = new String[0];

	/** キーワード */
	public String keyword;

	/** 業種（複数選択用） */
	public String[] industryKbnList = new String[0];

	/** 勤務地（複数選択用）  */
	public String[] webAreaKbnList = new String[0];

	/** 待遇検索条件（複数選択用） */
	public String[] treatmentKbnList = new String[0];

	/** 職種 （複数選択用）*/
	public String[] jobKbnList = new String[0];

	/** 雇用形態 （複数選択用）*/
	public String[] employPtnKbnList = new String[0];

	/** 特集コード */
	public String specialId;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 求人情報一覧のリスト */
	public List<PreviewDto> dataList = new ArrayList<PreviewDto>();

	/** 検索条件を保持するMap */
	public Map<String, Object> searchConditionMap = new HashMap<String, Object>();

	/** SEO用文言（h1タグ用） */
	public String seoH1;

	/** SEO用文言（title用） */
	public String seoTitle;

	/** SEO用文言（keywords用） */
	public String seoKeywords;

	/** SEO用文言（description用） */
	public String seoDescription;

	/** エリアコード */
	public int areaCd;

	/** 公開側のURLのルートパスを取得します。*/
	public String frontHttpUrl;

	/** 画像の取得方法 */
	public ImgMethodKbn imgMethodKbn;

	/** 基本カラー(デフォルトは関西) */
	public String SITE_COLOR = "#FF9900";

	/** 基本文字カラー(デフォルトは関西) */
	public String SITE_TEXT_COLOR = "#000000";

	/** 基本絵文字(デフォルトは首都圏) */
	public String SITE_EMOJI = "/i/emoji/images/";

	/** 基本リンクカラー(デフォルトは首都圏) */
	public String SITE_TEXT_LINK_COLOR = "#FF6600";

	/** webId */
	public String webId;


	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		jobKbn = null;
		industryKbn = null;
		webArea = null;
		railroadId = null;
		routeId = null;
		stationIdFrom = null;
		stationIdFromDisplayOrder = 0;
		stationIdTo = null;
		stationIdToDisplayOrder = 0;
		mainStation = null;
		treatment = null;
		employPtn = null;
		shopsKbn = null;
		otherConditionKbnList = new String[0];
		keyword = null;
		industryKbnList = new String[0];
		webAreaKbnList = new String[0];
		treatmentKbnList = new String[0];
		jobKbnList = new String[0];
		employPtnKbnList = new String[0];
		specialId = null;
		pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
		pageNum = null;
		dataList = new ArrayList<PreviewDto>();
		searchConditionMap = new HashMap<String, Object>();
		seoH1 = null;
		seoTitle = null;
		seoKeywords = null;
		seoDescription = null;
		areaCd = 0;
		frontHttpUrl = null;
		imgMethodKbn = null;
		SITE_COLOR = "#FF9900";
		SITE_TEXT_COLOR = "#000000";
		SITE_EMOJI = "/i/emoji/images/";
		SITE_TEXT_LINK_COLOR = "#FF6600";
		webId = null;
	}

}
