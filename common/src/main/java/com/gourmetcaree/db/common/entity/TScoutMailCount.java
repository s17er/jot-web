package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * スカウトメール数のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_scout_mail_count")
public class TScoutMailCount extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_mail_count_id_gen")
    @SequenceGenerator(name="t_scout_mail_count_id_gen", sequenceName="t_scout_mail_count_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** スカウトメール数 */
    @Column(name="scout_count")
    public Integer scoutCount;

}