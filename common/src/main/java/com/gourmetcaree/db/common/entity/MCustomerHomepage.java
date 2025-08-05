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
 * 顧客ホームページのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_customer_homepage")
public class MCustomerHomepage extends AbstractCommonMasqueradeEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 8715282731740416506L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_customer_homepage_id_gen")
    @SequenceGenerator(name="m_customer_homepage_id_gen", sequenceName="m_customer_homepage_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    @Column(name=CUSTOMER_ID)
    public Integer customerId;

	/** url */
	@Column(name = URL)
	public String url;

	/** コメント */
	@Column(name = COMMENT)
	public String comment;

	/** 顧客エンティティ */
	@ManyToOne
	@JoinColumn(name="customer_id")
	public MCustomer mCustomer;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_customer_homepage";

	/** ID */
	public static final String ID ="id";

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

	/** url */
	public static final String URL ="url";

	/** コメント */
	public static final String COMMENT ="comment";

	/** 顧客エンティティ */
	public static final String M_CUSTOMER ="m_customer";
}