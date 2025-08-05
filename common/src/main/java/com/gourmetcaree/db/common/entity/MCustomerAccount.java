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
 * 顧客アカウントマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_customer_account")
public class MCustomerAccount extends AbstractCommonMasqueradeEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_customer_account_id_gen")
    @SequenceGenerator(name="m_customer_account_id_gen", sequenceName="m_customer_account_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** ログインID */
    @Column(name="login_id")
    public String loginId;

    /** パスワード */
    @Column(name="password")
    public String password;

    /** 顧客マスタのリスト */
    @OneToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_customer_account";

    /** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** ログインID */
	public static final String LOGIN_ID = "login_id";

	/** パスワード */
	public static final String PASSWORD = "password";

	/** 顧客マスタのリスト */
	public static final String M_CUSTOMER = "m_customer";
}
