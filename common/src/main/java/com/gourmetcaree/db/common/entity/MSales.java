package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 営業担当者マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_sales")
public class MSales extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_sales_id_gen")
    @SequenceGenerator(name="m_sales_id_gen", sequenceName="m_sales_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 登録日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="registration_datetime")
    public Date registrationDatetime;

    /** 会社ID */
    @Column(name="company_id")
    public Integer companyId;

    /** 営業担当者名 */
    @Column(name="sales_name")
    public String salesName;

    /** 営業担当者名（カナ） */
    @Column(name="sales_name_kana")
    public String salesNameKana;

    /** 権限コード */
    @Column(name="authority_cd")
    public String authorityCd;

    /** メインメールアドレス */
    @Column(name="main_mail")
    public String mainMail;

    /** サブメールアドレス */
    @Column(name="sub_mail")
    public String subMail;

    /** サブメール受信フラグ */
    @Column(name="submail_reception_flg")
    public Integer submailReceptionFlg;

    /** 携帯番号1 */
    @Column(name="mobile_no1")
    public String mobileNo1;

    /** 携帯番号2 */
    @Column(name="mobile_no2")
    public String mobileNo2;

    /** 携帯番号3 */
    @Column(name="mobile_no3")
    public String mobileNo3;

    /** 所属部署 */
    @Column(name="department")
    public String department;

    /** ログインID */
    @Column(name="login_id")
    public String loginId;

    /** パスワード */
    @Column(name="password")
    public String password;

    /** 備考 */
    @Column(name="note")
    public String note;

    /** 旧システム担当者ID */
    @Column(name="old_system_sales_id")
    public String oldSystemSalesId;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** 会社 */
    @ManyToOne
    @JoinColumn(name="company_id")
    public MCompany mCompany;

	/** WEBデータエンティティ */
	@OneToOne(mappedBy = "mSales")
	public TWeb tWeb;

	/**
	 * 権限レベルの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class AuthLevelValue {

		/** 管理者 */
		public static final String OWNER_ADMIN = "1";

		/** 自社スタッフ */
		public static final String OWNER_STAF = "2";

		/** 代理店 */
		public static final String OWNER_AGENCY = "3";

		/** 他社スタッフ */
		public static final String OWNER_OTHER = "4";

		/** 営業権限 */
		public static final String OWNER_SALES = "5";

	}

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

	/** 権限レベルマップ */
	public static final Map<String, String> AUTH_LEVEL_MAP;

	static {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(AuthLevelValue.OWNER_ADMIN, "管理者");
		map.put(AuthLevelValue.OWNER_SALES, "営業権限");
		map.put(AuthLevelValue.OWNER_STAF, "自社スタッフ");
		map.put(AuthLevelValue.OWNER_AGENCY, "代理店");
		map.put(AuthLevelValue.OWNER_OTHER, "他社スタッフ");

		AUTH_LEVEL_MAP = Collections.unmodifiableMap(map);
	}

	/** テーブル名 */
	public static final String TABLE_NAME = "m_sales";

	/** ID */
	public static final String ID ="id";

	/** 登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";

	/** 会社ID */
	public static final String COMPANY_ID = "company_id";

	/** 営業担当者名 */
	public static final String SALES_NAME = "sales_name";

	/** 営業担当者名（カナ） */
	public static final String SALES_NAME_KANA = "sales_name_kana";

	/** 権限コード */
	public static final String AUTHORITY_CD = "authority_cd";

	/** メインメールアドレス */
	public static final String MAIN_MAIL = "main_mail";

	/** サブメールアドレス */
	public static final String SUB_MAIL = "sub_mail";

	/** サブメール受信フラグ */
	public static final String SUBMAIL_RECEPTION_FLG = "submail_reception_flg";

	/** 携帯番号1 */
	public static final String MOBILE_NO1 = "mobile_no1";

	/** 携帯番号2 */
	public static final String MOBILE_NO2 = "mobile_no2";

	/** 携帯番号3 */
	public static final String MOBILE_NO3 = "mobile_no3";

	/** 所属部署 */
	public static final String DEPARTMENT = "department";

	/** ログインID */
	public static final String LOGIN_ID = "login_id";

	/** パスワード */
	public static final String PASSWORD = "password";

	/** 備考 */
	public static final String NOTE = "note";

	/** 旧システム担当者ID */
	public static final String OLD_SYSTEM_SALES_ID = "old_system_sales_id";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";

	/** 会社エンティティ */
	public static final String M_COMPANY = "m_company";

}