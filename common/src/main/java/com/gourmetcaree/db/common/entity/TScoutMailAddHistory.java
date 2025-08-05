package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * スカウトメール追加履歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_scout_mail_add_history")
public class TScoutMailAddHistory extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_mail_add_history_id_gen")
    @SequenceGenerator(name="t_scout_mail_add_history_id_gen", sequenceName="t_scout_mail_add_history_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** 追加日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="add_date")
    public Date addDate;

    /** 追加件数 */
    @Column(name="add_count")
    public Integer addCount;

    /** 追加担当者ID */
    @Column(name="sales_id")
    public Integer salesId;

}