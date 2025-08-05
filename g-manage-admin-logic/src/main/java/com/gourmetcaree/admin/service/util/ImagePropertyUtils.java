package com.gourmetcaree.admin.service.util;

import java.util.Properties;

import org.seasar.framework.util.ResourceUtil;

public class ImagePropertyUtils {

	/**
	 * 画像用プロパティファイルの取得
	 * @return
	 */
	private static Properties getProperties() {
		return ResourceUtil.getProperties("image.properties");
	}

	/**
	 * PCのキャッシュフォルダ名を取得します。
	 * @return
	 */
	public static String getPcCacheFolder() {
		return getProperties().getProperty("gc.cache.front.image.pc.dir.path");
	}

	/**
	 * 携帯のキャッシュフォルダ名を取得します。
	 * @return
	 */
	public static String getMobileCacheFolder() {
		return getProperties().getProperty("gc.cache.front.image.mobile.dir.path");
	}

	/**
	 * スマホのキャッシュフォルダ名を取得します。
	 * @return
	 */
	public static String getSmartPhoneCacheFolder() {
		return getProperties().getProperty("gc.cache.image.smart.dir.path");
	}

}
