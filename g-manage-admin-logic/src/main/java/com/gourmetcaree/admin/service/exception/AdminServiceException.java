package com.gourmetcaree.admin.service.exception;

import com.gourmetcaree.common.exception.GourmetCareeServiceException;

/**
 * 運営者側管理のサービスで発生した例外のスーパークラスです。<br>
 * このクラスを継承する例外クラスは、呼び出し側で適切な処理を行って下さい。
 * @author Hiroyuki Sugimoto
 * @version 1.0
 */
public class AdminServiceException extends GourmetCareeServiceException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 863990914599353456L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AdminServiceException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AdminServiceException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AdminServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AdminServiceException(Throwable cause) {
		super(cause);
	}
}
