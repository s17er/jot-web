package com.gourmetcaree.common.exception;

/**
 * 担当エラー発生時にスローされる例外クラスです。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class NoHandlingPermissionException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2092155176899137097L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public NoHandlingPermissionException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public NoHandlingPermissionException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public NoHandlingPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public NoHandlingPermissionException(Throwable cause) {
		super(cause);
	}
}
