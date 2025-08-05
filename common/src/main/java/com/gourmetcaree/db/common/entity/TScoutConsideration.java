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
 * スカウト検討中のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_scout_consideration")
public class TScoutConsideration extends AbstractCommonMasqueradeEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_consideration_id_gen")
    @SequenceGenerator(name="t_scout_consideration_id_gen", sequenceName="t_scout_consideration_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** 会員ID */
    @Column(name="member_id")
    public Integer memberId;

    /** 会員エンティティ */
    @OneToOne
    @JoinColumn(name="member_id")
    public MMember mMember;

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

	/** 会員ID */
	public static final String MEMBRE_ID ="member_id";

}