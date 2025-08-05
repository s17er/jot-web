package com.gourmetcaree.db.common.entity;

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

import com.gourmetcaree.db.common.entity.member.BaseMemberEntity;

/**
 * 会員マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_member")
public class MMember extends BaseMemberEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_member_id_gen")
	@SequenceGenerator(name="m_member_id_gen", sequenceName="m_member_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 登録日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="registration_datetime")
	public Date registrationDatetime;




	/** 自動ログインコード */
	@Column(name="auto_login_cd")
	public String autoLoginCd;

	/** 会員区分 */
	@Column(name="member_kbn")
	public Integer memberKbn;

	/** 旧会員ID */
	@Column(name="old_member_id")
	public String oldMemberId;

	/** ジャスキル登録フラグ */
	@Column(name="juskill_flg")
	public Integer juskillFlg;

	/** 最終更新日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_update_datetime")
	public Date lastUpdateDatetime;

	/** 削除理由区分 */
	@Column(name="delete_reason_kbn")
	public Integer deleteReasonKbn;

	/** サブメールアドレス */
	@Column(name="sub_mail_address")
	public String subMailAddress;

	/** メインメール受信フラグ */
	@Column(name ="main_mail_stop_flg")
	public Integer mainMailStopFlg;

	/** サブメール受信フラグ */
	@Column(name ="sub_mail_stop_flg")
	public Integer subMailStopFlg;

	/** プロフィール更新日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="profile_update_datetime")
	public Date profileUpdateDatetime;

	/** 経験役職区分 */
	@Column(name="exp_manager_kbn")
	public Integer expManagerKbn;

	/** 経験役職年数区分 */
	@Column(name="exp_manager_year_kbn")
	public Integer expManagerYearKbn;

	/** 経験管理人数区分 */
	@Column(name="exp_manager_persons_kbn")
	public Integer expManagerPersonsKbn;

	/** 取得資格その他 */
	@Column(name = "qualification_other")
	public String qualificationOther;

	/** ジャスキル連絡フラグ（グルメキャリー転職相談窓口） */
	@Column(name = "juskill_contact_flg")
	public Integer juskillContactFlg;

	/** 会員属性エンティティリスト */
	@OneToMany(mappedBy = "mMember")
	public List<MMemberAttribute> mMemberAttributeList;

	/** ログイン履歴エンティティ */
	@OneToOne(mappedBy = "mMember")
	public TLoginHistory tLoginHistory;

	/** スカウト検討中エンティティ */
	@OneToOne(mappedBy = "mMember")
	public TScoutConsideration tScoutConsideration;

	/** 会員エリアマスタエンティティ */
	@OneToMany(mappedBy = "mMember")
	public List<MMemberArea> mMemberAreaList;

	/** 会員希望勤務地マスタエンティティ */
	@OneToMany(mappedBy = "mMember")
	public List<MMemberHopeCity> mMemberHopeCityList;

	/** 会員希望勤務地viewエンティティ */
	@OneToMany(mappedBy = "mMember")
	public List<VMemberHopeCity> vMemberHopeCityList;

	/** 配送状態 */
	@Column(name="delivery_status")
	public Integer deliveryStatus;

	/** メルマガトークン */
	@Column(name="mail_magazine_token")
	public String mailMagazineToken;

	/** 管理者用パスワード */
	@Column(name="admin_password")
	public String adminPassword;


	/** テーブル名 */
	public static final String TABLE_NAME = "m_member";

	/** ID */
	public static final String ID ="id";

	/** 登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";


	/** 自動ログインコード */
	public static final String AUTO_LOGIN_CD = "auto_login_cd";

	/** 会員区分 */
	public static final String MEMBER_KBN = "member_kbn";

	/** 旧会員ID */
	public static final String OLD_MEMBER_ID = "old_member_id";

	/** ジャスキル登録フラグ */
	public static final String JUSKILL_FLG = "juskill_flg";

	/** 最終更新日時 */
	public static final String LAST_UPDATE_DATETIME = "last_update_datetime";

	/** 削除理由区分 */
	public static final String DELETE_REASON_KBN = "delete_reason_kbn";

	/** 経験役職区分 */
	public static final String EXP_MANAGER_KBN = "exp_manager_kbn";

	/** 経験役職年数区分 */
	public static final String EXP_MANAGER_YEAR_KBN = "exp_manager_year_kbn";

	/** 経験管理人数区分 */
	public static final String EXP_MANAGER_PERSONS_KBN = "exp_manager_persons_kbn";

	/** 取得資格その他 */
	public static final String QUALIFICATION_OTHER = "qualification_other";

	/** ジャスキル連絡フラグ */
	public static final String JUSKILL_CONTACT_FLG = "juskill_contact_flg";

	/** サブメールアドレス */
	public static final String sub_mail_address = "sub_mail_address";

	/** メインメール受信フラグ */
	public static final String main_mail_stop_flg = "main_mail_stop_flg";

	/** サブメール受信フラグ */
	public static final String sub_mail_stop_flg = "sub_mail_stop_flg";

	/** プロフィール更新日時 */
	public static final String profile_update_datetime = "profile_update_datetime";

	/** 会員属性エンティティリスト */
	public static final String M_MEMBER_ATTRIBUTE_LIST = "m_member_attribute_list";

	/** ログイン履歴エンティティ */
	public static final String TLOGINHISTORY = "t_login_history";

	/** スカウト検討中エンティティ */
	public static final String T_SCOUT_CONSIDERATION = "t_scout_consideration";

	/** 会員エリアマスタエンティティリスト */
	public static final String M_MEMBER_AREA_LIST = "m_member_area_list";

	/** 会員希望勤務地マスタエンティティリスト */
	public static final String M_MEMBER_HOPE_CITY_LIST = "m_member_hope_city_list";

	/** 会員希望勤務地viewエンティティリスト */
	public static final String V_MEMBER_HOPE_CITY_LIST = "v_member_hope_city_list";

	/** 発送状況 */
	public static final String DELIVERY_STATUS ="delivery_status";

	/** メルマガトークン */
	public static final String MAIL_MAGAZINE_TOKEN = "mail_magazine_token";

	/** 管理者用パスワード */
	public static final String ADMIN_PASSWORD = "admin_password";

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}