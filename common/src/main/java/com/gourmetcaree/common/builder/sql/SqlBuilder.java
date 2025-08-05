package com.gourmetcaree.common.builder.sql;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SQL文を生成するためのインタフェース
 * 
 * @author nakamori
 *
 */
public interface SqlBuilder {

    /**
     * SQLを生成
     * 
     * @return SQLとパラメータを保持する {@link SqlCondition}
     */
    SqlCondition build();

    /**
     * SQLクエリとパラメータを保持するクラス
     * 
     * @author nakamori
     *
     */
    public static class SqlCondition implements Serializable {

        private static final long serialVersionUID = 3159063491750625680L;

        /** パラメータ */
        private final List<Object> params;
        /** SQL */
        private final CharSequence sql;

        public SqlCondition(CharSequence sql, List<Object> params) {
            this.params = params;
            this.sql = sql;
        }

        public SqlCondition(CharSequence sql, Object... params) {
            this(sql, Arrays.asList(params));
        }

        /**
         * パラメータの取得
         */
        public List<Object> getParams() {
            return params;
        }

        /**
         * パラメータを配列で取得
         */
        public Object[] getParamsArray() {
            return params.toArray();
        }

        /**
         * SQLの取得
         */
        public String getSql() {
            return sql.toString();
        }

        public static SqlCondition empty() {
            return new SqlCondition("", Collections.emptyList());
        }
    }

}
