package com.gourmetcaree.common.exception;

/**
 * 何らかの理由で自動ログインに失敗した場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AutoLoginFailedException extends GourmetCareeServiceException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7480153110527336535L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AutoLoginFailedException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AutoLoginFailedException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AutoLoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AutoLoginFailedException(Throwable cause) {
		super(cause);
	}
}
