package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 号数マスタのラベルとバリューが格納されたリストをページコンテキストに設定します。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class SimpleVolumeListTag extends TagSupport {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3062120796739944130L;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 接尾辞 */
	private String suffix = "";

	/** 非表示値 */
	private Object noDisplayValue = new ArrayList<Integer>();

	/** 限定値 */
	private String limitValue = null;

	/** 権限 */
	private String authLevel = null;


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

	/**
	 * 非表示値を設定します。
	 * @param noDisplayValue 非表示値
	 */
	public void setNoDisplayValue(Object noDisplayValue) {
		this.noDisplayValue = noDisplayValue;
	}

	/**
	 * 限定値を設定します。
	 * @param limitValue 限定値
	 */
	public void setLimitValue(String limitValue) {
		this.limitValue = limitValue;
	}

	/**
	 * 権限を設定します。
	 * @param authLevel 権限
	 */
	public void setAuthLevel(String authLevel) {
		this.authLevel = authLevel;
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
		Class<?>[] argsClass = {String.class, String.class, List.class, String.class, String.class};
		Method method = ClassUtil.getMethod(clazz, "getSimpleVolumeList", argsClass);

		Object[] args = new Object[5];
		args[0] = (String)blankLineLabel;
		args[1] = (String)suffix;
		args[2] = (List<Integer>)noDisplayValue;
		if (StringUtils.isNotBlank((String)limitValue)) {
			args[3] = (String)limitValue;
		} else {
			args[3] = null;
		}
		if (StringUtils.isNotBlank((String)authLevel)) {
			args[4] = (String)authLevel;
		} else {
			args[4] = null;
		}


		try {
			pageContext.setAttribute(name, MethodUtil.invoke(method, obj, args));
		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
