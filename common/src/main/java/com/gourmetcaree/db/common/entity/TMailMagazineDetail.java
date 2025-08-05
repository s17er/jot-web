package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * メルマガ詳細のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_mail_magazine_detail")
public class TMailMagazineDetail extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_mail_magazine_detail_id_gen")
	@SequenceGenerator(name="t_mail_magazine_detail_id_gen", sequenceName="t_mail_magazine_detail_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** メルマガID */
	@Column(name="mail_magazine_id")
	public Integer mailMagazineId;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** メルマガタイトル */
	@Column(name="mail_magazine_title")
	public String mailMagazineTitle;

	/** 本文 */
	@Column(name="body")
	public String body;

	/** 配信形式 */
	@Column(name="delivery_type")
	public Integer deliveryType;

	/** メルマガ */
	@ManyToOne
	@JoinColumn(name="mail_magazine_id")
	public TMailMagazine tMailMagazine;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_mail_magazine_detail";

	/** ID */
	public static final String ID = "id";

	/** メルマガID */
	public static final String MAIL_MAGAZINE_ID = "mail_magazine_id";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** メルマガタイトル */
	public static final String MAIL_MAGAZINE_TITLE = "mail_magazine_title";

	/** 本文 */
	public static final String BODY = "body";

	/** 配信形式 */
	public static final String DELIVERY_TYPE = "delivery_type";

	/** メルマガエンティティ */
	public static final String T_MAIL_MAGAZINE = "t_mail_magazine";
}