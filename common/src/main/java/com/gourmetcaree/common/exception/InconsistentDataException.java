package com.gourmetcaree.common.exception;

/**
 * DBデータを操作する際に、別ユーザなどにより対象データが変更、削除されてしまっていた場合を
 * 想定した例外
 * @author Takahiro Ando
 *
 */
public class InconsistentDataException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8102688949380816151L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public InconsistentDataException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public InconsistentDataException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public InconsistentDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public InconsistentDataException(Throwable cause) {
		super(cause);
	}
}
