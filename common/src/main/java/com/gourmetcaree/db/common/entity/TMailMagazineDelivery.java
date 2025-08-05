package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * メルマガ配信先のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_mail_magazine_delivery")
public class TMailMagazineDelivery extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_mail_magazine_delivery_id_gen")
	@SequenceGenerator(name="t_mail_magazine_delivery_id_gen", sequenceName="t_mail_magazine_delivery_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** メルマガID */
	@Column(name="mail_magazine_id")
	public Integer mailMagazineId;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** ユーザー区分 */
	@Column(name="user_kbn")
	public Integer userKbn;

	/** 配信先ID */
	@Column(name="delivery_id")
	public Integer deliveryId;

	/** 配信先名 */
	@Column(name="delivery_name")
	public String deliveryName;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** メールアドレス */
	@Column(name="mail")
	public String mail;

	/** 配信フラグ */
	@Column(name="delivery_flg")
	public Integer deliveryFlg;

	/** 配信日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="delivery_datetime")
	public Date deliveryDatetime;

	/** エラーフラグ */
	@Column(name="error_flg")
	public Integer errorFlg;

	/** エラー受信日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="error_datetime")
	public Date errorDatetime;

	/** 備考 */
	@Column(name="note")
	public String note;

	/** メルマガ */
	@ManyToOne
	@JoinColumn(name="mail_magazine_id")
	public TMailMagazine tMailMagazine;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_mail_magazine_delivery";

	/** ID */
	public static final String ID = "id";

	/** メルマガID */
	public static final String MAIL_MAGAZINE_ID = "mail_magazine_id";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";

	/** ユーザー区分 */
	public static final String USER_KBN = "user_kbn";

	/** 配信先ID */
	public static final String DELIVERY_ID = "delivery_id";

	/** 配信先名 */
	public static final String DELIVERY_NAME = "delivery_name";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** メールアドレス */
	public static final String MAIL = "mail";

	/** 配信フラグ */
	public static final String DELIVERY_FLG = "delivery_flg";

	/** 配信日時 */
	public static final String DELIVERY_DATETIME = "delivery_datetime";

	/** エラーフラグ */
	public static final String ERROR_FLG = "error_flg";

	/** エラー受信日時 */
	public static final String ERROR_DATETIME = "error_datetime";

	/** 備考 */
	public static final String NOTE = "note";

	/** メルマガエンティティ */
	public static final String T_MAIL_MAGAZINE = "t_mail_magazine";

}