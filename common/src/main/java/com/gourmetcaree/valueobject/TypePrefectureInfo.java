package com.gourmetcaree.valueobject;

import com.gourmetcaree.db.common.entity.MType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 都道府県に対してMTypeの紐付きを表すオブジェクト
 */
@Data
@RequiredArgsConstructor
public class TypePrefectureInfo {

    private final int prefectureCd;

    private final String prefectureName;

    private final List<MType> typeList;

}
