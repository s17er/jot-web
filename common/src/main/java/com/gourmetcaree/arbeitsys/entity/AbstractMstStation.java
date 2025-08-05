package com.gourmetcaree.arbeitsys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.gourmetcaree.arbeitsys.constants.MArbeitConstants.ArbeitSite;

/**
 * グルメdeバイト用mst_stationエンティティです。
 *
 * @author Takehiro Nakamori
 */
@MappedSuperclass
public class AbstractMstStation implements Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 7430173916536643162L;

    /**
     * ID
     */
    @Id
    @Column(name = "id")
    public Integer id;

    /**
     * 路線ID
     */
    @Column(name = "route_id")
    public Integer routeId;


    /**
     * 駅名
     */
    @Column(name = "name")
    public String name;


    /**
     * ID
     */
    public static final String ID = "id";

    /**
     * 路線ID
     */
    public static final String ROUTE_ID = "route_id";

    /**
     * 駅名
     */
    public static final String NAME = "name";

    /**
     * 表示順
     * 北日本だけ表示順がある。
     */
    public static final String DISP_NO = "disp_no";


    /**
     * 駅エンティティクラスを取得
     *
     * @param todouhukenId 都道府県ID
     */
    public static Class<? extends AbstractMstStation> getRailloadClass(int todouhukenId) {
        switch (ArbeitSite.getArbeitSiteEnum(todouhukenId)) {

            case KANSAI_SITE_CONST:
                return MstStationKansai.class;

            case SENDAI_SITE_CONST:
                return MstStationSendai.class;

            case SHUTOKEN_SITE_CONST:
            default:
                return MstStationShutoken.class;
        }
    }

    /**
     * 駅テーブルを取得
     *
     * @param todouhukenId 都道府県ID
     */
    public static String getRailloadTable(int todouhukenId) {
        switch (ArbeitSite.getArbeitSiteEnum(todouhukenId)) {

            case KANSAI_SITE_CONST:
                return MstStationKansai.class.getAnnotation(Table.class).name();

            case SENDAI_SITE_CONST:
                return MstStationSendai.class.getAnnotation(Table.class).name();

            case SHUTOKEN_SITE_CONST:
            default:
                return MstStationShutoken.class.getAnnotation(Table.class).name();
        }
    }

    /**
     * 北日本だけ表示順があるので、getterでアクセスできるようにする。
     *
     * @return 表示順(北日本だけ値がある.それ以外は常に0)
     */
    public Integer getDispNo() {
        return 0;
    }
}
