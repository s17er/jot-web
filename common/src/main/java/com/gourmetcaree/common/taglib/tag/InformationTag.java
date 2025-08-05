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
 * お知らせをページコンテキストに設定します。
 * エリアコードと管理画面区分を必須とする。
 * 空の場合にデフォルトメッセージを表示する場合は、
 * noInfoMsgを指定します。
 * @author Takahiro Ando
 * @version 1.0
 */
public class InformationTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3168432819073960549L;

	/** 名前 */
	private String name;

	/** システムからのお知らせがない場合のメッセージ */
	private String noInfoMsg;

	/** エリアコード */
	private Integer areaCd;

    /** 管理画面区分 */
	private Integer managementScreenKbn;

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		// お知らせ取得メソッドをコール
		// 注) HotDeploy対応のためリフレクションで呼んでいます
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent("informationService");
		Class<?> clazz = container.getComponentDef("informationService").getComponentClass();

		Class<?>[] argsClass = {int.class, int.class};

		Method method = ClassUtil.getMethod(clazz, "getInformationBody", argsClass);

		Object[] args = new Object[2];
		args[0] = managementScreenKbn;
		args[1] = areaCd;

		try {
			String informationBody = (String) MethodUtil.invoke(method, obj, args);

			if (StringUtils.isNotEmpty(informationBody)) {
				pageContext.setAttribute(name, informationBody);
			} else if (StringUtils.isNotEmpty(noInfoMsg)) {
				pageContext.setAttribute(name, noInfoMsg);
			}
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
	 * システムからのお知らせがない場合のメッセージを設定します。
	 * @param noInfoMsg システムからのお知らせがない場合のメッセージ
	 */
	public void setNoInfoMsg(String noInfoMsg) {
		this.noInfoMsg = noInfoMsg;
	}

	/**
	 * エリアコードを取得します。
	 * @return エリアコード
	 */
	public Integer getAreaCd() {
		return areaCd;
	}

	/**
	 * エリアコードを設定します。
	 * @param areaCd エリアコード
	 */
	public void setAreaCd(Integer areaCd) {
		this.areaCd = areaCd;
	}

	/**
	 * 管理画面区分を取得します。
	 * @return 管理画面区分
	 */
	public Integer getManagementScreenKbn() {
		return managementScreenKbn;
	}

	/**
	 * 管理画面区分を設定します。
	 * @param managementScreenKbn 管理画面区分
	 */
	public void setManagementScreenKbn(Integer managementScreenKbn) {
		this.managementScreenKbn = managementScreenKbn;
	}
}
