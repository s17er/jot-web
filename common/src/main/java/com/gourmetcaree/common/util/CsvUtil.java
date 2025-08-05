package com.gourmetcaree.common.util;

import java.util.List;


/**
 * グルメキャリーCSVに関するユーティリティクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public final class CsvUtil {

	/** 改行文字列 */
	public static final String BREAK = "(\r|\n)";

	/** 全角スペース */
	public static final String ZENKAKU_SPACE = "　";

	/** 区切り文字 */
	public static final String DELIMITER = ",";

	/** エスケープ区切り文字 */
	public static final String ESC_DELIMITER  = "\\,";

	/** ダブルクォーテーション */
	public static final String DOUBLE_QUOTATION = "\"";

	/** エスケープダブルクォーテーション */
	public static final String ESC_DOUBLE_QUOTATION = "\"\"";

	/** 生成不可 */
	private CsvUtil() {}

	/**
	 * 改行を全角スペースに変換します。
	 * @param target 対象文字列
	 * @return 変換後文字列
	 */
	public static String convertBreakToBrTag(String target) {
		return target == null ? "" : target.replaceAll(BREAK, ZENKAKU_SPACE);
	}

	/**
	 * デリミタをエスケープします。
	 * @param target 対象文字列
	 * @return エスケープ後文字列
	 */
	public static String escapeDelimiter(String target) {
		return target == null ? "" : target.replaceAll(DELIMITER, ESC_DELIMITER);
	}

	/**
	 * ダブルクォーテーションをエスケープします。
	 * @param target 対象文字列
	 * @return エスケープ後文字列
	 */
	public static String escapeDoubleQuot(String target) {
		return target == null ? "" : target.replaceAll(DOUBLE_QUOTATION, ESC_DOUBLE_QUOTATION);
	}

	/**
	 * "（ダブルクォーテーション)で囲む。
	 * @param target 対象文字列
	 * @return
	 */
	public static String nipStrByDbQuot(String target) {
		return target == null ? "" : DOUBLE_QUOTATION + target + DOUBLE_QUOTATION;
	}

	/**
	 * 文字列のリストから"で囲ったカンマ区切りの文字列を生成して返す
	 * @param strList 文字列リスト
	 * @return カンマ区切り文字列
	 */
	public static String createDelimiterStr(List<String> strList) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strList.size(); i++) {

			if (i == 0) {
				sb.append(nipStrByDbQuot(escapeDoubleQuot(strList.get(i))));
			} else {
				sb.append(DELIMITER);
				sb.append(nipStrByDbQuot(escapeDoubleQuot(strList.get(i))));
			}
		}

		return sb.toString();
	}

	/**
	 * オブジェクトをカンマを付けた文字列にして返します。
	 * @param obj 変換するオブジェクト
	 * @return カンマを付けた文字列
	 */
	public static String convertCommaStr(Object obj) {
		return obj == null ? "," : String.valueOf(obj) + ",";
	}
}
