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
 * 駅マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_station")
public class MStation extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_station_id_gen")
    @SequenceGenerator(name="m_station_id_gen", sequenceName="m_station_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 路線ID */
    @Column(name="route_id")
    public Integer routeId;

    /** 駅名 */
    @Column(name="station_name")
    public String stationName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_station";

    /** ID */
    public static final String ID ="id";

    /** 路線ID */
    public static final String ROUTE_ID ="route_id";

    /** 駅名 */
    public static final String STATION_NAME ="station_name";

    /** 表示順 */
    public static final String DISPLAY_ORDER ="display_order";
}