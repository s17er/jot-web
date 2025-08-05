package com.gourmetcaree.arbeitsys.logic;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * グルメdeバイトの駅リストを生成して出力します。
 * @author Takehiro Nakamori
 *
 */
public class StationListTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7864165990811409318L;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;

	/** 接尾辞 */
	private String suffix = "";

	/** 駅ID */
	private String stationId;


	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		Object obj = container.getComponent(ArbeitLabelValueListLogic.class);
		Class<?> clazz = container.getComponentDef(ArbeitLabelValueListLogic.class).getComponentClass();
		Class<?>[] argClass = {String.class, String.class, String.class};
		Method method = ClassUtil.getMethod(clazz, "getStationList", argClass);

		Object[] args = new Object[]{stationId, blankLineLabel, suffix};

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



	/**
	 * 駅IDをセットします。
	 * @param stationId 駅ID
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}




}
