package com.gourmetcaree.common.parser.csv.impl;

import lombok.Data;
import org.seasar.s2csv.csv.annotation.column.CSVColumn;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * CSVパーサの基底クラス
 */
class BaseCsvParserImpl {

    @Data
    static class ColumnData {

        /** カラムINDEX */
        private final int index;
        /** カラムの値 */
        private final String value;
        /** ヘッダ名 */
        private final String headerName;
        /** フィールド */
        private final Field field;

        /**
         * 値の取得
         * @param obj インスタンスオブジェクト
         * @param field フィールド
         * @param annotation カラムアノテーション
         * @return カラムデータ
         */
        static ColumnData getInstance(Object obj, Field field, CSVColumn annotation) {
            try {
                field.setAccessible(true);
                Object data = field.get(obj);
                return new ColumnData(annotation.columnIndex(),
                        Optional.ofNullable(data)
                                .map(String::valueOf)
                                .orElse(""),
                        annotation.columnName(),
                        field);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("フィールドの情報取得中にエラーが発生しました。", e);
            }
        }

        /**
         * 値が空のカラムデータを生成
         * @param field フィールド
         * @param annotation アノテーション
         * @return データ
         */
        static ColumnData getEmptyValueInstance(Field field, CSVColumn annotation) {
            return new ColumnData(annotation.columnIndex(), null, annotation.columnName(), field);
        }
    }
}
