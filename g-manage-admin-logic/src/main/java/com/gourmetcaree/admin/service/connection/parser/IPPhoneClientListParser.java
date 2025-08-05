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

import com.gourmetcaree.common.parser.AbstractXmlParser;

/**
 * IP電話登録のXMLパーサ
 * @author nakamori
 *
 */
public class IPPhoneClientListParser extends AbstractXmlParser {


	/**
	 * パースを行います。
	 */
	public List<IntecClientRegisterDto> parse(InputStream inputStream) throws DocumentException {
		Document docment = createDocumentFromInputStream(inputStream);
		List<IntecClientRegisterDto> dtoList = new ArrayList<IntecClientRegisterDto>();
		parse(docment, dtoList);
		return dtoList;
	}

	/**
	 * パースを行います
	 * @param document
	 * @param dtoList
	 */
	public void parse(Document document, List<IntecClientRegisterDto> dtoList) {
		for (Iterator<?> it = document.getRootElement().elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String name = element.getName();
			if ("client".equals(name)) {
				dtoList.add(parseClient(element));
			}
		}
	}

	/**
	 * clientのパースを行います。
	 */
	private IntecClientRegisterDto parseClient(Element client) {

		IntecClientRegisterDto dto = new IntecClientRegisterDto();
		for (Iterator<?> it = client.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String name = element.getName();

			if ("publish".equals(name)) {
				parsePublish(element, dto);
			} else if ("client_cd".equals(name)) {
				dto.clientCd = String.valueOf(element.getData());
			}
		}
		return dto;
	}

	/**
	 * publish のパースを行います。
	 */
	private void parsePublish(Element publish, IntecClientRegisterDto dto) {
		dto.ipTel = String.valueOf(publish.element("number").getData());
	}


	/**
	 * 登録成功時に返却されるXMLのDTO
	 * @author nakamori
	 *
	 */
	public static class IntecClientRegisterDto implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = -3109888669662373403L;

		/** IP電話番号 */
		private String ipTel;

		/** クライアントコード */
		private String clientCd;

		public void setIpTel(String ipTel) {
			this.ipTel = ipTel;
		}

		public void setClientCd(String clientCd) {
			this.clientCd = clientCd;
		}

		public String getIpTel() {
			return ipTel;
		}

		public String getClientCd() {
			return clientCd;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}
}
