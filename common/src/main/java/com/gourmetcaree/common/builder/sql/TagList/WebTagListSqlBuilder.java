package com.gourmetcaree.common.builder.sql.TagList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * タグリストを取得するSQLを作成する
 *
 * @author yamane
 *
 */
public class WebTagListSqlBuilder implements SqlBuilder {

    /** WebId */
    private final Integer webId;

    public WebTagListSqlBuilder(Integer webId) {
        super();
        this.webId = webId;
    }

    @Override
    public SqlCondition build() {

    	StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append("     MWT.* ");
		sql.append(" FROM ");
		sql.append("     m_web_tag_mapping AS MWTM");
		sql.append("     INNER JOIN m_web_tag AS MWT ON (MWTM.web_tag_id = MWT.id) ");
		sql.append(" WHERE ");
		sql.append("     MWTM.delete_flg = ? AND ");
		sql.append("     MWT.delete_flg = ? AND ");
		sql.append("     web_id = ? ");
		sql.append(" ORDER BY id ");

		return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED,
                DeleteFlgKbn.NOT_DELETED,
                webId
                );
    }

}
