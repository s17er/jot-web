package com.gourmetcaree.arbeitsys.taglib.tag;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

import com.gourmetcaree.arbeitsys.logic.ArbeitLabelValueListLogic;

public class ArbeitIndustoryListTag extends TagSupport {


	private static final long serialVersionUID = -4678019777467947330L;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 接尾辞 */
	private String suffix = "";

	@Override
	public int doEndTag() throws JspException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent(ArbeitLabelValueListLogic.class);
		Class<?> clazz = container.getComponentDef(ArbeitLabelValueListLogic.class).getComponentClass();
		Class<?>[] argClass  = new Class[]{String.class, String.class};
		Object[] args = new Object[] {blankLineLabel, suffix};

		Method method = ClassUtil.getMethod(clazz, "getGyotaiList", argClass);

		try {
			pageContext.setAttribute(name, MethodUtil.invoke(method, obj, args));
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;

	}


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


}
