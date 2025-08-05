package com.gourmetcaree.db.common.helper;

import com.gourmetcaree.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * ページャーに使用するDTOクラス
 * @author ando
 *
 */
public class PageNavigatorDto extends BaseDto {

	private static final long serialVersionUID = 7470836310704263929L;

	public String pageNum = "";

//	public String cssSuffix = "_OFF";

	public String label = "";

	/** 現在の繰り返し値(1から始まるループ) */
	public int count = 1;

	/** ページ数ではなく「前へ」を保持している場合にtrueとなるフラグ */
	public boolean prevLabelFlg = false;

	/** ページ数ではなく「次へ」を保持している場合にtrueとなるフラグ */
	public boolean nextLabelFlg = false;

	/**
	 * 最終ページへの遷移リンクフラグ。
	 * 普通のページングで表示される最終ページではfalseなので注意。
	 * 1,2,3...20 のように最後のページを出すことを目的にした最終ージがtrueとなる。
	 *
	 */
	@Getter
	@Setter
	public boolean toLastPageFlg = false;
	/**
	 * 現在の繰り返し値(1から始まるループ)を取得します。
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 現在の繰り返し値(1から始まるループ)を設定します。
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * ページ数ではなく「前へ」を保持している場合にtrueとなるフラグを取得します。
	 * @return ページ数ではなく「前へ」を保持している場合にtrueとなるフラグ
	 */
	public boolean isPrevLabelFlg() {
	    return prevLabelFlg;
	}

	/**
	 * ページ数ではなく「前へ」を保持している場合にtrueとなるフラグを設定します。
	 * @param prevLabelFlg ページ数ではなく「前へ」を保持している場合にtrueとなるフラグ
	 */
	public void setPrevLabelFlg(boolean prevLabelFlg) {
	    this.prevLabelFlg = prevLabelFlg;
	}

	/**
	 * ページ数ではなく「次へ」を保持している場合にtrueとなるフラグを取得します。
	 * @return ページ数ではなく「次へ」を保持している場合にtrueとなるフラグ
	 */
	public boolean isNextLabelFlg() {
	    return nextLabelFlg;
	}

	/**
	 * ページ数ではなく「次へ」を保持している場合にtrueとなるフラグを設定します。
	 * @param nextLabelFlg ページ数ではなく「次へ」を保持している場合にtrueとなるフラグ
	 */
	public void setNextLabelFlg(boolean nextLabelFlg) {
	    this.nextLabelFlg = nextLabelFlg;
	}

	public boolean isLinkFlg() {
		return linkFlg;
	}

	public void setLinkFlg(boolean linkFlg) {
		this.linkFlg = linkFlg;
	}

	public boolean linkFlg = false;

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
}
