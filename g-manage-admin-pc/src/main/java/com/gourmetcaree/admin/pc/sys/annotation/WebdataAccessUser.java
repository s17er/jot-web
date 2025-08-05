package com.gourmetcaree.admin.pc.sys.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * webデータアクセス用アノテーション
 * WEBデータ編集の、排他制御で使用します。
 * @author nakamori
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface WebdataAccessUser {

	/**
	 * アクセスタイプ
	 * @author nakamori
	 *
	 */
	public static enum AccessType {
		/** アクセスのチェック */
		CHECK_ACCESS,
		/** アクセス時間の更新 */
		UPDATE_TIME,
		/** アクセス情報の除去 */
		REMOVE
	}

	/** アクセスタイプ */
	AccessType accessType() default AccessType.CHECK_ACCESS;


}
