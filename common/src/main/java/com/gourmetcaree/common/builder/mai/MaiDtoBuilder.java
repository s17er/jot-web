package com.gourmetcaree.common.builder.mai;

/**
 * メール送信用DTOを作成するビルダ
 * @author nakamori
 *
 * @param <RESULT> ビルドをした時の結果として返す型
 */
public interface MaiDtoBuilder<RESULT> {

	/**
	 * メール送信用DTOのビルドを行います。
	 */
	public RESULT build();
}
