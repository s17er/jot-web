package com.gourmetcaree.common.exception;

/**
 * 事前登録の募集期間外を想定した例外
 * @author Makoto Otani
 *
 */
public class AdvancedRegistrationOutOfPeriodException extends RuntimeException {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6756818125162739795L;


	private Integer id;

	/**
	 * 詳細メッセージにnullを使用して例外を構築します。
	 */
	public AdvancedRegistrationOutOfPeriodException() {
		super();
	}


	public AdvancedRegistrationOutOfPeriodException(String message, Integer id) {
		super(message);
		this.id = id;
	}

	/**
	 * 指定された詳細メッセージを使用して例外を構築します。
	 * @param message
	 */
	public AdvancedRegistrationOutOfPeriodException(String message) {
		super(message);
	}

	/**
	 * 指定された詳細メッセージと原因を使用して例外を構築します。
	 * @param message
	 * @param cause
	 */
	public AdvancedRegistrationOutOfPeriodException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 指定された原因を使用して例外を構築します。
	 * @param cause
	 */
	public AdvancedRegistrationOutOfPeriodException(Throwable cause) {
		super(cause);
	}


	public Integer getId() {
		return id;
	}
}
