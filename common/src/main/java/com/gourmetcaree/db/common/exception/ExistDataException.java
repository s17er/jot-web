package com.gourmetcaree.db.common.exception;

/**
 * 店舗のデータが存在しない場合にスローされます。
 * @author Takahiro Ando
 * @version 1.0
 */
public class ExistDataException extends Exception {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2638791366481681053L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public ExistDataException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public ExistDataException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public ExistDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public ExistDataException(Throwable cause) {
		super(cause);
	}
}
