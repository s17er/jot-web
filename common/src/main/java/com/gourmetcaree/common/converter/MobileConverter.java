package com.gourmetcaree.common.converter;

import jp.co.whizz_tech.commons.WztCp932Util;
import jp.co.whizz_tech.commons.WztKatakanaUtil;

import org.seasar.framework.beans.Converter;

/**
 * 携帯用のコンバータです。<br>
 * 全角カタカナを半角カタカナに、WAVE DASH問題の文字コードを変換します。
 * @author Takahiro Ando
 * @version 1.0
 */
public class MobileConverter implements Converter {

	/*
	 * (非 Javadoc)
	 * @see org.seasar.framework.beans.Converter#getAsObject(java.lang.String)
	 */
	@Override
	public Object getAsObject(String value) {
		return WztCp932Util.toCp932(WztKatakanaUtil.hankakuToZenkaku(value));
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.framework.beans.Converter#getAsString(java.lang.Object)
	 */
	@Override
	public String getAsString(Object value) {
		return WztCp932Util.toJIS(WztKatakanaUtil.zenkakuToHankaku(value.toString()));
	}

	/*
	 * (非 Javadoc)
	 * @see org.seasar.framework.beans.Converter#isTarget(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean isTarget(Class clazz) {
		return clazz == String.class;
	}

}
