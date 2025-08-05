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
 * ステータスマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_status")
public class MStatus extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
	private static final long serialVersionUID = 7848963706458961984L;

	/** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_status_id_gen")
    @SequenceGenerator(name="m_status_id_gen", sequenceName="m_status_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** ステータス区分 */
    @Column(name="status_kbn")
    public Integer statusKbn;

    /** ステータスコード */
    @Column(name="status_cd")
    public Integer statusCd;

    /** ステータス名 */
    @Column(name="status_name")
    public String statusName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_status";

    /** ID */
    public static final String ID ="id";

    /** ステータス区分 */
    public static final String STATUS_KBN ="status_kbn";

    /** ステータスコード */
    public static final String STATUS_CD ="status_cd";

    /** ステータス名 */
    public static final String STATUS_NAME ="status_name";

    /** 表示順 */
    public static final String DISPLAY_ORDER ="display_order";

}