package com.gourmetcaree.common.util;

import jp.co.whizz_tech.commons.WztDigestUtil;

/**
 * グルメキャリーで扱うダイジェスト用のユーティリティ
 * @author Takahiro Ando
 *
 */
public final class DigestUtil {

	/**
	 * インスタンスは生成できません
	 */
	private DigestUtil(){};

	/**
	 * ダイジェストを作成します。
	 * @param str
	 * @return
	 */
	public static String createDigest(String str) {
		return WztDigestUtil.createDigest(str, WztDigestUtil.Algorithm.SHA1);
	}

	/**
	 * ダイジェストと文字列が一致するかを判定します。
	 * @param digest
	 * @param str
	 * @return true:一致, false:不一致
	 */
	public static boolean isSame(String digest, String str) {
		return digest.equals(WztDigestUtil.createDigest(str, WztDigestUtil.Algorithm.SHA1));
	}
}
