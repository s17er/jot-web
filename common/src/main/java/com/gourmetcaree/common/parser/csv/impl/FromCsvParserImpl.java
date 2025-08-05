package com.gourmetcaree.common.parser.csv.impl;

import com.gourmetcaree.common.parser.csv.FromCsvParser;
import org.apache.commons.csv.CSVRecord;
import org.seasar.s2csv.csv.annotation.column.CSVColumn;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FromCsvParserImpl<E> extends BaseCsvParserImpl implements FromCsvParser<E> {

    private final Class<E> clazz;
    private final List<ColumnData> columnList;
    private final Collection<CSVRecord> records;

    private FromCsvParserImpl(Collection<CSVRecord> records, Class<E> clazz, List<ColumnData> columnList) {
        this.records = records;
        this.clazz = clazz;
        this.columnList = columnList;
    }

    @Override
    public void parse(ReadIterator<E> iterator) {
        records.forEach(r -> iterator.iterate(createData(r), r));
    }


    /**
     * データの生成
     * @param record CSVの行
     * @return データ
     */
    private E createData(CSVRecord record) {
        final E instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("クラス[%s]のインスタンス生成に失敗しました。", clazz.getName()), e);
        }

        for (ColumnData column : columnList) {
            final String value = Optional.ofNullable(record.get(column.getIndex()))
                    .map(String::valueOf)
                    .orElse(null);

            try {
                final Field f = column.getField();
                f.setAccessible(true);
                f.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        String.format("データのセットに失敗しました。 フィールド名:[%s]",
                                column.getField().getName()),
                        e);
            }
        }

        return instance;
    }

    public static <E> FromCsvParser<E> getInstance(Collection<CSVRecord> records, Class<E> clazz) {
        List<ColumnData> list = Stream.of(clazz.getFields())
                .filter(f -> f.isAnnotationPresent(CSVColumn.class))
                .map(f -> ColumnData.getEmptyValueInstance(f, f.getAnnotation(CSVColumn.class)))
                .collect(Collectors.toList());

        return new FromCsvParserImpl<>(records, clazz, list);
    }
}
