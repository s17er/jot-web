package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 区分マスタのラベルとバリューが格納されたリストをページコンテキストに設定します。
 * @author Kaneko
 * @version 1.0
 */
public class AllAreaForFrontTag extends TagSupport {

	private static final long serialVersionUID = -5201469361253908908L;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 非表示値 */
	private Object noDisplayValue = new ArrayList<Integer>();

	/** 接尾辞 */
	private String suffix = "";

	/** スコープ */
	private String scope = "";

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
	 * 非表示値を設定します。
	 * @param noDisplayValue 非表示値
	 */
	public void setNoDisplayValue(Object noDisplayValue) {
		this.noDisplayValue = noDisplayValue;
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


	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	@SuppressWarnings(value="unchecked")
	public int doEndTag() throws JspException {
		// 注) HotDeploy対応のためリフレクションで呼んでいます
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent("labelValueListLogic");
		Class<?> clazz = container.getComponentDef("labelValueListLogic").getComponentClass();
		Class<?>[] argsClass = {String.class, String.class, List.class};
		Method method = ClassUtil.getMethod(clazz, "getAllAreaListForFront", argsClass);

		Object[] args = new Object[3];
		args[0] = (String)blankLineLabel;
		args[1] = (String)suffix;
		args[2] = (List<Integer>)noDisplayValue;

		Object methodResult = MethodUtil.invoke(method, obj, args);
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