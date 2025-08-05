package com.gourmetcaree.db.common.exception;

/**
 * 店舗のデータが存在しない場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class VNoResultException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -998628169520465648L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public VNoResultException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public VNoResultException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public VNoResultException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public VNoResultException(Throwable cause) {
		super(cause);
	}
}
