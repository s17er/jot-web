package com.gourmetcaree.common.exception;


/**
 * 画像出力時のキャッチ例外クラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class FileWriteErrorException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6248933450179063112L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public FileWriteErrorException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public FileWriteErrorException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public FileWriteErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public FileWriteErrorException(Throwable cause) {
		super(cause);
	}
}
