package com.gourmetcaree.common.parser.csv.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.seasar.s2csv.csv.annotation.column.CSVColumn;

import com.gourmetcaree.common.parser.csv.ToCsvParser;

/**
 * ToCsvParserの実装クラス
 */
public class ToCsvParserImpl<E> extends BaseCsvParserImpl implements ToCsvParser<E> {

    private final List<E> dataList;

    public ToCsvParserImpl(List<E> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void parse(CsvIterator<E> iterator) {
        int index = 0;
        for (E data : dataList) {
            List<ColumnData> columnList = parseData(data);
            if (index++ == 0) {
                // ヘッダ行。既にインクリメンタルされているので-1している。
                iterator.iterate(index - 1, data, columnList.stream()
                        .map(ColumnData::getHeaderName)
                        .collect(Collectors.toList()));
            }

            iterator.iterate(index, data, columnList.stream()
            .map(ColumnData::getValue)
            .collect(Collectors.toList()));
        }
    }



    private List<ColumnData> parseData(Object data) {
        return Stream.of(data.getClass().getFields())
                .filter(f -> f.isAnnotationPresent(CSVColumn.class))
                .map(f -> ColumnData.getInstance(data, f, f.getAnnotation(CSVColumn.class)))
                .sorted(Comparator.comparingInt(ColumnData::getIndex))
                .collect(Collectors.toList());

    }





}
