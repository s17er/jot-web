package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 仮登録のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_temporary_registration")
public class TTemporaryRegistration extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temporary_registration_id_gen")
	@SequenceGenerator(name="t_temporary_registration_id_gen", sequenceName="t_temporary_registration_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** メールアドレス */
	@Column(name="mail")
	public String mail;

	/** アクセスコード */
	@Column(name="access_cd")
	public String accessCd;

	/** ログインメールアドレス */
	@Column(name="login_mail")
	public String loginMail;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 顧客ログインID */
	@Column(name="customer_login_id")
	public String customerLoginId;

	/** 仮登録区分 */
	@Column(name="temporary_registration_kbn")
	public Integer temporaryRegistrationKbn;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** 発行日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="publish_datetime")
	public Date publishDatetime;

	/** アクセスフラグ */
	@Column(name="access_flg")
	public Integer accessFlg;

	/** アクセス日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="access_datetime")
	public Date accessDatetime;

	/** 事前登録マスタID */
	@Column(name="advanced_registration_id")
	public Integer advancedRegistrationId;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_temporary_registration";

	/** ID */
	public static final String ID ="id";

	/** エリアコード */
	public static final String AREA_CD ="area_cd";

	/** メールアドレス */
	public static final String MAIL = "mail";

	/** アクセスコード */
	public static final String ACCESS_CD = "access_cd";

	/** ログインメールアドレス */
	public static final String LOGIN_MAIL = "login_mail";

	/** 会員ID */
	public static final String MEMBER_ID ="member_id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 顧客ログインID */
	public static final String USTOMER_LOGIN_ID = "customer_login_id";

	/** 仮登録区分 */
	public static final String TEMPORARY_REGISTRATION_KBN = "temporary_registration_kbn";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** 発行日時 */
	public static final String PUBLISH_DATETIME = "publish_datetime";

	/** アクセスフラグ */
	public static final String ACCESS_FLG = "access_flg";

	/** アクセス日時 */
	public static final String ACCESS_DATETIME = "access_datetime";

	/** 事前登録マスタID */
	public static final String ADVANCED_REGISTRATION_ID = "advanced_registration_id";


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}