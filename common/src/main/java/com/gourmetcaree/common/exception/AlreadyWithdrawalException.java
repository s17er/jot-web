package com.gourmetcaree.common.exception;

/**
 * 有効期限の認証に失敗した場合を想定した例外
 * @author Makoto Otani
 *
 */
public class AlreadyWithdrawalException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1188612350677662283L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AlreadyWithdrawalException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AlreadyWithdrawalException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AlreadyWithdrawalException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AlreadyWithdrawalException(Throwable cause) {
		super(cause);
	}
}
