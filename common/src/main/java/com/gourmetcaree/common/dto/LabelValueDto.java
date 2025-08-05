package com.gourmetcaree.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;
import com.gourmetcaree.arbeitsys.entity.MstTodouhuken;
import com.gourmetcaree.db.common.entity.MType;

/**
 * ラベルと値を保持するクラスです。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class LabelValueDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1804875640693232613L;

	/** ラベル */
	private String label;

	/** 値 */
	private String value;

	/** 並び順 */
	private Integer sort;


	public LabelValueDto() {}


	public LabelValueDto(MType type) {
		this(type.typeName, type.typeValue);
	}
	public LabelValueDto(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public LabelValueDto(String label, Object value) {
		this.label = label;
		this.value = String.valueOf(value);
	}

	/**
	 * ラベルを取得します。
	 * @return ラベル
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * ラベルを設定します。
	 * @param label ラベル
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 値を取得します。
	 * @return 値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 値を設定します。
	 * @param value 値
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * 空のリストを作成
	 * @param blankLineLabel 空のラベル
	 * @return
	 */
	public static List<LabelValueDto> createBlankList(String blankLineLabel) {
		List<LabelValueDto> list = new ArrayList<LabelValueDto>(0);
		if (StringUtils.isNotBlank(blankLineLabel)) {
			list.add(createBlankLabelValueDto(blankLineLabel));
		}
		return list;
	}

	/**
	 * 空のDTOを作成
	 * @param blankLineLabel 空のラベル
	 * @return
	 */
	public static LabelValueDto createBlankLabelValueDto(String blankLineLabel) {
		return new LabelValueDto(blankLineLabel, null);
	}

	/**
	 * MTypeのコレクションからLabelValueDtoのリストを生成
	 * @param types MTypeのコレクション
	 * @return LabelValueDtoのリスト
	 */
	public static List<LabelValueDto> newInstanceList(Collection<MType> types) {
		List<LabelValueDto> list = Lists.newArrayList();
		if (CollectionUtils.isEmpty(types)) {
			return list;
		}
		for (MType type : types) {
			list.add(new LabelValueDto(type));
		}
		return list;
	}

	public static List<LabelValueDto> newInstanceListFromMstTodoufukenList(Collection<MstTodouhuken> prefs, String suffix) {
		if (CollectionUtils.isEmpty(prefs)) {
			return Collections.emptyList();
		}

		final String suf = StringUtils.defaultString(suffix);
		List<LabelValueDto> list = Lists.newArrayList();
		for (MstTodouhuken entity : prefs) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.name + suf);
			dto.setValue(String.valueOf(entity.id));
			list.add(dto);
		}
		return list;


	}

    /**
     * 空用のラベルを追加
     *
     * @param dtoList        DTOリスト
     * @param blankLineLabel 空用ラベル文字
     */
    public static void addBlankLineLabel(List<LabelValueDto> dtoList, String blankLineLabel) {
        if (blankLineLabel == null) {
            return;
        }

        dtoList.add(0, LabelValueDto.createBlankLabelValueDto(blankLineLabel));
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
