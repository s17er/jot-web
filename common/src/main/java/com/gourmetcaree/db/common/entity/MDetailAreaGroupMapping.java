package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 詳細エリアグループマッピング
 */
@Entity
@Table(name = MDetailAreaGroupMapping.TABLE_NAME)
public class MDetailAreaGroupMapping extends AbstractCommonEntity {
    private static final long serialVersionUID = 6637900623325422290L;

    /** ID */
    @Id
    @Column(name = ID)
    public Integer id;

    /** エリアコード */
    @Column(name = AREA_CD)
    public Integer areaCd;

    /** 詳細エリア区分グループ */
    @Column(name = DETAIL_AREA_KBN_GROUP)
    public Integer detailAreaKbnGroup;

    /** 詳細エリア区分 */
    @Column(name = DETAIL_AREA_KBN)
    public Integer detailAreaKbn;

    public static final String TABLE_NAME = "m_detail_area_group_mapping";

    /** ID */
    public static final String ID = "id";

    /** エリアコード */
    public static final String AREA_CD = "area_cd";

    /** 詳細エリア区分グループ */
    public static final String DETAIL_AREA_KBN_GROUP = "detail_area_kbn_group";

    /** 詳細エリア区分 */
    public static final String DETAIL_AREA_KBN = "detail_area_kbn";

}
