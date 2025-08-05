package com.gourmetcaree.common.taglib.tag;

import java.util.List;

import javax.servlet.jsp.JspException;

import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.logic.LabelValueListLogic;

/**

 * @author Makoto Otani
 * @version 1.0
 */
public class MaxRowListTag extends BaseTagSupport {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3929781976629746216L;

	/** 名前 */
	private String name;

	/** 分割する文字列 */
	private String value;

	/** 接尾辞 */
	private String suffix = "";

	/** 全件ラベル */
	private String allLabel;

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 取得する定義名を設定します。
	 * @param value 取得する定義名
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 接尾辞を設定します。
	 * @param suffix 接尾辞
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}


	/**
	 * 全件ラベルを設定します。
	 * @param allLabel
	 */
	public void setAllLabel(String allLabel) {
		this.allLabel = allLabel;
	}


	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		try {
			LabelValueListLogic logic = getComponent(LabelValueListLogic.class);
			List<LabelValueDto> result = logic.getMaxRowList(value, suffix, allLabel);

			setAttribute(name, result);

			return EVAL_PAGE;
		} catch (Throwable t) {
			logger.error("MaxRowListTagの処理時にエラーが発生しました。", t);
			throw new JspException("MaxRowListTagの処理時にエラーが発生しました。", t);
		}
	}
}
