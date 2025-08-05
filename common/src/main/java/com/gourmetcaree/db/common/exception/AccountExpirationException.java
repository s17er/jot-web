package com.gourmetcaree.db.common.exception;

/**
 * アカウントの期間が無効の場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AccountExpirationException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1785465098976555138L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AccountExpirationException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AccountExpirationException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AccountExpirationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AccountExpirationException(Throwable cause) {
		super(cause);
	}
}
