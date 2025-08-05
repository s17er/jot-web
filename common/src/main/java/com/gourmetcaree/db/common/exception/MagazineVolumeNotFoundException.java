package com.gourmetcaree.db.common.exception;

/**
 * 誌面号数が見つからない場合にスローされます。
 * @author Hiroyuki Sugimoto
 */
public class MagazineVolumeNotFoundException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6749763604216175070L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public MagazineVolumeNotFoundException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public MagazineVolumeNotFoundException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public MagazineVolumeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public MagazineVolumeNotFoundException(Throwable cause) {
		super(cause);
	}
}
