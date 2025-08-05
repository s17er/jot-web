package com.gourmetcaree.common.exception;

/**
 * 何らかの理由でログインに失敗した場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class LoginFailedException extends GourmetCareeServiceException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5361257518873980104L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public LoginFailedException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public LoginFailedException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public LoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public LoginFailedException(Throwable cause) {
		super(cause);
	}
}
