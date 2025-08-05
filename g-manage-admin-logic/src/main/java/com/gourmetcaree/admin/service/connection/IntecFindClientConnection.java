package com.gourmetcaree.admin.service.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * インテックのAPIの
 * クライアント詳細表示のクラスです。
 *
 * @author hara
 *
 */
public class IntecFindClientConnection extends AbstractBaseIntecConnection {

	/** 認証トークン */
	private String token;

	/** クライアントコード */
	private String clientCd;

	/**
	 * コンストラクタ
	 * @param ipPhoneList
	 */
	public IntecFindClientConnection(String clientCd, String token) {
		this.clientCd = clientCd;
		this.token = token;
	}

	/**
	 * 接続先URLを取得します
	 */
	@Override
	protected String getUrl() {

		String baseUrl = getBaseUrl();

		if (StringUtils.isBlank(baseUrl)) {
			return null;
		}

		return String.format("%s/%s/%s/clients/%s.xml", baseUrl, COMPANY_CD, SERVICE_CD, clientCd);
	}

	@Override
	protected RequestMethod getMethod() {
		return RequestMethod.GET;
	}

	/**
	 * パラメータを取得します
	 */
	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("token", token);

		return params;
	}

}
