package com.gourmetcaree.common.exception;

/**
 * アクセスエラー発生時にスローされる例外クラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AuthorizationException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2092155176899137097L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AuthorizationException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AuthorizationException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AuthorizationException(Throwable cause) {
		super(cause);
	}
}
