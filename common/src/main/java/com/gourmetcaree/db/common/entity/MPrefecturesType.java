package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "m_prefectures_type")
@Entity
public class MPrefecturesType extends AbstractCommonEntity {

    private static final long serialVersionUID = -4799797331105426050L;

    @Id
    @Column(name = ID)
    public Integer id;

    @Column(name = PREFECTURES_CD)
    public Integer prefecturesCd;

    @Column(name = TYPE_CD)
    public String typeCd;

    @Column(name = TYPE_VALUE)
    public Integer typeValue;

    @Column(name = DISPLAY_ORDER)
    public Integer displayOrder;

    public static final String TABLE_NAME = "m_prefectures_type";

    public static final String ID = "id";

    public static final String PREFECTURES_CD = "prefectures_cd";

    public static final String TYPE_CD = "type_cd";

    public static final String TYPE_VALUE = "type_value";

    public static final String DISPLAY_ORDER = "display_order";
}

