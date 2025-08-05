package com.gourmetcaree.arbeitsys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * グルメdeバイト用mst_stationエンティティです。
 *
 * @author Takehiro Nakamori
 */
@Entity
@Table(name = "mst_station_g002")
public class MstStationSendai extends AbstractMstStation {

    /**
     *
     */
    private static final long serialVersionUID = -1844930606528246161L;

    /**
     * 表示順
     * 北日本だけ表示順がある。
     */
    @Column(name = DISP_NO)
    public Integer dispNo;

    @Override
    public Integer getDispNo() {
        return dispNo;
    }
}
