package com.gourmetcaree.common.exception;


/**
 * 指定のレコードが存在しないことを示す例外クラスです。
 * @author Hiroyuki Sugimoto
 */
public class RecordNotFoundException extends GourmetCareeServiceException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3089438476893482901L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public RecordNotFoundException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public RecordNotFoundException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public RecordNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public RecordNotFoundException(Throwable cause) {
		super(cause);
	}
}
