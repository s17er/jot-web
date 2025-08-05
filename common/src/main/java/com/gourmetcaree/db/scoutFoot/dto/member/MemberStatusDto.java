package com.gourmetcaree.db.scoutFoot.dto.member;

import java.io.Serializable;


/**
 * 会員の状態のDTO
 * @author Takahiro Kimura
 * @version 1.0
 */
public class MemberStatusDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3084999216162977019L;

	/** 会員ID */
	public Integer id;

	/** スカウトフラグ */
	public Integer scoutFlg;

	/** 応募フラグ */
	public Integer applicationFlg;

	/** 足あとフラグ */
	public Integer footprintFlg;

	/** 検討中フラグ */
	public Integer considerationFlg;

	/** スカウトブロックフラグ */
	public Integer scoutBlockFlg;

	/** スカウト受取区分 */
	public Integer scoutReceiveKbn;

	/**
	 * スカウトブロックフラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class ScoutBlockFlgKbn {

		/** スカウトブロック設定なし */
		public static final int NOT_BLOCK = 0;

		/** スカウトブロック設定あり */
		public static final int BLOCK = 1;

	}

	/**
	 * キープ済みフラグの定義です。
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static class ConsiderationFlgKbn {

		/** 未キープ */
		public static final int NOT_KEEP = 0;

		/** キープ済み */
		public static final int KEEP = 1;

	}


}