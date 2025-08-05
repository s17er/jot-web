package com.gourmetcaree.common.exception;

/**
 * DBデータを操作する際の不正なプロセスを
 * 想定した例外
 * @author Takahiro Kimura
 *
 */
public class PageNotFoundException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8248332274676897377L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public PageNotFoundException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public PageNotFoundException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public PageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public PageNotFoundException(Throwable cause) {
		super(cause);
	}
}
