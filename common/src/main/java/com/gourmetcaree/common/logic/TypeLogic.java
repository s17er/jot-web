package com.gourmetcaree.common.logic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gourmetcaree.common.logic.dto.PrefectureTypeResult;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.TypeMappingService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.valueobject.TypePrefectureInfo;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * タイプロジック
 */
public class TypeLogic extends AbstractGourmetCareeLogic {

    @Resource
    private TypeService typeService;

    @Resource
    private TypeMappingService typeMappingService;

    @Resource
    private WebAttributeService webAttributeService;

    /**
     * エリアコードをキーにwebエリア区分をセレクト
     * @param areaCd エリアコード
     * @return 検索結果
     */
    public List<MType> selectWebAreaKbnsByAreaCdAndNotInTypeValues(int areaCd, Collection<Integer> noDisplayValues) {


        final String typeCd = MTypeConstants.WebAreaKbn.getTypeCd(areaCd);

        // 仙台の場合は、タイプマッピングに登録されている子タイプ値を非表示対象として検索
        if (MAreaConstants.AreaCd.isSendai(areaCd)) {
            List<Integer> noDisplayTypes = typeMappingService.findChildTypeValuesByTypeCd(typeCd);
            if (CollectionUtils.isNotEmpty(noDisplayValues)) {
                noDisplayTypes.addAll(noDisplayValues);
            }
            return typeService.findByTypeCdAndNotInTypeValues(typeCd, noDisplayTypes);
        }

        try {
            return typeService.getMTypeList(typeCd);
        } catch (WNoResultException e) {
            return Lists.newArrayList();
        }
    }



    /**
     * ウェブエリアを都道府県ごとにまとめたリストを生成
     * @param areaCd エリアコード
     * @return webエリアの都道府県まとめリスト
     */
    public List<TypePrefectureInfo> selectWebAreaKbnPrefectures(int areaCd) {
        final String typeCd = MTypeConstants.WebAreaKbn.getTypeCd(areaCd);

        Map<Integer, List<PrefectureTypeResult>> map = createResultListMap(typeCd);

        List<TypePrefectureInfo> list = convertToWebAreaKbnPrefectureInfoList(map, typeCd);

        MTypeConstants.WebAreaKbn.getOtherAreaList(areaCd);
        list.add(new TypePrefectureInfo(-1,
                "",
                typeService.findByTypeCdAndTypeValues(typeCd, MTypeConstants.WebAreaKbn.getOtherAreaList(areaCd))));
        return list;
    }

    /**
     * 詳細エリアを都道府県ごとにまとめたリストを生成
     * @param areaCd エリアコード
     * @return 詳細エリアの都道府県まとめリスト
     */
    public List<TypePrefectureInfo> selectDetailAreaKbnPrefectures(int areaCd) {
        final String typeCd = MTypeConstants.DetailAreaKbn.getTypeCd(areaCd);

        Map<Integer, List<PrefectureTypeResult>> map = createResultListMap(typeCd);

        List<TypePrefectureInfo> list = convertToWebAreaKbnPrefectureInfoList(map, typeCd);

        MTypeConstants.WebAreaKbn.getOtherAreaList(areaCd);
        list.add(new TypePrefectureInfo(-1,
                "",
                typeService.findByTypeCdAndTypeValues(typeCd, MTypeConstants.DetailAreaKbn.getOtherAreaList(areaCd))));
        return list;
    }

    /**
     * 詳細エリアグループを都道府県ごとにまとめたリストを生成
     * @param areaCd エリアコード
     * @return 詳細エリアグループの都道府県まとめリスト
     */
    public List<TypePrefectureInfo> selectDetailAreaKbnGroupPrefectures(int areaCd) {
        final String typeCd = MTypeConstants.DetailAreaKbnGroup.getTypeCd(areaCd);

        Map<Integer, List<PrefectureTypeResult>> map = createResultListMap(typeCd);

        List<TypePrefectureInfo> list = convertToWebAreaKbnPrefectureInfoList(map, typeCd);

        MTypeConstants.WebAreaKbn.getOtherAreaList(areaCd);
        list.add(new TypePrefectureInfo(-1,
                "",
                typeService.findByTypeCdAndTypeValues(typeCd, MTypeConstants.DetailAreaKbnGroup.getOtherAreaList(areaCd))));
        return list;
    }



    /**
     * 都道府県マップをWebAreaKbnPrefectureInfoのリストにまとめる
     * @param map 都道府県マップ
     * @param typeCd タイプコード
     * @return WebAreaKbnPrefectureInfoのリスト
     */
    private List<TypePrefectureInfo> convertToWebAreaKbnPrefectureInfoList(Map<Integer, List<PrefectureTypeResult>> map, String typeCd) {
        List<TypePrefectureInfo> list = Lists.newArrayList();
        for (Map.Entry<Integer, List<PrefectureTypeResult>> entry : map.entrySet()) {
            final Integer prefCd = entry.getKey();
            String prefName = null;
            List<Integer> typeList = Lists.newArrayList();

            for (PrefectureTypeResult result : entry.getValue()) {
                if (prefName == null) {
                    prefName = result.getPrefecturesName();
                }
                typeList.add(result.getTypeValue());
            }

            List<MType> hierarchyList = selectHierarchyType(typeCd, typeList);
            list.add(new TypePrefectureInfo(prefCd, prefName, hierarchyList));
        }
        return list;
    }

