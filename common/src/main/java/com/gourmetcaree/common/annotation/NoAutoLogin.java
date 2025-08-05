package com.gourmetcaree.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * オートログイン対象外を示す<br>
 * アクションクラスに対して指定をして下さい。
 * @author Takahiro Ando
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface NoAutoLogin {
}
