package com.gourmetcaree.common.logic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 都道府県とタイプをINNER JOINしてセレクトする結果保持データ
 */
@Data
public class PrefectureTypeResult implements Serializable {

    private static final long serialVersionUID = -348296337403420074L;

    private Integer prefecturesCd;

    private String typeCd;

    private Integer typeValue;

    private String prefecturesName;
}
