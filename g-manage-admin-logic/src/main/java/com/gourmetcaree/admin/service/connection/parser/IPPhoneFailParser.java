package com.gourmetcaree.admin.service.connection.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.gourmetcaree.common.parser.AbstractXmlParser;

/**
 * IP電話登録に失敗した時のパーサ
 * @author nakamori
 *
 */
public class IPPhoneFailParser extends AbstractXmlParser {

	/**
	 * パースを行います。
	 */
	public List<String> parse(InputStream inputStream) throws DocumentException {
		Document document = createDocumentFromInputStream(inputStream);
		List<String> clientCdList = new ArrayList<String>();
		parse(document, clientCdList);
		return clientCdList;
	}


	/**
	 * パースを行います。
	 */
	public void parse(Document document, List<String> clientCdList) {
		for (Iterator<?> it = document.getRootElement().elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String name = element.getName();
			if ("client".equals(name)) {
				clientCdList.add(parseClient(element));
			}
		}
	}

	/**
	 * clientタグをパースします。
	 * @return クライアントコード
	 */
	private String parseClient(Element client) {
		return String.valueOf(client.element("client_cd").getData());
	}


}
