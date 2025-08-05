package com.gourmetcaree.common.exception;

/**
 * OpenIDの認証に失敗した場合にスローされる例外
 * @author nakamori
 *
 */
public class OpenIdAuthFailException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -5030911043111391317L;

	public OpenIdAuthFailException() {
	}

	public OpenIdAuthFailException(String message) {
		super(message);
	}

	public OpenIdAuthFailException(Throwable cause) {
		super(cause);
	}

	public OpenIdAuthFailException(String message, Throwable cause) {
		super(message, cause);
	}

}
