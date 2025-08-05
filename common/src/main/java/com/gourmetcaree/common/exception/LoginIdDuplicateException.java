package com.gourmetcaree.common.exception;

/**
 * ログインIDが重複している場合にスローされます。<br>
 * ログイン時やアカウント登録時に発生します。
 * @author Hiroyuki Sugimoto
 */
public class LoginIdDuplicateException extends GourmetCareeServiceException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3759629910564778195L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public LoginIdDuplicateException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public LoginIdDuplicateException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public LoginIdDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public LoginIdDuplicateException(Throwable cause) {
		super(cause);
	}
}
