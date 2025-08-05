package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.entity.MWebTag;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグを名前から取得するSQLを作成する.
 * <P>名前は完全一致</P>
 *
 * @author yamane
 *
 */
public class WebTagFindByNameSqlBuilder implements SqlBuilder {

    /** name */
    private final String name;

    public WebTagFindByNameSqlBuilder(String name) {
        super();
        this.name = name;
    }

    @Override
    public SqlCondition build() {

    	StringBuilder sql = new StringBuilder();

    	sql.append(" SELECT ")
    		.append(" * ")
    		.append(" FROM ")
    		.append(MWebTag.TABLE_NAME)
    		.append(" WHERE ")
    		.append(MWebTag.WEB_TAG_NAME).append(" = ? ")
    		.append(" AND ").append(MWebTag.DELETE_FLG).append(" = ? ")
    		;

		return new SqlCondition(sql,
                name,
                DeleteFlgKbn.NOT_DELETED
                );
    }

}
