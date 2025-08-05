package com.gourmetcaree.db.common.exception;

/**
 * ユーザが存在しない場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class UserNotFoundException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1361426349915286674L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public UserNotFoundException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public UserNotFoundException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public UserNotFoundException(Throwable cause) {
		super(cause);
	}
}
