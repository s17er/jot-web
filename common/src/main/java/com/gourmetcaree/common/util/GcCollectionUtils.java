package com.gourmetcaree.common.util;

import com.google.common.collect.Lists;
import com.gourmetcaree.db.common.entity.MType;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * グルメ用コレクションユーティリティ
 * Created by ZRX on 2017/05/15.
 */
public class GcCollectionUtils {

    /** インスタンス生成不可 */
    private GcCollectionUtils(){}

    /**
     * コレクションを文字列のリストに変換
     * @return 文字列に変換されたリスト
     */
    public static List<String> toStringList(Collection<?> col) {
        if (CollectionUtils.isEmpty(col)) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<String>(col.size());
        for (Object obj : col) {
            list.add(String.valueOf(obj));
        }
        return list;
    }

    /**
     * コレクションをString配列に変換
     * @param col コレクション
     * @return String配列
     */
    public static String[] toStringArray(Collection<?> col) {
        return toStringList(col).toArray(new String[0]);
    }


    /**
     * コレクションをIntegerのリストに変換
     * @param col コレクション
     * @return Integerに変換されたリスト
     */
    public static List<Integer> toIntegerList(Collection<?> col) {
        List<Integer> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(col)) {
            return list;
        }

        for (Object obj : col) {
            list.add(Integer.parseInt(obj.toString()));
        }

        return list;
    }


    /**
     * 配列をリストに変換
     * XXX リスト→配列も作ろうと思ったけど、new E[0] ができないのでやめた。
     * @param array 配列
     * @param <E> ゲネリック
     * @return リスト
     */
    public static <E> List<E> toList(E... array) {
        if (array == null) {
            return new ArrayList<E>();
        }
        return Arrays.asList(array);
    }


    public static List<String> typeToStringValueList(Collection<MType> types) {
        return toStringList(typeToValueList(types));
    }
    public static List<Integer> typeToValueList(Collection<MType> types) {
        List<Integer> list = Lists.newArrayList();
        for (MType t : types) {
            list.add(t.typeValue);
        }
        return list;
    }

}
