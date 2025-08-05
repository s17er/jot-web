package com.gourmetcaree.common.taglib.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.SingletonS2Container;

import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.logic.LabelValueListLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * 区分マスタのラベルとバリューが格納されたリストをページコンテキストに設定します。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class TypeListTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4111771263131897068L;

	/** 名前 */
	private String name;

	/** 区分マスタコード */
	private String typeCd;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 接尾辞 */
	private String suffix = "";

	/** 非表示値 */
	private Object noDisplayValue = new ArrayList<Integer>();

	/** スコープ */
	private String scope = "";

	/** type_valueの最小値 */
	private Object minValue;

	/** type_valueの最大値 */
	private Object maxValue;

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 区分マスタコードを設定します。
	 * @param typeCd 区分マスタコード
	 */
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
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

	/**
	 * スコープを設定します。
	 * @param scope
	 */
	public void setScope (String scope) {
		this.scope = scope;
	}

	/**
	 * 非表示値を設定します。
	 * @param noDisplayValue 非表示値
	 */
	public void setNoDisplayValue(Object noDisplayValue) {
		this.noDisplayValue = noDisplayValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	/*
         * (非 Javadoc)
         * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
         */
	@Override
	@SuppressWarnings(value="unchecked")
	public int doEndTag() throws JspException {
		LabelValueListLogic logic = SingletonS2Container.getComponent(LabelValueListLogic.class);
		List<LabelValueDto> methodResult = logic.getTypeList(
		        typeCd, 
		        blankLineLabel, 
		        GourmetCareeUtil.objectToInteger(minValue), 
		        GourmetCareeUtil.objectToInteger(maxValue), 
		        suffix, 
		        (List<Integer>) noDisplayValue);
		
		try {
			if (StringUtils.isBlank(scope) || "page".equals(scope)) {
				pageContext.setAttribute(name, methodResult);
			} else if ("request".equals(scope)) {
				pageContext.setAttribute(name, methodResult, PageContext.REQUEST_SCOPE);
			} else {
				pageContext.setAttribute(name, methodResult);
			}

		} catch (Exception e) {
			throw new JspException(e);
		}


		return EVAL_PAGE;
	}
}