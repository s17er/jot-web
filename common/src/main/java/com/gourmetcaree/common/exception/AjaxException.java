package com.gourmetcaree.common.exception;

/**
 * Ajaxでのリクエストのエラー発生時にスローされる例外クラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class AjaxException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7405311039860354257L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AjaxException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AjaxException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AjaxException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AjaxException(Throwable cause) {
		super(cause);
	}
}
