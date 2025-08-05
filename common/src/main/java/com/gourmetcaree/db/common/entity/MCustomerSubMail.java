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
 * 顧客サブメールのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_customer_sub_mail")
public class MCustomerSubMail extends AbstractCommonMasqueradeEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 8215281730740416506L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_customer_sub_mail_id_gen")
    @SequenceGenerator(name="m_customer_sub_mail_id_gen", sequenceName="m_customer_sub_mail_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    @Column(name=CUSTOMER_ID)
    public Integer customerId;

    /** サブメールアドレス */
    @Column(name=SUB_MAIL)
    public String subMail;

    /** サブメール受信フラグ */
    @Column(name=SUBMAIL_RECEPTION_FLG)
    public Integer submailReceptionFlg;

    @ManyToOne
    @JoinColumn(name="customer_id")
    public MCustomer mCustomer;


    /** テーブル名 */
    public static final String TABLE_NAME = "m_customer_sub_mail";

    /** ID */
    public static final String ID ="id";

    /** 顧客ID */
    public static final String CUSTOMER_ID = "customer_id";

    /** サブメールアドレス */
    public static final String SUB_MAIL = "sub_mail";

    /** サブメール受信フラグ */
    public static final String SUBMAIL_RECEPTION_FLG = "submail_reception_flg";
}