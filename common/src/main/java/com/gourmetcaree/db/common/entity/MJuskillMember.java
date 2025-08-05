package com.gourmetcaree.db.common.entity;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.Serializable;
import java.sql.Date;
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
 * ジャスキル会員マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_juskill_member")
public class MJuskillMember extends AbstractCommonEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 513836841156014670L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_juskill_member_id_gen")
	@SequenceGenerator(name="m_juskill_member_id_gen", sequenceName="m_juskill_member_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name = MEMBER_ID)
	public Integer memberId;

	/** ジャスキル移行会員フラグ */
	@Column(name = JUSKILL_MIGRATION_FLG)
	public Integer juskillMigrationFlg;

	/** ジャスキル会員No */
	@Column(name = JUSKILL_MEMBER_NO)
	public Integer juskillMemberNo;

	/** ジャスキル登録日 */
	@Column(name = JUSKILL_ENTRY_DATE)
	public Date juskillEntryDate;

	/** メール希望 */
	@Column(name = MAIL_HOPE)
	public String mailHope;

	/** 転職時期 */
	@Column(name = TRANSFER_TIMING)
	public String transferTiming;

	/** ジャスキル会員名 */
	@Column(name = JUSKILL_MEMBER_NAME)
	public String juskillMemberName;

	/** 生年月日 */
	@Column(name = BIRTHDAY)
	public Date birthday;

	/** 性別区分 */
	@Column(name = SEX_KBN)
	public Integer sexKbn;

	/** 希望業態 */
	@Column(name = HOPE_INDUSTRY)
	public String hopeIndustry;

	/** 希望職種 */
	@Column(name = HOPE_JOB)
	public String hopeJob;

	/** 経験 */
	@Column(name = EXPERIENCE)
	public String experience;

	/** 希望年収 */
	@Column(name = HOPE_SALARY)
	public String hopeSalary;

	/** 郵便番号 */
	@Column(name = ZIP_CD)
	public String zipCd;

	/** 都道府県コード */
	@Column(name = PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 住所1 */
	@Column(name = ADDRESS)
	public String address;

	/** ビル名 */
	@Column(name = BUILDING_NAME)
	public String buildingName;

	/** 路線 */
	@Column(name = ROUTE)
	public String route;

	/** 最寄駅 */
	@Column(name = CLOSEST_STATION)
	public String closestStation;

	/** 電話1 */
	@Column(name = PHONE_NO1)
	public String phoneNo1;

	/** 電話2 */
	@Column(name = PHONE_NO2)
	public String phoneNo2;

	/** 連絡先3（ＰＣメール） */
	@Column(name = PC_MAIL)
	public String pcMail;

	/** メール */
	@Column(name = MAIL)
	public String mail;

	/** PW */
	@Column(name = PW)
	public String pw;

	/** 最終学歴 */
	@Column(name = FINAL_SCHOOL_HISTORY)
	public String finalSchoolHistory;

	/** 学歴備考 */
	@Column(name = SCHOOL_HISTORY_NOTE)
	public String schoolHistoryNote;

	/** 最終職種 */
	@Column(name = FINAL_CAREER_HISTORY)
	public String finalCareerHistory;

	/** 希望職種2 */
	@Column(name = HOPE_JOB2)
	public String hopeJob2;

	/** 取得資格 */
	@Column(name = QUALIFICATION)
	public String qualification;

	/** 登録経路 */
	@Column(name = ENTRY_PATH)
	public String entryPath;

	/** 特記事項 */
	@Column(name = NOTICE)
	public String notice;

	/** 健康状態区分 */
	@Column(name = HEALTH_KBN)
	public Integer healthKbn;

	/** 刑事罰訴訟区分 */
	@Column(name = SIN_KBN)
	public Integer sinKbn;

	/** 暴力団区分 */
	@Column(name = GANGSTERS_KBN)
	public Integer gangstersKbn;

	/** 履歴修正フラグ */
	@Column(name = HISTORY_MODIFICATION_FLG)
	public Integer historyModificationFlg;

	/** 入退社修正フラグ */
	@Column(name = ON_LEAVING_MODIFICATION_FLG)
	public Integer onLeavingModificationFlg;

	/** 取得資格修正フラグ */
	@Column(name = QUALIFICATION_MODIFICATION_FLG)
	public Integer qualificationModificationFlg;

	/** 名前(苗字のみ) */
	@Column(name = FAMILY_NAME)
	public String familyName;

	/** ヒアリング担当者 */
	@Column(name = HEARING_REP)
	public String hearingRep;

	/** 転職状況 */
	@Column(name = JOB_CHANGE_STATUS)
	public String jobChangeStatus;

	/** パスワード */
	@Column(name = PASSWORD)
	public String password;

	/**
	 * nullでアップデートするカラムを返す
	 * @return
	 */
	public static String[] getNullables() {
		return	new String[]{
				toCamelCase(SEX_KBN),
				toCamelCase(PREFECTURES_CD),
				toCamelCase(BIRTHDAY)};
	}

	/** ジャスキル会員職歴エンティティリスト */
	@OneToMany(mappedBy = "mJuskillMember")
	public List<MJuskillMemberCareerHistory> mJuskillMemberCareerHistoryList;
	public static final String M_JUSKILL_MEMBER_CAREER_HISTORY_LIST = "mJuskillMemberCareerHistoryList";

	/** テーブル名 */
	public static final String TABLE_NAME = "m_juskill_member";

	/** ID */
	public static final String ID = "id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** ジャスキル移行会員フラグ */
	public static final String JUSKILL_MIGRATION_FLG = "juskill_migration_flg";

	/** ジャスキル会員No */
	public static final String JUSKILL_MEMBER_NO = "juskill_member_no";

	/** ジャスキル登録日 */
	public static final String JUSKILL_ENTRY_DATE = "juskill_entry_date";

	/** メール希望 */
	public static final String MAIL_HOPE = "mail_hope";

	/** 転職時期 */
	public static final String TRANSFER_TIMING = "transfer_timing";

	/** ジャスキル会員名 */
	public static final String JUSKILL_MEMBER_NAME = "juskill_member_name";

	/** 生年月日 */
	public static final String BIRTHDAY = "birthday";

	/** 性別区分 */
	public static final String SEX_KBN = "sex_kbn";

	/** 希望業態 */
	public static final String HOPE_INDUSTRY = "hope_industry";

	/** 希望職種 */
	public static final String HOPE_JOB = "hope_job";

	/** 経験 */
	public static final String EXPERIENCE = "experience";

	/** 希望年収 */
	public static final String HOPE_SALARY = "hope_salary";

	/** 郵便番号 */
	public static final String ZIP_CD = "zip_cd";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 住所1 */
	public static final String ADDRESS = "address";

	/** ビル名 */
	public static final String BUILDING_NAME = "building_name";

	/** 路線 */
	public static final String ROUTE = "route";

	/** 最寄駅 */
	public static final String CLOSEST_STATION = "closest_station";

	/** 電話1 */
	public static final String PHONE_NO1 = "phone_no1";

	/** 電話2 */
	public static final String PHONE_NO2 = "phone_no2";

	/** 連絡先3（ＰＣメール） */
	public static final String PC_MAIL = "pc_mail";

	/** メール */
	public static final String MAIL = "mail";

	/** フリーワード */
	public static final String FREE_WORD = "free_word";

	/** PW */
	public static final String PW = "pw";

	/** 最終学歴 */
	public static final String FINAL_SCHOOL_HISTORY = "final_school_history";

	/** 学歴備考 */
	public static final String SCHOOL_HISTORY_NOTE = "school_history_note";

	/** 最終職種 */
	public static final String FINAL_CAREER_HISTORY = "final_career_history";

	/** 希望職種2 */
	public static final String HOPE_JOB2 = "hope_job2";

	/** 取得資格 */
	public static final String QUALIFICATION = "qualification";

	/** 登録経路 */
	public static final String ENTRY_PATH = "entry_path";

	/** 特記事項 */
	public static final String NOTICE = "notice";

	/** ジャスキル登録フラグ */
	public static final String JUSKILL_FLG = "juskill_flg";

	/** 健康状態区分 */
	public static final String HEALTH_KBN = "health_kbn";

	/** 刑事罰訴訟区分 */
	public static final String SIN_KBN = "sin_kbn";

	/** 暴力団区分 */
	public static final String GANGSTERS_KBN = "gangsters_kbn";

	/** 履歴修正フラグ */
	public static final String HISTORY_MODIFICATION_FLG = "history_modification_flg";

	/** 入退社修正フラグ */
	public static final String ON_LEAVING_MODIFICATION_FLG = "on_leaving_modification_flg";

	/** 取得資格修正フラグ */
	public static final String QUALIFICATION_MODIFICATION_FLG = "qualification_modification_flg";

	/** 名前(苗字のみ) */
	public static final String FAMILY_NAME = "family_name";

	/** ヒアリング担当者 */
	public static final String HEARING_REP = "hearing_rep";

	/** 転職状況 */
	public static final String JOB_CHANGE_STATUS = "job_change_status";

	/** パスワード */
	public static final String PASSWORD = "password";

}