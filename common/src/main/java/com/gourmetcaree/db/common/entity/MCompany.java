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
 * 会社マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_company")
public class MCompany extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_company_id_gen")
    @SequenceGenerator(name="m_company_id_gen", sequenceName="m_company_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 登録日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="registration_datetime")
    public Date registrationDatetime;

    /** 社名 */
    @Column(name="company_name")
    public String companyName;

    /** 社名（カナ） */
    @Column(name="company_name_kana")
    public String companyNameKana;

    /** 担当者名 */
    @Column(name="contact_name")
    public String contactName;

    /** 担当者名（カナ） */
    @Column(name="contact_name_kana")
    public String contactNameKana;

    /** 代理店フラグ */
    @Column(name="agency_flg")
    public Integer agencyFlg;

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

    /** 備考 */
    @Column(name="note")
    public String note;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** 会社エリアマスタのリスト */
    @OneToMany(mappedBy = "mCompany")
    public List<MCompanyArea> mCompanyAreaList;

	/** WEBデータエンティティ */
	@OneToOne(mappedBy = "mCompany")
	public TWeb tWeb;


    /** テーブル名 */
    public static final String TABLE_NAME = "m_company";

    /** ID */
    public static final String ID ="id";

    /** 登録日時 */
    public static final String REGISTRATION_DATETIME = "registration_datetime";

    /** 社名 */
    public static final String COMPANY_NAME = "company_name";

    /** 社名（カナ） */
    public static final String COMPANY_NAME_KANA = "company_name_kana";

    /** 担当者名 */
    public static final String CONTACT_NAME = "contact_name";

    /** 担当者名（カナ） */
    public static final String CONTACT_NAME_KANA = "contact_name_kana";

    /** 代理店フラグ */
    public static final String AGENCY_FLG = "agency_flg";

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

    /** 備考 */
    public static final String NOTE = "note";

    /** 表示順 */
    public static final String DISPLAY_ORDER = "display_order";

    /** 会社エリアマスタのリスト */
    public static final String M_COMPANYAREA_LIST = "m_companyArea_list";

    /**
	 * 代理店フラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class AgencyFlgValue {

		/** 代理店以外 */
		public static final int NOT_AGENCY = 0;

		/** 代理店 */
		public static final int AGENCY = 1;

	}
}