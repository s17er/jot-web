package com.gourmetcaree.db.common.entity.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;

/**
 * 会員・一時登録会員の基底エンティティ
 * @author nakamori
 *
 */
@MappedSuperclass
public abstract class BaseMemberEntity extends AbstractCommonEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 9173384545245241005L;


	/** 事前登録登録日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="advanced_registration_datetime")
	public Date advancedRegistrationDatetime;

	/** 会員名 */
	@Column(name="member_name")
	public String memberName;

	/** 会員名（カナ） */
	@Column(name="member_name_kana")
	public String memberNameKana;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** PCメールアドレス */
	@Column(name="pc_mail")
	public String pcMail;

	/** 携帯メールアドレス */
	@Column(name="mobile_mail")
	public String mobileMail;

	/** 生年月日 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="birthday")
	public Date birthday;

	/** 性別区分 */
	@Column(name="sex_kbn")
	public Integer sexKbn;

	/** 電話番号1 */
	@Column(name="phone_no1")
	public String phoneNo1;

	/** 電話番号2 */
	@Column(name="phone_no2")
	public String phoneNo2;

	/** 電話番号3 */
	@Column(name="phone_no3")
	public String phoneNo3;

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

	/** 雇用形態区分 */
	@Column(name="employ_ptn_kbn")
	public Integer employPtnKbn;

	/** 転勤フラグ */
	@Column(name="transfer_flg")
	public Integer transferFlg;

	/** 深夜勤務フラグ */
	@Column(name="midnight_shift_flg")
	public Integer midnightShiftFlg;

	/** 海外勤務フラグ */
	@Column(name="foreign_work_flg")
	public Integer foreignWorkFlg;

	/** 飲食業界経験区分 */
	@Column(name="food_exp_kbn")
	public Integer foodExpKbn;

	/** 希望年収区分 */
	@Column(name="salary_kbn")
	public Integer salaryKbn;

	/** 現在（前職）の年収  */
	@Column(name = "work_salary")
	public String workSalary;

	/** スカウト自己PR */
	@Column(name="scout_self_pr")
	public String scoutSelfPr;

	/** 応募自己PR */
	@Column(name="application_self_pr")
	public String applicationSelfPr;

	/** 事前登録用自己PR */
	@Column(name="advanced_registration_self_pr")
	public String advancedRegistrationSelfPr;

	/** 新着求人情報受信フラグ */
	@Column(name="job_info_reception_flg")
	public Integer jobInfoReceptionFlg;

	/** メルマガ受信フラグ */
	@Column(name="mail_magazine_reception_flg")
	public Integer mailMagazineReceptionFlg;

	/** スカウトメール受信フラグ */
	@Column(name="scout_mail_reception_flg")
	public Integer scoutMailReceptionFlg;

	/** 事前会員メルマガ受信フラグ */
	@Column(name="advanced_mail_magazine_reception_flg")
	public Integer advancedMailMagazineReceptionFlg;

	/** PCメール配信停止フラグ */
	@Column(name="pc_mail_stop_flg")
	public Integer pcMailStopFlg;

	/** モバイルメール配信停止フラグ */
	@Column(name="mobile_mail_stop_flg")
	public Integer mobileMailStopFlg;

	/** 雑誌受け取りフラグ */
	@Column(name="gourmet_magazine_reception_flg")
	public Integer gourmetMagazineReceptionFlg;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** ログインID */
	@Column(name="login_id")
	public String loginId;

	/** パスワード */
	@Column(name="password")
	public String password;

	/**
	 * IDの取得
	 */
	public abstract Integer getId();

	/**
	 * IDのセット
	 */
	public abstract void setId(Integer id);


	/** 事前登録登録日時 */
	public static final String ADVANCED_REGISTRATION_DATETIME = "advanced_registration_datetime";

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

	/** 雇用形態区分 */
	public static final String EMPLOY_PTN_KBN = "employ_ptn_kbn";

	/** 転勤フラグ */
	public static final String TRANSFER_FLG = "transfer_flg";

	/** 深夜勤務フラグ */
	public static final String MIDNIGHT_SHIFT_FLG = "midnight_shift_flg";

	/** 海外勤務フラグ */
	public static final String FOREIGN_WORK_FLG = "foreign_work_flg";

	/** 飲食業界経験区分 */
	public static final String FOOD_EXP_KBN = "food_exp_kbn";

	/** 希望年収区分 */
	public static final String SALARY_KBN = "salary_kbn";

	/** 現在（前職）の年収 */
	public static final String WORK_SALARY = "work_salary";

	/** スカウト自己PR */
	public static final String SCOUT_SELF_PR = "scout_self_pr";

	/** 応募自己PR */
	public static final String APPLICATION_SELF_PR = "application_self_pr";

	/** 事前登録用自己PR  */
	public static final String ADVANCED_REGISTRATION_SELF_PR = "advanced_registration_self_pr";

	/** 新着求人情報受信フラグ */
	public static final String JOB_INFO_RECEPTION_FLG = "job_info_reception_flg";

	/** メルマガ受信フラグ */
	public static final String MAIL_MAGAZINE_RECEPTION_FLG = "mail_magazine_reception_flg";

	/** スカウトメール受信フラグ */
	public static final String SCOUT_MAIL_RECEPTION_FLG = "scout_mail_reception_flg";

	/** 事前登録メルマガ受信フラグ */
	public static final String ADVANCED_MAIL_MAGAZINE_RECEPTION_FLG = "advanced_mail_magazine_reception_flg";

	/** 雑誌受け取りフラグ */
	public static final String GOURMET_MAGAZINE_RECEPTION_FLG ="gourmet_magazine_reception_flg";

	/** PCメール配信停止フラグ */
	public static final String PC_MAIL_STOP_FLG = "pc_mail_stop_flg";

	/** モバイルメール配信停止フラグ */
	public static final String MOBILE_MAIL_STOP_FLG = "mobile_mail_stop_flg";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** ログインID */
	public static final String LOGIN_ID = "login_id";

	/** パスワード */
	public static final String PASSWORD = "password";

}
