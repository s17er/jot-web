package com.gourmetcaree.common.builder.sql.web;

import java.sql.Timestamp;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ApplicationFormKbn;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 応募可能WEBを検索するSQLを作成するビルダ
 * 掲載開始日時が現在より前で、ステータスが3or5(掲載確定or掲載終了)のものを対象とする。
 * 
 * @author ZRX
 *
 */
public class AppliableWebSqlBuilder implements SqlBuilder {

    /** WEBデータID */
    private final int id;

    /** エリアコード */
    private final int areaCd;

    public AppliableWebSqlBuilder(int id, int areaCd) {
        super();
        this.id = id;
        this.areaCd = areaCd;
    }

    @Override
    public SqlCondition build() {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append("   WEB.* ");
        sql.append(" FROM ");
        sql.append("   t_web WEB ");
        sql.append(" WHERE ");
        sql.append("   WEB.id = ? ");
        sql.append("   AND WEB.area_cd = ? ");
        sql.append("   AND WEB.status IN (?, ?) ");
        sql.append("   AND WEB.application_form_kbn = ? ");
        sql.append("   AND WEB.delete_flg = ? ");
        sql.append("   AND EXISTS ( ");
        sql.append("     SELECT ");
        sql.append("       * ");
        sql.append("     FROM ");
        sql.append("       m_volume VOL ");
        sql.append("     WHERE ");
        sql.append("       VOL.id = WEB.volume_id ");
        sql.append("       AND VOL.post_start_datetime <= ? ");
        sql.append("       AND VOL.delete_flg = ? ");
        sql.append("   ) ");

        return new SqlCondition(sql,
                id,
                areaCd,
                MStatusConstants.DBStatusCd.POST_FIXED,
                MStatusConstants.DBStatusCd.POST_END,
                ApplicationFormKbn.EXIST,
                DeleteFlgKbn.NOT_DELETED,
                new Timestamp(System.currentTimeMillis()),
                DeleteFlgKbn.NOT_DELETED);
    }

}
