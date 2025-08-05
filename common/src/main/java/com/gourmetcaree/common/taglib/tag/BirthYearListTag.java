package com.gourmetcaree.common.taglib.tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.common.dto.LabelValueDto;

/**
 * 生年月日の年のリストを生成して出力します。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class BirthYearListTag extends TagSupport{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -373923898627754779L;

	/** 開始年 */
	private static final int START_YEAR = 1939;

	/** 最小年齢年数 */
	private static final int MIN_AGE_YEAR = 15;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 接尾辞 */
	private String suffix = "";

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 初期ラベルを設定します。
	 * @param blankLineLabel 初期ラベル
	 */
	public void setBlankLineLabel(String blankLineLabel) {
		this.blankLineLabel = blankLineLabel;
	}

	/**
	 * 接尾辞を設定します。
	 * @param suffix 接尾辞
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}


	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		try {

			Calendar lastCal = Calendar.getInstance();
			lastCal.add(Calendar.YEAR, -(MIN_AGE_YEAR));

			for (int i = START_YEAR; i <= lastCal.get(Calendar.YEAR); i++) {
				String label = String.valueOf(i) + suffix;
				LabelValueDto dto = new LabelValueDto();
				dto.setLabel(label);
				dto.setValue(label);
				labelValueList.add(dto);
			}

			pageContext.setAttribute(name, labelValueList);
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}
}
