package com.gourmetcaree.common.builder.html;

/**
 *
 * htmlのhead部に、next/prev のタグを生成するビルダ
 *
 * &lt;link rel="next" href="http://www.example.com/article?story=abc&page=2" /&gt;
 * &lt;link rel="prev" href="http://www.example.com/article?story=abc&page=1" /&gt;
 * @author nakamori
 *
 */
public interface LinkNavigationTagBuilder {

	/**
	 * 次ページがあるか
	 */
	boolean hasNext();

	/**
	 * 次ページのタグを取得
	 */
	String getNext();

	/**
	 * 前ページがあるか
	 */
	boolean hasPrev();

	/**
	 * 前ページのタグを作成
	 */
	String getPrev();

}
