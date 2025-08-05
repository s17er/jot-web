package com.gourmetcaree.common.exception;

/**
 * 有効期限の認証に失敗した場合を想定した例外
 * @author Makoto Otani
 *
 */
public class OutOfDateException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6766818025962739795L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public OutOfDateException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public OutOfDateException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public OutOfDateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public OutOfDateException(Throwable cause) {
		super(cause);
	}
}
