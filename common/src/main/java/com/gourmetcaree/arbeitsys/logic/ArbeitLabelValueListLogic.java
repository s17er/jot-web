package com.gourmetcaree.arbeitsys.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.arbeitsys.service.ArbeitTodouhukenService;
import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.arbeitsys.entity.AbstractMstRailload;
import com.gourmetcaree.arbeitsys.entity.AbstractMstRoute;
import com.gourmetcaree.arbeitsys.entity.AbstractMstStation;
import com.gourmetcaree.arbeitsys.entity.MstArea;
import com.gourmetcaree.arbeitsys.entity.MstGyotai;
import com.gourmetcaree.arbeitsys.entity.MstSubArea;
import com.gourmetcaree.arbeitsys.entity.MstTodouhuken;
import com.gourmetcaree.common.dto.LabelValueDto;

import javax.annotation.Resource;

/**
 * グルメdeバイト用ラベルバリューリストロジック
 *
 * @author Takehiro Nakamori
 */
public class ArbeitLabelValueListLogic extends AbstractArbeitLogic {

    @Resource(name = "arbeitTodouhukenService")
    private ArbeitTodouhukenService prefService;



    public List<LabelValueDto> getTodouhukenList(String blankLineLabel, final String suffix) {
        return getTodouhukenList(blankLineLabel, suffix, null);
    }
    /**
     * 都道府県リストを取得します。
     *
     * @return 都道府県リスト
     */
    public List<LabelValueDto> getTodouhukenList(String blankLineLabel, final String suffix, Collection<?> ids) {
        final List<MstTodouhuken> prefList;
        if (CollectionUtils.isEmpty(ids)) {
            prefList = prefService.findAll();
        } else {
            List<Integer> idList = Lists.newArrayList();
            for (Object value : ids) {
                idList.add(Integer.parseInt(String.valueOf(value)));
            }
            prefList = prefService.findByIds(idList);
        }

        List<LabelValueDto> dtoList = LabelValueDto.newInstanceListFromMstTodoufukenList(prefList, suffix);
        addBrankLineLabel(dtoList, blankLineLabel);
        return dtoList;
    }


    /**
     * 市区町村リストを取得します。
     *
     * @param todoufukenId 都道府県ID
     * @return 市区町村リスト
     */
    public List<LabelValueDto> getAreaList(String todoufukenId, String blankLineLabel, final String suffix) {
        if (StringUtils.isBlank(todoufukenId)) {
            return createBlankLabelValueList(blankLineLabel);
        }

        List<LabelValueDto> dtoList =
                jdbcManager.from(MstArea.class)
                        .where(new SimpleWhere()
                                .eq(WztStringUtil.toCamelCase(MstArea.TODOUHUKEN_ID), NumberUtils.toInt(todoufukenId)))
                        .orderBy(MstArea.ID)
                        .iterate(new IterationCallback<MstArea, List<LabelValueDto>>() {
                            private List<LabelValueDto> list = new ArrayList<LabelValueDto>();

                            @Override
                            public List<LabelValueDto> iterate(MstArea entity, IterationContext context) {
                                if (entity != null) {
                                    LabelValueDto dto = new LabelValueDto();
                                    dto.setLabel(entity.name + suffix);
                                    dto.setValue(String.valueOf(entity.id));
                                    list.add(dto);
                                }
                                return list;
                            }
                        });

        if (dtoList == null) {
            dtoList = new ArrayList<LabelValueDto>();
        }

        addBrankLineLabel(dtoList, blankLineLabel);

        return dtoList;
    }


    /**
     * サブエリアの取得
     *
     * @param todouhukenId   都道府県ID
     * @param areaId         エリアID
     * @param blankLineLabel 空ラベル
     * @param suffix         サフィックス
     */
    public List<LabelValueDto> getSubAreaList(String todouhukenId, String areaId, String blankLineLabel, String suffix) {
        if (StringUtils.isBlank(todouhukenId)
                || StringUtils.isBlank(areaId)) {
            return createBlankLabelValueList(blankLineLabel);
        }

        SimpleWhere where = new SimpleWhere();
        where.eq(WztStringUtil.toCamelCase(MstSubArea.TODOUHUKEN_ID), NumberUtils.toInt(todouhukenId));
        where.eq(WztStringUtil.toCamelCase(MstSubArea.AREA_ID), NumberUtils.toInt(areaId));

        final String suffixVal = StringUtils.defaultString(suffix);
        List<LabelValueDto> dtoList =
                jdbcManager.from(MstSubArea.class)
                        .where(where)
                        .orderBy(MstSubArea.ID)
                        .iterate(new IterationCallback<MstSubArea, List<LabelValueDto>>() {
                            private List<LabelValueDto> retList = new ArrayList<LabelValueDto>();

                            @Override
                            public List<LabelValueDto> iterate(MstSubArea entity, IterationContext context) {
                                if (entity == null) {
                                    return retList;
                                }

                                retList.add(new LabelValueDto(entity.name.concat(suffixVal), entity.id));


                                return retList;
                            }

                        });

        if (CollectionUtils.isEmpty(dtoList)) {
            return createBlankLabelValueList(blankLineLabel);
        }


        addBrankLineLabel(dtoList, blankLineLabel);

        return dtoList;
    }

