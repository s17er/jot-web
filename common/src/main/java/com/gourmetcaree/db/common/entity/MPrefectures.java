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
 * 都道府県マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_prefectures")
public class MPrefectures extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_prefectures_id_gen")
    @SequenceGenerator(name="m_prefectures_id_gen", sequenceName="m_prefectures_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 都道府県コード */
    @Column(name="prefectures_cd")
    public Integer prefecturesCd;

    /** 都道府県名 */
    @Column(name="prefectures_name")
    public String prefecturesName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    public static final String TABLE_NAME = "m_prefectures";

	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";

    /** 海外の値 */
    public static final int KAIGAI = 48;

}