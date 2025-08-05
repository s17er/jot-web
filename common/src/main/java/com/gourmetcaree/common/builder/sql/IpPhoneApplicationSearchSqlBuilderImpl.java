package com.gourmetcaree.common.builder.sql;

import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService;
import org.apache.commons.lang.math.NumberUtils;

import java.util.List;

public class IpPhoneApplicationSearchSqlBuilderImpl implements SqlBuilder {

    private final StringBuilder sql;

    private final List<Object> params;

    private WebIpPhoneHistoryService.SearchProperty condition;

    public IpPhoneApplicationSearchSqlBuilderImpl(StringBuilder sql, List<Object> params, WebIpPhoneHistoryService.SearchProperty condition) {
        this.sql = sql;
        this.params = params;
        this.condition = condition;
    }

    @Override
    public SqlBuilder.SqlCondition build() {
        appendCompanySql();

        appendSalesId();

        // 接ぎ木なので使う場面がないが一応・・・
        return new SqlBuilder.SqlCondition(sql, params);
    }

    /**
     * 会社ID追加
     */
    private void appendCompanySql() {
        if (!NumberUtils.isDigits(condition.where_companyId)) {
            return;
        }

        sql.append(" AND WEB.company_id = ? ");
        params.add(Integer.parseInt(condition.where_companyId));
    }

    /**
     * 営業ID追加
     */
    private void appendSalesId() {
        if (!NumberUtils.isDigits(condition.where_salesId)) {
            return;
        }

        sql.append(" AND WEB.sales_id = ? ");
        params.add(Integer.parseInt(condition.where_salesId));
    }

}
