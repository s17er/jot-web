package com.gourmetcaree.common.exception;

/**
 * グルメキャリーのサービスまたはロジックで発生した例外のスーパークラスです。<br>
 * このクラスを継承する例外クラスは、呼び出し側で適切な処理を行って下さい。
 * @author Hiroyuki Sugimoto
 * @version 1.0
 */
public class GourmetCareeServiceException extends Exception {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1806855730420504424L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public GourmetCareeServiceException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public GourmetCareeServiceException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public GourmetCareeServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public GourmetCareeServiceException(Throwable cause) {
		super(cause);
	}
}
