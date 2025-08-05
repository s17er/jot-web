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
 * 特集表示マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_special_display")
public class MSpecialDisplay extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_special_display_id_gen")
    @SequenceGenerator(name="m_special_display_id_gen", sequenceName="m_special_display_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 特集ID */
    @Column(name="special_id")
    public Integer specialId;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public Integer terminalKbn;

	/** 表示位置区分 */
	@Column(name="display_point_kbn")
	public Integer displayPointKbn;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

	/** 特集マスタ */
	@ManyToOne
	@JoinColumn(name="special_id")
	public MSpecial mSpecial;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_special_display";

    /** ID */
    public static final String ID ="id";

    /** 特集ID */
    public static final String SPECIAL_ID ="special_id";

    /** エリアコード */
    public static final String AREA_CD = "area_cd";

    /** 端末区分 */
    public static final String TERMINAL_KBN = "terminal_kbn";

    /** 表示位置区分 */
    public static final String DISPLAY_POINT_KBN = "display_point_kbn";

    /** 表示順 */
    public static final String DISPLAY_ORDER = "display_order";

    /** 特集マスタ */
    public static final String M_SPECIAL = "m_special";
}