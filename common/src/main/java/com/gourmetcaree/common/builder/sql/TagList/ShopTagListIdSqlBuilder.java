package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグリストを取得するSQLを作成する
 *
 * @author yamane
 *
 */
public class ShopTagListIdSqlBuilder implements SqlBuilder {

    @Override
    public SqlCondition build() {
        StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append("     shop_list_id ");
		sql.append(" FROM ");
		sql.append("     m_shop_list_tag_mapping ");
		sql.append(" WHERE ");
		sql.append("     delete_flg = ? ");
		sql.append(" GROUP BY shop_list_id ");
		sql.append(" ORDER BY shop_list_id ");

		return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED
                );
    }

}
