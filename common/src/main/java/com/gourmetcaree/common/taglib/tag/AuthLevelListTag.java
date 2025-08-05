package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 権限レベルリストをページコンテキストに設定します。<br>
 * セレクトボックスやラベルに使用して下さい。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class AuthLevelListTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8399802137005433181L;

	/** 名前 */
	private String name;

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
		Method method = ClassUtil.getMethod(clazz, "getAuthLevelList", null);

		try {
			pageContext.setAttribute(name, MethodUtil.invoke(method, obj, null));
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
}
