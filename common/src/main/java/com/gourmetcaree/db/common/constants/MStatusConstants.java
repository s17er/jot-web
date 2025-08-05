package com.gourmetcaree.db.common.constants;

/**
 *ステータスマスタの定義ファイル
 * @author Makoto Otani
 * @version 1.0
 *
 */
public interface MStatusConstants {

	/**
	 * DBステータスコードの値を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class DBStatusCd {

		/** ステータスコード */
		public static final String STATUS_KBN = "1";

		/** 下書き */
		public static final Integer DRAFT = 1;

		/** 承認中 */
		public static final Integer REQ_APPROVAL = 2;

		/** 掲載確定 */
		public static final Integer POST_FIXED = 3;

		/** 掲載終了 */
		public static final Integer POST_END = 5;

	}

	/**
	 * 画面表示ステータスコードの値を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class DisplayStatusCd {

		/** ステータスコード */
		public static final String STATUS_KBN = "2";

		/** 下書き */
		public static final Integer DRAFT = 1;

		/** 承認中 */
		public static final Integer REQ_APPROVAL = 2;

		/** 掲載待ち */
		public static final Integer POST_WAIT = 3;

		/** 掲載中 */
		public static final Integer POST_DURING = 4;

		/** 掲載終了 */
		public static final Integer POST_END = 5;

	}
}
