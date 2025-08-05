package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.common.logic.LabelValueListLogic;

/**
 * 登録年のリストを取得します。
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationYearTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6238296614371565910L;

	/** 名前 */
	private String name;

	/** スコープ */
	private String scope;

	@Override
	public int doEndTag() throws JspException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent(LabelValueListLogic.class);
		Class<?> clazz = container.getComponentDef(LabelValueListLogic.class).getComponentClass();
		Method method = ClassUtil.getMethod(clazz, "getAdvancedRegistrationYearList", null);

		try {
			Object result = MethodUtil.invoke(method, obj, null);
			if (StringUtil.isBlank(scope) || "page".equals(scope)) {
				pageContext.setAttribute(name, result, PageContext.PAGE_SCOPE);
			} else if ("request".equals(scope)) {
				pageContext.setAttribute(name, result, PageContext.REQUEST_SCOPE);
			} else {
				pageContext.setAttribute(name, result, PageContext.PAGE_SCOPE);
			}
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}

	/**
	 * 名前のセット
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * スコープのセット
	 * @param scope スコープ
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}


}
