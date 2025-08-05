package com.gourmetcaree.db.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 気になる通知のVIEWエンティティクラス
 * @author t_shiroumaru
 *
 */
@Entity
@Table(name="v_interest")
public class VInterest extends AbstractBaseEntity {

	/** ID */
	@Id
	@Column(name=ID)
	public Integer id;

	/** webID */
	@Column(name=WEB_ID)
	public Integer webId;

	/** 会員ID */
	@Column(name=MEMBER_ID)
	public Integer memberId;

	/** 顧客ID */
	@Column(name=CUSTOMER_ID)
	public Integer customerId;

	/** 気になる通知日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name=INTEREST_DATETIME)
	public Date interestDatetime;

	/** 原稿名 */
	@Column(name=MANUSCRIPT_NAME)
	public String manuscriptName;

	/** 担当会社ID */
	@Column(name=COMPANY_ID)
	public Integer companyId;

	/** 営業担当者ID */
	@Column(name=SALES_ID)
	public Integer salesId;

	/** エリアコード */
	@Column(name=AREA_CD)
	public Integer areaCd;

	/** 顧客名 */
	@Column(name=CUSTOMER_NAME)
	public String customerName;

	/** 会員名 */
	@Column(name=MEMBER_NAME)
	public String memberName;

	/** 都道府県コード */
	@Column(name=PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 市区町村 */
	@Column(name=MUNICIPALITY)
	public String municipality;

	/** その他住所 */
	@Column(name=ADDRESS)
	public String address;

	/** ログインID */
	@Column(name=LOGIN_ID)
	public String loginId;

	/** サブメールアドレス */
	@Column(name=SUB_MAIL_ADDRESS)
	public String subMailAddress;

	/** 性別区分 */
	@Column(name=SEX_KBN)
	public Integer sexKbn;

	/** 生年月日 */
	@Column(name=BIRTHDAY)
	public Date birthday;

	/** テーブル名 */
	public static final String TABLE_NAME = "v_interest";
	/** ID */
	public static final String ID = "id";
	/** webID */
	public static final String WEB_ID = "web_id";
	/** 会員ID */
	public static final String MEMBER_ID = "member_id";
	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";
	/** 気になる通知日時 */
	public static final String INTEREST_DATETIME = "interest_datetime";
	/** 原稿名 */
	public static final String MANUSCRIPT_NAME = "manuscript_name";
	/** 担当会社ID */
	public static final String COMPANY_ID = "company_id";
	/** 営業担当者ID */
	public static final String SALES_ID = "sales_id";
	/** エリアコード */
	public static final String AREA_CD = "area_cd";
	/** 顧客名 */
	public static final String CUSTOMER_NAME = "customer_name";
	/** 会員名 */
	public static final String MEMBER_NAME = "member_name";
	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";
	/** 市区町村コード */
	public static final String MUNICIPALITY = "municipality";
	/** その他住所 */
	public static final String ADDRESS = "address";
	/** ログインID */
	public static final String LOGIN_ID = "login_id";
	/** サブメールアドレス */
	public static final String SUB_MAIL_ADDRESS = "sub_mail_address";
	/** 性別区分 */
	public static final String SEX_KBN = "sex_kbn";
	/** 生年月日 */
	public static final String BIRTHDAY = "birthday";
}
