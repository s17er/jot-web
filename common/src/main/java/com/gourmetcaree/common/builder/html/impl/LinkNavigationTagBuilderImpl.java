package com.gourmetcaree.common.builder.html.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.seasar.framework.container.SingletonS2Container;

import com.gourmetcaree.common.builder.html.LinkNavigationTagBuilder;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.env.EnvDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * {@link LinkNavigationTagBuilder}の実装クラス
 * @author nakamori
 *
 */
public class LinkNavigationTagBuilderImpl implements LinkNavigationTagBuilder {

	/** ログ */
	private static final Logger log = Logger.getLogger(LinkNavigationTagBuilderImpl.class);

	/** GETキー用フォーマット */
	private static final String FORMAT = "%s=%s";

	/** ページナビ */
	private final PageNavigateHelper pageNavi;

	/** リンクフォーマット */
	private final String linkFormat;

	/** GETキーのパラメータ */
	private final Map<?, ?> params;



	public LinkNavigationTagBuilderImpl(PageNavigateHelper pageNavi, String linkFormat, Map<?, ?> params) {
		this.pageNavi = pageNavi;
		this.linkFormat = linkFormat;
		this.params = params;
	}

	public static LinkNavigationTagBuilder newInstance(PageNavigateHelper pageNavi, String linkFormat, Map<?, ?> params) {
		return new LinkNavigationTagBuilderImpl(pageNavi, convertUrl(linkFormat), params);
	}

	public static LinkNavigationTagBuilder newInstance(PageNavigateHelper pageNavi, String linkFormat) {
		return newInstance(pageNavi, linkFormat, null);
	}

	@Override
	public boolean hasNext() {
		if (pageNavi == null) {
			return false;
		}
		return pageNavi.nextPageFlg;
	}

	@Override
	public String getNext() {
		if (!hasNext()) {
			return "";
		}
		return createTag("next", createLink(pageNavi.currentPage + 1));
	}

	@Override
	public boolean hasPrev() {
		if (pageNavi == null) {
			return false;
		}
		return pageNavi.prevPageFlg;
	}

	@Override
	public String getPrev() {
		if (!hasPrev()) {
			return "";
		}
		return createTag("prev", createLink(pageNavi.currentPage - 1));
	}

	/**
	 * タグを作成
	 * @param rel nextかprev
	 * @param url リンクurl
	 */
	private String createTag(String rel, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append("<link rel=\"")
			.append(rel)
			.append("\" href=\"")
			.append(url)
			.append("\" />\n");
		return sb.toString();
	}


	/**
	 * リンクを作成
	 * @param pageNum
	 * @return
	 */
	private String createLink(int pageNum) {
		log.debug(String.format("createLink format:[%s] pateNum:[%d]", linkFormat, pageNum));
		final String link = String.format(linkFormat, pageNum);
		if (GourmetCareeUtil.isEmptyMap(params)) {
			return link;
		}


		StringBuilder sb = new StringBuilder();
		for (Entry<?, ?> entry : params.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();

			String param = createParam(key, value);
			if (param == null) {
				continue;
			}

			if (sb.length() > 0) {
				sb.append("&");
			}


			sb.append(param);
		}

		return String.format("%s?%s", link, sb);
	}

	/**
	 * パラメータを作成
	 * @param key キー
	 * @param value 値
	 */
	private String createParam(Object key, Object value) {
		if (value == null) {
			return String.format(FORMAT, key, "");
		}

		if (value instanceof Collection) {
			return createCollectionParam(key, (Collection<?>) value);
		}

		if (value.getClass().isArray()) {
			return createArrayParam(key, (Object[]) value);
		}

		if (value instanceof String) {
			return String.format(FORMAT,
					key,
					GourmetCareeUtil.urlEncode(
							GourmetCareeUtil.toString(value),
							GourmetCareeConstants.ENCODING));
		}
		return String.format(FORMAT, key, value);
	}

	/**
	 * コレクション系のパラメータを作成
	 * コレクションがnullの場合は、パラメータを作るがsizeが0の場合はパラメータを作らない html:link の謎仕様に合わせる。
	 * @param key キー
	 * @param col コレクション
	 */
	private String createCollectionParam(Object key, Collection<?> col) {
		if (col == null) {
			return String.format(FORMAT, key, "");
		}

		if (col.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Object v : col) {
			if (index++ > 0) {
				sb.append("&");
			}
			sb.append(String.format(FORMAT, key, v));
		}
		return sb.toString();
	}

	/**
	 * 配列系のパラメータを作成
	 * 配列がnullの場合は、パラメータを作るがlengthが0の場合はパラメータを作らない html:link の謎仕様に合わせる。
	 * @param key キー
	 * @param array 配列
	 */
	private String createArrayParam(Object key, Object[] array) {
		if (array == null) {
			return String.format(FORMAT, key, "");
		}

		if (array.length == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Object v : array) {
			if (index++ > 0) {
				sb.append("&");
			}
			sb.append(String.format(FORMAT, key, v));
		}
		return sb.toString();
	}


	/**
	 * /始まりのURLから絶対パスのURIを作成
	 * @param url /始まりURL
	 */
	private static String convertUrl(String url) {
		StringBuilder sb = new StringBuilder(GourmetCareeUtil.createContextUrl());
		EnvDto envDto = SingletonS2Container.getComponent(EnvDto.class);
		sb.append(MAreaConstants.Prefix.getPrefixPath(envDto.getAreaCd()).concat(url));
		return sb.toString();
	}

}
