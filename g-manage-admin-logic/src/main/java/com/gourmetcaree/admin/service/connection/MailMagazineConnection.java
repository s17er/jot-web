package com.gourmetcaree.admin.service.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.util.ResourceUtil;

import com.gourmetcaree.common.connection.AbstractBaseConnection;
import com.gourmetcaree.common.constants.GourmetCareeConstants;

/**
 * メルマガ送信の公開側APIに接続するためのクラスです。
 *
 * @author whizz
 *
 */
public class MailMagazineConnection extends AbstractBaseConnection {

	private String token;

	private int mailMagazineId;



	public MailMagazineConnection(String token, int mailMagazineId) {
		super();
		this.token = token;
		this.mailMagazineId = mailMagazineId;
	}


	@Override
	protected String getUrl() {
		String baseUrl = ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gc.realPath");

		if (StringUtils.isBlank(baseUrl)) {
			return null;
		}

		return String.format("%s/api/mailMagazine/send/%s", baseUrl, mailMagazineId);
	}

	@Override
	protected Map<String, String> getRequestHeaders() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(GourmetCareeConstants.MAILMAGAZINE_ACCESS_TOKEN_KEY, token);
		return map;
	}

}
