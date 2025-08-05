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
 * 顧客メルマガエリアのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_customer_mail_magazine_area")
public class MCustomerMailMagazineArea extends AbstractCommonMasqueradeEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 8215282731740416506L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_customer_mail_magazine_area_id_gen")
    @SequenceGenerator(name="m_customer_mail_magazine_area_id_gen", sequenceName="m_customer_mail_magazine_area_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name=CUSTOMER_ID)
    public Integer customerId;

	/** エリアコード */
	@Column(name = AREA_CD)
	public Integer areaCd;

	/** 顧客エンティティ */
	@ManyToOne
	@JoinColumn(name="customer_id")
	public MCustomer mCustomer;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_customer_mail_magazine_area";

	/** ID */
	public static final String ID ="id";

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

	/** エリアコード */
	public static final String AREA_CD ="area_cd";

	/** 顧客エンティティ */
	public static final String M_CUSTOMER ="m_customer";
}