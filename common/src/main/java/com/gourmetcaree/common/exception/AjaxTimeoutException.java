package com.gourmetcaree.common.exception;

/**
 * Ajaxでのリクエストのセッションタイムアウト発生時にスローされる例外クラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AjaxTimeoutException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4828812203836389963L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AjaxTimeoutException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AjaxTimeoutException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AjaxTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AjaxTimeoutException(Throwable cause) {
		super(cause);
	}
}
