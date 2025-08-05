package com.gourmetcaree.common.logic;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gourmetcaree.db.common.entity.MTypeMapping;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.service.TypeMappingService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * typeMapping用ロジック
 */
public class TypeMappingLogic {

    @Resource(name = "typeMappingService")
    private TypeMappingService service;

    public List<Integer> getTypeValues(String typeCd, Collection<Integer> values) {
        List<Integer> parentValues =
                service.findDistinctParentTypeValuesByTypeCdAndChildTypeValues(typeCd, values);

        parentValues.addAll(values);
        return parentValues;
    }

    /**
     * 引数の値コレクションから、マスタに登録されている子の値をremove
     * @param typeCd タイプコード
     * @param values 値コレクション
     */
    public void removeChildValues(String typeCd, Collection<Integer> values) {
        List<MTypeMapping> entityList = service.findByTypeCd(typeCd);
        for (MTypeMapping entity : entityList) {
            values.remove(entity.childTypeValue);
        }
    }

    /**
     * 子の値を親の値に集約
     * @param typeCd タイプコード
     * @param values タイプ値
     * @return この値を親の値に集約し直したリスト
     */
    public List<Integer> reduceChildrenToParent(String typeCd, Collection<Integer> values) {
        List<MTypeMapping> mappingList = service.findByTypeCdAndChildTypeValues(typeCd, values);

        Set<Integer> parentSet = Sets.newHashSet();
        Set<Integer> valueSet = Sets.newLinkedHashSet(values);
        for (MTypeMapping entity : mappingList) {
            parentSet.add(entity.parentTypeValue);
            valueSet.remove(entity.childTypeValue);
        }

        valueSet.addAll(parentSet);
        return new ArrayList<Integer>(valueSet);
    }

}
