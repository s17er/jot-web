package com.gourmetcaree.shop.pc.sys.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.gourmetcaree.common.util.PropertiesUtil;



/**
 * 店舗PCユーティリティ
 * @author nakamori
 *
 */
public class ShopPcUtil {
	private static final Logger log = Logger.getLogger(ShopPcUtil.class);


	private ShopPcUtil() {}

	/**
	 * インデックスのインクルードファイルを読み込みます
	 * @return
	 */
	public static String readIndexIncludeFile() {
		String path = PropertiesUtil.getGourmetCareeProperty("gc.indexBannerPath");

		File file = new File(path);
		if (!file.exists()) {
			return "";
		}

		FileInputStream input = null;
		try {
			byte[] data = new byte[(int) file.length()];
			input = new FileInputStream(file);
			input.read(data);

			return new String(data, "UTF-8");
		} catch (IOException e) {
			log.warn(String.format("ファイルの読み込み時にエラーが発生しました。パス：[%s]", path), e);
			return "";
		} finally {
			if (input != null) {
				IOUtils.closeQuietly(input);
			}
		}
	}

	public static String readPriceIncludeFile() {
		String path = PropertiesUtil.getGourmetCareeProperty("gc.priceValuePath");

		File file = new File(path);
		if (!file.exists()) {
			return "";
		}

		FileInputStream input = null;
		try {
			byte[] data = new byte[(int) file.length()];
			input = new FileInputStream(file);
			input.read(data);

			return new String(data, "UTF-8");
		} catch (IOException e) {
			log.warn(String.format("ファイルの読み込み時にエラーが発生しました。パス：[%s]", path), e);
			return "";
		} finally {
			if (input != null) {
				IOUtils.closeQuietly(input);
			}
		}
	}
}