    /**
     * 都道府県ごとにタイプをまとめたmapを作成
     * @param typeCd タイプコード
     * @return 都道府県ごとにタイプをまとめたmap
     */
    private Map<Integer, List<PrefectureTypeResult>> createResultListMap(String typeCd) {
        List<PrefectureTypeResult> prefTypeList = selectJoinedDataByTypeCd(typeCd);

        // 都道府県ごとにタイプをまとめる
        Map<Integer, List<PrefectureTypeResult>> typeValueMap = Maps.newLinkedHashMap();
        for (PrefectureTypeResult result : prefTypeList) {
            List<PrefectureTypeResult> resultList = typeValueMap.get(result.getPrefecturesCd());
            if (resultList == null) {
                resultList = Lists.newArrayList();
                typeValueMap.put(result.getPrefecturesCd(), resultList);
            }
            resultList.add(result);
        }
        return typeValueMap;
    }

    /**
     * MType内での階層リストを生成
     * @param typeCd タイプコード
     * @param parentTypeValues 親タイプ値リスト
     * @return 親子階層になったタイプリスト
     */
    private List<MType> selectHierarchyType(String typeCd, Collection<Integer> parentTypeValues) {
        List<MType> typeList = typeService.findByTypeCdAndTypeValues(typeCd, parentTypeValues);

        for (MType type : typeList) {
            type.childrenTypeList = selectChildrenType(type.typeCd, type.typeValue);
        }

        return typeList;

    }

    /**
     * 子のタイプリストをセレクト
     * @param typeCd タイプコード
     * @param parentTypeValue 親のタイプ値
     * @return 子のタイプコードリスト
     */
    private List<MType> selectChildrenType(String typeCd, int parentTypeValue) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT TYPE.* ");
        sql.append(" FROM m_type_mapping MAP ");
        sql.append("   INNER JOIN m_type TYPE ON MAP.type_cd = TYPE.type_cd AND MAP.child_type_value = TYPE.type_value ");
        sql.append(" WHERE MAP.type_cd = ? ");
        sql.append("       AND MAP.parent_type_value = ? ");
        sql.append("       AND MAP.delete_flg = ? ");
        sql.append("       AND TYPE.delete_flg = ? ");
        sql.append(" ORDER BY TYPE.display_order ");

        return jdbcManager.selectBySql(MType.class,
                sql.toString(),
                typeCd,
                parentTypeValue,
                DeleteFlgKbn.NOT_DELETED,
                DeleteFlgKbn.NOT_DELETED
                )
                .getResultList();

    }

    /**
     * 都道府県と都道府県タイプをJOINした結果をセレクト
     * @param typeCd タイプコード
     * @return 都道府県と都道府県タイプをJOINした結果
     */
    private List<PrefectureTypeResult> selectJoinedDataByTypeCd(String typeCd) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT MPT.prefectures_cd, ");
        sql.append("   MPT.type_cd, ");
        sql.append("   MPT.type_value, ");
        sql.append("   PREF.prefectures_name ");
        sql.append(" FROM m_prefectures_type MPT ");
        sql.append("   INNER JOIN m_prefectures PREF ");
        sql.append("     ON MPT.prefectures_cd = PREF.prefectures_cd ");
        sql.append("   WHERE MPT.type_cd = ? ");
        sql.append("   AND MPT.delete_flg = ? ");
        sql.append("   AND PREF.delete_flg = ? ");
        sql.append(" ORDER BY MPT.display_order ");

        return jdbcManager.selectBySql(PrefectureTypeResult.class,
                sql.toString(),
                typeCd, DeleteFlgKbn.NOT_DELETED, DeleteFlgKbn.NOT_DELETED)
                .getResultList();
    }


    /**
     * 詳細エリアリストをセレクト
     * 首都圏と仙台で公開側の表記が違うため、切り分けて検索する。
     * 首都圏 : 詳細エリア
     * 仙台  : 詳細エリアグループ
     * @param webId webID
     * @param areaCd エリアコード
     * @return エリアに対応した詳細エリアリスト
     */
    public List<MType> selectDetailAreaList(Integer webId, Integer areaCd) {
        if (webId == null || areaCd == null) {
            return Collections.emptyList();
        }

        final String typeCd;
        if (MAreaConstants.AreaCd.isSendai(areaCd)) {
            typeCd = MTypeConstants.DetailAreaKbnGroup.getTypeCd(areaCd);
        } else {
            typeCd = MTypeConstants.DetailAreaKbn.getTypeCd(areaCd);
        }

        List<Integer> integerList = webAttributeService.getWebAttributeValueIntegerList(webId,typeCd);
        return typeService.findByTypeCdAndTypeValues(typeCd, integerList);

    }

}
