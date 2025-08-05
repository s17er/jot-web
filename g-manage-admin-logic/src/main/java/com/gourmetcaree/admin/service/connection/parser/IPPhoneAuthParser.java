package com.gourmetcaree.admin.service.connection.parser;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.gourmetcaree.common.parser.AbstractXmlParser;

/**
 * 認証XMLパーサ
 * @author nakamori
 *
 */
public class IPPhoneAuthParser extends AbstractXmlParser {

//
//	/**
//	 * パースを行います。
//	 */
//	public List<IntecClientRegisterDto> parse(InputStream inputStream) throws DocumentException {
//		Document docment = createDocumentFromInputStream(inputStream);
//		List<IntecClientRegisterDto> dtoList = new ArrayList<IntecClientRegisterDto>();
//		parse(docment, dtoList);
//		return dtoList;
//	}

	/**
	 * パースを行います
	 * @param document
	 * @param dtoList
	 * @throws ParseException
	 */
	public void parse(Document document, IntecAuthDto dto) throws ParseException {
		for (Iterator<?> it = document.getRootElement().elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String name = element.getName();
			if ("expires".equals(name)) {
				// ISO-8601形式を日付にフォーマット
				String datetimeStr = String.valueOf(element.getData());
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
				dto.datetime = parseDate(datetimeStr);

			} else if ("token".equals(name)) {
				dto.token = String.valueOf(element.getData());
			}
		}
	}

//	/**
//	 * authのパースを行います。
//	 * @throws ParseException
//	 */
//	private IntecAuthDto parseAuth(Element client) throws ParseException {
//
//		IntecAuthDto dto = new IntecAuthDto();
//		for (Iterator<?> it = client.elementIterator(); it.hasNext();) {
//			Element element = (Element) it.next();
//			String name = element.getName();
//
//			if ("expires".equals(name)) {
//				// ISO-8601形式を日付にフォーマット
//				String datetimeStr = String.valueOf(element.getData());
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//				dto.datetime = df.parse(datetimeStr);
//
//			} else if ("token".equals(name)) {
//				dto.token = String.valueOf(element.getData());
//			}
//		}
//		return dto;
//	}

	/**
	 * 認証成功時に返却されるXMLのDTO
	 * @author hara
	 *
	 */
	public static class IntecAuthDto implements Serializable {

		private static final long serialVersionUID = 4679709712147066012L;

		/** 認証トークン期限 */
		private Date datetime;

		/** 認証トークン */
		private String token;

		public Date getDatetime() {
			return datetime;
		}

		public String getToken() {
			return token;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}


	}

	public static Date parseDate(String dateString){
		Date date = null;
		try {
			date = DateUtils.parseDate(dateString, DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
		} catch (ParseException e) {
				e.printStackTrace();
		}
		return date;
	}
}
