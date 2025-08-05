package com.gourmetcaree.common.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * リクエストごとに生成するDTOです。
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.REQUEST)
public class RequestDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6302150791307897140L;
	/**
	 * スマホフラグ
	 */
	private boolean smartPhoneFlg;

	/** canonicalに表記するURL */
	private String canonicalURL;

	/** コンテキスト名までのURL */
	private String contextURL;

	/** リファラURL */
	private String refererURL;

	/** リクエストスキーム */
	private String requestScheme;

	/** alternativeタグ用URL */
	private String alternativeURL;

	/**
	 * スマホフラグを取得します。
	 * @return
	 */
	public boolean getSmartPhoneFlg() {
		return smartPhoneFlg;
	}

	/**
	 * スマホフラグを設定します。
	 */
	public void setSmartPhoneFlg(boolean smartPhoneFlg) {
		this.smartPhoneFlg = smartPhoneFlg;
	}

	/**
	 * canonicalURLの取得
	 * @return
	 */
	public String getCanonicalURL() {
		return canonicalURL;
	}

	/**
	 * canonicalURLを設定します。
	 * @param canonicalURL
	 */
	public void setCanonicalURL(String canonicalURL) {
		this.canonicalURL = canonicalURL;
	}

	/**
	 * コンテキスト名までのURLを取得
	 * @return contextURL
	 */
	public String getContextURL() {
		return contextURL;
	}

	/**
	 * コンテキスト名までのURLをセット
	 * @param contextURL セットする contextURL
	 */
	public void setContextURL(String contextURL) {
		this.contextURL = contextURL;
	}

	/**
	 * リファラURLの取得
	 * @return
	 */
	public String getRefererURL() {
		return refererURL;
	}

	/**
	 * リファラURLのセット
	 * @param refererURL
	 */
	public void setRefererURL(String refererURL) {
		this.refererURL = refererURL;
	}

	/**
	 * alternativeタグ用URLのセット
	 * @param alternativeURL alternativeタグ用URL
	 */
	public void setAlternativeURL(String alternativeURL) {
		this.alternativeURL = alternativeURL;
	}

	/**
	 * alternativeタグ用URL の取得
	 * @return
	 */
	public String getAlternativeURL() {
		return alternativeURL;
	}

	/**
	 * リクエストスキームの取得
	 * @return リクエストスキーム
	 */
	public String getRequestScheme() {
		return requestScheme;
	}

	/**
	 * リクエストスキームのセット
	 * @param requestScheme リクエストスキーム
	 */
	public void setRequestScheme(String requestScheme) {
		this.requestScheme = requestScheme;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}

}
