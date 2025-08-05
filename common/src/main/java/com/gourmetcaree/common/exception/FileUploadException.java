package com.gourmetcaree.common.exception;

public class FileUploadException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -6516949216670517790L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public FileUploadException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public FileUploadException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public FileUploadException(Throwable cause) {
		super(cause);
	}
}
