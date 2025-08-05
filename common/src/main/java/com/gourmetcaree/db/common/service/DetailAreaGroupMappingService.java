package com.gourmetcaree.db.common.service;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MDetailAreaGroupMapping;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import jp.co.whizz_tech.commons.WztStringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import java.util.Collection;
import java.util.List;

/**
 * 詳細エリアグループマッピングサービス
 */
public class DetailAreaGroupMappingService extends AbstractGroumetCareeBasicService<MDetailAreaGroupMapping> {

    /**
     * DISTINCTな詳細エリアグループを詳細エリア区分とエリアコードから検索
     * @param detailAreaKbns 詳細エリア区分
     * @param areaCd エリアコード
     * @return 詳細エリアグループ
     */
    public List<Integer> findDistinctDetailAreaKbnGroupByDetailAreaKbnsAndAreaCd(Collection<Integer> detailAreaKbns, int areaCd) {
        if (CollectionUtils.isEmpty(detailAreaKbns)) {
            return Lists.newArrayList();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT detail_area_kbn_group FROM m_detail_area_group_mapping ");
        sql.append(" WHERE ");
        sql.append(String.format(" detail_area_kbn IN (%s) ", SqlUtils.getQMarks(detailAreaKbns.size())));
        sql.append(" AND area_cd = ? ");
        sql.append(" AND delete_flg = ? ");

        List<Object> params = Lists.newArrayList();
        params.addAll(detailAreaKbns);
        params.add(areaCd);
        params.add(DeleteFlgKbn.NOT_DELETED);

        return jdbcManager.selectBySql(Integer.class, sql.toString(), params.toArray())
                .getResultList();
    }
}
