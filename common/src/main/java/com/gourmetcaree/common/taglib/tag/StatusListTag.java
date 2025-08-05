package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * ステータスマスタのラベルとバリューが格納されたリストをページコンテキストに設定します。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class StatusListTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8846598426435409973L;

	/** 名前 */
	private String name;

	/** ステータス区分 */
	private String statusKbn;

	/** ステータスコード */
	private String statusCd;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 接尾辞 */
	private String suffix = "";

	/** 非表示値 */
	private Object noDisplayValue = new ArrayList<Integer>();

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ステータス区分を設定します。
	 * @param statusKbn ステータス区分
	 */
	public void setStatusKbn(String statusKbn) {
		this.statusKbn = statusKbn;
	}

	/**
	 * ステータスコードを設定します。
	 * @param statusCd ステータスコード
	 */
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
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
		Class<?>[] argsClass = {String.class, String.class, String.class, String.class, List.class};
		Method method = ClassUtil.getMethod(clazz, "getStatusList", argsClass);

		Object[] args = new Object[5];
		args[0] = (String)statusKbn;
		args[1] = (String)statusCd;
		args[2] = (String)blankLineLabel;
		args[3] = (String)suffix;
		args[4] = (List<Integer>)noDisplayValue;

		try {
			pageContext.setAttribute(name, MethodUtil.invoke(method, obj, args));
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}
}