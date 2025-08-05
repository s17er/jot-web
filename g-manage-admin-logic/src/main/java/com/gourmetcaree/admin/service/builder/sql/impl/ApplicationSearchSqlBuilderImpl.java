package com.gourmetcaree.admin.service.builder.sql.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.gourmetcaree.admin.service.builder.sql.ApplicationSearchSqlBuilder;
import com.gourmetcaree.common.util.SqlUtils;

public class ApplicationSearchSqlBuilderImpl implements ApplicationSearchSqlBuilder {

    private final StringBuilder sql;

    private final List<Object> params;

    private final Map<String, String> condition;

    public ApplicationSearchSqlBuilderImpl(StringBuilder sql, List<Object> params, Map<String, String> condition) {
        this.sql = sql;
        this.params = params;
        this.condition = condition;
    }

    @Override
    public SqlCondition build() {
        appendCompanySql();

        appendSalesId();

        //リニューアルで削除
//        appendFreeword();

        // 接ぎ木なので使う場面がないが一応・・・
        return new SqlCondition(sql, params);
    }

    /**
     * 会社ID追加
     */
    private void appendCompanySql() {
        final String companyId = condition.get(COMPANY_ID);
        if (!NumberUtils.isDigits(companyId)) {
            return;
        }

        sql.append(" AND WEB.company_id = ? ");
        params.add(Integer.parseInt(companyId));
    }

    /**
     * 営業ID追加
     */
    private void appendSalesId() {
        final String salesId = condition.get(SALES_ID);
        if (!NumberUtils.isDigits(salesId)) {
            return;
        }

        sql.append(" AND WEB.sales_id = ? ");
        params.add(Integer.parseInt(salesId));
    }

    private void appendFreeword() {
        final String hopeJob = condition.get(HOPE_JOB);
        if (StringUtils.isBlank(hopeJob)) {
            return;
        }
        sql.append(" AND ");
        SqlUtils.appendFreewordQuery(hopeJob,
                "APP.hope_job",
                sql,
                params);
    }
}
