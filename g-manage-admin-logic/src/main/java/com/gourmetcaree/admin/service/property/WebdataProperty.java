package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TApplicationTest;
import com.gourmetcaree.db.common.entity.TMaterial;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.entity.TWebRoute;
import com.gourmetcaree.db.common.entity.TWebShopList;
import com.gourmetcaree.db.common.entity.TWebSpecial;

/**
 *
 * WEBデータを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class WebdataProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5104133999172906154L;

	/** WEBデータエンティティ */
	public TWeb tWeb;

	/** WEBデータID */
	public String[] webId;

	/** 顧客マスタエンティティ */
	public MCustomer mCustomer;

	/** 会社マスタエンティティ */
	public MCompany mCompany;

	/** ログイン会社マスタエンティティ */
	public MCompany loginCompany;

	/** 営業担当者マスタエンティティ */
	public MSales mSales;

	/** 号数マスタエンティティ */
	public MVolume mVolume;

	/** 一覧の検索結果を保持するリスト */
	public List<TWeb> entityList = new ArrayList<TWeb>();

	/** WEBデータ属性を保持するリスト */
	public List<TWebAttribute> tWebAttributeList = new ArrayList<TWebAttribute>();

	/** WEBデータ特集を保持するリスト */
	public List<TWebSpecial> tWebSpecialList = new ArrayList<TWebSpecial>();

	/** WEBデータ路線図を保持するリスト */
	public List<TWebRoute> tWebRouteList = new ArrayList<TWebRoute>();

	/** 素材を保持するリスト */
	public List<TMaterial> tMaterialList = new ArrayList<TMaterial>();

	/** 不正なWEBデータID */
	public List<String> failWebId;

	/** 削除されたWEBデータID */
	public List<String> deleteWebId;

	/** 会社が存在していないWEBデータIDリスト */
	public List<String> noExistCompanyWebIdList;

	/** 営業担当者が存在していないWEBデータIDリスト */
	public List<String> noExistSalesWebIdList;

	/** 顧客が登録されていないWEBデータIDリスト */
	public List<String> noRegistCustomerWebIdList;

	/** 顧客が存在していないWEBデータIDリスト */
	public List<String> noExistCustomerWebIdList;

	/** 号数が登録されていないWEBデータIDリスト */
	public List<String> noRegistVolumeWebIdList;

	/** 号数が存在していないWEBデータIDリスト */
	public List<String> noExistVolumeWebIdList;

	/** ステータスが不正なWEBデータIDリスト */
	public List<String> failStatusWebIdList;

	/** 掲載確定日時が不正なWEBデータIDリスト */
	public List<String> failFixedDatetimeWebIdList;

	/** 応募テストエンティティ */
	public TApplicationTest tApplicationTest;

	/** エリアコード */
	public Integer areaCd;

	/** 会社ID */
	public Integer companyId;

	/** 顧客ID */
	public Integer customerId;

	/** webdataのファイル配置フォルダ(セッション保持時) */
	public String webdataSessionImgdirPath;

	/** セッションID */
	public String idForDir;

	/** 追加するスカウトメール数 */
	public int addScoutMailCount;

	/** WEB職種のリスト */
	public List<TWebJob> tWebJobList = new ArrayList<>();

	/** WEBデータ系列店舗のリスト */
	public List<TWebShopList> tWebShopListList = new ArrayList<>();

	/** 募集職の仕事内容 */
	public String workContent;

	/**
	 * 画面からの変更が反映されたエンティティのMapを取得します。
	 * Listが空の場合は空のMapを返します。
	 * @return
	 */
	public Map<Integer, TWeb> getBeforeEntityMap() {

		 Map<Integer, TWeb> map = new HashMap<Integer, TWeb>();

		if (entityList == null || entityList.isEmpty() ) {
			return map;
		}

		for (TWeb webEntity : entityList) {
			map.put(webEntity.id, webEntity);
		}

		return map;
	}
}
