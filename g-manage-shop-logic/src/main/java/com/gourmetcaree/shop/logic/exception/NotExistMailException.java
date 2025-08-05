package com.gourmetcaree.shop.logic.exception;

import java.util.List;

/**
 * メールが存在しない場合の例外
 * @author Takehiro Nakamori
 *
 */
public class NotExistMailException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 3866008308914573856L;

	/** 存在しないIDリスト */
	private List<Integer> notExistIdList;

	public NotExistMailException(List<Integer> notExistIdList) {
		super();
		this.notExistIdList = notExistIdList;
	}

	public NotExistMailException(String message, List<Integer> notExistIdList) {
		super(message);
		this.notExistIdList = notExistIdList;
	}

	public NotExistMailException(Throwable cause, List<Integer> notExistIdList) {
		super(cause);
		this.notExistIdList = notExistIdList;
	}

	public NotExistMailException(String message, Throwable cause, List<Integer> notExistIdList) {
		super(message, cause);
		this.notExistIdList = notExistIdList;
	}

	/**
	 * 存在しないIDリストの取得
	 * @return 存在しないIDリスト
	 */
	public List<Integer> getNotExistIdList() {
		return notExistIdList;
	}

}
