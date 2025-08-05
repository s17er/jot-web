package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグリストを取得するSQLを作成する
 *
 * @author yamane
 *
 */
public class WebTagListIdSqlBuilder implements SqlBuilder {

    @Override
    public SqlCondition build() {

    	StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append("     web_id ");
		sql.append(" FROM ");
		sql.append("     m_web_tag_mapping ");
		sql.append(" WHERE ");
		sql.append("     delete_flg = ? ");
		sql.append(" GROUP BY web_id ");
		sql.append(" ORDER BY web_id ");

		return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED
                );
    }

}
