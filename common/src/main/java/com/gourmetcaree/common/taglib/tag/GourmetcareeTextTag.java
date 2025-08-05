package com.gourmetcaree.common.taglib.tag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.taglib.html.TextTag;

/**
 * グルメキャリー用textタグです。
 * placeHolserをセットできるようにオーバーライドしています。
 * @author Takehiro Nakamori
 *
 */
public class GourmetcareeTextTag extends TextTag {

	/**
	 *
	 */
	private static final long serialVersionUID = -2364663521204804228L;


	/** プレースホルダー */
	private String placeHolder;



	@Override
	public int doStartTag() throws JspException {
		// TODO 自動生成されたメソッド・スタブ
		return super.doStartTag();
	}


	@Override
	protected String renderInputElement() throws JspException {

		StringBuffer results = new StringBuffer("<input");

		prepareAttribute(results, "type", this.type);
		prepareAttribute(results, "name", prepareName());
		prepareAttribute(results, "accesskey", getAccesskey());
		prepareAttribute(results, "accept", getAccept());
		prepareAttribute(results, "maxlength", getMaxlength());
		prepareAttribute(results, "size", getCols());
		prepareAttribute(results, "tabindex", getTabindex());
		if (StringUtils.isNotBlank(placeHolder)) {
			prepareAttribute(results, "placeholder", placeHolder);
		}
		prepareValue(results);
		results.append(this.prepareEventHandlers());
		results.append(this.prepareStyles());
		prepareOtherAttributes(results);
		results.append(this.getElementClose());
		return results.toString();
	}



	/**
	 * プレースホルダーをセットします。
	 * @param placeHolder プレースホルダー
	 */
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
}
