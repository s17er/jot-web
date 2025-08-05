package com.gourmetcaree.db.common.exception;

/**
 * 代理店権限の選択が不正な場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AgencyAuthLevelException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1870625645573391361L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AgencyAuthLevelException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AgencyAuthLevelException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AgencyAuthLevelException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AgencyAuthLevelException(Throwable cause) {
		super(cause);
	}
}
