package com.gourmetcaree.common.util;

import java.util.Properties;

import org.seasar.framework.util.ResourceUtil;

/**
 * プロパティユーティル
 * @author nakamori
 *
 */
public class PropertiesUtil {

	/** goumetcaree.properties */
	private static final Properties GOURMET_PROPERTIES = ResourceUtil.getProperties("gourmetcaree.properties");


	private PropertiesUtil() {}

	/**
	 * gourmetcaree.propertiesからプロパティを取得
	 * @param key キー
	 */
	public static final String getGourmetCareeProperty(String key) {
		return GOURMET_PROPERTIES.getProperty(key);
	}
	
	/**
	 * generalPropertyの取得
	 * @param key キー
	 */
	public static String getGeneralProperty(String key) {
		return ResourceUtil.getProperties("general.properties")
					.getProperty(key);
	}
}
