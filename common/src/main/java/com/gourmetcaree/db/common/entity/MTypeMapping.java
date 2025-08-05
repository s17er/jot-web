package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 同タイプコードで親子関係をマッピングするマスタテーブル
 */
@Table(name = MTypeMapping.TABLE_NAME)
@Entity
public class MTypeMapping extends AbstractCommonEntity {


    private static final long serialVersionUID = -3739775792680070305L;

    /** ID */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="m_type_mapping_id_gen")
    @SequenceGenerator(name="m_type_mapping_id_gen", sequenceName="m_type_mapping_id_seq", allocationSize=1)
    @Column(name=ID)
    public Integer id;

    @Column(name = TYPE_CD)
    public String typeCd;

    @Column(name = PARENT_TYPE_VALUE)
    public Integer parentTypeValue;

    @Column(name = CHILD_TYPE_VALUE)
    public Integer childTypeValue;

    public static final String TABLE_NAME = "m_type_mapping";

    public static final String ID = "id";

    /** タイプコード */
    public static final String TYPE_CD = "type_cd";

    /** 親タイプ値 */
    public static final String PARENT_TYPE_VALUE = "parent_type_value";

    /** 子タイプ値 */
    public static final String CHILD_TYPE_VALUE = "child_type_value";
}
