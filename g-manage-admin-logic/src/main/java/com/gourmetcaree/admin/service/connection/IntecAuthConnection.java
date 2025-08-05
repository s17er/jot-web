package com.gourmetcaree.admin.service.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.util.ResourceUtil;


/**
 * インテックのAPIに認証するためのクラスです。
 *
 * @author hara
 *
 */
public class IntecAuthConnection extends AbstractBaseIntecConnection {

	/**
	 * 接続先URLを取得します
	 */
	@Override
	protected String getUrl() {
		String baseUrl = getBaseUrl();

		if (StringUtils.isBlank(baseUrl)) {
			return null;
		}

//		return "https://testapi.callnotes.jp/joffice/gourmetcaree/auth.xml";

		return String.format("%s/%s/%s/auth.xml", baseUrl, COMPANY_CD, SERVICE_CD);
	}

	@Override
	protected RequestMethod getMethod() {
		return RequestMethod.POST;
	}

	/**
	 * パラメータを取得します
	 */
	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();

		String authAccount  = ResourceUtil.getProperties("gourmetcaree.properties").getProperty("intec.api.auth.account");
		String authPassword = ResourceUtil.getProperties("gourmetcaree.properties").getProperty("intec.api.auth.password");

		if (StringUtils.isBlank(authAccount) || StringUtils.isBlank(authPassword)) {
			return null;
		}

		params.put("account", authAccount);
		params.put("password", authPassword);
//		params.put("account", "apigourmetcaree_dev");
//		params.put("password", "apigourmetcaree01!");
		return params;
	}



}