    /**
     * 鉄道会社リストを取得します。
     */
    public List<LabelValueDto> getRailloadList(String blankLineLabel, final String suffix, int todouhukenId) {

// 仕様変更 関西・仙台以外の都道府県が選択された場合、首都圏版の鉄道会社リストを表示する
//		if (!ArbeitSite.isKindOfArea(todouhukenId)) {
//			return createBlankLabelValueList(blankLineLabel);
//		}


        List<? extends AbstractMstRailload> list = jdbcManager.from(AbstractMstRailload.getRailloadClass(todouhukenId))
                .orderBy(AbstractMstRailload.ID)
                .getResultList();


        if (CollectionUtils.isEmpty(list)) {
            return createBlankLabelValueList(blankLineLabel);
        }

        List<LabelValueDto> dtoList = new ArrayList<LabelValueDto>(list.size());


        addBrankLineLabel(dtoList, blankLineLabel);

        for (AbstractMstRailload entity : list) {
            dtoList.add(new LabelValueDto(entity.name, entity.id));
        }
        return dtoList;

    }


    /**
     * 路線リストを取得します。
     */
    public List<LabelValueDto> getRouteList(String railloadId, String blankLineLabel, final String suffix, int todouhukenId) {

// 仕様変更 関西・仙台以外の都道府県が選択された場合、首都圏版の路線リストを表示する
//		if (StringUtils.isBlank(railloadId) || !ArbeitSite.isKindOfArea(todouhukenId)) {
        if (StringUtils.isBlank(railloadId)) {
            return createBlankLabelValueList(blankLineLabel);
        }

        List<? extends AbstractMstRoute> list =
                jdbcManager.from(AbstractMstRoute.getRailloadClass(todouhukenId))
                        .where(new SimpleWhere()
                                .eq(WztStringUtil.toCamelCase(AbstractMstRoute.RAILLOAD_ID), NumberUtils.toInt(railloadId)))
                        .orderBy(AbstractMstRoute.ID)
                        .getResultList();


        if (CollectionUtils.isEmpty(list)) {
            return createBlankLabelValueList(blankLineLabel);
        }


        List<LabelValueDto> dtoList = new ArrayList<LabelValueDto>(list.size());
        addBrankLineLabel(dtoList, blankLineLabel);
        for (AbstractMstRoute entity : list) {
            dtoList.add(new LabelValueDto(entity.name, entity.id));
        }

        return dtoList;
    }


    /**
     * 駅リストを取得します。
     *
     * @param routeId 路線ID
     */
    public List<LabelValueDto> getStationList(String routeId, String blankLineLabel, final String suffix, int todouhukenId) {

// 仕様変更 関西・仙台以外の都道府県が選択された場合、首都圏版の駅リストを表示する
//		if (StringUtils.isBlank(routeId) || !ArbeitSite.isKindOfArea(todouhukenId)) {
        if (StringUtils.isBlank(routeId)) {
            return createBlankLabelValueList(blankLineLabel);
        }


        AutoSelect<? extends AbstractMstStation> select =
                jdbcManager.from(AbstractMstStation.getRailloadClass(todouhukenId))
                        .where(new SimpleWhere()
                                .eq(WztStringUtil.toCamelCase(AbstractMstStation.ROUTE_ID),
                                        NumberUtils.toInt(routeId)));

        final String orderBy;
        if (MArbeitConstants.SendaiSiteEnum.isSendaiSite(todouhukenId)) {
            orderBy = AbstractMstStation.DISP_NO;
        } else {
            orderBy = AbstractMstStation.ID;
        }
        List<? extends AbstractMstStation> entityList = select.orderBy(orderBy)
                .getResultList();

        if (CollectionUtils.isEmpty(entityList)) {
            return createBlankLabelValueList(blankLineLabel);
        }

        List<LabelValueDto> dtoList = new ArrayList<LabelValueDto>(entityList.size());

        addBrankLineLabel(dtoList, blankLineLabel);

        for (AbstractMstStation entity : entityList) {
            dtoList.add(new LabelValueDto(entity.name, entity.id));
        }


        return dtoList;
    }

    /**
     * 業態リストを取得します。
     */
    public List<LabelValueDto> getGyotaiList(String blankLineLabel, final String suffix) {
        List<LabelValueDto> dtoList =
                jdbcManager.from(MstGyotai.class)
                        .orderBy("id ASC")
                        .iterate(new IterationCallback<MstGyotai, List<LabelValueDto>>() {
                            private List<LabelValueDto> list = new ArrayList<LabelValueDto>();

                            @Override
                            public List<LabelValueDto> iterate(MstGyotai entity, IterationContext context) {
                                if (entity != null) {
                                    LabelValueDto dto = new LabelValueDto();
                                    dto.setLabel(entity.name + suffix);
                                    dto.setValue(String.valueOf(entity.id));
                                    list.add(dto);
                                }
                                return list;
                            }
                        });

        if (dtoList == null) {
            dtoList = new ArrayList<LabelValueDto>();
        }

        addBrankLineLabel(dtoList, blankLineLabel);

        return dtoList;
    }


    /**
     * 空のリストを作成します。
     */
    private static List<LabelValueDto> createBlankLabelValueList(String blankLineLabel) {
        List<LabelValueDto> list = new ArrayList<LabelValueDto>();
        addBrankLineLabel(list, blankLineLabel);
        return list;
    }

    /**
     * 初期ラベルを追加します。
     */
    private static void addBrankLineLabel(List<LabelValueDto> labelValueDtoList, String blankLineLabel) {
        if (StringUtils.isBlank(blankLineLabel)) {
            return;
        }
        LabelValueDto blankDto = new LabelValueDto();
        blankDto.setLabel(blankLineLabel);
        blankDto.setValue("");

        labelValueDtoList.add(0, blankDto);
    }


}
