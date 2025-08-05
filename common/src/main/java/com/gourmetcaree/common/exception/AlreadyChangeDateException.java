package com.gourmetcaree.common.exception;

/**
 * データ編集時にすでにデータが変更されている場合を想定した例外されている場合を想定した例外
 * @author Makoto Otani
 *
 */
public class AlreadyChangeDateException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8280452985699890749L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AlreadyChangeDateException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AlreadyChangeDateException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AlreadyChangeDateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AlreadyChangeDateException(Throwable cause) {
		super(cause);
	}
}
