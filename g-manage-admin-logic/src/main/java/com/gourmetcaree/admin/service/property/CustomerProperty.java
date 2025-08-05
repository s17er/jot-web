package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.MCustomerHomepage;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.entity.TScoutMailManage;

/**
 *
 * 顧客管理のデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class CustomerProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6458410002088108405L;


	/** 顧客マスタエンティティ */
	public MCustomer mCustomer;

	/** 顧客アカウントマスタエンティティ */
	public MCustomerAccount mCustomerAccount;

	/** 顧客アカウントマスタエンティティリスト */
	public List<MCustomerAccount> mCustomerAccountList;

	/** 顧客担当マスタエンティティリスト */
	public List<MCustomerCompany> mCustomerCompanyList;

	/** スカウトメール管理エンティティ */
	public TScoutMailManage tScoutMailManage;

	/** スカウトメールログエンティティ */
	public TScoutMailLog tScoutMailLog;

	/** 定型文マスタエンティティリスト */
	public List<MSentence> mSentenceList;

	/** スカウトメールマネージエンティティをインサートするかどうかのフラグ */
	public boolean insertScoutManageFlg;

	/** 追加するスカウトメール数 */
	public int addScoutMailCount;

	/** 系列店舗の都道府県一覧 */
	public List<Integer> shopListPrefecturesCdList;

	/** 系列店舗の海外エリア一覧 */
	public List<Integer> shopListShutokenForeignAreaKbnList;

	/** 系列店舗の業態区分一覧 */
	public List<Integer> shopListIndustryKbnList;

	/** 系列店舗の件数 */
	public int shopListCount;

	/** 顧客サブメールエンティティのリスト */
	public List<MCustomerSubMail> customerSubMailList;

	/** メルマガエリアのリスト */
	public List<Integer> mailMagazineAreaCdList;

	/** ホームページのリスト */
	public List<MCustomerHomepage> customerHomepageList;


}
