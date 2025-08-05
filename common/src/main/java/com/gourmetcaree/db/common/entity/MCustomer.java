package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 顧客マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_customer")
public class MCustomer extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_customer_id_gen")
	@SequenceGenerator(name="m_customer_id_gen", sequenceName="m_customer_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 登録日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registration_datetime")
	public Date registrationDatetime;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** 顧客名 */
	@Column(name="customer_name")
	public String customerName;

	/** 顧客名（カナ） */
	@Column(name="customer_name_kana")
	public String customerNameKana;

	/** 表示用会社名 */
	@Column(name="display_company_name")
	public String displayCompanyName;

	/** 担当者名 */
	@Column(name="contact_name")
	public String contactName;

	/** 担当者名（カナ） */
	@Column(name="contact_name_kana")
	public String contactNameKana;

	/** 担当者部署 */
	@Column(name="contact_post")
	public String contactPost;

	/** 郵便番号 */
	@Column(name="zip_cd")
	public String zipCd;

	/** 都道府県コード */
	@Column(name="prefectures_cd")
	public Integer prefecturesCd;

	/** 市区町村 */
	@Column(name="municipality")
	public String municipality;

	/** 住所 */
	@Column(name="address")
	public String address;

	/** 電話番号1 */
	@Column(name="phone_no1")
	public String phoneNo1;

	/** 電話番号2 */
	@Column(name="phone_no2")
	public String phoneNo2;

	/** 電話番号3 */
	@Column(name="phone_no3")
	public String phoneNo3;

	/** FAX番号1 */
	@Column(name="fax_no1")
	public String faxNo1;

	/** FAX番号2 */
	@Column(name="fax_no2")
	public String faxNo2;

	/** FAX番号3 */
	@Column(name="fax_no3")
	public String faxNo3;

	/** メインメールアドレス */
	@Column(name="main_mail")
	public String mainMail;

	/** サブメールアドレス */
	@Column(name="sub_mail")
	public String subMail;

	/** サブメール受信フラグ */
	@Column(name="submail_reception_flg")
	public Integer submailReceptionFlg;

	/** ログインフラグ */
	@Column(name="login_flg")
	public Integer loginFlg;

	/** 掲載フラグ */
	@Column(name="publication_flg")
	public Integer publicationFlg;

	/** 掲載終了の表示フラグ */
	@Column(name="publication_end_display_flg")
	public Integer publicationEndDisplayFlg;

	/** スカウトメール使用フラグ */
	@Column(name="scout_use_flg")
	public Integer scoutUseFlg;

	/** 備考 */
	@Column(name="note")
	public String note;

	/** テストフラグ */
	@Column(name="test_flg")
	public Integer testFlg;

	/** メルマガ受信フラグ */
	@Column(name="mail_magazine_reception_flg")
	public Integer mailMagazineReceptionFlg;

	@Column(name = MAIL_MAGAZINE_AREA_CD)
	public Integer mailMagazineAreaCd;

	/** 設立 */
	@Column(name = ESTABLISHMENT)
	public String establishment;

	/** 資本金 */
	@Column(name = CAPITAL)
	public String capital;

	/** 代表者 */
	@Column(name = REPRESENTATIVE)
	public String representative;

	/** 従業員 */
	@Column(name = EMPLOYEE)
	public String employee;

	/** 事業内容 */
	@Column(name = BUSINESS_CONTENT)
	public String businessContent;

	/** 表示順 */
	@Column(name="disp_order")
	public Integer dispOrder;

	/** 顧客担当会社エンティティリスト */
	@OneToMany(mappedBy = "mCustomer")
	public List<MCustomerCompany> mCustomerCompanyList;

	/** WEBデータエンティティ */
	@OneToOne(mappedBy = "mCustomer")
	public TWeb tWeb;

    /** 応募エンティティ */
    @OneToOne(mappedBy = "mCustomer")
    public TApplication tApplication;

    /** プレ応募エンティティ */
    @OneToOne(mappedBy = "mCustomer")
    public TPreApplication tPreApplication;

	/** 顧客アカウントエンティティ */
	@OneToOne(mappedBy = "mCustomer")
	public MCustomerAccount mCustomerAccount;

	/** 足跡エンティティ */
	@OneToOne(mappedBy = "mCustomer")
	public TFootprint tFootprint;

	/** スカウトブロックエンティティ */
	@OneToOne(mappedBy = "mCustomer")
	public TScoutBlock tScoutBlock;

	/** 系列店舗の都道府県 */
	@OneToMany(mappedBy = "mCustomer")
	public List<VShopListPrefectures> vShopListArbeitTodouhukenList;

	/** 系列店舗の業態 */
	@OneToMany(mappedBy = "mCustomer")
	public List<VShopListIndustryKbn> vShopListIndustryKbnList;

	/** 系列店舗数 */
	@OneToOne(mappedBy = "mCustomer")
	public VCustomerShopCount vCustomerShopCount;

	/** サブメールエンティティリスト */
	@OneToMany(mappedBy = "mCustomer")
	public List<MCustomerSubMail> mCustomerSubMailList;
	/**
	 * サブアドレス受信フラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class SubMailReceptionFlgKbn {

		/** 受信否 */
		public static final int NOT_RECEIVE = 0;

		/** 受信 */
		public static final int RECEIVE = 1;

	}

	/**
	 * ログインフラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class LoginFlgKbn {

		/** ログイン否 */
		public static final int LOGIN_NG = 0;

		/** ログイン可 */
		public static final int LOGIN_OK = 1;

	}

	/**
	 * 掲載フラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class PublicationFlg {

		/** 掲載否 */
		public static final int PUBLICATION_NG = 0;

		/** 掲載可 */
		public static final int PUBLICATION_OK = 1;

	}

	/**
	 * 掲載終了時の表示フラグの定義です。
	 * @author Takehiro Nakamori
	 *
	 */
	public static class PublicationEndDisplayFlg {
		/** 表示しない(404NotFound) */
		public static final int DISPLAY_NG = 0;

		/** 表示する */
		public static final int DISPLAY_OK = 1;
	}

	/**
	 * スカウトメール使用フラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class ScoutUseFlg {

		/** スカウトメール使用否 */
		public static final int SCOUT_USE_NG = 0;

		/** スカウトメール使用可 */
		public static final int SCOUT_USE_OK = 1;

	}

	/**
	 * ログインフラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class LoginFlg {

		/** ログイン否 */
		public static final int LOGIN_NG = 0;

		/** ログイン可 */
		public static final int LOGIN_OK = 1;

	}

	/** テーブル名 */
	public static final String TABLE_NAME = "m_customer";

	/** ID */
	public static final String ID ="id";

	/** 登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	/** 顧客名 */
	public static final String CUSTOMER_NAME = "customer_name";

	/** 顧客名（カナ） */
	public static final String CUSTOMER_NAME_KANA = "customer_name_kana";

	/** 表示用会社名 */
	public static final String DISPLAY_COMPANY_NAME = "display_company_name";

	/** 担当者名 */
	public static final String CONTACT_NAME = "contact_name";

	/** 担当者名（カナ） */
	public static final String CONTACT_NAME_KANA = "contact_name_kana";

	/** 担当者部署 */
	public static final String CONTACT_POST = "contact_post";

	/** 郵便番号 */
	public static final String ZIP_CD = "zip_cd";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 市区町村 */
	public static final String MUNICIPALITY = "municipality";

	/** 住所 */
	public static final String ADDRESS = "address";

	/** 電話番号1 */
	public static final String PHONE_NO1 = "phone_no1";

	/** 電話番号2 */
	public static final String PHONE_NO2 = "phone_no2";

	/** 電話番号3 */
	public static final String PHONE_NO3 = "phone_no3";

	/** FAX番号1 */
	public static final String FAX_NO1 = "fax_no1";

	/** FAX番号2 */
	public static final String FAX_NO2 = "fax_no2";

	/** FAX番号3 */
	public static final String FAX_NO3 = "fax_no3";

	/** メインメールアドレス */
	public static final String MAIN_MAIL = "main_mail";

	/** サブメールアドレス */
	public static final String SUB_MAIL = "sub_mail";

	/** サブメール受信フラグ */
	public static final String SUBMAIL_RECEPTION_FLG = "submail_reception_flg";

	/** ログインフラグ */
	public static final String LOGIN_FLG = "login_flg";

	/** 掲載フラグ */
	public static final String PUBLICATION_FLG = "publication_flg";

	/** 掲載終了の表示フラグ */
	public static final String PUBLICATION_END_DISPLAY_FLG = "publication_end_display_flg";

	/** スカウトメール使用フラグ */
	public static final String SCOUT_USE_FLG = "scout_use_flg";

	/** 備考 */
	public static final String NOTE = "note";

	/** テストフラグ */
	public static final String TEST_FLG = "test_flg";

	/** メルマガ受信フラグ */
	public static final String MAIL_MAGAZINE_RECEPTION_FLG = "mail_magazine_reception_flg";

	/** 設立 */
	public static final String ESTABLISHMENT = "establishment";

	/** 資本金 */
	public static final String CAPITAL = "capital";

	/** 代表者 */
	public static final String REPRESENTATIVE = "representative";

	/** 従業員 */
	public static final String EMPLOYEE = "employee";

	/** 事業内容 */
	public static final String BUSINESS_CONTENT = "business_content";

	/** 表示順 */
	public static final String DISP_ORDER = "disp_order";


	/** メルマガエリアコード */
	public static final String MAIL_MAGAZINE_AREA_CD = "mail_magazine_area_cd";

	/** 顧客担当会社エンティティリストリスト */
	public static final String M_CUSTOMER_COMPANY_LIST = "m_customer_company_list";

	/** GCWコードマスタエンティティ */
	public static final String M_GCW = "m_gcw";

	/** WEBデータエンティティ */
	public static final String T_WEB = "t_web";

    /** 応募エンティティ */
    public static final String T_APPLICATION = "t_application";

	/** 顧客アカウントエンティティ */
	public static final String M_CUSTOMER_ACCOUNT = "m_customer_account";

	/** 足あとエンティティ */
	public static final String T_FOOTPRINT = "t_footprint";

	/** スカウトブロックエンティティ */
	public static final String T_SCOUT_BLOCK = "t_scout_block";
}