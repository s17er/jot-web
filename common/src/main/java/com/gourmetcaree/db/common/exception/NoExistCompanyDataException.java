package com.gourmetcaree.db.common.exception;

/**
 * 会社のデータが存在しない場合にスローされます。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class NoExistCompanyDataException extends Exception {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8861631106614160922L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public NoExistCompanyDataException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public NoExistCompanyDataException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public NoExistCompanyDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public NoExistCompanyDataException(Throwable cause) {
		super(cause);
	}
}
