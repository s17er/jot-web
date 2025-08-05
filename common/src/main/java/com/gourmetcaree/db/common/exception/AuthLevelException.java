package com.gourmetcaree.db.common.exception;

/**
 * 権限の選択が不正な場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AuthLevelException extends Exception {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5979807936640448861L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AuthLevelException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AuthLevelException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AuthLevelException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AuthLevelException(Throwable cause) {
		super(cause);
	}
}
