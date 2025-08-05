package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * エリアマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_area")
public class MArea extends AbstractCommonEntity {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_area_id_gen")
    @SequenceGenerator(name="m_area_id_gen", sequenceName="m_area_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

    /** エリア名 */
    @Column(name="area_name")
    public String areaName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** リンク名 */
    @Column(name="link_name")
    public String linkName;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_area";

    /** ID */
    public static final String ID ="id";

    /** エリアコード */
    public static final String AREA_CD = "area_cd";

    /** エリア名 */
    public static final String AREA_NAME = "area_name";

    /** 表示順 */
    public static final String DISPLAY_ORDER = "display_order";

    /** リンク名 */
    public static final String LINK_NAME = "link_name";

}