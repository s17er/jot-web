package com.gourmetcaree.admin.service.connection.parser;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.gourmetcaree.admin.service.connection.parser.IPPhoneClientListParser.IntecClientRegisterDto;
import com.gourmetcaree.common.parser.AbstractXmlParser;

/**
 * IP電話登録のXMLパーサ
 * @author nakamori
 *
 */
public class IPPhoneClientParser extends AbstractXmlParser {


	/**
	 * パースを行います
	 * @param document
	 * @param dtoList
	 */
	public void parse(Document document, IntecClientRegisterDto dto) {
		for (Iterator<?> it = document.getRootElement().elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String name = element.getName();
			if ("publish".equals(name)) {
				parsePublish(element, dto);
			} else if ("client_cd".equals(name)) {
				dto.setClientCd(String.valueOf(element.getData()));
			}
		}
	}

	/**
	 * publish のパースを行います。
	 */
	private void parsePublish(Element publish, IntecClientRegisterDto dto) {
		dto.setIpTel(String.valueOf(publish.element("number").getData()));
	}

}
