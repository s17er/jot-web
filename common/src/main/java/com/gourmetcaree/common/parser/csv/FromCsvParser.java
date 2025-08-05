package com.gourmetcaree.common.parser.csv;

import com.gourmetcaree.common.parser.csv.impl.FromCsvParserImpl;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

/**
 * CSVからデータへパース
 * @param <E>
 */
public interface FromCsvParser<E> {

    void parse(ReadIterator<E> iterator);


    /**
     * インスタンスの取得
     * @param clazz データクラス
     * @param records CSVレコードリスト
     * @param <E> CSVから生成するデータの型
     * @return インスタンス
     */
    static <E> FromCsvParser<E> newInstance(Class<E> clazz, List<CSVRecord> records) {
        return FromCsvParserImpl.getInstance(records, clazz);
    }
    /**
     * 読み込み時のイテレータ
     * @param <E>
     */
    interface ReadIterator<E> {
        /**
         * 1行ごとの処理
         * @param data  データ
         * @param record CSV行
         */
        void iterate(E data, CSVRecord record);
    }


}
