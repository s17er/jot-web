package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 業種配列をカンマ区切りのStringにして返します。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ConvertToIndustryNameTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2390635327271220155L;

	/** コードを格納するオブジェ */
	private Object items;

	/** コードをセットします */
	public void setItems(Object items) {
		this.items = items;
	}
	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		// 注) HotDeploy対応のためリフレクションで呼んでいます
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent("valueToNameConvertLogic");
		Class<?> clazz = container.getComponentDef("valueToNameConvertLogic").getComponentClass();
		Class<?>[] argsClass = {String[].class};
		Method method = ClassUtil.getMethod(clazz, "convertToIndustryName", argsClass);

		String[] industryKbn;

		//配列、List、Stringのみ処理の対象とする。
		if (items instanceof String[]) {

			industryKbn = (String[])items;

		} else if (items instanceof List) {

			List<?> list = (List<?>)items;
			industryKbn = new String[list.size()];

			for (int i=0; i<list.size(); i++) {
				industryKbn[i] = (String)list.get(i);
			}

		} else if (items instanceof String){

			industryKbn = new String[1];
			industryKbn[0] = (String)items;
		} else {

			industryKbn = new String[0];
		}

		//配列が空の場合は未処理とする。
		if (industryKbn.length == 0) {
			return EVAL_PAGE;
		}

		//メソッドの引数を1つセット。
		Object[] args = new Object[1];
		args[0] = industryKbn;

		try {
			pageContext.getOut().write((String)MethodUtil.invoke(method, obj, args));
		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
