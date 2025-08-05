package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 定型文のラベルとバリューが格納されたリストをページコンテキストに設定します。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class SentenceListTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8424625958778803009L;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 限定値 */
	private String limitValue = null;

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
	 * 限定値を設定します。
	 * @param limitValue 限定値
	 */
	public void setLimitValue(String limitValue) {
		this.limitValue = limitValue;
	}

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		// 注) HotDeploy対応のためリフレクションで呼んでいます
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent("labelValueListLogic");
		Class<?> clazz = container.getComponentDef("labelValueListLogic").getComponentClass();
		Class<?>[] argsClass = {String.class, String.class};
		Method method = ClassUtil.getMethod(clazz, "getSentenceList", argsClass);

		Object[] args = new Object[2];
		args[0] = (String)blankLineLabel;
		if (StringUtils.isNotBlank((String)limitValue)) {
			args[1] = (String)limitValue;
		} else {
			args[1] = null;
		}

		try {
			pageContext.setAttribute(name, MethodUtil.invoke(method, obj, args));
		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
