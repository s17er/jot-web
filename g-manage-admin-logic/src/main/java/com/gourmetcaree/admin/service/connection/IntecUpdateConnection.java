package com.gourmetcaree.admin.service.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.admin.service.connection.IntecCreateConnection.ClientData;
import com.gourmetcaree.db.common.entity.TWebIpPhone;

/**
 * インテックのAPIに接続するためのクラスです。
 *
 * @author hara
 *
 */
public class IntecUpdateConnection extends AbstractBaseIntecConnection {

	/** 認証トークン */
	private String token;

	/** IP電話データ */
	private ClientData ipPhone;

	/**
	 * コンストラクタ
	 * @param ipPhoneList
	 */
	public IntecUpdateConnection(ClientData ipPhone, String token) {
		this.ipPhone = ipPhone;
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

		return String.format("%s/%s/%s/clients/update/%s.xml", baseUrl, COMPANY_CD, SERVICE_CD, ipPhone.clientCd);
	}

	/**
	 * パラメータを取得します
	 */
	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("client_number", ipPhone.customerTel);
		params.put("name", ipPhone.customerName);
		params.put("token", token);

		return params;
	}



}
