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
 * 顧客担当会社マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_customer_company")
public class MCustomerCompany extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_customer_company_id_gen")
    @SequenceGenerator(name="m_customer_company_id_gen", sequenceName="m_customer_company_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** 会社ID */
    @Column(name="company_id")
    public Integer companyId;

    /** 営業担当者ID */
    @Column(name="sales_id")
    public Integer salesId;

    @ManyToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;


    /** テーブル名 */
    public static final String TABLE_NAME = "m_customer_company";

    /** ID */
    public static final String ID ="id";

    /** 顧客ID */
    public static final String CUSTOMER_ID ="customer_id";

    /** 会社ID */
    public static final String COMPANY_ID ="company_id";

    /** 営業担当者ID */
    public static final String SALES_ID ="sales_id";

    /** 顧客マスタエンティティ */
    public static final String M_CUSTOMER ="m_customer";

}