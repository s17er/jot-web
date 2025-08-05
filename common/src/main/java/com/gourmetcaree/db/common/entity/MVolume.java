package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 号数マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_volume")
public class MVolume extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_volume_id_gen")
    @SequenceGenerator(name="m_volume_id_gen", sequenceName="m_volume_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

    /** 号数 */
    @Column(name="volume")
    public Integer volume;

    /** 締切日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deadline_datetime")
    public Date deadlineDatetime;

    /** 確定締切日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fixed_deadline_datetime")
    public Date fixedDeadlineDatetime;

    /** 掲載開始日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="post_start_datetime")
    public Date postStartDatetime;

    /** 掲載終了日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="post_end_datetime")
    public Date postEndDatetime;

	/** WEBデータエンティティ */
	@OneToOne(mappedBy = "mVolume")
	public TWeb tWeb;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_volume";

    /** ID */
    public static final String ID ="id";

    /** エリアコード */
    public static final String AREA_CD ="area_cd";

    /** 号数 */
    public static final String VOLUME = "volume";

    /** 締切日時 */
    public static final String DEADLINE_DATETIME = "deadline_datetime";

    /** 確定締切日時 */
    public static final String FIXED_DEADLINE_DATETIME = "fixed_deadline_datetime";

    /** 掲載開始日時 */
    public static final String POST_START_DATETIME = "post_start_datetime";

    /** 掲載終了日時 */
    public static final String POST_END_DATETIME = "post_end_datetime";


}