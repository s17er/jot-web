package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグリストを取得するSQLを作成する
 *
 * @author yamane
 *
 */
public class WebTagListCountSqlBuilder implements SqlBuilder {

	private static final int LIMIT = 5;

	@Override
    public SqlCondition build() {

    	StringBuilder sql = new StringBuilder();


		sql.append(" SELECT ");
		sql.append("     web_tag_id, COUNT(web_tag_id) AS COUNT_A ");
		sql.append(" FROM ");
		sql.append("     m_web_tag_mapping ");
		sql.append(" WHERE ");
		sql.append("     delete_flg = ? ");
		sql.append(" GROUP BY web_tag_id ");
		sql.append(" ORDER BY COUNT_A DESC ");
		sql.append(" LIMIT ? ");

		return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED,
                LIMIT
                );
    }

}
