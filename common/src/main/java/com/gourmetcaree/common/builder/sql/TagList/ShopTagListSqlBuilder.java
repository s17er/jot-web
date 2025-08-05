package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグリストを取得するSQLを作成する
 *
 * @author yamane
 *
 */
public class ShopTagListSqlBuilder implements SqlBuilder {

    /** ShopId */
    private final Integer shopId;

    public ShopTagListSqlBuilder(Integer shopId) {
        super();
        this.shopId = shopId;
    }

    @Override
    public SqlCondition build() {
        StringBuilder sql = new StringBuilder();


		sql.append(" SELECT ");
		sql.append("     MSLT.* ");
		sql.append(" FROM ");
		sql.append("     m_shop_list_tag_mapping AS MSLTM ");
		sql.append("     INNER JOIN m_shop_list_tag AS MSLT ON (MSLTM.shop_list_tag_id = MSLT.id) ");
		sql.append(" WHERE ");
		sql.append("     MSLTM.delete_flg = ? AND ");
		sql.append("     MSLT.delete_flg = ? AND ");
		sql.append("     MSLTM.shop_list_id = ? ");
		sql.append(" ORDER BY MSLTM.id ");

		return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED,
                DeleteFlgKbn.NOT_DELETED,
                shopId
                );
    }

}
