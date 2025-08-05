package com.gourmetcaree.db.common.helper;

import java.io.Serializable;

import org.apache.log4j.Logger;


/**
 * ページナビゲータ
 * 一覧画面でページ遷移用のリンクなどを配置するときに使用するHelperクラス
 * @author Takahiro Ando
 *
 */
public class PageNavigateHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(this.getClass());

	/** 全件数 */
	public int allCount;
	/** offset */
	public int offset;
	/** limit */
	public int limit;
	/** 最大表示件数 */
	public int maxRow;
	/** 現在ページ数 */
	public int currentPage;
	/** 最初のページ数 */
	public int firstPage;
	/** 最後のページ数 */
	public int lastPage;
	/** ソート順 */
	public String sortKey;
	/** 「次のページ」表示用フラグ */
	public boolean nextPageFlg;
	/** 「前のページ」表示用フラグ */
	public boolean prevPageFlg;
	/** 最小表示件数 */
	public int minDisp;
	/** 最大表示件数 */
	public int maxDisp;


	/** コンストラクタ */
	public PageNavigateHelper(String argSortKey, int argMaxRow) {
		super();

		//1以下は認めずデフォルトの１とする
		if (argMaxRow >= 1) {
			maxRow = argMaxRow;
		} else {
			maxRow = 1;
		}

		sortKey = argSortKey;
		offset = 0;
		allCount = 0;
		limit = maxRow;
		currentPage = 1;
		nextPageFlg = false;
		prevPageFlg = false;
		firstPage = 1;
		lastPage = 1;
	}

	/** コンストラクタ */
	public PageNavigateHelper(int argMaxRow) {
		super();

		//1以下は認めずデフォルトの１とする
		if (argMaxRow >= 1) {
			maxRow = argMaxRow;
		} else {
			maxRow = 1;
		}

		offset = 0;
		allCount = 0;
		limit = maxRow;
		currentPage = 1;
		nextPageFlg = false;
		prevPageFlg = false;
		firstPage = 1;
		lastPage = 1;
	}

	/**
	 * 最大行数をセットする。
	 * ※検索後に再度変更する場合に使用。
	 * @param argMaxRow
	 */
	public void setMaxRow(int argMaxRow) {
		//1以下は認めずデフォルトの１とする
		if (argMaxRow >= 1) {
			maxRow = argMaxRow;
		} else {
			maxRow = 1;
		}
	}

	/**
	 * ソートキーをセットする。
	 * ※検索後に再度変更する場合に使用。
	 */
	public void setSortKey(String argSortKey) {
		sortKey = argSortKey;
	}

	/**
	 * 全件数のセットを行う
	 */
	public void changeAllCount(int count) {

		allCount = count;

		//最終ページをセット
		if ((allCount % maxRow) == 0) {
			lastPage = (allCount / maxRow);
		} else {
			lastPage =  (allCount / maxRow) + 1;
		}
	}

	/**
	 * ページをセットする。
	 * @param targetPage
	 */
	public void setPage(int targetPage) {

		//存在しないページの場合は1ページ目と同じとする
		if (targetPage < 1 || targetPage > lastPage) {
			currentPage = 1;
		} else {
			currentPage = targetPage;
		}

		//offsetとlimitを計算
		offset = (currentPage - 1) * maxRow;
		limit = maxRow;

		//次ページがあるか
		if (lastPage > currentPage ) {
			nextPageFlg = true;
		} else {
			nextPageFlg = false;
		}

		//前ページがあるか
		if (currentPage > 1) {
			prevPageFlg = true;
		} else {
			prevPageFlg = false;
		}
	}

	/**
	 * 表示件数の最小を取得します。
	 * @return
	 */
	public int getMinDispNum() {
		return (currentPage - 1) * maxRow + 1;
	}

	/**
	 * 表示件数の最大を取得します。
	 * @return
	 */
	public int getMaxDispNum() {
		final int max = currentPage * maxRow;
		if (max > allCount && currentPage == lastPage) {
			return allCount;
		}
		return max;
	}


	public boolean isDisplayLastPageFlg() {
		logger.debug(String.format("lastPage:[%d] nextPageFlg:[%s]",
				lastPage,
				nextPageFlg));
		if (lastPage < 10) {
			return false;
		}

		return nextPageFlg;
	}

}