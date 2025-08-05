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
 * 鉄道会社マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_railroad")
public class MRailroad extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_railroad_id_gen")
    @SequenceGenerator(name="m_railroad_id_gen", sequenceName="m_railroad_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

    /** 鉄道会社名 */
    @Column(name="railroad_name")
    public String railroadName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_railroad";

    /** ID */
    public static final String ID ="id";

    /** エリアコード */
    public static final String AREA_CD ="area_cd";

    /** 鉄道会社名 */
    public static final String RAILROAD_NAME ="railroad_name";

    /** 表示順 */
    public static final String DISPLAY_ORDER ="display_order";

}