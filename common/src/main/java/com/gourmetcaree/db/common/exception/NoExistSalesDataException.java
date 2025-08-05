package com.gourmetcaree.db.common.exception;

/**
 * 営業担当者のデータが存在しない場合にスローされます。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class NoExistSalesDataException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8180330888502107200L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public NoExistSalesDataException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public NoExistSalesDataException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public NoExistSalesDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public NoExistSalesDataException(Throwable cause) {
		super(cause);
	}
}
