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
 * お知らせのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_information")
public class TInformation extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_information_id_gen")
    @SequenceGenerator(name="t_information_id_gen", sequenceName="t_information_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 管理画面区分 */
    @Column(name="management_screen_kbn")
    public Integer managementScreenKbn;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

    /** 本文 */
    @Column(name="body")
    public String body;

}