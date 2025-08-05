package com.gourmetcaree.common.parser.csv;

import java.util.List;

import com.gourmetcaree.common.parser.csv.impl.ToCsvParserImpl;

/**
 * データをCSVにパースします
 */
public interface ToCsvParser<E> {

    /**
     * パース処理を行います
     * @param iterator 1行ずつのパース処理
     */
    void parse(CsvIterator<E> iterator);


    @SuppressWarnings({ "rawtypes", "unchecked" })
	static <E> ToCsvParser<E> newInstance(List<E> dataList) {
        return new ToCsvParserImpl(dataList);
    }
    /**
     * パース時のイテレート処理
     */
    @FunctionalInterface
    interface CsvIterator<E> {

        /**
         * 1行ずつの処理を行う
         * @param index 行番号
         * @param data データオブジェクト
         * @param row 行データ
         */
        void iterate(int index, E data, List<String> row);
    }

}
