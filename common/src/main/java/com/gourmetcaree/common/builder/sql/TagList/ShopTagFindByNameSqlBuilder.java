package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグを名前から取得するSQLを作成する.
 * <P>名前は完全一致</P>
 *
 * @author yamane
 *
 */
public class ShopTagFindByNameSqlBuilder implements SqlBuilder {

    /** name */
    private final String name;

    public ShopTagFindByNameSqlBuilder(String name) {
        super();
        this.name = name;
    }

    @Override
    public SqlCondition build() {

    	StringBuilder sql = new StringBuilder();

    	sql.append(" SELECT ")
    		.append(" * ")
    		.append(" FROM ")
    		.append(MShopListTag.TABLE_NAME)
    		.append(" WHERE ")
    		.append(MShopListTag.SHOP_LIST_TAG_NAME).append(" = ? ")
    		.append(" AND ").append(MShopListTag.DELETE_FLG).append(" = ? ")
    		;

		return new SqlCondition(sql,
                name,
                DeleteFlgKbn.NOT_DELETED
                );
    }

}
