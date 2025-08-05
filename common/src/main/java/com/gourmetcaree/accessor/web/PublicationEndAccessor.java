package com.gourmetcaree.accessor.web;

import java.util.Date;

/**
 * 掲載終了かどうかを判定するために必要な情報にアクセスするアクセサ
 * @author nakamori
 *
 */
public interface PublicationEndAccessor {

	/**
	 * 掲載終了フラグの取得
	 */
	Integer getPublicationEndDisplayFlg();

	/**
	 * 顧客IDの取得
	 */
	Integer getCustomerId();

	/**
	 * エリアコードの取得
	 */
	Integer getAreaCd();

	/**
	 * 掲載開始日の取得
	 */
	Date getPostStartDatetime();

	/**
	 * 掲載終了日の取得
	 */
	Date getPostEndDatetime();
}
