package com.gourmetcaree.common.taglib.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.logic.LabelValueListLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * タイプの値を指定して表示するタイプリスト
 * @author Takehiro Nakamori
 *
 */
public class SpecifiedTypeListTag extends BaseTagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 9040784074626112926L;

	/** var */
	private String var;

	/** タイプコード */
	private String typeCd;

	/** タイプの値 */
	private Object typeValue;

	/** 空ラベル */
	private String blankLineLabel;

	/** サフィックス */
	private String suffix;

	/** スコープ */
	private String scope;

	public void setVar(String var) {
		this.var = var;
	}

	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}

	public void setTypeValue(Object typeValue) {
		this.typeValue = typeValue;
	}

	public void setBlankLineLabel(String blankLineLabel) {
		this.blankLineLabel = blankLineLabel;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}


	@Override
	public int doEndTag() throws JspException {
		try {
			setAttribute(var, createList(), scope);
			return EVAL_PAGE;
		} catch (Exception e) {
			logger.error("SpecifiedTypeListTagでエラーが発生しました。", e);
			throw new JspException("SpecifiedTypeListTagでエラーが発生しました。", e);
		}
	}


	/**
	 * リストを作成(JUNITで参照しやすくするために分割。
	 * @return
	 */
	private List<LabelValueDto> createList() {
		LabelValueListLogic logic = getComponent(LabelValueListLogic.class);
		List<Integer> typeValueList = new ArrayList<Integer>();
		if (typeValue instanceof List) {
			for (Object obj : ((List<?>) typeValue)) {
				typeValueList.add(GourmetCareeUtil.toObjectToInt(obj, 0));
			}
		} else if (typeValue instanceof Object[]) {
			for (Object obj : ((Object[]) typeValue)) {
				typeValueList.add(GourmetCareeUtil.toObjectToInt(obj, 0));
			}
		} else if (typeValue instanceof int[]) {
			for (int obj : ((int[]) typeValue)) {
				typeValueList.add(obj);
			}
		} else {
			typeValueList.add(GourmetCareeUtil.toObjectToInt(typeValue, 0));
		}

		return logic.getSpecifiedTypeList(typeCd, blankLineLabel, suffix, typeValueList);
	}

}
