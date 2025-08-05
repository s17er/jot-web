package com.gourmetcaree.db.common.service.dto;


/**
 * サービスにアクセスする顧客側ユーザのインタフェースです。
 * @author Takahiro Ando
 * @version 1.0
 */
public interface ServiceShopAccessUser extends ServiceAccessUser {

	/**
	 * エリアコードを取得
	 * @return
	 */
	public int getAreaCd();

	/**
	 * 顧客コードを取得
	 * @return
	 */
	public int getCustomerCd();

	/**
	 * なりすましログイン中かを取得する
	 */
	public boolean isMasqueradeFlg();
}
