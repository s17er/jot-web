package com.gourmetcaree.db.common.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 事前登録エントリ
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name = "t_advanced_registration_entry")
public class TAdvancedRegistrationEntry extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4599986596636841349L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_advanced_registration_entry_id_gen")
	@SequenceGenerator(name="t_advanced_registration_entry_id_gen", sequenceName="t_advanced_registration_entry_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 事前登録ID */
	@Column(name = ADVANCED_REGISTRATION_ID)
	public Integer advancedRegistrationId;

	/** 事前登録ユーザID */
	@Column(name = ADVANCED_REGISTRATION_USER_ID)
	public Integer advancedRegistrationUserId;

	/** ログインID */
	@Column(name = LOGIN_ID)
	public String loginId;

	/** パスワード */
	@Column(name = PASSWORD)
	public String password;

	/** 事前登録登録日時 */
	@Column(name = REGISTRATION_DATETIME)
	public Timestamp registrationDatetime;

	/** 会員名 */
	@Column(name = MEMBER_NAME)
	public String memberName;

	/** 会員名（カナ） */
	@Column(name = MEMBER_NAME_KANA)
	public String memberNameKana;

	/** エリアコード */
	@Column(name = AREA_CD)
	public Integer areaCd;

	/** PCメールアドレス */
	@Column(name = PC_MAIL)
	public String pcMail;

	/** 携帯メールアドレス */
	@Column(name = MOBILE_MAIL)
	public String mobileMail;

	/** 生年月日 */
	@Column(name = BIRTHDAY)
	public Timestamp birthday;

	/** 性別区分 */
	@Column(name = SEX_KBN)
	public Integer sexKbn;

	/** 電話番号1 */
	@Column(name = PHONE_NO1)
	public String phoneNo1;

	/** 電話番号2 */
	@Column(name = PHONE_NO2)
	public String phoneNo2;

	/** 電話番号3 */
	@Column(name = PHONE_NO3)
	public String phoneNo3;

	/** 郵便番号 */
	@Column(name = ZIP_CD)
	public String zipCd;

	/** 都道府県コード */
	@Column(name = PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 市区町村 */
	@Column(name = MUNICIPALITY)
	public String municipality;

	/** 住所 */
	@Column(name = ADDRESS)
	public String address;

	/** 現在（前職）の年収 */
	@Column(name = WORK_SALARY)
	public String workSalary;

	/** 事前登録用自己PR */
	@Column(name = ADVANCED_REGISTRATION_SELF_PR)
	public String advancedRegistrationSelfPr;

	/** 転職先に望むこと */
	@Column(name = HOPE_CAREER_CHANGE_TEXT)
	public String hopeCareerChangeText;

	/** 転職希望年 */
	@Column(name = HOPE_CAREER_CHANGE_YEAR)
	public String hopeCareerChangeYear;

	/** 転職希望月 */
	@Column(name = HOPE_CAREER_CHANGE_MONTH)
	public String hopeCareerChangeMonth;

	/** 事前会員メルマガ受信フラグ */
	@Column(name = ADVANCED_MAIL_MAGAZINE_RECEPTION_FLG)
	public Integer advancedMailMagazineReceptionFlg;

	/** PCメール配信停止フラグ */
	@Column(name = PC_MAIL_STOP_FLG)
	public Integer pcMailStopFlg;

	/** モバイルメール配信停止フラグ */
	@Column(name = MOBILE_MAIL_STOP_FLG)
	public Integer mobileMailStopFlg;

	/** 端末区分 */
	@Column(name = TERMINAL_KBN)
	public Integer terminalKbn;

	/** 自動ログインコード */
	@Column(name = AUTO_LOGIN_CD)
	public String autoLoginCd;

	/** 最終更新日時 */
	@Column(name = LAST_UPDATE_DATETIME)
	public Timestamp lastUpdateDatetime;

	/** 来場ステータス */
	@Column(name = ATTENDED_STATUS)
	public Integer attendedStatus;

	/** 取得資格その他 */
	@Column(name = QUALIFICATION_OTHER)
	public String qualificationOther;

	/** 転職希望時期その他 */
	@Column(name = HOPE_CAREER_CHANGE_TERM_OTHER)
	public String hopeCareerChangeTermOther;

	/** 事前登録エントリ属性エンティティリスト */
	@OneToMany(mappedBy = "tAdvancedRegistrationEntry")
	public List<TAdvancedRegistrationEntryAttribute> tAdvancedRegistrationEntryAttributeList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_advanced_registration_entry";

	/** ID */
	public static final String ID = "id";

	/** 事前登録ID */
	public static final String ADVANCED_REGISTRATION_ID = "advanced_registration_id";

	/** 事前登録ユーザID */
	public static final String ADVANCED_REGISTRATION_USER_ID = "advanced_registration_user_id";

	/** ログインID */
	public static final String LOGIN_ID = "login_id";

	/** パスワード */
	public static final String PASSWORD = "password";

	/** 事前登録登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";

	/** 会員名 */
	public static final String MEMBER_NAME = "member_name";

	/** 会員名（カナ） */
	public static final String MEMBER_NAME_KANA = "member_name_kana";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	/** PCメールアドレス */
	public static final String PC_MAIL = "pc_mail";

	/** 携帯メールアドレス */
	public static final String MOBILE_MAIL = "mobile_mail";

	/** 生年月日 */
	public static final String BIRTHDAY = "birthday";

	/** 性別区分 */
	public static final String SEX_KBN = "sex_kbn";

	/** 電話番号1 */
	public static final String PHONE_NO1 = "phone_no1";

	/** 電話番号2 */
	public static final String PHONE_NO2 = "phone_no2";

	/** 電話番号3 */
	public static final String PHONE_NO3 = "phone_no3";

	/** 郵便番号 */
	public static final String ZIP_CD = "zip_cd";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 市区町村 */
	public static final String MUNICIPALITY = "municipality";

	/** 住所 */
	public static final String ADDRESS = "address";

	/** 現在（前職）の年収 */
	public static final String WORK_SALARY = "work_salary";

	/** 事前登録用自己PR */
	public static final String ADVANCED_REGISTRATION_SELF_PR = "advanced_registration_self_pr";


	/** 転職先に望むこと */
	public static final String HOPE_CAREER_CHANGE_TEXT = "hope_career_change_text";

	/** 転職希望年 */
	public static final String HOPE_CAREER_CHANGE_YEAR = "hope_career_change_year";

	/** 転職希望月 */
	public static final String HOPE_CAREER_CHANGE_MONTH = "hope_career_change_month";

	/** メルマガ受信フラグ */
	public static final String ADVANCED_MAIL_MAGAZINE_RECEPTION_FLG = "advanced_mail_magazine_reception_flg";

	/** PCメール配信停止フラグ */
	public static final String PC_MAIL_STOP_FLG = "pc_mail_stop_flg";

	/** モバイルメール配信停止フラグ */
	public static final String MOBILE_MAIL_STOP_FLG = "mobile_mail_stop_flg";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** 自動ログインコード */
	public static final String AUTO_LOGIN_CD = "auto_login_cd";

	/** 最終更新日時 */
	public static final String LAST_UPDATE_DATETIME = "last_update_datetime";

	/** 来場ステータス */
	public static final String ATTENDED_STATUS = "attended_status";

	/** 取得資格その他 */
	public static final String QUALIFICATION_OTHER = "qualification_other";

	/** 転職希望時期その他 */
	public static final String HOPE_CAREER_CHANGE_TERM_OTHER = "hope_career_change_term_other";


	/** 事前登録エントリ属性エンティティリスト */
	public static final String T_ADVANCED_REGISTRATION_ENTRY_ATTRIBUTE_LIST = "t_advanced_registration_entry_attribute_list";
}
