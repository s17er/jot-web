package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;

/**
 * 区分マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_type")
@Getter
public class MType extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_type_id_gen")
    @SequenceGenerator(name="m_type_id_gen", sequenceName="m_type_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 区分コード */
    @Column(name="type_cd")
    public String typeCd;

    /** 区分値 */
    @Column(name="type_value")
    public Integer typeValue;

    /** 区分名 */
    @Column(name="type_name")
    public String typeName;

    /** 区分別名 */
    @Column(name="type_other_name")
    public String typeOtherName;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    /** 備考 */
    @Column(name="note")
    public String note;

    /** 子のタイプリスト */
    @Transient
    public List<MType> childrenTypeList;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_type";

    /** ID */
    public static final String ID ="id";

    /** 区分コード */
    public static final String TYPE_CD ="type_cd";

    /** 区分値 */
    public static final String TYPE_VALUE ="type_value";

    /** 区分名 */
    public static final String TYPE_NAME ="type_name";

    /** 区分別名 */
    public static final String TYPE_OTHER_NAME ="type_other_name";

    /** 表示順 */
    public static final String DISPLAY_ORDER ="display_order";

    /** 備考 */
    public static final String NOTE ="note";
}