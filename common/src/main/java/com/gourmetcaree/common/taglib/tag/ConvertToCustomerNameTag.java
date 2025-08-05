package com.gourmetcaree.common.taglib.tag;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 顧客IDを顧客名に変換するタグです。
 * @author Takehiro Nakamori
 *
 */
public class ConvertToCustomerNameTag extends TagSupport implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -162860841995634461L;

	/** 顧客ID */
	private String customerId;

	/** 名称 */
	private String name;

	@Override
	@SuppressWarnings("rawtypes")
	public int doEndTag() throws javax.servlet.jsp.JspException {
		// 注) HotDeploy対応のためリフレクションで呼んでいます
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent("valueToNameConvertLogic");
		Class clazz = container.getComponentDef("valueToNameConvertLogic").getComponentClass();
		Class[] argsClass = {int.class};
		Method method = ClassUtil.getMethod(clazz, "convertToCustomerName", argsClass);
		try {

			if (StringUtils.isBlank(customerId)) {
				if (StringUtils.isBlank(name)) {
					pageContext.getOut().write("");
				} else {
					pageContext.setAttribute(name, "");
				}
				return EVAL_PAGE;
			}

			Object[] args = new Object[1];
			args[0] = NumberUtils.toInt(customerId);
			String customerName = (String) MethodUtil.invoke(method, obj, args);
			if (StringUtils.isBlank(name)) {
				pageContext.getOut().write(customerName);
			} else {
				pageContext.setAttribute(name, customerName);
			}
		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	};

	/**
	 * 顧客IDをセットします。
	 * @param customerId
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * 名称をセットします。
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
