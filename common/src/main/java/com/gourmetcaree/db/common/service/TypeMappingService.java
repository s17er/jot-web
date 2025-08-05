package com.gourmetcaree.db.common.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MTypeMapping;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import jp.co.whizz_tech.commons.WztStringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeMappingService extends AbstractGroumetCareeBasicService<MTypeMapping> {

    /**
     * タイプコードと子タイプ値から親タイプ値をDISTINCTでセレクト
     *
     * @param typeCd     タイプコード
     * @param typeValues 子タイプ値
     * @return 親タイプ値のDISTINCT
     */
    public List<Integer> findDistinctParentTypeValuesByTypeCdAndChildTypeValues(String typeCd,
                                                                                Collection<Integer> typeValues) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT parent_type_value ");
        sql.append(" FROM m_type_mapping ");
        sql.append(" WHERE type_cd = ? ");
        sql.append(String.format("       AND child_type_value IN (%s) ", SqlUtils.getQMarks(typeValues.size())));
        sql.append("       AND delete_flg = ? ");

        List<Object> params = Lists.newArrayList();
        params.add(typeCd);
        params.addAll(typeValues);
        params.add(DeleteFlgKbn.NOT_DELETED);

        return jdbcManager.selectBySql(Integer.class,
                sql.toString(),
                params.toArray())
                .getResultList();

    }


    /**
     * タイプコードをキーにエンティティをセレクト
     * @param typeCd タイプコード
     * @return 検索結果
     */
    public List<MTypeMapping> findByTypeCd(String typeCd) {
        SimpleWhere where = new SimpleWhere();
        where.eq(WztStringUtil.toCamelCase(MTypeMapping.TYPE_CD), typeCd);
        SqlUtils.addNotDeleted(where);

        return jdbcManager.from(entityClass)
                .where(where)
                .getResultList();

    }

    /**
     * タイプコードをキーに子タイプ値リストをセレクト
     * @param typeCd タイプコード
     * @return 子タイプ値リスト
     */
    public List<Integer> findChildTypeValuesByTypeCd(String typeCd) {
        List<Integer> list = Lists.newArrayList();
        for (MTypeMapping entity : findByTypeCd(typeCd)) {
            list.add(entity.childTypeValue);
        }
        return list;
    }


    /**
     * 親の値リストを取得
     * @param typeCd タイプコード
     * @return タイプコードの親値リスト
     */
    public List<Integer> findParentValuesByTypeCd(String typeCd) {
        Set<Integer> set = Sets.newLinkedHashSet();
        for (MTypeMapping entity : findByTypeCd(typeCd)) {
            set.add(entity.parentTypeValue);
        }
        return new ArrayList<Integer>(set);
    }

    /**
     * タイプコードと子の値からタイプマッピングを取得
     * @param typeCd タイプコード
     * @param childTypeValues 子の値コレクション
     * @return タイプマッピング
     */
    public List<MTypeMapping> findByTypeCdAndChildTypeValues(String typeCd, Collection<Integer> childTypeValues) {
        if (StringUtils.isBlank(typeCd)
                || CollectionUtils.isEmpty(childTypeValues)) {
            return Collections.emptyList();
        }

        SimpleWhere where = new SimpleWhere();
        where.eq(WztStringUtil.toCamelCase(MTypeMapping.TYPE_CD), typeCd);
        where.in(WztStringUtil.toCamelCase(MTypeMapping.CHILD_TYPE_VALUE), childTypeValues);
        SqlUtils.addNotDeleted(where);

        return jdbcManager.from(entityClass)
                    .where(where)
                    .getResultList();
    }
}

