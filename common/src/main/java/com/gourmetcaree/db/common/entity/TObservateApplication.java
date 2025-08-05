package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 店舗見学応募のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_observate_application")
public class TObservateApplication extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1371807126691402459L;

	/** ID */
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_observate_application_id_gen")
	@SequenceGenerator(name="t_observate_application_id_gen", sequenceName="t_observate_application_id_seq", allocationSize=1)
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

	/** 内容 */
	@Column(name="contents")
	public String contents;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 応募先 */
	@Column(name="application_name")
	public String applicationName;

	/** 店舗見学区分 */
	@Column(name="observation_kbn")
	public Integer observationKbn;

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

	/** 生年月日 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="birthday")
	public Date birthday;

	/** メールトークン */
	@Column(name="mail_token")
	public String mailToken;

	/** 顧客エンティティ */
	@OneToOne
	@JoinColumn(name="customer_id")
	public MCustomer mCustomer;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_observate_application";

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

	/** 内容 */
	public static final String CONTENTS = "contents";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 応募先 */
	public static final String APPLICATION_NAME = "application_name";

	/** 店舗見学区分 */
	public static final String OBSERVATION_KBN = "observation_kbn";

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

	/** メモ更新日時 */
	public static final String MEMO_UPDATE_DATETIME = "memo_update_datetime";

	/** いたずらフラグ */
	public static final String MISCHIEF_FLG = "mischief_flg";

	/** 生年月日 */
	public static final String BIRTHDAY = "birthday";

	/** メールトークン */
	public static final String MAIL_TOKEN = "mail_token";

	/** 顧客エンティティ */
	public static final String M_CUSTOMER = "m_customer";

}
