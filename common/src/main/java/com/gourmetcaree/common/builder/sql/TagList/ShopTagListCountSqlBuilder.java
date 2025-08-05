package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグリストを取得するSQLを作成する
 *
 * @author yamane
 *
 */
public class ShopTagListCountSqlBuilder implements SqlBuilder {

	private static final int LIMIT = 5;

    @Override
    public SqlCondition build() {
        StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append("     shop_list_tag_id, COUNT(shop_list_tag_id) AS COUNT_A ");
		sql.append(" FROM ");
		sql.append("     m_shop_list_tag_mapping ");
		sql.append(" WHERE ");
		sql.append("     delete_flg = ? ");
		sql.append(" GROUP BY shop_list_tag_id ");
		sql.append(" ORDER BY COUNT_A DESC ");
		sql.append(" LIMIT ? ");

		return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED,
                LIMIT
                );
    }

}
