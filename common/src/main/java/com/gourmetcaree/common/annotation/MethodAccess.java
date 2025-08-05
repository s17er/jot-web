package com.gourmetcaree.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Actionの@Executeを持つメソッドに付与するアノテーション<br>
 * メソッド単位のアクセス制御に使用します。
 *
 * @author Takahiro Ando
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface MethodAccess {
	/** メソッドのアクセス制御用コード */
	String accessCode();
}
