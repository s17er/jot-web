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
 * スカウト履歴のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_scout_history")
public class TScoutHistory extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_history_id_gen")
    @SequenceGenerator(name="t_scout_history_id_gen", sequenceName="t_scout_history_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** 会員ID */
    @Column(name="member_id")
    public Integer memberId;

    /** 送信日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="send_datetime")
    public Date sendDatetime;

}