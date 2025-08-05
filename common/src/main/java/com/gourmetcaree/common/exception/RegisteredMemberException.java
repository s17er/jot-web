package com.gourmetcaree.common.exception;

/**
 * 仮会員が本会員登録をする時に、既に登録されている場合にスローされる例外
 *
 * 会員登録はすでに完了しております。
MYページへログインしてご確認ください。
 * @author nakamori
 *
 */
public class RegisteredMemberException extends RuntimeException {


	/**
	 *
	 */
	private static final long serialVersionUID = 4391150575479977363L;


	public RegisteredMemberException(String message) {
		super(message);
	}


	public RegisteredMemberException(String message, Throwable cause) {
		super(message, cause);
	}

}
