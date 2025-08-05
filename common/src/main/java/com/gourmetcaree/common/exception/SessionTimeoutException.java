package com.gourmetcaree.common.exception;

/**
 * セッションタイムアウト発生時にスローされる例外クラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class SessionTimeoutException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8144730428737552005L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public SessionTimeoutException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public SessionTimeoutException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public SessionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public SessionTimeoutException(Throwable cause) {
		super(cause);
	}
}
