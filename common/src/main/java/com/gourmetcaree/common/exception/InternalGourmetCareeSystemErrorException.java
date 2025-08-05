package com.gourmetcaree.common.exception;

/**
 * プログラム内部で発生したユーザ操作と関係のないエラー用の例外クラスです。
 * BasicEsceptonHandlerに渡すためインターセプトは行わないようにしてください。
 * @author Takahiro Ando
 *
 */
public class InternalGourmetCareeSystemErrorException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1672736776838800177L;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public InternalGourmetCareeSystemErrorException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public InternalGourmetCareeSystemErrorException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public InternalGourmetCareeSystemErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public InternalGourmetCareeSystemErrorException(Throwable cause) {
		super(cause);
	}
}
