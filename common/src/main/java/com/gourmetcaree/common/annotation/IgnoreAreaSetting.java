package com.gourmetcaree.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * エリア設定を無視するアノテーション
 * インタセプタなどでエリアの自動設定を防ぐ目的で作成
 * @author Takehiro Nakamori
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface IgnoreAreaSetting {

}
