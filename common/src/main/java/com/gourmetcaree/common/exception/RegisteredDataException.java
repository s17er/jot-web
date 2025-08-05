package com.gourmetcaree.common.exception;

/**
 * データ登録時にすでにデータが登録されている場合を想定した例外
 * @author Makoto Otani
 *
 */
public class RegisteredDataException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8280452985699890749L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public RegisteredDataException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public RegisteredDataException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public RegisteredDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public RegisteredDataException(Throwable cause) {
		super(cause);
	}
}
