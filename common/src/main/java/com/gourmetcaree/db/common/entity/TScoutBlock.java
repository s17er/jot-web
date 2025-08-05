package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * スカウトブロックのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_scout_block")
public class TScoutBlock extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_block_id_gen")
	@SequenceGenerator(name="t_scout_block_id_gen", sequenceName="t_scout_block_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

    /** 顧客マスタエンティティ */
    @OneToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_scout_block";

	/** ID */
	public static final String ID ="id";

	/** 会員ID */
	public static final String MEMBER_ID ="member_id";

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

    /** 顧客マスタエンティティ */
	public static final String M_CUSTOMER ="m_customer";
}