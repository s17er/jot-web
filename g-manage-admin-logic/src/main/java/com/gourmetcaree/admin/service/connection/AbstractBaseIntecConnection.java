package com.gourmetcaree.admin.service.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.seasar.framework.util.ResourceUtil;

import com.gourmetcaree.common.connection.AbstractBaseConnection;

/**
 * インテックAPIの基底コネクションクラス
 * ※インテック系のコネクションクラスはこのクラスを継承してください。
 *
 * @author hara
 *
 */
public abstract class AbstractBaseIntecConnection extends AbstractBaseConnection {

	/** サービスコード */
	protected static final String SERVICE_CD = "gourmetcaree";

	/** 会社コード */
	protected static final String COMPANY_CD = "joffice";

	/**
	 * 接続を行います。
	 * @throws IOException
	 * @throws DocumentException
	 */
	public Document connect() throws IOException, DocumentException {
		InputStream inputStream = null;
		try {

			inputStream = super.execute();
			SAXReader reader = new SAXReader();
			return reader.read(inputStream);

		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	@Override
	public InputStream execute(){
		throw new UnsupportedOperationException("connect() を使ってください。");
	}

	/**
	 * 接続先の
	 * @return
	 *
	 * @author hara
	 */
	protected String getBaseUrl() {
		return ResourceUtil.getProperties("gourmetcaree.properties").getProperty("intec.api.httpUrl");
	}


	@Override
	protected RequestMethod getMethod() {
		return RequestMethod.POST;
	}

//	@Override
//	protected Map<String, String> getRequestHeaders() {
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("Content-Type", "text/xml");
//
//		return headers;
//	}
}