package com.gourmetcaree.common.enums;

/**
 * 管理画面側、運営者の権限レベルを定義します。
 * @author Takahiro Ando
 * @version 1.0
 */
public enum ManageAuthLevel {

	/** 管理者 */
	ADMIN("1"),

	/** 自社スタッフ */
	STAFF("2"),

	/** 代理店 */
	AGENCY("3"),

	/** 他社スタッフ */
	OTHER("4"),

	/** 営業権限 */
	SALES("5"),

	/** なし */
	NONE("99")
	;

	/** 値 */
	private String value;

	/**
	 * このクラスのオブジェクトを構築します。
	 * @param value 値
	 */
	private ManageAuthLevel(String value) {
		this.value = value;
	}

	/**
	 * 値を返します。
	 * @return 値
	 */
	public String value() {
		return value;
	}


	/**
	 * 値からenumを取得
	 */
	public static ManageAuthLevel getEnumFromValue(String value) {
		for (ManageAuthLevel level : values()) {
			if(level.value.equals(value)) {
				return level;
			}
		}
		return null;
	}


	/**
	 * 管理者かどうか
	 */
	public static boolean isAdmin(String value) {
		return ADMIN == getEnumFromValue(value);
	}

	/**
	 * 自社スタッフかどうか
	 */
	public static boolean isStaff(String value) {
		return STAFF == getEnumFromValue(value);
	}

	/**
	 * 代理店かどうか
	 */
	public static boolean isAgency(String value) {
		return AGENCY == getEnumFromValue(value);
	}

	/**
	 * 営業権限かどうか
	 */
	public static boolean isSales(String value) {
		return SALES == getEnumFromValue(value);
	}

	/**
	 * 他社スタッフかどうか
	 */
	public static boolean isOther(String value) {
		return OTHER == getEnumFromValue(value);
	}
}
