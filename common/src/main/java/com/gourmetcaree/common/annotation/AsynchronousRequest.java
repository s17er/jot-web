package com.gourmetcaree.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 非同期通信で使用されるActionを示す<br />
 * アクションクラスに対して指定をして下さい。<br />
 * このアノテーションは自動ログイン時のロジック分岐条件などで使用されます。
 * @author Takahiro Ando
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface AsynchronousRequest {
}
