package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 特集マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_special")
public class MSpecial extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_special_id_gen")
    @SequenceGenerator(name="m_special_id_gen", sequenceName="m_special_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 特集名 */
    @Column(name="special_name")
    public String specialName;

    /** 表示名 */
    @Column(name="display_name")
    public String displayName;

    /** 掲載開始日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="post_start_datetime")
    public Date postStartDatetime;

    /** 掲載終了日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="post_end_datetime")
    public Date postEndDatetime;

    /** 説明 */
    @Column(name="explanation")
    public String explanation;

    /** 特集表示マスタのリスト */
	@OneToMany(mappedBy = "mSpecial")
    public List<MSpecialDisplay> mSpecialDisplayList;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_special";

    /** ID */
    public static final String ID ="id";

    /** 特集名 */
    public static final String SPECIAL_NAME ="special_name";

    /** 表示名 */
    public static final String DISPLAY_NAME = "display_name";

    /** 掲載開始日時 */
    public static final String POST_START_DATETIME = "post_start_datetime";

    /** 掲載終了日時 */
    public static final String POST_END_DATETIME = "post_end_datetime";

    /** 説明 */
    public static final String EXPLANATION = "explanation";

    /** 特集表示マスタのリスト */
    public static final String M_SPECIAL_DISPLAY_LIST = "m_special_display_list";
}