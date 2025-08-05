package com.gourmetcaree.common.taglib.tag;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 勤務地エリア(WEBエリアから名称変更)配列をカンマ区切りのStringにして返します。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ConvertToWebAreaNameTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8691435459623416834L;

	/** コードを格納するオブジェ */
	private Object items;

	/** エリアコード */
	private String areaCd;

	/** 勤務地エリア配列をセット */
	public void setItems(Object items) {
		this.items = items;
	}

	/** エリアコードをセット */
	public void setAreaCd(String areaCd) {
		this.areaCd = areaCd;
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
		Class<?>[] argsClass = {String[].class, String.class};
		Method method = ClassUtil.getMethod(clazz, "convertToWebAreaName", argsClass);

		String areaKbn = null;
		if (String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA).equals(areaCd)){
			areaKbn = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
		}

		String[] webAreaKbn;

		//配列、List、Stringのみ処理の対象とする。
		if (items instanceof String[]) {

			webAreaKbn = (String[])items;

		} else if (items instanceof List) {

			List<?> list = (List<?>)items;
			webAreaKbn = new String[list.size()];

			for (int i=0; i<list.size(); i++) {
				webAreaKbn[i] = (String)list.get(i);
			}

		} else if (items instanceof String){

			webAreaKbn = new String[1];
			webAreaKbn[0] = (String)items;
		} else {

			webAreaKbn = new String[0];
		}

		//配列が空の場合は未処理とする。
		if (webAreaKbn.length == 0) {
			return EVAL_PAGE;
		}

		//メソッドの引数を1つセット。
		Object[] args = new Object[2];
		args[0] = webAreaKbn;
		args[1] = areaKbn;

		try {
			pageContext.getOut().write((String)MethodUtil.invoke(method, obj, args));
		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
