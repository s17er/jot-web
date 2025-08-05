package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

import com.gourmetcaree.common.logic.LabelValueListLogic;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;

/**
 * 事前登録リストタグ
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationListTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7897592820906094542L;

	/** 名前 */
	private String name;

	/** 初期ラベル */
	private String blankLineLabel = null;


	/** 名前区分 */
	private int nameKbn = MAdvancedRegistration.NameKbn.NORMAL_NAME;

	/** スコープ */
	private String scope = "";





	@Override
	public int doEndTag() throws JspException {
		try {
			S2Container container = SingletonS2ContainerFactory.getContainer();
			Object obj = container.getComponent(LabelValueListLogic.class);
			Class<?> clazz = container.getComponentDef(LabelValueListLogic.class).getComponentClass();
			Class<?>[] argClass = {String.class, int.class};
			Method method = ClassUtil.getMethod(clazz, "getAdvancedRegistrationList", argClass);

			Object[] args = {blankLineLabel, nameKbn};


			Object result = MethodUtil.invoke(method, obj, args);

			int context;
			if (StringUtils.isBlank(scope) || "page".equals(scope)) {
				context = PageContext.PAGE_SCOPE;
			} else if ("request".equals(scope)) {
				context = PageContext.REQUEST_SCOPE;
			} else {
				context = PageContext.PAGE_SCOPE;
			}

			pageContext.setAttribute(name, result, context);



			return EVAL_PAGE;
		} catch (Exception e) {
			throw new JspException(e);
		}

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
	 * 名前区分をセットします。
	 * @param nameKbn 名前区分
	 */
	public void setNameKbn(int nameKbn) {
		this.nameKbn = nameKbn;
	}


	/**
	 * スコープを設定します。
	 * @param scope
	 */
	public void setScope (String scope) {
		this.scope = scope;
	}

}
