package com.gourmetcaree.common.builder.sql.web;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.container.SingletonS2Container;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebJobService;
import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;

/**
 * 運営管理のWEBデータ一覧、プレビュー、CSV出力でWEBデータを検索するためのSQLビルダー
 * 下記追加項目のみのWHERE条件を生成する。
 * ・職種検索条件（チェックボックス）
 * ・店舗数（チェックボックス）
 * ・雇用形態（チェックボックス）
 * ・求める人物像、待遇、休日休暇、オープン日のチェックボックス（チェックボックス）
 * ・勤務地詳細エリア（チェックボックス）
 * XXX これの元処理がcommonのWebListServiceでやってしまってるのでcommonに配備しているが、本来は運営ロジックのモデルがやるべき処理。
 * XXX 既にロジック内でSQLが組み上がってしまっているので、新規追加分のみをここで設定する。
 */
public class WebDataSearchSqlBuilderImpl implements SqlBuilder {
    private final Logger logger = Logger.getLogger(this.getClass());

    /** t_webのエイリアス */
    private final String webAlias;

    private final StringBuilder sql;

    private final List<Object> params;

    private final VWebListDto condition;

    /** WEB属性サービス */
    private final WebAttributeService attributeService;

    /** WEB職種サービス */
    private final WebJobService webJobService;

    /**
     * 属性検索でヒットしたWebIdのセット
     */
    private List<List<Integer>> webIdListList;

    /** 指定の属性検索があるかどうか。 */
    private boolean isSearchable;

    public WebDataSearchSqlBuilderImpl(String webAlias, StringBuilder sql, List<Object> params, VWebListDto condition) {
        this.webAlias = StringUtils.isBlank(webAlias) ? "" : webAlias.concat(".");
        this.sql = sql;
        this.params = params;
        this.condition = condition;
        this.attributeService = SingletonS2Container.getComponent(WebAttributeService.class);
        this.webJobService = SingletonS2Container.getComponent(WebJobService.class);
    }



    @Override
    public SqlCondition build() {
        this.webIdListList = Lists.newArrayList();
        this.isSearchable = false;

        addConditions();


        if (isSearchable) {
            for (List<Integer> webIdList : webIdListList) {
                // 本来ならクエスチョンマークを指定するが、数が多すぎて例外が発生する可能性があるため、webIdを直接入力する。
                sql.append(String.format(" AND %sid IN (%s) ",
                        webAlias,
                        StringUtils.join(webIdList, ", ")));
            }
        }


        return new SqlCondition(sql, params);
    }


    /**
     * 検索条件の追加
     */
    private void addConditions() {
        addDetailAreaKbns();

        // 検索属性があり、webIdが空の場合は検索結果がなくなる条件を入力
        if (isSearchable && webIdListList.isEmpty()) {
            webIdListList.add(Collections.singletonList(-1));
        }
    }

    /**
     * 区分の検索結果webIdをwebIdListListに追加
     * @param typeCd タイプコード
     * @param values 値
     */
    private void addKbn(String typeCd, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return;
        }
        this.isSearchable = true;
        List<Integer> list = attributeService.findWebIdsByAttributeCdAndAttributeValues(
                typeCd,
                GcCollectionUtils.toIntegerList(values));

        if (CollectionUtils.isNotEmpty(list)) {
            this.webIdListList.add(list);
        } else {
            // 検索結果が0件になるリストを渡す
            this.webIdListList.add(Collections.singletonList(-1));
        }
    }
    /**
     * 職種のWebId検索結果を追加
     */
    private void addJobKbns() {
        addKbn(MTypeConstants.JobKbn.TYPE_CD,
                condition.jobKbnList);
    }

    /**
     * 店舗数のWebId検索結果を追加
     */
    private void addShopsKbns() {
        addKbn(MTypeConstants.ShopsKbn.TYPE_CD,
                condition.shopsKbnList);
    }

    /**
     * 雇用形態のWebId検索結果を追加
     */
    private void addEmployPtnKbns() {
        addKbn(MTypeConstants.EmployPtnKbn.TYPE_CD,
                condition.employPtnKbnList);
    }

    /**
     * 詳細エリアのWebId検索結果を追加
     */
    private void addDetailAreaKbns() {
        final String typeCd;
        if (MAreaConstants.AreaCd.isSendai(condition.areaCd)) {
            typeCd = MTypeConstants.DetailAreaKbnGroup.getTypeCd(condition.areaCd);
        } else {
            typeCd = MTypeConstants.DetailAreaKbn.getTypeCd(condition.areaCd);
        }
        logger.debug(String.format("areaCd:[%s] typeCd:[%s]",
                condition.areaCd,
                typeCd));
        addKbn(typeCd,
                condition.detailAreaList);
    }

    /**
     * 待遇のWebId検索結果を追加
     */
    private void addTreatmentKbns() {
        addKbn(MTypeConstants.TreatmentKbn.TYPE_CD,
                condition.treatmentList);
    }

    /**
     * その他条件のWebId検索結果を追加
     */
    private void addOtherConditionKbns() {
        addKbn(MTypeConstants.OtherConditionKbn.TYPE_CD,
                condition.otherConditionList);
    }
}
