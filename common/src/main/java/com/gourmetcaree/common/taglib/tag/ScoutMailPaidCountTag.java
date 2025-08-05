package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * スカウトメールの残りを取得します。
 * @author Keita Yamane
 * @version 1.0
 */
public class ScoutMailPaidCountTag extends BaseTagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8455898654832984168L;

	/** ログオブジェクト */
	private final static Logger log = Logger.getLogger(ScoutMailPaidCountTag.class);

	/** 顧客ID */
	private int customerId;

	/** 名前 */
	private String name;


	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * 顧客IDをセットします。
	 * @param customerId
	 * @author Keita Yamane
	 */
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		try {

			Class<?> clazz = Class.forName("com.gourmetcaree.shop.logic.logic.MemberLogic");
			Object obj = getComponent(clazz);
			Class<?>[] argsClass = {int.class};
			Method method = ClassUtil.getMethod(clazz, "getRemainScoutMail", argsClass);
			Object[] args = new Object[1];
			args[0] = (int) customerId;

			setAttribute(name, MethodUtil.invoke(method, obj, args), SCOPE_PAGE);
		} catch (Exception e) {
			log.fatal(String.format("スカウトメール数が取得できませんでした。：店舗ID : %d", customerId));
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
