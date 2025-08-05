package com.gourmetcaree.common.builder.sql;

/**
 * 既にSQL生成済みのものに対してSQLや条件を追加するSQLビルダ
 * ※ 普通にやってればこんなクラス作らない
 */
public interface SqlAppender {

    /**
     * SQLデータの追加
     */
    void append();
}
