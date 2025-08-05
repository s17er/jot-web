package com.gourmetcaree.common.taglib.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.common.dto.LabelValueDto;

/**
 * 年齢のリストを生成して出力します。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class AgeListTag extends TagSupport{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4115095022708030234L;

	/** 最低年齢 */
	private static final int MIN_AGE = 15;

	/** 最高年齢 */
	private static final int MAX_AGE = 80;

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

			for (int i = MIN_AGE; i <= MAX_AGE; i++) {
				LabelValueDto dto = new LabelValueDto();
				dto.setLabel(String.valueOf(String.valueOf(i) + suffix));
				dto.setValue(String.valueOf(String.valueOf(i) + suffix));
				labelValueList.add(dto);
			}

			pageContext.setAttribute(name, labelValueList);
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}
}
