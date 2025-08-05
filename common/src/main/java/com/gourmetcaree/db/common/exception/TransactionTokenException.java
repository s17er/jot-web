package com.gourmetcaree.db.common.exception;

import java.io.Serializable;

/**
 * トランザクショントークンの判定で、
 * ダブルクリックなどの二重投稿がされた場合にスローされる例外です。
 *
 * @author Takehiro Nakamori
 *
 */
public class TransactionTokenException extends RuntimeException implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3314170287486023322L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public TransactionTokenException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public TransactionTokenException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public TransactionTokenException(Throwable cause) {
		super(cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public TransactionTokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
