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
 * 路線マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_route")
public class MRoute extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_route_id_gen")
    @SequenceGenerator(name="m_route_id_gen", sequenceName="m_route_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 鉄道会社ID */
    @Column(name="railroad_id")
    public Integer railroadId;

    /** 路線名 */
    @Column(name="route_name")
    public String routeName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_route";

    /** ID */
    public static final String ID ="id";

    /** 鉄道会社ID */
    public static final String RAILROAD_ID ="railroad_id";

    /** 路線名 */
    public static final String ROUTE_NAME ="route_name";

    /** 表示順 */
    public static final String DISPLAY_ORDER ="display_order";
}