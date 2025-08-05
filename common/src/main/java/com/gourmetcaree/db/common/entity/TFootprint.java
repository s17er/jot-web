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
 * 足あとのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_footprint")
public class TFootprint extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_footprint_id_gen")
	@SequenceGenerator(name="t_footprint_id_gen", sequenceName="t_footprint_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** 訪問日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="access_datetime")
	public Date accessDatetime;

	/** 閲覧フラグ */
	@Column(name="read_flg")
	public Integer readFlg;

	/** 顧客マスタエンティティ */
	@OneToOne
	@JoinColumn(name="customer_id")
	public MCustomer mCustomer;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_footprint";

	/** ID */
	public static final String ID ="id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** 訪問日時 */
	public static final String ACCESS_DATETIME = "access_datetime";

	/** 閲覧フラグ */
	public static final String READ_FLG = "read_flg";

	/** 顧客マスタエンティティ */
	public static final String M_CUSTOMER = "m_customer";
}