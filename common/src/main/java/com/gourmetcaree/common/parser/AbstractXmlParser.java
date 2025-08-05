package com.gourmetcaree.common.parser;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * XMLパーサの基底クラス
 * @author nakamori
 *
 */
public abstract class AbstractXmlParser {


	/**
	 * InputStreamからドキュメントを生成
	 * @throws DocumentException ドキュメント変換に失敗した場合
	 */
	protected Document createDocumentFromInputStream(InputStream inputStream) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(inputStream);
	}
}
