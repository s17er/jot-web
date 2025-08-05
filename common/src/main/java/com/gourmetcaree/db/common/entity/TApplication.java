package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 応募のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_application")
public class TApplication extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_application_id_gen")
	@SequenceGenerator(name="t_application_id_gen", sequenceName="t_application_id_seq", allocationSize=1)
	@Column(name="id")
	@Id
	public Integer id;

	/** 応募日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="application_datetime")
	public Date applicationDatetime;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** メールID */
	@Column(name="mail_id")
	public Integer mailId;

	/** 氏名 */
	@Column(name="name")
	public String name;

	/** 氏名（カナ） */
	@Column(name="name_kana")
	public String nameKana;

	/** 性別区分 */
	@Column(name="sex_kbn")
	public Integer sexKbn;

	/** 年齢 */
	@Column(name="age")
	public Integer age;

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

	/** PCメールアドレス */
	@Column(name="pc_mail")
	public String pcMail;

	/** 携帯メールアドレス */
	@Column(name="mobile_mail")
	public String mobileMail;

	/** 希望連絡時間 */
	@Column(name="connect_time")
	public String connectTime;

	/** 現在の状況 */
	@Column(name="current_employed_situation_kbn")
	public Integer currentEmployedSituationKbn;

	/** 入社可能時期 */
	@Column(name="possible_entry_term_kbn")
	public String possibleEntryTermKbn;

	/** 雇用形態区分 */
	@Column(name="employ_ptn_kbn")
	public Integer employPtnKbn;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 応募先 */
	@Column(name="application_name")
	public String applicationName;

	/** 希望職種 */
	@Column(name="hope_job")
	public String hopeJob;

	/** 応募自己PR */
	@Column(name="application_self_pr")
	public String applicationSelfPr;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** 会員フラグ */
	@Column(name="member_flg")
	public Integer memberFlg;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** 選考対象フラグ */
	@Column(name="selection_flg")
	public Integer selectionFlg;

	/** 職務経歴表示フラグ */
	@Column(name="job_history_display_flg")
	public Integer jobHistoryDisplayFlg;

	/** メモ */
	@Column(name="memo")
	public String memo;

	/** メモ更新日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="memo_update_datetime")
	public Date memoUpdateDatetime;

	/** いたずらフラグ */
	@Column(name="mischief_flg")
	public Integer mischiefFlg;

	/** 応募区分 */
	@Column(name="application_kbn")
	public Integer applicationKbn;

	/** 生年月日 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="birthday")
	public Date birthday;

	/** 希望職種 */
	@Column(name="job_kbn")
	public Integer jobKbn;

	/** 海外勤務経験 */
	@Column(name="foreign_work_flg")
	public Integer foreignWorkFlg;

	/** その他資格 */
	@Column(name="qualification_other")
	public String qualificationOther;

	/**希望業態 */
	@Column(name="hope_industry")
	public String hopeIndustry;

	/** その他希望連絡方法 */
	@Column(name="contact_method")
	public String contactMethod;

	/** 第一希望 */
	@Column(name="first_desired")
	public String firstDesired;

	/** 第二希望 */
	@Column(name="second_desired")
	public String secondDesired;

	/** 第三希望 */
	@Column(name="third_desired")
	public String thirdDesired;

	/** その他 */
	@Column(name="note")
	public String note;

	/** メールトークン */
	@Column(name="mail_token")
	public String mailToken;

	/** 店舗ID */
	@Column(name="shop_list_id")
	public Integer shopListId;

	/** 顧客エンティティ */
	@OneToOne
	@JoinColumn(name="customer_id")
	public MCustomer mCustomer;

	/** 応募学歴エンティティ */
	@OneToOne
	@JoinColumn(name="id")
	public TApplicationSchoolHistory tApplicationSchoolHistory;

	/** 応募属性エンティティ */
	@OneToMany(mappedBy="tApplication")
	public List<TApplicationAttribute> tApplicationAttributeList;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_application";

	/** ID */
	public static final String ID ="id";

	/** 応募日時 */
	public static final String APPLICATION_DATETIME = "application_datetime";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	/** メールID */
	public static final String MAIL_ID = "mail_id";

	/** 氏名 */
	public static final String NAME = "name";

	/** 氏名（カナ） */
	public static final String NAME_KANA = "name_kana";

	/** 性別区分 */
	public static final String SEX_KBN = "sex_kbn";

	/** 年齢 */
	public static final String AGE = "age";

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

	/** PCメールアドレス */
	public static final String PC_MAIL = "pc_mail";

	/** 携帯メールアドレス */
	public static final String MOBILE_MAIL = "mobile_mail";

	/** 希望連絡時間 */
	public static final String CONNECT_TIME = "connect_time";

	/** 現在の状況 */
	public static final String CURRENT_EMPLOYED_SITUATION_KBN = "current_employed_situation_kbn";

	/** 入社可能時期 */
	public static final String POSSIBLE_ENTRY_TERM_KBN = "possible_entry_term_kbn";

	/** 雇用形態区分 */
	public static final String EMPLOY_PTN_KBN = "employ_ptn_kbn";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 応募先 */
	public static final String APPLICATION_NAME = "application_name";

	/** 希望職種 */
	public static final String HOPE_JOB = "hope_job";

	/** 応募自己PR */
	public static final String APPLICATION_SELF_PR = "application_self_pr";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** 会員フラグ */
	public static final String MEMBER_FLG = "member_flg";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** WEBデータID */
	public static final String WEB_ID = "web_id";

	/** 選考対象フラグ */
	public static final String SELECTION_FLG = "selection_flg";

	/** メモ */
	public static final String MEMO = "memo";

	/** 生年月日 */
	public static final String BIRTHDAY = "birthday";

	/** 希望職種 */
	public static final String JOB_KBN = "job_kbn";

	/** メモ更新日時 */
	public static final String MEMO_UPDATE_DATETIME = "memo_update_datetime";

	/** いたずらフラグ */
	public static final String MISCHIEF_FLG = "mischief_flg";

	/** 職務経歴表示フラグ */
	public static final String JOB_HISTORY_DISPLAY_FLG = "job_history_display_flg";

	/** 顧客エンティティ */
	public static final String M_CUSTOMER = "m_customer";

	/** 応募区分 */
	public static final String APPLICATION_KBN = "application_kbn";

	/** その他資格 */
	public static final String QUALIFICATION_OTHER = "qualification_other";

	/** 希望業態 */
	public static final String HOPE_INDUSTRY = "hope_industry";

	/** その他連絡方法 */
	public static final String CONTACT_METHOD = "contact_method";

	/** 第一希望 */
	public static final String FIRST_DESIRED = "first_desired";

	/** 第二希望 */
	public static final String SECOND_DESIRED = "second_desired";

	/** 第三希望 */
	public static final String THIRD_DESIRED = "third_desired";

	/** その他 */
	public static final String NOTE = "note";

	/** メールトークン */
	public static final String MAIL_TOKEN = "mail_token";

}