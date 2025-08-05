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
@Table(name="t_pre_application")
public class TPreApplication extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_pre_application_id_gen")
	@SequenceGenerator(name="t_pre_application_id_gen", sequenceName="t_pre_application_id_seq", allocationSize=1)
	@Column(name="id")
	@Id
	public Integer id;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** メールID */
	@Column(name="mail_id")
	public Integer mailId;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** 名前 */
	@Column(name="name")
	public String name;

	/** 年齢 */
	@Column(name="age")
	public Integer age;

	/** 性別区分 */
	@Column(name="sex_kbn")
	public Integer sexKbn;

	/** 応募日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="application_datetime")
	public Date applicationDatetime;

	/** 都道府県コード */
	@Column(name="prefectures_cd")
	public Integer prefecturesCd;

	/** 市区町村 */
	@Column(name="municipality")
	public String municipality;

	/** 飲食業界経験年数 */
	@Column(name="food_exp_kbn")
	public Integer foodExpKbn;

	/** マネジメント経験(役職) */
	@Column(name="exp_manager_kbn")
	public Integer expManagerKbn;

	/** マネジメント経験(経験年数) */
	@Column(name="exp_manager_year_kbn")
	public Integer expManagerYearKbn;

	/** マネジメント経験(人数) */
	@Column(name="exp_manager_persons_kbn")
	public Integer expManagerPersonsKbn;

	/** 応募自己PR */
	@Column(name="application_self_pr")
	public String applicationSelfPr;

	/** 給与区分 */
	@Column(name="salary_kbn")
	public Integer salaryKbn;

	/** 転勤 */
	@Column(name="transfer_flg")
	public Integer transferFlg;

	/** 深夜勤務 */
	@Column(name="midnight_shift_flg")
	public Integer midnightShiftFlg;

	/** その他資格 */
	@Column(name="qualification_other")
	public String qualificationOther;

	/** 希望職種 */
	@Column(name="job_kbn")
	public Integer jobKbn;

	/** 雇用形態区分 */
	@Column(name="employ_ptn_kbn")
	public Integer employPtnKbn;

	/** 海外勤務経験 */
	@Column(name="foreign_work_flg")
	public Integer foreignWorkFlg;

	/**希望業態 */
	@Column(name="hope_industry")
	public String hopeIndustry;

	/** 希望連絡時間 */
	@Column(name="connect_time")
	public String connectTime;

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

	/** メモ */
	@Column(name="memo")
	public String memo;

	/** ステータス */
	@Column(name="selection_flg")
	public Integer selectionFlg;

	/** 顧客エンティティ */
	@OneToOne
	@JoinColumn(name="customer_id")
	public MCustomer mCustomer;

	/** 応募学歴エンティティ */
	@OneToOne
	@JoinColumn(name="id")
	public TPreApplicationSchoolHistory tPreApplicationSchoolHistory;

	/** 応募属性エンティティ */
	@OneToMany(mappedBy="tPreApplication")
	public List<TPreApplicationAttribute> tPreApplicationAttributeList;

	/** 応募職歴エンティティ */
	@OneToMany(mappedBy="tPreApplication")
	public List<TPreApplicationCareerHistory> tPreApplicationCareerHistoryList;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_pre_application";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String WEB_ID = "web_id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** メールID */
	public static final String MAIL_ID = "mail_id";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** 名前 */
	public static final String NAME = "name";

	/** 年齢 */
	public static final String AGE = "age";

	/** 性別区分 */
	public static final String SEX_KBN = "sex_kbn";

	/** 応募日時 */
	public static final String APPLICATION_DATETIME = "application_datetime";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 市区町村 */
	public static final String MUNICIPALITY = "municipality";

	/** 飲食業界経験年数 */
	public static final String FOOD_EXP_KBN = "food_exp_kbn";

	/**マネジメント経験(役職)*/
	public static final String EXP_MANAGER_KBN = "exp_manager_kbn";

	/**マネジメント経験(経験年数)*/
	public static final String EXP_MANAGER_YEAR_KBN = "exp_manager_year_kbn";

	/**マネジメント経験(人数)*/
	public static final String EXP_MANAGER_PERSONS_KBN = "exp_manager_persons_kbn";

	/** 応募自己PR */
	public static final String APPLICATION_SELF_PR = "application_self_pr";

	/** 給与区分 */
	public static final String SALARY_KBN = "salary_kbn";

	/** 転勤 */
	public static final String TRANSFER_FLG = "transfer_flg";

	/** 深夜勤務 */
	public static final String MIDNIGHT_SHIFT_FLG = "midnight_shift_flg";

	/** その他資格 */
	public static final String QUALIFICATION_OTHER = "qualification_other";

	/** 希望職種 */
	public static final String JOB_KBN = "job_kbn";

	/** 雇用形態区分 */
	public static final String EMPLOY_PTN_KBN = "employ_ptn_kbn";

	/** 海外勤務経験 */
	public static final String FOREIGN_WORK_FLG = "foreign_work_flg";

	/** 希望業態 */
	public static final String HOPE_INDUSTRY = "hope_industry";

	/** 希望連絡時間 */
	public static final String CONNECT_TIME = "connect_time";

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

	/** 顧客エンティティ */
	public static final String M_CUSTOMER = "m_customer";

}